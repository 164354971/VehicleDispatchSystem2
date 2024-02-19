package cn.lingbaocrisps.car.controller;

import cn.lingbaocrisps.car.domain.po.Parkade;
import cn.lingbaocrisps.car.service.IParkadeService;
import cn.lingbaocrisps.common.domain.R;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "停车场模块")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/parkades")
public class ParkadeController {
    private final IParkadeService parkadeService;
    @GetMapping("/{id}")
    public R<Parkade> findParkadeById(@PathVariable Integer id) {
        log.info("/parkades/{id} get -> findParkadeById: id = {}, 查找停车场", id);
        return R.ok(parkadeService.getById(id));
    }

    @GetMapping("/list/{area}")
    public R<List<Parkade>> findParkadeListByArea(@PathVariable String area) {
        log.info("/parkades/list/{area} get -> findParkadeListByArea: id = {}, 根据地区查找停车场列表", area);
        return R.ok(parkadeService.findParkadeListByArea(area));
    }
}
