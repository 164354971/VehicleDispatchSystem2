package cn.lingbaocrisps.car.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.lingbaocrisps.car.domain.po.Car;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

@Mapper
public interface CarMapper extends BaseMapper<Car> {

    @Select("select day_price from car where id = #{id}")
    BigDecimal getDayPrice(Integer id);
}
