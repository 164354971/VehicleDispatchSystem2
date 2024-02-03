package cn.lingbaocrisps.user.domain.po;

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
public class ThirdAccount implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;         //int auto_increment comment '主键id'
    private Integer userId              ;//bigint     default 0                 not null,
    private String thirdUniqueAccount ;//varchar(80)                          not null comment '第三方唯一用户id，可以是微信的openid，可以是QQ的openid，抑或苹果id',
    private Integer type                 ;//tinyint    default 0                 not null comment '标识第三方类型，这里规定1.代表微信，2.代表QQ，3.代表苹果',
    private Boolean bindFlag            ;//tinyint(1) default 0                 not null comment '标识是否绑定，1绑定，0解绑',
    @TableField(fill = FieldFill.INSERT) //插入时填充字段
    private LocalDateTime createTime          ;//datetime   default CURRENT_TIMESTAMP not null comment '账号绑定时间',
    @TableField(fill = FieldFill.INSERT_UPDATE) //插入和更新时填充字段
    private LocalDateTime updateTime          ;//datetime   default CURRENT_TIMESTAMP not null comment '账号更新时间',
}
