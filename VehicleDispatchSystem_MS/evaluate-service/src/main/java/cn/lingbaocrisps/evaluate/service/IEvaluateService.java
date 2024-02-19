package cn.lingbaocrisps.evaluate.service;

import cn.lingbaocrisps.evaluate.domain.dto.EvaluateQueryDTO;
import cn.lingbaocrisps.evaluate.domain.po.Evaluate;
import cn.lingbaocrisps.evaluate.domain.vo.EvaluateDetailVO;
import cn.lingbaocrisps.evaluate.domain.vo.EvaluateVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Set;

public interface IEvaluateService extends IService<Evaluate> {
    List<EvaluateVO> getNewEvaluateList(Integer brandId);

    IPage<EvaluateDetailVO> getEvaluateDetailPage(EvaluateQueryDTO queryDTO);
}
