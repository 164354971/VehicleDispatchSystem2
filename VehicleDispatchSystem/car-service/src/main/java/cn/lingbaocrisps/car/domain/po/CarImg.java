package cn.lingbaocrisps.car.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("car_img")
public class CarImg implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;//  bigint auto_increment
    private Integer carId;//int                             not null comment '车辆id',
    private String img;//  varchar(255)										 not null comment '车辆图片'
}
