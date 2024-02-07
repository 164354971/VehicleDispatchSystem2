package cn.lingbaocrisps.websocket.listener;

import cn.lingbaocrisps.websocket.domain.bo.MessageBO;
import cn.lingbaocrisps.websocket.domain.po.Message;
import cn.lingbaocrisps.websocket.service.IMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class MessageStatusListener {

    private final IMessageService messageService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "mark.message.status.queue", durable = "true"),
            exchange = @Exchange(value = "message.topic", type = ExchangeTypes.TOPIC, durable = "true"),
            key = "message.status"
    ))
    public void listenMessageStatus(Message message){
        log.info("消息状态监听到消息：{}", message);
        messageService.insertMessage(message);
        log.info("{}消息已存数据库", message);

    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "mark.simpleMessage.status.queue", durable = "true"),
            exchange = @Exchange(value = "message.topic", type = ExchangeTypes.TOPIC, durable = "true"),
            key = "simpleMessage.status"
    ))
    public void listenSimpleMessageAddStatus(MessageBO messageBO){
        log.info("简单消息状态监听到消息：{}", messageBO);
        if(messageBO != null)
            messageService.addOrUpdateSimpleMessage(messageBO);
    }
}
