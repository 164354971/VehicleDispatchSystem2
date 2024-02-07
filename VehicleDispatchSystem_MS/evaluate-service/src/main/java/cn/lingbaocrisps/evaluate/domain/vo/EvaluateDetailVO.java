package cn.lingbaocrisps.evaluate.domain.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@ApiModel(value = "详细评价VO")
@Data
public class EvaluateDetailVO {
    private Long id;
    private Integer userId;//int                                   not null comment '用户id',
    private Integer brandId;//int																	 not null comment '用户评价的品牌id',
    private Integer modelId;//int																	 not null comment '用户评价的型号id',
    private Integer carId;//int                                   not null comment '用户评价的车辆id',
    private Integer score;//tinyint     default 5                 not null comment '用户对车辆的评分',
    private String evaluate;//varchar(80) default ''                not null comment '对车辆的评价的文字信息',
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
//    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;//datetime    default CURRENT_TIMESTAMP not null
    private String userImg;//展示的用户头像
    private String userNickname;//用户昵称
    private BigDecimal dayPrice;//decimal(9,2) default 0.00
    private Integer rentDays;//租用天数
    private Integer vipLevel;//会员等级
    private Integer replyCount;//回复总数
    private EvaluateImgVO evaluateImgVO;
}
