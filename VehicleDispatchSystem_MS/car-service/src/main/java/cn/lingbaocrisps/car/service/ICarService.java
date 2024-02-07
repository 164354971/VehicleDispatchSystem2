package cn.lingbaocrisps.car.service;

import cn.lingbaocrisps.car.domain.dto.CarPageFormDTO;
import cn.lingbaocrisps.car.domain.vo.CarPageFormVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.lingbaocrisps.car.domain.po.Car;

import java.math.BigDecimal;
import java.util.List;

public interface ICarService extends IService<Car> {


    List<Car> findCarList(Integer id);

    BigDecimal getDayPrice(Integer id);

    IPage<CarPageFormVO> findCarPage(CarPageFormDTO carPageFormDTO);
}
