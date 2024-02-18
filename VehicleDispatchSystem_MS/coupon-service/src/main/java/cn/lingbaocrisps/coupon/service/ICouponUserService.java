package cn.lingbaocrisps.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.lingbaocrisps.coupon.domain.po.CouponUser;

import java.util.List;

public interface ICouponUserService extends IService<CouponUser> {

    boolean updateStatus(Long couponId, Integer status, Integer oldCouponUserStatus);
}
