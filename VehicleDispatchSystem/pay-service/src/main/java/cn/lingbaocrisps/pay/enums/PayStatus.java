package cn.lingbaocrisps.pay.enums;

import lombok.Getter;

@Getter
public enum PayStatus {

    WAIT_SUBMIT(0, "待提交"),
    WAIT_PAY(1, "待支付"),
    PAY_FAIL(2, "支付失败"),
    PAY_SUCCESS(3, "支付成功"),
    PAY_CANCEL(4, "支付取消"),
    PAY_TIMEOUT(5, "支付超时"),
    PAY_REFUND(6, "退款"),
    PAY_REFUND_FAIL(7, "退款失败"),
    PAY_REFUND_SUCCESS(8, "退款成功"),
    PAY_REFUND_CANCEL(9, "退款取消"),
    PAY_REFUND_TIMEOUT(10, "退款超时"),
    ;
    private final int value;
    private final String desc;

    PayStatus(int value, String desc) {
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
