package love.lingbao.car.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("`brand`")
public class Brand implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;         //int auto_increment comment '主键id'
    private String brand;       //varchar(32) not null unique comment '品牌'
    private String img;       //varchar(255) not null unique comment '图片链接'
    private String initial;       //char(1) not null unique comment '首字母'
    private String info;       //varchar(255) not null unique comment '品牌描述'
}
