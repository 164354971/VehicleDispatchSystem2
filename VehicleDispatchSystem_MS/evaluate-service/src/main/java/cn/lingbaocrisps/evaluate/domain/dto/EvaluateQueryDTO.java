package cn.lingbaocrisps.evaluate.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel(description = "新增评价查询dto")
public class EvaluateQueryDTO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("1:品牌 2:型号 3:车辆")
    @NotNull(message = "分类不能为空")
    private Integer type;

    @ApiModelProperty("查询id")
    @NotNull(message = "查询id不能为空")
    private Integer queryId;

    @ApiModelProperty("1:按时间查询 2:按评分从高到低查询 3:按评分从低到高查询")
    @NotNull(message = "查询类型不能为空")
    private Integer queryType;

    @ApiModelProperty("查询页数")
    @NotNull(message = "查询页数不能为空")
    private Integer page;

    @ApiModelProperty("查询数量")
    @NotNull(message = "查询数量不能为空")
    private Integer size;
}
