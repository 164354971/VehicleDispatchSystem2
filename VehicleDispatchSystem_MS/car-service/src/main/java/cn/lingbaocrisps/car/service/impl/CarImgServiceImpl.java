package cn.lingbaocrisps.car.service.impl;

import cn.lingbaocrisps.car.domain.po.CarImg;
import cn.lingbaocrisps.car.domain.vo.CarImgVO;
import cn.lingbaocrisps.car.mapper.CarImgMapper;
import cn.lingbaocrisps.car.service.ICarImgService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class CarImgServiceImpl extends ServiceImpl<CarImgMapper, CarImg> implements ICarImgService {
    @Override
    public CarImgVO getCarImgVO(Integer carId, Integer number) {
        if (number == null) {
            number = 0;
        }
        if (carId == null) {
            return null;
        }
        CarImgVO carImgVO = new CarImgVO();
        carImgVO.setCarId(carId);
        if (number != 0){
            carImgVO.setImgList(this.baseMapper.getCarImgList(carId, number));
        }else{
            carImgVO.setImgList(this.baseMapper.getCarImgList(carId));
        }
        return carImgVO;
    }
}
