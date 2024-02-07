package cn.lingbaocrisps.websocket.service.impl;

import cn.lingbaocrisps.common.utils.BeanUtils;
import cn.lingbaocrisps.websocket.domain.bo.MessageBO;
import cn.lingbaocrisps.websocket.domain.po.Message;
import cn.lingbaocrisps.websocket.service.WebSocketService;
import com.alibaba.fastjson.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author GXM
 * @version 1.0.0
 * @Description TODO
 * @createTime 2023年07月19日
 */
@Slf4j
public class WebSocketServiceImpl implements WebSocketService {

    private final Map<Integer, WebSocketSession> clients = new ConcurrentHashMap<>();

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void handleOpen(WebSocketSession session) {
        // 这个时候就需要在建立 webSocket 时存储的 用户信息了
        Map<String, Object> attributes = session.getAttributes();
        Integer clientUserId = (Integer) attributes.get("clientUserId");
        clients.put(clientUserId, session);

        log.info("一个新的连接被打开，用户id为{}，当前在线人数：{}", clientUserId, clients.size());
    }

    @Override
    public void handleClose(WebSocketSession session) {
        // 这个时候就需要在建立 webSocket 时存储的 用户信息了
        Map<String, Object> attributes = session.getAttributes();
        Integer clientUserId = (Integer) attributes.get("clientUserId");
        clients.remove(clientUserId);
        log.info("用户id为{}的连接被关闭，当前在线人数：{}", clientUserId, clients.size());
    }

    @Override
    public void handleMessage(WebSocketSession session, String messageJSON) {
        // 只处理前端传来的文本消息，并且直接丢弃了客户端传来的消息
        log.info("服务端收到的消息：{}", messageJSON);
        Map<String, Object> attributes = session.getAttributes();
        Integer clientUserId = (Integer) attributes.get("clientUserId");
        Message message = JSON.parseObject(messageJSON, Message.class);
        try{
            sendMessage(clientUserId, message.getGetterId(), message);
        } catch (IOException e) {
            log.error("发送消息失败");
            throw new RuntimeException(e);
        }
    }

    //发送消息(本身)
    @Override
    public void sendMessage(WebSocketSession session, String message) throws IOException {
        //直接发送
        this.sendMessage(session, new TextMessage(message));
    }

    @Override
    public void sendMessage(Integer senderId, Integer getterId, Message message) throws IOException {
        WebSocketSession webSocketSession = clients.get(getterId);
        //生成uuid
        message.setId(UUID.randomUUID().toString());
        MessageBO messageBO = BeanUtils.copyBean(message, MessageBO.class);
        if (webSocketSession == null) {
            log.info("用户{}不在线，消息发往mq", getterId);
            messageBO.setStatus(1);
            rabbitTemplate.convertAndSend("message.topic", "message.status", message);
            rabbitTemplate.convertAndSend("message.topic", "simpleMessage.status", messageBO);
            return;
        }
        //判断是否连接
        if (webSocketSession.isOpen()) {
            log.info("{}向{}发送消息：{}", senderId, getterId, message);
            messageBO.setStatus(0);
            rabbitTemplate.convertAndSend("message.topic", "message.status", message);
            rabbitTemplate.convertAndSend("message.topic", "simpleMessage.status", messageBO);
            String messageJson = JSON.toJSONString(message);
            webSocketSession.sendMessage(new TextMessage(messageJson));
        }else{
            log.info("用户{}不在线，消息发送失败", getterId);
        }
    }


    @Override
    public void sendMessage(WebSocketSession session, TextMessage message) throws IOException {
        session.sendMessage(message);
    }

    @Override
    public void broadCast(String message) throws IOException {
        clients.values().forEach(session -> {
            if (session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(message));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Override
    public void broadCast(TextMessage message) throws IOException {
        clients.values().forEach(session -> {
            if (session.isOpen()) {
                try {
                    session.sendMessage(message);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Override
    public void handleError(WebSocketSession session, Throwable error) {
        log.error("websocket error：{}，session id：{}", error.getMessage(), session.getId());
        log.error("", error);
    }

}

