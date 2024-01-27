package love.lingbao.gateway.fliters;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.AntPathMatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import love.lingbao.gateway.config.AuthProperties;
import love.lingbao.gateway.config.JwtProperties;
import love.lingbao.gateway.utils.JwtTool;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class LoginGlobalFilter implements GlobalFilter, Ordered {

    private final AuthProperties authProperties;

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    private final JwtTool jwtTool;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        //1.获取请求头中的token
        ServerHttpRequest request = exchange.getRequest();
        //如果请求的路径和方法符合则直接放行
        if(isAllowPath(request))
            return chain.filter(exchange);
        //获取请求头中的token
        HttpHeaders header = request.getHeaders();
        List<String> authorization = header.get("authorization");
        String token = null;
        if(CollUtil.isNotEmpty(authorization))
            token = authorization.get(0);

        //2.jwt校验
        Long userId = null;
        try {
            userId = jwtTool.parseToken(token);
            log.info("LoginGlobalFilter userId: {}", userId);
        }catch (Exception e){
            log.info("LoginGlobalFilter token is null");
            ServerHttpResponse response = exchange.getResponse();
            //response.setRawStatusCode(401);
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
        //5.传递用户信息到下游服务
        String userInfo = userId.toString();
        ServerWebExchange exc = exchange.mutate()
                .request(builder -> builder.header("user-info", userInfo))
                .build();

        return chain.filter(exc);
    }

    @Override
    public int getOrder() {
        return 0;
    }

    boolean isAllowPath(ServerHttpRequest request){
        //方法
        String methodValue = request.getMethodValue();
        //路径
        String path = request.getPath().toString();
        for(String excludePath : authProperties.getExcludePaths()){
            if(antPathMatcher.match(excludePath, path)){
                return true;
            }
        }
        return false;
    }
}
