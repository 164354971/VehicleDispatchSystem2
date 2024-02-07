package cn.lingbaocrisps.websocket.service.impl;

import cn.lingbaocrisps.api.client.UserClient;
import cn.lingbaocrisps.api.domain.bo.UserBO;
import cn.lingbaocrisps.common.domain.RedisConstants;
import cn.lingbaocrisps.common.exception.UnauthorizedException;
import cn.lingbaocrisps.common.utils.BeanUtils;
import cn.lingbaocrisps.common.utils.RedisTools;
import cn.lingbaocrisps.common.utils.UserContext;
import cn.lingbaocrisps.websocket.domain.bo.MessageBO;
import cn.lingbaocrisps.websocket.domain.dto.SimpleMessageDTO;
import cn.lingbaocrisps.websocket.domain.po.Message;
import cn.lingbaocrisps.websocket.domain.vo.MessageVO;
import cn.lingbaocrisps.websocket.domain.vo.SimpleMessageVO;
import cn.lingbaocrisps.websocket.mapper.MessageMapper;
import cn.lingbaocrisps.websocket.service.IMessageService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements IMessageService {
    private final RedisTools redisTools;

    private final UserClient userClient;

    private final MessageMapper messageMapper;

    @Override
    public List<MessageVO> getMessageList(Integer senderId, Integer getterId) {
        Integer userId = UserContext.getUser();
        if(userId == null){
            throw new UnauthorizedException("用户未登录");
        }
        if(!userId.equals(senderId)){
            throw new UnauthorizedException("用户登录状态异常");
        }

        LambdaQueryWrapper<Message> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Message::getSenderId, senderId).eq(Message::getGetterId, getterId).or().eq(Message::getSenderId, getterId).eq(Message::getGetterId, senderId).orderByAsc(Message::getSendTimestamp);
        List<Message> messageList = list(lqw);

        List<MessageVO> messageVOList = new ArrayList<>();
        if(messageList != null && messageList.size() > 0){
            messageVOList = BeanUtils.copyList(messageList, MessageVO.class);
        }
        //未读消息置为0
        redisTools.set(RedisConstants.SIMPLE_MESSAGE_KEY + senderId + ":" + getterId, 0L);
        return messageVOList;
    }

    @Override
    public void updateSimpleMessageNoticeContentNum(Integer senderId, Integer getterId){
        Integer userId = UserContext.getUser();
        if(userId == null){
            throw new UnauthorizedException("用户未登录");
        }
        redisTools.set(RedisConstants.SIMPLE_MESSAGE_KEY + getterId + ":" + senderId, 0L);
        SimpleMessageVO simpleMessageVO = redisTools.hGet(RedisConstants.MESSAGE_SET_KEY + getterId, String.valueOf(senderId), SimpleMessageVO.class);
        log.info("simpleMessageVO:{}", simpleMessageVO);
        simpleMessageVO.setNoticeContentNum(0);
        redisTools.hput(RedisConstants.MESSAGE_SET_KEY + getterId, String.valueOf(senderId), simpleMessageVO);
    }

    @Override
    public List<SimpleMessageVO> getSimpleMessageList() {
        Integer userId = UserContext.getUser();
        if(userId == null){
            throw new UnauthorizedException("用户未登录");
        }
        Map<String, SimpleMessageVO> simpleMessageVOMap = redisTools.hGetAll(RedisConstants.MESSAGE_SET_KEY + userId, SimpleMessageVO.class);
        List<SimpleMessageVO> simpleMessageVOList = new ArrayList<>(simpleMessageVOMap.values());
        simpleMessageVOList.sort(Comparator.comparing(SimpleMessageVO::getLastContentTimestamp).reversed());
        return simpleMessageVOList;
    }

    @Override
    public void addOrUpdateSimpleMessage(MessageBO messageBO) {
        //获取发送者信息
        SimpleMessageVO senderSimpleMessageVO = getSimpleMessageVO(messageBO.getGetterId(), messageBO);
        //获取接收者消息
        SimpleMessageVO getterSimpleMessageVO = getSimpleMessageVO(messageBO.getSenderId(), messageBO);

        if(messageBO.getStatus().equals(0)){//双方用户在线时
            //未读消息置为0
            redisTools.set(RedisConstants.SIMPLE_MESSAGE_KEY + messageBO.getSenderId() + ":" + messageBO.getGetterId(), 0L);
        }else{//接收者用户不在线时
            redisTools.incr(RedisConstants.SIMPLE_MESSAGE_KEY + messageBO.getSenderId() + ":" + messageBO.getGetterId());
        }

        Integer num = redisTools.get(RedisConstants.SIMPLE_MESSAGE_KEY + messageBO.getSenderId() + ":" + messageBO.getGetterId(), Integer.class);

        senderSimpleMessageVO.setNoticeContentNum(0);
        getterSimpleMessageVO.setNoticeContentNum(num);

        redisTools.hput(RedisConstants.MESSAGE_SET_KEY + messageBO.getSenderId(), String.valueOf(messageBO.getGetterId()), senderSimpleMessageVO);
        redisTools.hput(RedisConstants.MESSAGE_SET_KEY + messageBO.getGetterId(), String.valueOf(messageBO.getSenderId()), getterSimpleMessageVO);

    }

    @Override
    public void insertMessage(Message message) {
        messageMapper.insertMessage(message);
    }

    SimpleMessageVO getSimpleMessageVO(Integer userId, MessageBO messageBO){
        SimpleMessageVO simpleMessageVO = new SimpleMessageVO();
        UserBO userBO = userClient.getUserBO(userId);
        simpleMessageVO.setUserId(userId);
        simpleMessageVO.setUsername(userBO.getUsername());
        simpleMessageVO.setNickname(userBO.getNickname());
        simpleMessageVO.setUserImg(userBO.getUserImg());
        simpleMessageVO.setLastContent(messageBO.getContent());
        simpleMessageVO.setLastContentTimestamp(messageBO.getSendTimestamp());
        return simpleMessageVO;
    }
}
