package cn.lingbaocrisps.evaluate.domain.vo;


import cn.lingbaocrisps.evaluate.domain.po.Evaluate;
import cn.lingbaocrisps.evaluate.domain.po.EvaluateImg;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@ApiModel(value = "评价图VO")
@Data
public class EvaluateImgVO {
    private String brandName;
    private String modelName;
    private String carName;//评价的车辆名称
    private List<String> evaluateImgList;//评价图片列表
}
