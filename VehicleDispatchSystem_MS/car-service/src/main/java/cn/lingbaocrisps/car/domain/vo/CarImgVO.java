package cn.lingbaocrisps.car.domain.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@ApiModel(value = "车辆图VO")
@Data
public class CarImgVO {
    private Integer carId;
    private List<String> imgList;
}
