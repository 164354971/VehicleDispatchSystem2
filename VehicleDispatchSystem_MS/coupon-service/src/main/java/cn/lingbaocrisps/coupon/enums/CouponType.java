package cn.lingbaocrisps.coupon.enums;

import lombok.Getter;

@Getter
public enum CouponType {
    NORMAL(0, "无优惠券"),
    COUPON_AMOUNT(1, "金额优惠券"),
    COUPON_DAYS(2, "天数优惠券"),
    DISCOUNT_AMOUNT(3, "金额折扣券"),
    DISCOUNT_DAYS(4, "天数折扣券");

    private final int value;
    private final String desc;

    CouponType(int value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public boolean equalsValue(Integer value) {
        if (value == null) {
            return false;
        }
        return getValue() == value;
    }
}
