package cn.lingbaocrisps.coupon.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.lingbaocrisps.coupon.domain.po.Coupon;
import cn.lingbaocrisps.coupon.mapper.CouponMapper;
import cn.lingbaocrisps.coupon.service.ICouponService;
import org.springframework.stereotype.Service;

@Service
public class CouponServiceImpl extends ServiceImpl<CouponMapper, Coupon> implements ICouponService {
}
