package cn.lingbaocrisps.trade.listeners;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import cn.lingbaocrisps.trade.domain.po.Order;
import cn.lingbaocrisps.trade.service.IOrderService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PayStatusListener {

    private final IOrderService orderService;

    @RabbitListener(bindings = @QueueBinding(
                value = @Queue(value = "mark.order.pay.queue", durable = "true"),
            exchange = @Exchange(value = "pay.topic", type = ExchangeTypes.TOPIC, durable = "true"),
            key = "pay.success"
    ))
    public void listenOrderPay(Long orderId){
        log.info("监听到订单支付，订单号：{}", orderId);
        //1.查询订单
        Order order = orderService.getById(orderId);
        //2.检查订单状态
        if (order == null || order.getOrderStatus() != 1){
            log.info("订单状态不正确，无法支付订单号：{}", orderId);
            return;
        }
        //3.修改订单状态为已支付
        orderService.markOrderPaySuccess(orderId);
        log.info("订单支付成功，订单号：{}", orderId);
    }
}
