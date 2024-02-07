package cn.lingbaocrisps.evaluate.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = false)
@Data
@TableName("evaluate_reply")
public class EvaluateReply {
    private Long id;//bigint auto_increment
    private Integer userId;//int                                   not null comment '用户id',
    private String userNickname;//varchar(10)                           not null comment '用户昵称',
    private String userImg;//varchar(10)                           not null comment '用户头像',
    private Integer replyUserId;//int                                   not null comment '回复用户id',
    private String replyUserNickname;//varchar(10)                           not null comment '回复用户的昵称',
    private String evaluate;//varchar(80) default ''                not null comment '对车辆的评价的文字信息',
    private Long evaluateId;//bigint      default 0                 not null comment '回复评价的id',
    private Integer evaluateReplyId;//bigint      default 0                 not null comment '回复回复的id',
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    /*@JsonFormat(shape= JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")*/
    private LocalDateTime createTime;//datetime    default CURRENT_TIMESTAMP not null
}
