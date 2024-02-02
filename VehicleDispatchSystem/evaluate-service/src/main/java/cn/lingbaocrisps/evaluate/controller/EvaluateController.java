package cn.lingbaocrisps.evaluate.controller;

import cn.lingbaocrisps.common.domain.R;
import cn.lingbaocrisps.common.utils.BeanUtils;
import cn.lingbaocrisps.common.utils.UserContext;
import cn.lingbaocrisps.evaluate.domain.dto.EvaluateQueryDTO;
import cn.lingbaocrisps.evaluate.domain.dto.EvaluateReplyDTO;
import cn.lingbaocrisps.evaluate.domain.po.Evaluate;
import cn.lingbaocrisps.evaluate.domain.po.EvaluateReply;
import cn.lingbaocrisps.evaluate.domain.vo.EvaluateDetailVO;
import cn.lingbaocrisps.evaluate.domain.vo.EvaluateVO;
import cn.lingbaocrisps.evaluate.service.IEvaluateImgService;
import cn.lingbaocrisps.evaluate.service.IEvaluateReplyService;
import cn.lingbaocrisps.evaluate.service.IEvaluateService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/evaluates")
public class EvaluateController {

    private final IEvaluateService evaluateService;

    private final IEvaluateImgService evaluateImgService;

    private final IEvaluateReplyService evaluateReplyService;

    /**
     * 查询评价列表
     * @param query
     * */
    @PostMapping("/page")
    public R<IPage<EvaluateDetailVO>> getEvaluateDetailPage(@RequestBody EvaluateQueryDTO query) {
        return R.ok(evaluateService.getEvaluateDetailPage(query));
    }

    @GetMapping("/getNewEvaluateList/{brandId}")
    public R<List<EvaluateVO>> getNewEvaluateList(@PathVariable Integer brandId) {
        return R.ok(evaluateService.getNewEvaluateList(brandId));
    }

    @GetMapping("/reply/{evaluateId}")
    public R<List<EvaluateReply>> getEvaluateReplyList(@PathVariable Long evaluateId) {
        return R.ok(evaluateReplyService.getEvaluateReplyList(evaluateId));
    }

    @PostMapping("/reply")
    public R<Void> addEvaluateReply(@RequestBody EvaluateReplyDTO evaluateReplyDTO) {
        evaluateReplyService.addEvaluateReply(evaluateReplyDTO);
        return R.ok();
    }
}
