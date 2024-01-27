package love.lingbao.api.client;

import love.lingbao.api.domain.po.Car;
import love.lingbao.api.domain.po.Coupon;
import love.lingbao.api.domain.po.CouponUser;
import love.lingbao.common.domain.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
}
