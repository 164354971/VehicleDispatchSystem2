package love.lingbao.coupon.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import love.lingbao.coupon.domain.po.Coupon;
import love.lingbao.coupon.mapper.CouponMapper;
import love.lingbao.coupon.service.ICouponService;
import org.springframework.stereotype.Service;

@Service
public class CouponServiceImpl extends ServiceImpl<CouponMapper, Coupon> implements ICouponService {
}
