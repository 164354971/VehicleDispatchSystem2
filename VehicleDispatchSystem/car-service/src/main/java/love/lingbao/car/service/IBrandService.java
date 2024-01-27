package love.lingbao.car.service;

import com.baomidou.mybatisplus.extension.service.IService;
import love.lingbao.car.domain.po.Brand;
import love.lingbao.car.domain.vo.BrandVO;

import java.util.List;

public interface IBrandService extends IService<Brand> {
    List<BrandVO> findBrandVOList(String name);
}
