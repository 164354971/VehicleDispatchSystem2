package cn.lingbaocrisps.car.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.lingbaocrisps.car.domain.po.Car;

import java.math.BigDecimal;
import java.util.List;

public interface ICarService extends IService<Car> {


    List<Car> findCarList(Integer id);

    BigDecimal getDayPrice(Integer id);
}
