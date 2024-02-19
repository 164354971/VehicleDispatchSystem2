package cn.lingbaocrisps.api.client;

import cn.lingbaocrisps.api.domain.po.Coupon;
import cn.lingbaocrisps.api.domain.po.CouponUser;
import cn.lingbaocrisps.common.domain.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "vds-coupon-service")
public interface CouponClient {

    /**
     * 通过用户优惠券id查询优惠券的信息
     * */
    @GetMapping("/coupons/{couponId}")
    R<Coupon> findCouponById(@PathVariable Long couponId);

    /**
     * 通过用户优惠券id获取用户优惠券信息
     * */
    @GetMapping("/couponUsers/{couponUserId}")
    R<CouponUser> findByCouponUserId(@PathVariable Long couponUserId);

    /**
     * 通过用户优惠券id和原状态更新用户优惠券状态（乐观锁）
     * */
    @PutMapping("/couponUsers/update/{couponUserId}/{couponUserStatus}/{oldCouponUserStatus}")
    R<Boolean> updateCouponUser(@PathVariable Long couponUserId, @PathVariable Integer couponUserStatus, @PathVariable Integer oldCouponUserStatus);
}
