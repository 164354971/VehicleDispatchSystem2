package love.lingbao.common.mq;

import com.rabbitmq.client.MessageProperties;
import lombok.RequiredArgsConstructor;
import love.lingbao.common.domain.MultiDelayMessage;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;

@RequiredArgsConstructor
public class DelayMessageProcessor implements MessagePostProcessor {

    private final int delay;

    @Override
    public Message postProcessMessage(Message message) throws AmqpException {
        message.getMessageProperties().setDelay(delay);
        return message;
    }
}
