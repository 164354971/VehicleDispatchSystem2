package cn.lingbaocrisps.api.client;

import cn.lingbaocrisps.api.domain.po.Car;
import cn.lingbaocrisps.api.domain.vo.CarImgVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;

@FeignClient(value = "vds-car-service")
public interface CarClient {

    /**
    * 查询车辆在该时间段是否可用
    * */
    @GetMapping("/cars/{carId}/{startTime}/{endTime}")
    boolean isUsableCarTime(@PathVariable Integer carId, @PathVariable String startTime, @PathVariable String endTime);

    /**
     * 查询车辆id是否合法
     * */
    @GetMapping("/cars/{carId}")
    Car getById(@PathVariable Integer carId);

    /**
     * 获取车辆图片列表(imgNumber为0表示获取全部列表)
     * */
    @GetMapping("/cars/img/{carId}/{imgNumber}")
    CarImgVO getCarImgVO(@PathVariable Integer carId, @PathVariable Integer imgNumber);

    /**
     * 获取车辆日租价格
     * */
    @GetMapping("/cars/price/{carId}")
    BigDecimal getDayPrice(@PathVariable Integer carId);
}
