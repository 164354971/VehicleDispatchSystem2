package cn.lingbaocrisps.car.service;

import cn.lingbaocrisps.car.domain.po.CarImg;
import cn.lingbaocrisps.car.domain.vo.CarImgVO;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ICarImgService extends IService<CarImg> {

    CarImgVO getCarImgVO(Integer carId, Integer imgNumber);
}
