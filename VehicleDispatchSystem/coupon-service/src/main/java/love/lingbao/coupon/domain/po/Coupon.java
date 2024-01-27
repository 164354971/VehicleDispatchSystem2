package love.lingbao.coupon.domain.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Coupon implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;//bigint auto_increment
    private String name;
    private Integer type;//1优惠券(金额)2优惠券(天数)3折扣券(金额)4折扣券(天数)
    private BigDecimal reach;//使用券的门槛
    private BigDecimal discount;//优惠的最大金额或折扣
    private LocalDateTime startTime;//datetime      default CURRENT_TIMESTAMP not null comment '默认可使用的开始时间为现在',
    private LocalDateTime endTime;//datetime      default CURRENT_TIMESTAMP not null comment '默认可使用时间为获得时间的后7天',
    @TableField(fill = FieldFill.INSERT) //插入时填充字段
    private LocalDateTime createTime;//datetime      default CURRENT_TIMESTAMP not null
}
