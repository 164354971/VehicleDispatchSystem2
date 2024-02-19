package cn.lingbaocrisps.coupon.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.lingbaocrisps.coupon.domain.po.CouponUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface CouponUserMapper extends BaseMapper<CouponUser> {

    @Update("update coupon_user set status = #{couponUserStatus} where id = #{couponUserId} and status = #{oldCouponUserStatus}")
    boolean updateCouponUserStatus(Long couponUserId, Integer couponUserStatus, Integer oldCouponUserStatus);
}
