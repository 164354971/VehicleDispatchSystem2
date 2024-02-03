package cn.lingbaocrisps.pay.enums;

import lombok.Getter;

@Getter
public enum PayType {

    BALANCE(1, "余额支付"),
    WECHAT(2, "微信支付"),
    ALIPAY(3, "支付宝支付"),
    BANK_CARD(4, "扫码支付"),
    ;
    private final int value;
    private final String desc;

    PayType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public boolean equalsValue(Integer value){
        if (value == null) {
            return false;
        }
        return getValue() == value;
    }
}
