package cn.lingbaocrisps.evaluate.mapper;

import cn.lingbaocrisps.evaluate.domain.po.EvaluateImg;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface EvaluateImgMapper extends BaseMapper<EvaluateImg> {

    @Select("SELECT img FROM evaluate_img WHERE evaluate_id = #{evaluateId}")
    List<String> getEvaluateImgList(Long evaluateId);
}
