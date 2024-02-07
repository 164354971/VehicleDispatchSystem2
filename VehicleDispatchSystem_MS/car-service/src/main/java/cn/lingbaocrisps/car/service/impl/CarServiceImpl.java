package cn.lingbaocrisps.car.service.impl;

import cn.lingbaocrisps.car.domain.dto.CarPageFormDTO;
import cn.lingbaocrisps.car.domain.vo.CarPageFormVO;
import cn.lingbaocrisps.common.exception.BadRequestException;
import cn.lingbaocrisps.common.utils.BeanUtils;
import cn.lingbaocrisps.common.utils.RedisTools;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import cn.lingbaocrisps.car.domain.po.Car;
import cn.lingbaocrisps.car.mapper.CarMapper;
import cn.lingbaocrisps.car.service.ICarService;
import cn.lingbaocrisps.common.domain.RedisConstants;
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
                synchronized (this) {
                    redisTools.delete(RedisConstants.CAR_LIST_KEY + modelId);
                    redisTools.rightPushAll(RedisConstants.CAR_LIST_KEY + modelId, carList);
                    redisTools.expire(RedisConstants.CAR_LIST_KEY + modelId, 7L, TimeUnit.DAYS);
                }
            }
        }
        return carList;
    }

    @Override
    public BigDecimal getDayPrice(Integer id) {
        return this.baseMapper.getDayPrice(id);
    }

    @Override
    public IPage<CarPageFormVO> findCarPage(CarPageFormDTO carPageFormDTO) {
        if(carPageFormDTO.getStartTime() == null || carPageFormDTO.getEndTime() == null)
            throw new BadRequestException("查询时间不能为空");

        //1.品牌信息体的分页数据存放
        IPage<Car> pageInfo = new Page<>(carPageFormDTO.getPage(), carPageFormDTO.getPageSize());

        LambdaQueryWrapper<Car> queryWrapper = new LambdaQueryWrapper<>();

        //2.1 通过选择的停车场的ID查找对应的停车场车辆
        queryWrapper.eq(Car::getParkadeId, carPageFormDTO.getParkadeId());

        //2.4 类型
        if(!carPageFormDTO.getTypeList().get(0).equals("全部"))
            queryWrapper.in(Car::getType, carPageFormDTO.getTypeList());

        //2.5 价格区间
        if(!carPageFormDTO.getStartPrice().equals(new BigDecimal(-1))){
            queryWrapper.ge(Car::getDayPrice, carPageFormDTO.getStartPrice());
            if(!carPageFormDTO.getEndPrice().equals(new BigDecimal(-1)))
                queryWrapper.le(Car::getDayPrice, carPageFormDTO.getEndPrice());
        }

        if(!carPageFormDTO.getNln()){
            //2.6 是否会员免押
            if(carPageFormDTO.getNoDeposit()){
                queryWrapper.eq(Car::getNoDeposit, carPageFormDTO.getNoDeposit());
            }

            //2.7 是否豪华
            if(carPageFormDTO.getLuxury()){
                queryWrapper.eq(Car::getLuxury, carPageFormDTO.getLuxury());
            }

            //2.8 是否新能源
            if(carPageFormDTO.getNewEnergy()){
                queryWrapper.eq(Car::getNewEnergy, carPageFormDTO.getNewEnergy());
            }
        }

        //3.排序（价格）
        if(carPageFormDTO.getSort() == 1)
            queryWrapper.orderByAsc(Car::getDayPrice);
        else if(carPageFormDTO.getSort() == 2)
            queryWrapper.orderByDesc(Car::getDayPrice);
        queryWrapper.orderByDesc(Car::getNoDeposit, Car::getLuxury, Car::getNewEnergy);

        //4.数据导入info
        page(pageInfo, queryWrapper);
        List<CarPageFormVO> pageInfoRecords = BeanUtils.copyList(pageInfo.getRecords(), CarPageFormVO.class);

        IPage<CarPageFormVO> carVOIPage = new Page<>(pageInfo.getTotal(), pageInfo.getPages(), pageInfo.getCurrent());
        carVOIPage.setRecords(pageInfoRecords);
        return carVOIPage;
    }
}
