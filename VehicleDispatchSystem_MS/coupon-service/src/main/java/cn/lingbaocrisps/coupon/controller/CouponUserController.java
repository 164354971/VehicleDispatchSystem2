package cn.lingbaocrisps.coupon.controller;

import cn.lingbaocrisps.coupon.domain.vo.CouponVO;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import cn.lingbaocrisps.common.domain.R;
import cn.lingbaocrisps.coupon.domain.po.CouponUser;
import cn.lingbaocrisps.coupon.service.ICouponUserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
