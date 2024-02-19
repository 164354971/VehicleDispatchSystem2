package cn.lingbaocrisps.websocket.mapper;

import cn.lingbaocrisps.websocket.domain.po.Message;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MessageMapper extends BaseMapper<Message> {
    @Insert("INSERT INTO message (id, content,send_timestamp,sender_id,getter_id,message_type) VALUES (#{id}, #{content},#{sendTimestamp},#{senderId},#{getterId},#{messageType})")
    void insertMessage(Message message);
}
