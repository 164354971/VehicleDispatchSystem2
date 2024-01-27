package love.lingbao.api.client;

import io.swagger.models.auth.In;
import love.lingbao.api.domain.po.Car;
import love.lingbao.common.domain.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;

@FeignClient(value = "vds-car-service")
public interface CarClient {

    /**
    * 查询车辆在该时间段是否可用
    * */
    @GetMapping("/cars/{carId}/{startTime}/{endTime}")
    boolean isUsableCarTime(@PathVariable Integer carId, @PathVariable String startTime, @PathVariable String endTime);

    /**
     * 查询车辆id是否合法
     * */
    @GetMapping("/cars/{carId}")
    Car getById(@PathVariable Integer carId);
}
