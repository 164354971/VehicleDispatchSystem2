package cn.lingbaocrisps.user.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;
import cn.lingbaocrisps.common.exception.BadRequestException;

@Getter
public enum UserStatus {

    FROZEN(0, "禁止使用"),
    NORMAL(1, "已激活"),
            ;
    @EnumValue
    int value;
    String desc;

    UserStatus(int value, String desc){
        this.value = value;
        this.desc = desc;
    }

    public static UserStatus of(int value) {
        if (value == 0) {
            return FROZEN;
        }
        if (value == 1) {
            return NORMAL;
        }
        throw new BadRequestException("账户状态错误");
    }
}
