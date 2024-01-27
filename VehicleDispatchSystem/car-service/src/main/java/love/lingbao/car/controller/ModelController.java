package love.lingbao.car.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.lingbao.car.domain.po.Model;
import love.lingbao.car.domain.vo.BrandVO;
import love.lingbao.car.service.IBrandService;
import love.lingbao.car.service.IModelService;
import love.lingbao.common.domain.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "车辆品牌模块")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/models")
public class ModelController {

    private final IModelService modelService;

    /**
     * 根据品牌id查找型号列表
     * @param brandId 品牌id
     * @return
     */
    @ApiOperation("根据品牌id查找型号列表")
    @GetMapping("/list/{brandId}")
    public R<List<Model>> findModelList(@PathVariable Integer brandId){
        log.info("/models/list/{brandId} get -> findModelList: brandId = {}; 根据品牌id查找型号列表", brandId);
        return R.ok(modelService.findModelList(brandId));
    }
}
