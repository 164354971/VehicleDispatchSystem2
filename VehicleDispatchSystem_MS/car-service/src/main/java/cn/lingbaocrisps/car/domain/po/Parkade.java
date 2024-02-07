package cn.lingbaocrisps.car.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("parkade")
public class Parkade implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;//int auto_increment
    private String areaSchool;//enum ('永川', '巴南') default '永川' not null comment '停车场所在校区',
    private String name;//varchar(32)                          not null comment '停车场名称',
    private String longitude;//varchar(16)           default ''     not null comment '停车场经度',
    private String latitude;//varchar(16)           default ''     not null comment '纬度',
    private String img;//varchar(255)          default ''     null comment '停车场封面图'
}
