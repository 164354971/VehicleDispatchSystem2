package cn.lingbaocrisps.coupon.service.impl;

import cn.lingbaocrisps.common.utils.UserContext;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.lingbaocrisps.coupon.domain.po.CouponUser;
import cn.lingbaocrisps.coupon.mapper.CouponUserMapper;
import cn.lingbaocrisps.coupon.service.ICouponUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CouponUserServiceImpl extends ServiceImpl<CouponUserMapper, CouponUser> implements ICouponUserService {

    private final CouponUserMapper couponUserMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStatus(Long couponId, Integer status, Integer oldCouponUserStatus) {
        boolean flag = couponUserMapper.updateCouponUserStatus(couponId, status, oldCouponUserStatus);
        if (!flag)
            throw new RuntimeException("更新用户优惠券状态失败");
        return flag;
    }
}
