package cn.lingbaocrisps.evaluate.service.impl;

import cn.lingbaocrisps.api.client.UserClient;
import cn.lingbaocrisps.common.exception.ForbiddenException;
import cn.lingbaocrisps.common.utils.BeanUtils;
import cn.lingbaocrisps.common.utils.UserContext;
import cn.lingbaocrisps.evaluate.domain.dto.EvaluateReplyDTO;
import cn.lingbaocrisps.evaluate.domain.po.EvaluateReply;
import cn.lingbaocrisps.evaluate.mapper.EvaluateReplyMapper;
import cn.lingbaocrisps.evaluate.service.IEvaluateReplyService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EvaluateReplyServiceImpl extends ServiceImpl<EvaluateReplyMapper, EvaluateReply> implements IEvaluateReplyService {

    @Override
    public List<EvaluateReply> getEvaluateReplyList(Long evaluateId) {
        LambdaQueryWrapper<EvaluateReply> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EvaluateReply::getEvaluateId, evaluateId).orderByAsc(EvaluateReply::getCreateTime);
        return list(queryWrapper);

    }

    @Override
    public void addEvaluateReply(EvaluateReplyDTO evaluateReplyDTO) {
        Integer userId = UserContext.getUser();
        if(userId == null)
            throw new ForbiddenException("用户未登录");
        evaluateReplyDTO.setUserId(userId);
        save(BeanUtils.copyBean(evaluateReplyDTO, EvaluateReply.class));
    }
}
