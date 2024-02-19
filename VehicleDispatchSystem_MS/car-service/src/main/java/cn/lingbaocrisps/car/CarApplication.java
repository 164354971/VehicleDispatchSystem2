package cn.lingbaocrisps.car;

import cn.lingbaocrisps.api.config.DefaultFeignConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(value = "cn.lingbaocrisps.api.client", defaultConfiguration = DefaultFeignConfig.class)
@MapperScan("cn.lingbaocrisps.car.mapper")
@SpringBootApplication(scanBasePackages = "cn.lingbaocrisps")
public class CarApplication {
    public static void main(String[] args) {
        SpringApplication.run(CarApplication.class);
    }
}