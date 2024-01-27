package love.lingbao.trade.domain.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;

@ApiModel(value = "订单VO")
@Data
public class OrderVO {
    private Long id;
    private BigDecimal totalAmount;//decimal(9, 2) default 0.00              not null comment '订单总金额',
    private BigDecimal deposit;//decimal(9, 2) default 0.00              not null comment '押金',
    private BigDecimal commission;// decimal(9, 2)                            not null comment '手续费'
    private BigDecimal paymentAmount;//decimal(9, 2) default 0.00              not null comment '订单实付金额',
    private Integer rentDays;//int       default 0                 not null comment '租车天数',
}
