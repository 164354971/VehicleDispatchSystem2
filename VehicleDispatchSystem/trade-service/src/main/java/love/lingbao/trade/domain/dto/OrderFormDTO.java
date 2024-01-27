package love.lingbao.trade.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@ApiModel(description = "新增订单表单实体")
public class OrderFormDTO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("订单中对应的车辆id")
    @NotNull(message = "车辆id不能为空")
    private Integer carId;//int        default 0                 not null comment '租的车辆id',

    @ApiModelProperty("用户使用的优惠券id")
    @NotNull(message = "用户使用的优惠券id不能为空，为0表示不适用优惠券")
    private Long couponUserId;//bigint        default 0                 not null comment '用户使用的优惠券id',

    @ApiModelProperty("租车天数")
    @NotNull(message = "租车天数不能为空")
    private Integer rentDays;

    @ApiModelProperty("租车开始时间")
    @NotNull(message = "租车开始时间不能为空")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;//datetime      default CURRENT_TIMESTAMP not null comment '租车开始时间',

    @ApiModelProperty("租车结束时间")
    @NotNull(message = "租车结束时间不能为空")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;//datetime      default CURRENT_TIMESTAMP not null comment '租车结束时间',
}
