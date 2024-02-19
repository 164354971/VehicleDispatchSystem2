package cn.lingbaocrisps.api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "vds-pay-service")
public interface PayClient {

    /**
     * 根据订单号查询支付表的支付状态
     * @param orderNo 订单号
     * @return 支付状态
     * */
    @GetMapping("/pays/{orderNo}")
    Integer getPaymentStatus(@PathVariable Long orderNo);
}
