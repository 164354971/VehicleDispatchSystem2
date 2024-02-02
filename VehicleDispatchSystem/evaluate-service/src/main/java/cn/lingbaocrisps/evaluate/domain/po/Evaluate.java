package cn.lingbaocrisps.evaluate.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("evaluate")
public class Evaluate {
    private Long id;//bigint auto_increment
    private Integer userId;//int                                   not null comment '用户id',
    private Integer brandId;//int																	 not null comment '用户评价的品牌id',
    private Integer modelId;//int																	 not null comment '用户评价的型号id',
    private Integer carId;//int                                   not null comment '用户评价的车辆id',
    private Integer rentDays;//租用天数
    private Integer vipLevel;//会员等级
    private Integer score;//tinyint     default 5                 not null comment '用户对车辆的评分',
    private String userNickName;//varchar(10) default ''                not null comment '用户昵称',
    private String userImg;//varchar(255) default ''                not null comment '用户头像',
    private String evaluate;//varchar(80) default ''                not null comment '对车辆的评价的文字信息',
    private BigDecimal dayPrice;//decimal(9,2) default 0.00
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createTime;//datetime    default CURRENT_TIMESTAMP not null
}
