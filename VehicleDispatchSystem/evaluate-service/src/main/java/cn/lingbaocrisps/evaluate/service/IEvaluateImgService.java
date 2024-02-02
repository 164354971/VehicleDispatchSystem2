package cn.lingbaocrisps.evaluate.service;

import cn.lingbaocrisps.evaluate.domain.po.EvaluateImg;
import cn.lingbaocrisps.evaluate.domain.vo.EvaluateImgVO;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IEvaluateImgService extends IService<EvaluateImg> {
    EvaluateImgVO getEvaluateImgVO(Long evaluateId, Integer carId);
}
