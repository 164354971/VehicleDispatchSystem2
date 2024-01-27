package love.lingbao.coupon.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.lingbao.common.domain.R;
import love.lingbao.coupon.domain.po.CouponUser;
import love.lingbao.coupon.service.ICouponUserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "用户使用的优惠券模块")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/couponUsers")
public class CouponUserController {

    private final ICouponUserService couponUserService;
    /**
     * 通过用户优惠券id获取用户优惠券信息
     * */
    @GetMapping("/{couponUserId}")
    public R<CouponUser> findByCouponUserId(@PathVariable Long couponUserId) {
        return R.ok(couponUserService.getById(couponUserId));
    }
}
