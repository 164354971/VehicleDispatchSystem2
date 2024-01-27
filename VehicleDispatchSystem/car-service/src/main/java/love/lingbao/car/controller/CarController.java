package love.lingbao.car.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.lingbao.car.domain.po.Car;
import love.lingbao.car.domain.po.Model;
import love.lingbao.car.service.ICarService;
import love.lingbao.car.service.IDisableTimeService;
import love.lingbao.common.domain.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "车辆模块")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/cars")
public class CarController {

    private final IDisableTimeService disableTimeService;

    private final ICarService carService;

    /**
     * 根据型号id查找车辆列表
     * @param modelId 型号id
     * @return
     */
    @ApiOperation("根据型号id查找车辆列表")
    @GetMapping("/list/{modelId}")
    public R<List<Car>> findCarList(@PathVariable Integer modelId){
        log.info("/cars/list/{modelId} get -> findCarList: modelId = {}; 根据型号id查找车辆列表", modelId);
        return R.ok(carService.findCarList(modelId));
    }

    @ApiOperation("查询车辆在该时间段是否可用")
    @GetMapping("/{carId}/{startTime}/{endTime}")
    public boolean isUsableCarTime(
            @PathVariable Long carId,
            @PathVariable String startTime,
            @PathVariable String endTime){
        log.info("/cars/{carId}/{startTime}/{endTime} get -> isUsableCarTime: " +
                "carId = {}, startTime = {}, endTime = {}; 查询车辆在该时间段是否可用", carId, startTime, endTime);
        return disableTimeService.isUsableCarTime(carId, startTime, endTime);
    }

    @ApiOperation("根据id查询车辆")
    @GetMapping("/{carId}")
    public Car getById(@PathVariable Integer carId){
        log.info("/cars/{carId} get -> getById: " +
                "carId = {}; 根据id查询车辆", carId);
        return carService.getById(carId);
    }
}
