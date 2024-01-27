package love.lingbao.car.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import love.lingbao.car.domain.po.Model;
import love.lingbao.car.mapper.ModelMapper;
import love.lingbao.car.service.IModelService;
import love.lingbao.common.domain.RedisConstants;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ModelServiceImpl extends ServiceImpl<ModelMapper, Model> implements IModelService {

    private final RedisTemplate<String, ? extends Object> redisTemplate;

    @Override
    public List<Model> findModelList(Integer brandId) {
        //从redis中获取数据
        ListOperations<String, Model> listOperations = (ListOperations<String, Model>) redisTemplate.opsForList();
        List<Model> modelList = listOperations.range(RedisConstants.MODEL_LIST_KEY + brandId, 0, -1);
        //redis没有数据再去mysql找
        if(modelList == null || modelList.size() == 0){
            modelList = list(new LambdaQueryWrapper<Model>().eq(Model::getBrandId, brandId));
        }
        if(modelList.size() != 0){
            listOperations.rightPushAll(RedisConstants.MODEL_LIST_KEY + brandId, modelList);
            redisTemplate.expire(RedisConstants.MODEL_LIST_KEY + brandId, 7L, TimeUnit.DAYS);
        }
        return modelList;
    }
}
