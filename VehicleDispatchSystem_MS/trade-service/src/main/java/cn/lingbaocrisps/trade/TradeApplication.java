package cn.lingbaocrisps.trade;

import cn.lingbaocrisps.api.config.DefaultFeignConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@EnableFeignClients(basePackages = "cn.lingbaocrisps.api.client", defaultConfiguration = DefaultFeignConfig.class)
@MapperScan("cn.lingbaocrisps.trade.mapper")
@SpringBootApplication(scanBasePackages = "cn.lingbaocrisps")
public class TradeApplication {
    public static void main(String[] args) {
        SpringApplication.run(TradeApplication.class);
    }
}