package cn.lingbaocrisps.car.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import cn.lingbaocrisps.car.domain.po.Model;
import cn.lingbaocrisps.car.service.IModelService;
import cn.lingbaocrisps.common.domain.R;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation("首页展示车型")
    @GetMapping("/page/{page}/{area}")
    public R<IPage<Model>> getModelPage(@PathVariable Integer page, @PathVariable String area){
        log.info("/models/page/{page}/{area} get -> getModelPage: page = {}， area = {}, 首页展示车型", page, area);
        return R.ok(modelService.getModelPage(page, area));
    }
}
