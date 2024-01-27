package love.lingbao.car.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.lingbao.car.domain.vo.BrandVO;
import love.lingbao.car.service.IBrandService;
import love.lingbao.common.domain.R;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api(tags = "车辆品牌模块")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/brands")
public class BrandController {

    private final IBrandService brandService;

    /**
     * 根据字符串查找品牌列表
     * @param str 查找字符串
     * @return
     */
    @ApiOperation("根据字符串查找品牌列表")
    @GetMapping("/list")
    public R<List<BrandVO>> findBrandVOList(@RequestParam(required = false) String str){
        log.info("/brands/list get -> findBrandVOList: str = {}; 根据字符串查找品牌列表", str);
        return R.ok(brandService.findBrandVOList(str));
    }
}
