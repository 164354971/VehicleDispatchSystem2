package cn.lingbaocrisps.coupon.service;

import cn.lingbaocrisps.coupon.domain.po.CouponUser;
import cn.lingbaocrisps.coupon.domain.vo.CouponVO;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.lingbaocrisps.coupon.domain.po.Coupon;

import java.util.List;

public interface ICouponService extends IService<Coupon> {

    List<CouponVO> listByUserIdAndCurrentTime(String currentTime);
}
