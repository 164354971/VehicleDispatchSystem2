package cn.lingbaocrisps.pay.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("pay_order")
public class PayOrder {
    private Long id;//bigint auto_increment comment 'id'
    private Long orderNo;//bigint                                  not null comment '业务订单号',
    private Long payOrderNo;//bigint        default 0                 not null comment '支付单号',
    private Integer userId;//int                                     not null comment '支付用户id',
    private String paymentCode;//varchar(30)   default '0'               not null comment '支付渠道编码',
    private BigDecimal paymentAmount;//decimal(9, 2)                           not null comment '支付金额',
    private Integer paymentType;//tinyint                                 not null comment '支付渠道1余额2微信支付3支付宝4银行卡',
    private Integer paymentStatus;//tinyint       default 0                 not null comment '支付状态，0：待提交，1:待支付，2：支付超时或取消，3：支付成功',
    private String expandJson;//varchar(1024) default ''                not null comment '拓展字段，用于传递不同渠道单独处理的字段',
    private String resultCode;//varchar(20)   default ''                null comment '第三方返回业务码',
    private String resultMsg;//varchar(50)   default ''                null comment '第三方返回提示信息',
    private LocalDateTime paySuccessTime;//datetime                                null comment '支付成功时间',
    private LocalDateTime payOverTime;//datetime                                not null comment '支付超时时间',
    private String qrCodeUrl;//varchar(255)                            null comment '支付二维码链接',
    private LocalDateTime createTime;//datetime      default CURRENT_TIMESTAMP not null comment '创建时间',
    private LocalDateTime updateTime;//datetime      default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    private Long creater;//bigint        default 0                 not null comment '创建人',
    private Long updater;//bigint        default 0                 not null comment '更新人',
    private Boolean isDelete;//bit           default b'0'              not null comment '逻辑删除',
}
