package cn.lingbaocrisps.evaluate.service;

import cn.lingbaocrisps.evaluate.domain.dto.EvaluateReplyDTO;
import cn.lingbaocrisps.evaluate.domain.po.EvaluateReply;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.TreeMap;

public interface IEvaluateReplyService extends IService<EvaluateReply> {

    List<EvaluateReply> getEvaluateReplyList(Long evaluateId);

    void addEvaluateReply(EvaluateReplyDTO evaluateReplyDTO);
}
