package cn.lingbaocrisps.car.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.lingbaocrisps.car.domain.po.Brand;
import cn.lingbaocrisps.car.domain.vo.BrandVO;

import java.util.List;

public interface IBrandService extends IService<Brand> {
    List<BrandVO> findBrandVOList(String name);
}
