package cn.lingbaocrisps.evaluate.service.impl;

import cn.lingbaocrisps.api.client.CarClient;
import cn.lingbaocrisps.api.domain.po.Car;
import cn.lingbaocrisps.common.utils.RedisTools;
import cn.lingbaocrisps.evaluate.domain.po.EvaluateImg;
import cn.lingbaocrisps.evaluate.domain.vo.EvaluateImgVO;
import cn.lingbaocrisps.evaluate.mapper.EvaluateImgMapper;
import cn.lingbaocrisps.evaluate.service.IEvaluateImgService;
import cn.lingbaocrisps.evaluate.service.IEvaluateService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EvaluateImgServiceImpl extends ServiceImpl<EvaluateImgMapper, EvaluateImg> implements IEvaluateImgService {

    private final RedisTools redisTools;

    private final CarClient carClient;

    @Override
    public EvaluateImgVO getEvaluateImgVO(Long evaluateId, Integer carId) {
        //通过评价id获取评价的品牌名，车型名，车辆名以及评价图片列表
        Car car = carClient.getById(carId);
        List<String> list = this.baseMapper.getEvaluateImgList(evaluateId);
        EvaluateImgVO evaluateImgVO = new EvaluateImgVO();
        evaluateImgVO.setEvaluateImgList(list);
        evaluateImgVO.setBrandName(car.getBrand());
        evaluateImgVO.setModelName(car.getModel());
        evaluateImgVO.setCarName(car.getName());
        return evaluateImgVO;
    }
}
