package cn.lingbaocrisps.trade.service.impl;

import cn.lingbaocrisps.common.annotate.RepeatSubmit;
import cn.lingbaocrisps.common.domain.R;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import cn.lingbaocrisps.api.client.CarClient;
import cn.lingbaocrisps.api.client.CouponClient;
import cn.lingbaocrisps.api.client.UserClient;
import cn.lingbaocrisps.api.domain.po.Car;
import cn.lingbaocrisps.api.domain.po.Coupon;
import cn.lingbaocrisps.api.domain.po.CouponUser;
import cn.lingbaocrisps.common.domain.CouponTypeConstants;
import cn.lingbaocrisps.common.domain.MultiDelayMessage;
import cn.lingbaocrisps.common.domain.RedisConstants;
import cn.lingbaocrisps.common.exception.ForbiddenException;
import cn.lingbaocrisps.common.mq.DelayMessageProcessor;
import cn.lingbaocrisps.common.utils.BeanUtils;
import cn.lingbaocrisps.common.utils.LocalDateTimeUtils;
import cn.lingbaocrisps.common.utils.RedisTools;
import cn.lingbaocrisps.common.utils.UserContext;
import cn.lingbaocrisps.trade.constants.MqConstants;
import cn.lingbaocrisps.trade.domain.dto.OrderFormDTO;
import cn.lingbaocrisps.trade.domain.po.Order;
import cn.lingbaocrisps.trade.domain.vo.OrderVO;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import cn.lingbaocrisps.trade.mapper.OrderMapper;
import cn.lingbaocrisps.trade.service.IOrderService;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    private final UserClient userClient;

    private final CarClient carClient;

    private final CouponClient couponClient;

    private final RedisTools redisTools;

    private final RabbitTemplate rabbitTemplate;

    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Override
    @RepeatSubmit
    @GlobalTransactional        //seata分布式事务的全局事务，对其中的业务（@Transactional注解）进行分布式事务的控制
    public OrderVO createOrder(OrderFormDTO orderFormDTO) {
        //1.获取用户状态，从表中判断用户状态是否可用
        Integer userId = UserContext.getUser();
        log.info(String.valueOf(userId));
        Integer userStatus = userClient.getUserStatus(userId).getData();
        if(userStatus.equals(0)){
            throw new ForbiddenException("用户已被禁用");
        }

        //2.用户可用,判断租车id是否合法
        Integer carId = orderFormDTO.getCarId();
        Car car = carClient.getById(carId);
        if(car == null){
            throw new ForbiddenException("车辆数据非法");
        }
        //判断用户优惠券是否合法且可用
        if(orderFormDTO.getCouponUserId() != 0) {
            Callable<String> couponCallable = () -> {
                CouponUser couponUser = couponClient.findByCouponUserId(orderFormDTO.getCouponUserId()).getData();
                if(couponUser == null){
                    throw new ForbiddenException("优惠券数据非法");
                }
                if(couponUser.getCouponId() == null){
                    throw new ForbiddenException("优惠券数据非法");
                }
                if (couponUser.getStatus() != 1){
                    throw new ForbiddenException("优惠券已被禁用");
                }
                if(couponUser.getStartTime().isAfter(orderFormDTO.getEndTime()) || couponUser.getEndTime().isBefore(orderFormDTO.getStartTime())){
                    throw new ForbiddenException("优惠券不在可用时间");
                }
                //禁用此优惠券
                //更新
                R<Boolean> booleanR = couponClient.updateCouponUser(couponUser.getId(), 0, couponUser.getStatus());
                return String.format("couponUserId:%d已禁用\n", couponUser.getId());
            };
            Future<String> submit = threadPoolTaskExecutor.submit(couponCallable);
            try {
                String couponCallableSubmit = submit.get();
                log.info(couponCallableSubmit);
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
        //3 车辆id合法,判断租的车在订单选中的时间段是否不可用
        //3.1 先从redis中查询是否有订单中租车车辆在该时间段（订单的租车开始时间，订单的租车结束时间）
        Map<String, Order> orderMap = redisTools.hGetAll(RedisConstants.ORDER_MAP, Order.class);
        //3.1.1 如果时间重叠，说明该租车时间段已不可用，抛出异常
        if(orderMap != null)
            for(Map.Entry<String, Order> entry : orderMap.entrySet()) {
                String orderNo = entry.getKey();
                Order order = entry.getValue();
                //如果order_key不存在了，则移除这个键值对
                if(!redisTools.hasKey(RedisConstants.ORDER_KEY + orderNo)){
                    redisTools.hDelete(RedisConstants.ORDER_MAP, String.valueOf(orderNo));
                    continue;
                }
                if(!order.getCarId().equals(carId)){
                    continue;
                }
                if(orderFormDTO.getStartTime().isBefore(order.getEndTime()) || orderFormDTO.getEndTime().isAfter(order.getStartTime())){
                    throw new ForbiddenException("车辆在该时间段不可用");
                }
            }
        //3.1.2 如果没有重叠，说明该时间段在新创建的未支付的订单时间段中可用，继续去车辆禁用数据表中查询
        String startTime = orderFormDTO.getStartTime().format(LocalDateTimeUtils.FORMATTER);
        String endTime = orderFormDTO.getEndTime().format(LocalDateTimeUtils.FORMATTER);
        boolean usableCar = carClient.isUsableCarTime(carId, startTime, endTime);
        log.info("车辆id：{}， 在当前时间段是否可租： {}", carId, usableCar);
        if(!usableCar){
            throw new ForbiddenException("车辆在该时间段不可用");
        }

        //4.创建新订单
        Order order = BeanUtils.copyBean(orderFormDTO, Order.class);
        // 从redis获取订单号，不存在则先设0再获取自增后的订单号
        if(redisTools.get(RedisConstants.ORDER_NUMBER_KEY, String.class) == null)
            redisTools.set(RedisConstants.ORDER_NUMBER_KEY, 0L);

        Long orderNo = redisTools.incr(RedisConstants.ORDER_NUMBER_KEY);
        if(orderNo == null)
            throw new RuntimeException("订单号生成失败");
        // 将订单单号写入order
        order.setId(orderNo);
        // 将金额计算写入order
        order.setUserId(UserContext.getUser());
        //4.1 总金额=租车金额*天数+手续费+押金
        Integer rentDays = order.getRentDays();
        BigDecimal totalAmount = getTotalAmount(car, rentDays);
        order.setTotalAmount(totalAmount);
        //4.2 实付金额=(总金额-(押金))-折扣
        BigDecimal paymentAmount = getPaymentAmount(order, car);
        order.setDeposit(car.getDeposit());
        order.setCommission(car.getCommission());
        order.setPaymentAmount(paymentAmount);

        log.info(Objects.toString(order));
        //6.写入订单表
        save(order);
        //5.写入redis并配置订单过期时间为5分钟
        redisTools.hput(RedisConstants.ORDER_MAP, String.valueOf(orderNo), order);
        redisTools.set(RedisConstants.ORDER_KEY + orderNo, JSON.toJSONString(order), 5L, TimeUnit.MINUTES);

        //7.发送mq消息，延迟查询订单支付状态(10s, 10s, 20s, 20s, 30s, 30s, 60s, 120s)
        try {
            MultiDelayMessage<Long> msg = MultiDelayMessage.of(orderNo, 10000L, 10000L, 20000L, 20000L, 30000L, 30000L, 60000L, 120000L);
            rabbitTemplate.convertAndSend(
                    MqConstants.DELAY_EXCHANGE, MqConstants.DELAY_ORDER_ROUTING_KEY, msg,
                    new DelayMessageProcessor(msg.removeNextDelay().intValue())
            );
        } catch (AmqpException e) {
            log.error("延迟发送订单支付状态查询消息失败", e);
        }
        //8.返回订单vo
        return BeanUtils.copyBean(order, OrderVO.class);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void cancelOrder(Long orderId) {
        if(redisTools.hasKey(RedisConstants.ORDER_KEY + orderId)){
            Runnable removeKeyRunnable = () -> {
                //如果订单还存在，先移除redis中的订单
                redisTools.hDelete(RedisConstants.ORDER_MAP, RedisConstants.ORDER_KEY + orderId);
                //再移除这个string
                redisTools.delete(RedisConstants.ORDER_KEY + orderId);
            };
            threadPoolTaskExecutor.execute(removeKeyRunnable);
        }
        Callable<String> couponUserStatusUpdateCallable = () -> {
            //将使用的用户优惠券启用
            Order order = lambdaQuery().eq(Order::getId, orderId).one();
            Long couponUserId = order.getCouponUserId();
            if (couponUserId != 0) {
                CouponUser couponUser = couponClient.findByCouponUserId(couponUserId).getData();
                couponUser.setStatus(1);
                couponClient.updateCouponUser(couponUser.getId(), 1, couponUser.getStatus());
            }
            return String.format("couponUserId:%d已重新启用\n", couponUserId);
        };
        Future<String> couponUserStatusUpdateSubmit = threadPoolTaskExecutor.submit(couponUserStatusUpdateCallable);
        Callable<String> orderStatusUpdateCallable = () -> {
            //再更新数据库中的订单状态
            lambdaUpdate().set(Order::getOrderStatus, 3)
                    .set(Order::getCloseTime, LocalDateTime.now())
                    .eq(Order::getId, orderId)
                    .update();
            return String.format("订单id:%d已取消\n", orderId);
        };
        Future<String> orderStatusUpdateSubmit = threadPoolTaskExecutor.submit(orderStatusUpdateCallable);
        try {
            String couponUserStatusUpdateResult = couponUserStatusUpdateSubmit.get();
            String orderStatusUpdateResult = orderStatusUpdateSubmit.get();
            log.info(couponUserStatusUpdateResult + orderStatusUpdateResult);
        }catch (InterruptedException | ExecutionException e){
            e.printStackTrace();
        }
    }

    @Override
    public void markOrderPaySuccess(Long orderId){
        lambdaUpdate()
        .set(Order::getOrderStatus, 1)
        .set(Order::getPaymentTime, LocalDateTime.now())
        .eq(Order::getId, orderId)
        .eq(Order::getOrderStatus, 0)
        .update();
    }

    BigDecimal getTotalAmount(Car car, Integer rentDays){
        return car.getDayPrice().multiply(new BigDecimal(rentDays)).add(car.getCommission()).add(car.getDeposit());
    }

    BigDecimal getPaymentAmount(Order order, Car car){
        Integer userId = order.getUserId();
        Long couponUserId = order.getCouponUserId();
        //查用户会员等级
        Integer userVipLevel = userClient.getUserVipLevel(userId);
        Integer rentDays = order.getRentDays();
        //车辆是否会员免押金
        Boolean noDeposit = car.getNoDeposit();
        BigDecimal deposit = car.getDeposit();
        BigDecimal paymentAmount = order.getTotalAmount();
        order.setDeposit(deposit);
        if(userVipLevel > 0 && noDeposit) {
            paymentAmount = paymentAmount.subtract(deposit);
        }
        if(couponUserId == 0){
            return paymentAmount;
        }
        CouponUser couponUser = couponClient.findByCouponUserId(couponUserId).getData();
        //判断用户优惠券是否可用
        isRightCouponUser(couponUser);
        Coupon coupon = couponClient.findCouponById(couponUser.getCouponId()).getData();
        isRightCoupon(coupon, paymentAmount, rentDays);

        return getPaymentAmount(paymentAmount, coupon);
    }

    private void isRightCouponUser(CouponUser couponUser){
        if(couponUser == null){
            throw new ForbiddenException("用户优惠券数据非法");
        }
        if(couponUser.getStartTime().isAfter(LocalDateTime.now())){
            throw new ForbiddenException("用户优惠券未到使用时间");
        }
        if(couponUser.getEndTime().isBefore(LocalDateTime.now())){
            throw new ForbiddenException("用户优惠券已过期");
        }
    }

    private void isRightCoupon(Coupon coupon, BigDecimal paymentAmount, Integer rentDays){
        if(coupon == null){
            throw new ForbiddenException("优惠券数据非法");
        }

        switch (coupon.getType()){
            case CouponTypeConstants.COUPON_AMOUNT:
            case CouponTypeConstants.DISCOUNT_AMOUNT:
                //未达优惠券使用金额门槛
                if(coupon.getReach().compareTo(paymentAmount) > 0){
                    throw new ForbiddenException("用户优惠券未达使用门槛");
                }
            case CouponTypeConstants.DISCOUNT_DAYS:
            case CouponTypeConstants.COUPON_DAYS:
                //未达优惠券使用天数门槛
                if(coupon.getReach().compareTo(BigDecimal.valueOf(rentDays)) > 0){
                    throw new ForbiddenException("用户优惠券未达使用门槛");
                }
        }
    }

    private BigDecimal getPaymentAmount(BigDecimal paymentAmount, Coupon coupon){
        Integer couponType = coupon.getType();
        BigDecimal discount = coupon.getDiscount();
        switch (couponType){
            case CouponTypeConstants.COUPON_AMOUNT:
            case CouponTypeConstants.COUPON_DAYS:
                return paymentAmount.subtract(discount);
            case CouponTypeConstants.DISCOUNT_AMOUNT:
            case CouponTypeConstants.DISCOUNT_DAYS:
                return paymentAmount.multiply(discount);
            default:
                return paymentAmount;
        }
    }
}
