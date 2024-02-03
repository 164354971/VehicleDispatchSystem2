package cn.lingbaocrisps.car.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.lingbaocrisps.car.domain.po.DisableTime;
import org.apache.ibatis.annotations.Param;


public interface DisableTimeMapper extends BaseMapper<DisableTime> {
    Integer notTimeCarCount(@Param("carId")Long carId, @Param("startTime")String startTime, @Param("endTime")String endTime);
}
