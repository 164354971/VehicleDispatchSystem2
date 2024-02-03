package cn.lingbaocrisps.coupon;

import cn.lingbaocrisps.api.config.DefaultFeignConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(value = "cn.lingbaocrisps.api.client", defaultConfiguration = DefaultFeignConfig.class)
@MapperScan("cn.lingbaocrisps.coupon.mapper")
@SpringBootApplication(scanBasePackages = "cn.lingbaocrisps")
public class CouponApplication {
    public static void main(String[] args) {
        SpringApplication.run(CouponApplication.class);
    }
}