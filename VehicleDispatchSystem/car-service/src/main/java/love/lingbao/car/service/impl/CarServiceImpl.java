package love.lingbao.car.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import love.lingbao.car.domain.po.Car;
import love.lingbao.car.mapper.CarMapper;
import love.lingbao.car.service.ICarService;
import love.lingbao.common.domain.RedisConstants;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CarServiceImpl extends ServiceImpl<CarMapper, Car> implements ICarService {

    private final RedisTemplate<String, ? extends Object> redisTemplate;

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public List<Car> findCarList(Integer modelId) {
        ListOperations<String, Car> listOperations = (ListOperations<String, Car>) redisTemplate.opsForList();
        List<Car> carList = listOperations.range(RedisConstants.CAR_LIST_KEY + modelId, 0, -1);
        if(carList == null || carList.size() == 0){
            carList = list(new LambdaQueryWrapper<Car>().eq(Car::getModelId, modelId));
        }
        if(carList.size() != 0){
            listOperations.rightPushAll(RedisConstants.CAR_LIST_KEY + modelId, carList);
            redisTemplate.expire(RedisConstants.CAR_LIST_KEY + modelId, 7L, TimeUnit.DAYS);
        }
        return carList;
    }
}
