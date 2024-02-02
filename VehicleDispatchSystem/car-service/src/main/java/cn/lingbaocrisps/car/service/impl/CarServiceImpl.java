package cn.lingbaocrisps.car.service.impl;

import cn.lingbaocrisps.common.utils.RedisTools;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import cn.lingbaocrisps.car.domain.po.Car;
import cn.lingbaocrisps.car.mapper.CarMapper;
import cn.lingbaocrisps.car.service.ICarService;
import cn.lingbaocrisps.common.domain.RedisConstants;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CarServiceImpl extends ServiceImpl<CarMapper, Car> implements ICarService {

    private final RedisTools redisTools;

    @Override
    public List<Car> findCarList(Integer modelId) {
        List<Car> carList = redisTools.range(RedisConstants.CAR_LIST_KEY + modelId, 0, -1);
        if(carList == null || carList.size() == 0){
            carList = list(new LambdaQueryWrapper<Car>().eq(Car::getModelId, modelId));
            if(carList.size() != 0){
                redisTools.rightPushAll(RedisConstants.CAR_LIST_KEY + modelId, carList);
                redisTools.expire(RedisConstants.CAR_LIST_KEY + modelId, 7L, TimeUnit.DAYS);
            }
        }
        return carList;
    }

    @Override
    public BigDecimal getDayPrice(Integer id) {
        return this.baseMapper.getDayPrice(id);
    }
}
