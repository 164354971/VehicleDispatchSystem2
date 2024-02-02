package cn.lingbaocrisps.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.lingbaocrisps.user.domain.po.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("select img from user where id = #{userId}")
    String getUserImg(Integer userId);
}
