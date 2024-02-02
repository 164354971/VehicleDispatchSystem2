package cn.lingbaocrisps.coupon.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import cn.lingbaocrisps.common.domain.R;
import cn.lingbaocrisps.coupon.domain.po.Coupon;
import cn.lingbaocrisps.coupon.service.ICouponService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "优惠券模块")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/coupons")
public class CouponController {

    private final ICouponService couponService;

    /**
     * 通过用户优惠券id查询优惠券的信息
     * */
    @GetMapping("/{couponId}")
    public R<Coupon> findCouponById(@PathVariable Long couponId) {
        log.info("通过用户优惠券id查询优惠券的信息");
        return R.ok(couponService.getById(couponId));
    }
}
