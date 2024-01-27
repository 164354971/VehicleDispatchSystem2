package love.lingbao.car.service;

import com.baomidou.mybatisplus.extension.service.IService;
import love.lingbao.car.domain.po.Brand;
import love.lingbao.car.domain.po.Car;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface ICarService extends IService<Car> {


    List<Car> findCarList(Integer id);
}
