package cn.lingbaocrisps.car.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.lingbaocrisps.car.domain.po.DisableTime;

public interface IDisableTimeService extends IService<DisableTime> {

    boolean isUsableCarTime(Long carId, String startTime, String endTime);
}
