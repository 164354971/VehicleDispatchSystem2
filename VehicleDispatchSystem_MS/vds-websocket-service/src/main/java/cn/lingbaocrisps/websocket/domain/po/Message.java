package cn.lingbaocrisps.websocket.domain.po;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Message implements Serializable {
    private static final long serialVersionUID = 1L; //版本兼容标志
    private String id;        //			char(36)
    private Integer senderId;        //	int							not null comment '发送人id',
    private Integer getterId;        //	int							not null comment '接收者id',
    private String replyId;        //		char(36)				null comment '回复的信息id',
    private String content;        //		text 						not null comment '发送的文字信息',
    private String contentImgs; //text 						null comment '发送的图片的地址，逗号分割',
    private Long sendTimestamp;    //bigint 					not null comment '发送信息的时间戳',
    private Integer messageType;//	int 						not null comment '消息类型'
}
