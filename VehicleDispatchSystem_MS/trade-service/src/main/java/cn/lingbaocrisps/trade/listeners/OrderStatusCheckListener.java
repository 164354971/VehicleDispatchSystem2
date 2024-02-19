package cn.lingbaocrisps.trade.listeners;

import cn.lingbaocrisps.trade.constants.MqConstants;
import cn.lingbaocrisps.trade.domain.po.Order;
import cn.lingbaocrisps.trade.service.IOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import cn.lingbaocrisps.api.client.PayClient;
import cn.lingbaocrisps.common.domain.MultiDelayMessage;
import cn.lingbaocrisps.common.domain.RedisConstants;
import cn.lingbaocrisps.common.mq.DelayMessageProcessor;
import cn.lingbaocrisps.common.utils.RedisTools;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class OrderStatusCheckListener {

    private final RabbitTemplate rabbitTemplate;
    private final IOrderService orderService;

    private final RedisTools redisTools;
    private final PayClient payClient;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqConstants.DELAY_ORDER_QUEUE, durable = "true"),
            exchange = @Exchange(value = MqConstants.DELAY_EXCHANGE, delayed = "true", type = ExchangeTypes.TOPIC, durable = "true"),
            key = MqConstants.DELAY_ORDER_ROUTING_KEY
    ))
    public void listenOrderStatusCheck(MultiDelayMessage<Long> msg) {
        //1.查询订单
        Long orderId = msg.getData();
        Order order = orderService.getById(orderId);
        //2.检查订单状态
        if (order == null || order.getOrderStatus() == 1){
            //2.1.订单不存在，或者订单状态为已支付，直接返回
            log.info("订单id：{}，订单不存在或者订单状态为已支付，直接返回", orderId);
            return;
        }
        //3.去支付服务查询真正的支付状态
        Integer paymentStatus = payClient.getPaymentStatus(orderId);
        if (paymentStatus != null &&
                paymentStatus == 3){
            //3.1.支付成功，修改订单状态
            log.info("订单id：{}，支付成功，修改订单状态", orderId);
            orderService.markOrderPaySuccess(orderId);
            return;
        }
        //4.没有支付成功，判断是否还有延迟消息
        if (msg.hasNextDelay()){
            //4.1.有，获取第一个延迟消息，进行重发
            log.info("订单id：{}，还有{}个延迟消息，重发", orderId, msg.getDelayMillis().size());
            Integer nextDelay = msg.removeNextDelay().intValue();
            rabbitTemplate.convertAndSend(
                    MqConstants.DELAY_EXCHANGE, MqConstants.DELAY_ORDER_ROUTING_KEY,
                    msg, new DelayMessageProcessor(nextDelay)
            );
        }else{
            //4.2.没有，取消订单
            log.info("订单id：{}，没有延迟消息，取消订单", orderId);
            orderService.cancelOrder(orderId);
            //4.3.redis中删除订单
            redisTools.hDelete(RedisConstants.ORDER_MAP, String.valueOf(orderId));
        }
    }
}
