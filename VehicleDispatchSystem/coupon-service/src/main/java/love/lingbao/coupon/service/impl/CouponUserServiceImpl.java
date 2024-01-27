package love.lingbao.coupon.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import love.lingbao.coupon.domain.po.Coupon;
import love.lingbao.coupon.domain.po.CouponUser;
import love.lingbao.coupon.mapper.CouponUserMapper;
import love.lingbao.coupon.service.ICouponUserService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

@Service
public class CouponUserServiceImpl extends ServiceImpl<CouponUserMapper, CouponUser> implements ICouponUserService {

}
