package cn.lingbaocrisps.evaluate.domain.vo;


import cn.lingbaocrisps.evaluate.domain.po.Evaluate;
import cn.lingbaocrisps.evaluate.domain.po.EvaluateImg;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@ApiModel(value = "评价VO")
@Data
public class EvaluateVO {
    private Integer userId;//int                                   not null comment '用户id',
    private Integer score;//tinyint     default 5                 not null comment '用户对车辆的评分',
    private String evaluate;//varchar(80) default ''                not null comment '对车辆的评价的文字信息',
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createTime;//datetime    default CURRENT_TIMESTAMP not null
    private String userImg;//展示的用户头像
    private String coverImg;//展示的封面图片
    private String dayPrice;//日租价
}
