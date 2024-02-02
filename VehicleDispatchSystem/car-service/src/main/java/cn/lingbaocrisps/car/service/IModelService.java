package cn.lingbaocrisps.car.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.lingbaocrisps.car.domain.po.Model;

import java.util.List;

public interface IModelService extends IService<Model> {

    List<Model> findModelList(Integer id);

    IPage<Model> getModelPage(Integer page, String area);
}
