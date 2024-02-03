package cn.lingbaocrisps.pay;

import cn.lingbaocrisps.api.config.DefaultFeignConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "cn.lingbaocrisps.api.client", defaultConfiguration = DefaultFeignConfig.class)
@MapperScan("cn.lingbaocrisps.pay.mapper")
@SpringBootApplication(scanBasePackages = "cn.lingbaocrisps")
public class PayApplication {
    public static void main(String[] args) {
        SpringApplication.run(PayApplication.class);
    }
}