package love.lingbao.user.domain.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import love.lingbao.user.domain.dto.UserDTO;
import love.lingbao.user.enums.UserStatus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;         //int auto_increment comment '主键id'
    private String username;    //varchar(16)                            not null comment '用户名',
    private String password;    //varchar(16)                            not null comment '登录密码',
    private String img;    //varchar(255)           default '' not null comment '头像',
    private String phone;       //char(11)                            not null comment '手机号',
    private String name;        //varchar(10)                            not null comment '名字',
    private String gender;      //enum ('男', '女') default '男'      not null comment '性别'
    private String nickname;        //varchar(10)                           not null comment '昵称',
    private UserStatus status;         //用户账号状态
    private String idNumber;        //varchar(18)                            not null comment '身份证号码',
    private String driverType;      //enum('C1驾驶证'，'C2驾驶证',..)          not null comment '驾照类型',

    private BigDecimal salary;       //decimal(9,2) not null comment '账户余额'//

    private Integer vipLevel;       //tinyint default 0 not null comment '会员等级'//

    private String paymentPassword;       //char(6) null comment '支付密码'//

    @TableField(fill = FieldFill.INSERT) //插入时填充字段
    private LocalDateTime createTime;   //创建时间

    @TableField(fill = FieldFill.INSERT_UPDATE) //插入和更新时填充字段
    private LocalDateTime updateTime;   //资料更新时间
}
