package love.lingbao.car.domain.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

@Data
@EqualsAndHashCode
@TableName("model")
public class Model implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;// bigint auto_increment
    private Integer brandId;// bigint                     not null comment '车辆品牌主键',
    private Integer seatNumber;// tinyint       default 5    null,

    private BigDecimal minPrice;// decimal(9, 2) default 0.00 not null comment '型号最低价',

    private Boolean recommend;// tinyint(1)    default 0    not null comment '款式是否推荐',

    private String area;// enum ('永川', '巴南', '全部') comment '车辆存在对的位置',
    private String model;// varchar(32)                not null comment '车辆型号',
    private String type;// varchar(16)                not null comment '车辆类型',
    private String energy;// varchar(16)                not null comment '使用能源方式',
    private String img;// varchar(255)               null comment '型号图片',
    private String initial;// char          default 'A'  not null comment '品牌首字母',
    private String brand;// varchar(32)                null comment '品牌名称',
    private String marketPrice;// varchar(30)                null comment '市售价（字符串）',
}
