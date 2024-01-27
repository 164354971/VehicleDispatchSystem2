package love.lingbao.car.service;

import com.baomidou.mybatisplus.extension.service.IService;
import love.lingbao.car.domain.po.Brand;
import love.lingbao.car.domain.po.DisableTime;

import java.time.LocalDateTime;

public interface IDisableTimeService extends IService<DisableTime> {

    boolean isUsableCarTime(Long carId, String startTime, String endTime);
}
