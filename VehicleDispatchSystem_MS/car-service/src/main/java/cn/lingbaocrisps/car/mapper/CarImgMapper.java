package cn.lingbaocrisps.car.mapper;

import cn.lingbaocrisps.car.domain.po.CarImg;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CarImgMapper extends BaseMapper<CarImg> {
    @Select("select img from car_img where car_id = #{carId} limit #{limit}")
    List<String> getCarImgList(Integer carId, Integer limit);

    @Select("select img from car_img where car_id = #{carId}")
    List<String> getCarImgList(Integer carId);
}
