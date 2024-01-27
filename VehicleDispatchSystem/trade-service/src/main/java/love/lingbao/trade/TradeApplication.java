package love.lingbao.trade;

import love.lingbao.api.config.DefaultFeignConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "love.lingbao.api.client", defaultConfiguration = DefaultFeignConfig.class)
@MapperScan("love.lingbao.trade.mapper")
@SpringBootApplication(scanBasePackages = "love.lingbao")
public class TradeApplication {
    public static void main(String[] args) {
        SpringApplication.run(TradeApplication.class);
    }
}