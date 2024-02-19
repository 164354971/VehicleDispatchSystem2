package cn.lingbaocrisps.car.controller;

import cn.lingbaocrisps.car.domain.dto.CarPageFormDTO;
import cn.lingbaocrisps.car.domain.vo.CarImgVO;
import cn.lingbaocrisps.car.domain.vo.CarPageFormVO;
import cn.lingbaocrisps.car.service.ICarImgService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import cn.lingbaocrisps.car.domain.po.Car;
import cn.lingbaocrisps.car.service.ICarService;
import cn.lingbaocrisps.car.service.IDisableTimeService;
import cn.lingbaocrisps.common.domain.R;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Api(tags = "车辆模块")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/cars")
public class CarController {

    private final IDisableTimeService disableTimeService;

    private final ICarService carService;
    private final ICarImgService carImgService;

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

    @ApiOperation("根据id查询车辆图片列表")
    @GetMapping("/img/{carId}/{imgNumber}")
    public R<CarImgVO> getCarImgVO(@PathVariable Integer carId, @PathVariable Integer imgNumber){
        log.info("/cars/img/{carId}/{imgNumber} get -> getCarImgVO: " +
                "carId = {}, imgNumber = {}; 根据id查询车辆", carId, imgNumber);
        return R.ok(carImgService.getCarImgVO(carId, imgNumber));
    }

    @ApiOperation("根据iD查询车辆日租价格")
    @GetMapping("/price/{carId}")
    public BigDecimal getDayPrice(@PathVariable Integer carId){
        log.info("/cars/price/{carId} get -> getDayPrice: " +
                "carId = {}; 根据iD查询车辆日租价格", carId);
        return carService.getDayPrice(carId);
    }

    @ApiOperation("根据车辆分页查询表单查询车辆数据")
    @PostMapping("/page")
    public R<IPage<CarPageFormVO>> findCarPage(@RequestBody CarPageFormDTO carPageFormDTO){
        log.info("/cars/page post -> carPageFormDTO = {}; 车辆分页查询", carPageFormDTO.toString());
        return R.ok(carService.findCarPage(carPageFormDTO));
    }
}
