package cn.lingbaocrisps.car.service;

import cn.lingbaocrisps.car.domain.po.Parkade;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IParkadeService extends IService<Parkade> {
    List<Parkade> findParkadeListByArea(String area);
}
