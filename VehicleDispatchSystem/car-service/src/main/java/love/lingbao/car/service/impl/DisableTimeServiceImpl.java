package love.lingbao.car.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import love.lingbao.car.domain.po.Brand;
import love.lingbao.car.domain.po.DisableTime;
import love.lingbao.car.mapper.BrandMapper;
import love.lingbao.car.mapper.DisableTimeMapper;
import love.lingbao.car.service.IBrandService;
import love.lingbao.car.service.IDisableTimeService;
import love.lingbao.common.utils.LocalDateTimeUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
