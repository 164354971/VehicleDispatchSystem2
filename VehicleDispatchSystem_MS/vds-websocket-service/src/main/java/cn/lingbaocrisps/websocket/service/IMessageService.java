package cn.lingbaocrisps.websocket.service;

import cn.lingbaocrisps.websocket.domain.bo.MessageBO;
import cn.lingbaocrisps.websocket.domain.dto.SimpleMessageDTO;
import cn.lingbaocrisps.websocket.domain.po.Message;
import cn.lingbaocrisps.websocket.domain.vo.MessageVO;
import cn.lingbaocrisps.websocket.domain.vo.SimpleMessageVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IMessageService extends IService<Message> {
    public List<MessageVO> getMessageList(Integer senderId, Integer getterId);

    List<SimpleMessageVO> getSimpleMessageList();

    void updateSimpleMessageNoticeContentNum(Integer senderId, Integer getterId);

    void addOrUpdateSimpleMessage(MessageBO messageBO);

    void insertMessage(Message message);
}
