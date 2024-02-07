package cn.lingbaocrisps.websocket.domain.vo;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
public class MessageVO  implements Serializable {
    private static final long serialVersionUID = 1L; //版本兼容标志
    private String id;        //			char(36)
    private Integer senderId;        //	int							not null comment '发送人id',
    private Integer getterId;        //	int							not null comment '接收者id',
    private String replyId;        //		char(36)				null comment '回复的信息id',
    private String content;        //		text 						not null comment '发送的文字信息',
    private String contentImgs;//图片列表
    private Long sendTimestamp;    //bigint 					not null comment '发送信息的时间戳',
    private Integer messageType;//	int 						not null comment '消息类型'
}
