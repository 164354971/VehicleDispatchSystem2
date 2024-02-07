package cn.lingbaocrisps.coupon.service.impl;

import cn.lingbaocrisps.common.utils.BeanUtils;
import cn.lingbaocrisps.common.utils.UserContext;
import cn.lingbaocrisps.coupon.domain.po.CouponUser;
import cn.lingbaocrisps.coupon.domain.vo.CouponVO;
import cn.lingbaocrisps.coupon.service.ICouponUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.lingbaocrisps.coupon.domain.po.Coupon;
import cn.lingbaocrisps.coupon.mapper.CouponMapper;
import cn.lingbaocrisps.coupon.service.ICouponService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CouponServiceImpl extends ServiceImpl<CouponMapper, Coupon> implements ICouponService {
    
    private final ICouponUserService couponUserService;
    @Override
    public List<CouponVO> listByUserIdAndCurrentTime(String currentTimeString) {
        Integer userId = UserContext.getUser();

        SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        Date currentTime = null;
        try {
            currentTime = sdf.parse(currentTimeString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        LambdaQueryWrapper<CouponUser> lqw = new LambdaQueryWrapper<CouponUser>();
        lqw.eq(CouponUser::getUserId, userId).eq(CouponUser::getStatus, 1);
        lqw.le(CouponUser::getStartTime, currentTime).ge(CouponUser::getEndTime, currentTime);
        List<CouponUser> couponUserList = couponUserService.list(lqw);
        //2.查找可用的优惠券id对应的优惠券
        LambdaQueryWrapper<Coupon> clqw = new LambdaQueryWrapper<>();
        Set<Long> couponIdSet = new HashSet<>();
        couponUserList.forEach(e -> {
            couponIdSet.add(e.getCouponId());
            //map.put(e.getCouponId(), e);
        });
        clqw.in(couponIdSet.size() != 0, Coupon::getId, couponIdSet);
        List<Coupon> couponList = list(clqw);

        //3.返回用户可使用的优惠券（注意此时把时间从优惠劵本身的使用时间段修改为用户手中的优惠券的可使用时间段）
        List<CouponVO> couponVOList = new LinkedList<>();
        couponList.forEach(e -> {
            //创建迭代器去掉已筛选过的优惠券，减少遍历个数
            Iterator<CouponUser> iterator = couponUserList.listIterator();
            while(iterator.hasNext()){
                CouponUser couponUser = iterator.next();
                //比对成功，修改优惠券时间，并添加进VOList
                if (e.getId().equals(couponUser.getCouponId())) {
                    e.setStartTime(couponUser.getStartTime());
                    e.setEndTime(couponUser.getEndTime());
                    CouponVO couponVO = BeanUtils.copyBean(e, CouponVO.class);
                    couponVOList.add(couponVO);
                    iterator.remove();
                }
            }
        });
        return couponVOList;
    }
}
