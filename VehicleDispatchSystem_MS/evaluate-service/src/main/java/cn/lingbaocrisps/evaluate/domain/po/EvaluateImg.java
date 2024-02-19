package cn.lingbaocrisps.evaluate.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("evaluate_img")
public class EvaluateImg {
    private Long id;//bigint auto_increment
    private Long evaluateId;//int                             not null comment '评价id',
    private String img;//varchar(255)										 not null comment '评价图片'
}
