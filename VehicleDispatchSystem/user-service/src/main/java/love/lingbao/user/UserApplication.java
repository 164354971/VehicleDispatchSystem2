package love.lingbao.user;

import love.lingbao.api.config.DefaultFeignConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.spring.web.plugins.DefaultConfiguration;

@EnableFeignClients(basePackages = "love.lingbao.api.client", defaultConfiguration = DefaultFeignConfig.class)
@MapperScan("love.lingbao.user.mapper")
@SpringBootApplication(scanBasePackages = "love.lingbao")
public class UserApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class);
    }
}