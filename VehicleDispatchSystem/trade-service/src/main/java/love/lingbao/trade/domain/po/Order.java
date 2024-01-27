package love.lingbao.trade.domain.po;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("`order`")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 订单id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;
    private Integer userId;//int        default 0                 not null comment '用户id',
    private Integer carId;//int        default 0                 not null comment '车辆id',
    private Long couponUserId;//bigint        default 0                 not null comment '用户使用的优惠券id',

    private BigDecimal totalAmount;//decimal(9, 2) default 0.00              not null comment '订单总金额',
    private BigDecimal deposit;//decimal(9, 2) default 0.00              not null comment '押金',
    private BigDecimal commission;// decimal(9, 2)                            not null comment '手续费'
    private BigDecimal paymentAmount;//decimal(9, 2) default 0.00              not null comment '订单实付金额',

    private Integer rentDays;//int       default 0                 not null comment '租车天数',
    private Integer paymentType;//tinyint       default 0                 not null comment '支付渠道1余额2微信3支付宝4银行卡',
    private Integer orderStatus;//tinyint       default 0                 not null comment '0未付款,1已付款,2已完成,3取消交易',
    private Integer carStatus;//tinyint       default 0                 not null comment '0未取车,1已取车,2已还车,3退车申请,4退车中,5已退车',

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime paymentTime;//datetime                                null comment '订单支付时间',
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime closeTime;//datetime                                null comment '订单取消时间',
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime commentTime;//datetime                                null comment '订单评价时间',
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime startTime;//datetime      default CURRENT_TIMESTAMP not null comment '租车开始时间',
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime endTime;//datetime      default CURRENT_TIMESTAMP not null comment '租车结束时间',
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;//datetime      default CURRENT_TIMESTAMP not null comment '订单创建时间',
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;//datetime      default CURRENT_TIMESTAMP not null comment '订单更新时间',

}

