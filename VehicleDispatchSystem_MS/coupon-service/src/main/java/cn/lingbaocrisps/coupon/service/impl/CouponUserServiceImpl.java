package cn.lingbaocrisps.coupon.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.lingbaocrisps.coupon.domain.po.CouponUser;
import cn.lingbaocrisps.coupon.mapper.CouponUserMapper;
import cn.lingbaocrisps.coupon.service.ICouponUserService;
import org.springframework.stereotype.Service;

@Service
public class CouponUserServiceImpl extends ServiceImpl<CouponUserMapper, CouponUser> implements ICouponUserService {

}
