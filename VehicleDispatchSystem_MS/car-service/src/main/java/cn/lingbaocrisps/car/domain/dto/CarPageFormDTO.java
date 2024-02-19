package cn.lingbaocrisps.car.domain.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel(description = "车辆分页查询表单实体")
public class CarPageFormDTO {
    private Integer parkadeId;
    private Integer page;
    private Integer pageSize;
    private Integer sort;
    private List<String> typeList;
    private BigDecimal startPrice;
    private BigDecimal endPrice;
    private Boolean nln;
    private Boolean noDeposit;
    private Boolean luxury;
    private Boolean newEnergy;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
