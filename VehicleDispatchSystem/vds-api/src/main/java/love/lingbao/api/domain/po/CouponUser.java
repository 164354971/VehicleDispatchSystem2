package love.lingbao.api.domain.po;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class CouponUser implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;//bigint auto_increment
    private Long couponId;//bigint auto_increment
    private Integer userId;//bigint auto_increment
    private Integer status;//0禁用1启用2已使用
    private LocalDateTime startTime;//datetime      default CURRENT_TIMESTAMP not null comment '默认可使用的开始时间为现在',
    private LocalDateTime endTime;//datetime      default CURRENT_TIMESTAMP not null comment '默认可使用时间为获得时间的后7天',
    private LocalDateTime createTime;//datetime      default CURRENT_TIMESTAMP not null
    private LocalDateTime updateTime;//datetime      default CURRENT_TIMESTAMP not null
}
