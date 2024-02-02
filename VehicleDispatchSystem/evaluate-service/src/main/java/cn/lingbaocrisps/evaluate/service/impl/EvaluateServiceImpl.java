package cn.lingbaocrisps.evaluate.service.impl;

import cn.lingbaocrisps.api.client.CarClient;
import cn.lingbaocrisps.api.client.UserClient;
import cn.lingbaocrisps.api.domain.vo.CarImgVO;
import cn.lingbaocrisps.common.domain.RedisConstants;
import cn.lingbaocrisps.common.utils.BeanUtils;
import cn.lingbaocrisps.common.utils.CollUtils;
import cn.lingbaocrisps.common.utils.RedisTools;
import cn.lingbaocrisps.evaluate.domain.dto.EvaluateQueryDTO;
import cn.lingbaocrisps.evaluate.domain.po.Evaluate;
import cn.lingbaocrisps.evaluate.domain.po.EvaluateReply;
import cn.lingbaocrisps.evaluate.domain.vo.EvaluateDetailVO;
import cn.lingbaocrisps.evaluate.domain.vo.EvaluateVO;
import cn.lingbaocrisps.evaluate.mapper.EvaluateMapper;
import cn.lingbaocrisps.evaluate.mapper.EvaluateReplyMapper;
import cn.lingbaocrisps.evaluate.service.IEvaluateImgService;
import cn.lingbaocrisps.evaluate.service.IEvaluateReplyService;
import cn.lingbaocrisps.evaluate.service.IEvaluateService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EvaluateServiceImpl extends ServiceImpl<EvaluateMapper, Evaluate> implements IEvaluateService {
    private final RedisTools redisTools;
    private final CarClient carClient;
    private final UserClient userClient;

    private final IEvaluateImgService evaluateImgService;

    private final EvaluateReplyMapper evaluateReplyMapper;
    @Override
    public List<EvaluateVO> getNewEvaluateList(Integer brandId) {
        //获取该品牌最新的前5条评价
        //先从redis中获取
        Set<EvaluateVO> evaluateVOSet = redisTools.zReverseRange(RedisConstants.EVALUATE_VO_SET_NEW_KEY + brandId, 0, 5);
        if (evaluateVOSet != null && evaluateVOSet.size() > 0){
            redisTools.expire(RedisConstants.EVALUATE_VO_SET_NEW_KEY + brandId, 7L, TimeUnit.DAYS);
            return new ArrayList<>(evaluateVOSet);
        }
        //没有则去数据库中获取(该品牌分数不为0的前5条最新评价)
        Page<Evaluate> evaluatePage = page(new Page<Evaluate>(1, 5), new QueryWrapper<Evaluate>().eq("brand_id", brandId).orderByDesc("create_time"));
        List<Evaluate> evaluateList = evaluatePage.getRecords();
        //获取这5条最新评价的车辆图片
        List<EvaluateVO> evaluateVOList = new LinkedList<>();
        for (Evaluate evaluate : evaluateList) {
            EvaluateVO evaluateVO = BeanUtils.copyBean(evaluate, EvaluateVO.class);

            evaluateVO.setCoverImg("");
            CarImgVO carImgVO = carClient.getCarImgVO(evaluate.getCarId(), 1);
            if(carImgVO.getImgList().size() == 1){
                String coverImg = carImgVO.getImgList().get(0);
                evaluateVO.setCoverImg(coverImg);
            }

            evaluateVOList.add(evaluateVO);
            double createTimestamp = Timestamp.valueOf(evaluateVO.getCreateTime()).getTime();
            redisTools.zAdd(RedisConstants.EVALUATE_VO_SET_NEW_KEY + brandId,
                    evaluateVO, createTimestamp);
        }
        redisTools.expire(RedisConstants.EVALUATE_VO_SET_NEW_KEY + brandId, 7L, TimeUnit.DAYS);
        return evaluateVOList;
    }

    @Override
    public IPage<EvaluateDetailVO> getEvaluateDetailPage(EvaluateQueryDTO queryDTO) {
        String column;
        switch (queryDTO.getType()){
            case 1:
                column = "brand_id";
                break;
            case 2:
                column = "model_id";
                break;
            case 3:
                column = "car_id";
                break;
            default:
                column = "brand_id";
                break;
        }
        QueryWrapper<Evaluate> qw = new QueryWrapper<>();
        qw.eq(column, queryDTO.getType());
        switch (queryDTO.getQueryType()){
            case 1:
                qw.orderByDesc("create_time");
                break;
            case 2:
                qw.orderByDesc("score");
                break;
            case 3:
                qw.orderByAsc("score");
                break;
            default:
                qw.orderByDesc("create_time");
                break;
        }
        IPage<Evaluate> page = page(new Page<Evaluate>(queryDTO.getPage(), queryDTO.getSize()), qw);
        List<Evaluate> evaluateList = page.getRecords();

        List<EvaluateDetailVO> evaluateDetailVOList = new LinkedList<>();
        for (Evaluate evaluate : evaluateList) {
            EvaluateDetailVO evaluateDetailVO = BeanUtils.copyBean(evaluate, EvaluateDetailVO.class);
            evaluateDetailVO.setReplyCount(evaluateReplyMapper.selectCount(new QueryWrapper<EvaluateReply>().eq("evaluate_id", evaluate.getId())));
            evaluateDetailVO.setEvaluateImgVO(evaluateImgService.getEvaluateImgVO(evaluate.getId(), evaluate.getCarId()));
            evaluateDetailVOList.add(evaluateDetailVO);
        }

        IPage<EvaluateDetailVO> evaluateDetailVOIPage = new Page<>(queryDTO.getPage(), queryDTO.getSize());
        evaluateDetailVOIPage.setRecords(evaluateDetailVOList);
        evaluateDetailVOIPage.setTotal(evaluateDetailVOList.size());
        return evaluateDetailVOIPage;
    }
}
