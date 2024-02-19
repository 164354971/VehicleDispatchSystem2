package cn.lingbaocrisps.websocket;

import cn.lingbaocrisps.api.config.DefaultFeignConfig;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@EnableWebSocket
@Slf4j
@EnableFeignClients(basePackages = "cn.lingbaocrisps.api.client", defaultConfiguration = DefaultFeignConfig.class)
@MapperScan("cn.lingbaocrisps.websocket.mapper")
@SpringBootApplication(scanBasePackages = "cn.lingbaocrisps")
public class WebSocketApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebSocketApplication.class, args);

    }
}