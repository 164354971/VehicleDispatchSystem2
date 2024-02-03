package cn.lingbaocrisps.car.service.impl;

import cn.lingbaocrisps.car.domain.po.Car;
import cn.lingbaocrisps.car.domain.po.Model;
import cn.lingbaocrisps.car.mapper.ModelMapper;
import cn.lingbaocrisps.car.service.IModelService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import cn.lingbaocrisps.common.domain.RedisConstants;
import cn.lingbaocrisps.common.utils.RedisTools;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ModelServiceImpl extends ServiceImpl<ModelMapper, Model> implements IModelService {

    private final RedisTools redisTools;

    @Override
    public List<Model> findModelList(Integer brandId) {
        //从redis中获取数据
        List<Model> modelList = redisTools
                .range(RedisConstants.MODEL_LIST_KEY + brandId, 0, -1);
        if(modelList == null || modelList.size() == 0){
            modelList = list(new LambdaQueryWrapper<Model>().eq(Model::getBrandId, brandId));
            if(modelList.size() != 0){
                redisTools.rightPushAll(RedisConstants.MODEL_LIST_KEY + brandId, modelList);
                redisTools.expire(RedisConstants.MODEL_LIST_KEY + brandId, 7L, TimeUnit.DAYS);
            }
        }
        return modelList;
    }

    @Override
    public IPage<Model> getModelPage(Integer page, String area) {
        //尝试从redis中获取车型推荐数据
        Map<String, Model> modelMap = redisTools.hGetAll(RedisConstants.MODEL_HOT_MAP, Model.class);
        //redis没有数据或少于数据表中的车辆数据，则从mysql中获取数据
        if(modelMap == null || modelMap.size() < count()){
            List<Model> modelList = list(new LambdaQueryWrapper<Model>().in(Model::getArea, "全部", area));
            if(modelList != null && modelList.size() != 0){
                modelMap = modelList.stream()
                        .collect(Collectors.toMap(model -> String.valueOf(model.getId()), Function.identity()));
                //删除此键
                redisTools.delete(RedisConstants.MODEL_HOT_MAP);
                //重新赋值
                redisTools.hPutAll(RedisConstants.MODEL_HOT_MAP, modelMap);
            }
        }
        //重新设置过期时间
        redisTools.expire(RedisConstants.MODEL_HOT_MAP, 1L, TimeUnit.DAYS);
        if(modelMap == null || modelMap.size() == 0){
            return null;
        }
        //根据热度指数进行排序
        List<Model> modelList = modelMap.values().stream()
                .sorted((o1, o2) -> o2.getHotCount().compareTo(o1.getHotCount()))
                .collect(Collectors.toList());
        IPage<Model> pageInfo = new Page<>(page, 10);
        //给pageInfo赋值
        int modelListSize = modelList.size();
        if(modelListSize >= (page - 1) * 10)
            pageInfo.setRecords(modelList.subList((page - 1) * 10, modelListSize));

        return pageInfo;
    }
}
