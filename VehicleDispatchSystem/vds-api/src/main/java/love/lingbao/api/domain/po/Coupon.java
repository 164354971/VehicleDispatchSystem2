package love.lingbao.api.domain.po;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Coupon implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;//bigint auto_increment
    private String name;
    private Integer type;//1优惠券(金额)2优惠券(天数)3折扣券(金额)4折扣券(天数)
    private BigDecimal reach;//使用券的门槛
    private BigDecimal discount;//优惠的最大金额或折扣
    private LocalDateTime startTime;//datetime      default CURRENT_TIMESTAMP not null comment '默认可使用的开始时间为现在',
    private LocalDateTime endTime;//datetime      default CURRENT_TIMESTAMP not null comment '默认可使用时间为获得时间的后7天',
    private LocalDateTime createTime;//datetime      default CURRENT_TIMESTAMP not null
}
