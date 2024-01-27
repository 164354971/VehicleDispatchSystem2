package love.lingbao.car.service;

import com.baomidou.mybatisplus.extension.service.IService;
import love.lingbao.car.domain.po.Model;
import love.lingbao.car.domain.vo.BrandVO;

import java.util.List;

public interface IModelService extends IService<Model> {

    List<Model> findModelList(Integer id);
}
