package cn.lingbaocrisps.coupon.domain.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponUser implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;//bigint auto_increment
    private Long couponId;//bigint auto_increment
    private Integer userId;//bigint auto_increment
    private Integer status;//0禁用1启用2已使用
    private LocalDateTime startTime;//datetime      default CURRENT_TIMESTAMP not null comment '默认可使用的开始时间为现在',
    private LocalDateTime endTime;//datetime      default CURRENT_TIMESTAMP not null comment '默认可使用时间为获得时间的后7天',
    @TableField(fill = FieldFill.INSERT) //插入时填充字段
    private LocalDateTime createTime;//datetime      default CURRENT_TIMESTAMP not null
    @TableField(fill = FieldFill.INSERT_UPDATE) //插入时填充字段
    private LocalDateTime updateTime;//datetime      default CURRENT_TIMESTAMP not null
}
