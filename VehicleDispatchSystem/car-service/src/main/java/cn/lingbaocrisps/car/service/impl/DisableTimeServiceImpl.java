package cn.lingbaocrisps.car.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import cn.lingbaocrisps.car.domain.po.DisableTime;
import cn.lingbaocrisps.car.mapper.DisableTimeMapper;
import cn.lingbaocrisps.car.service.IDisableTimeService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DisableTimeServiceImpl extends ServiceImpl<DisableTimeMapper, DisableTime> implements IDisableTimeService {

    private final DisableTimeMapper disableTimeMapper;

    /**
     *
     * */
    @Override
    public boolean isUsableCarTime(Long carId, String startTime, String endTime) {
        //将时间字符串转为LocalDateTime
        /*LocalDateTime startTime = LocalDateTime.parse(start, LocalDateTimeUtils.FORMATTER);
        LocalDateTime endTime = LocalDateTime.parse(end, LocalDateTimeUtils.FORMATTER);

        log.debug(startTime.format(LocalDateTimeUtils.FORMATTER));
        log.debug(endTime.format(LocalDateTimeUtils.FORMATTER));*/

        /*LambdaQueryWrapper<DisableTime> lqw = new LambdaQueryWrapper<>();
        lqw.le(DisableTime::getEndTime, startTime).or().ge(DisableTime::getStartTime, endTime);

        //获取所有满足条件的数据
        List<DisableTime> disableTimeList = list(lqw);
        disableTimeList.forEach(e -> log.debug(e.toString()));*/
        Integer count = disableTimeMapper.notTimeCarCount(carId, startTime, endTime);
        log.debug(String.valueOf(count));
        return count == 0;
    }
}
