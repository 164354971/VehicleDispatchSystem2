package cn.lingbaocrisps.websocket.handler;

import cn.lingbaocrisps.websocket.utils.JwtTool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;
import java.util.Objects;

/**
 * @author GXM
 * @version 1.0.0
 * @Description TODO
 * @createTime 2023年07月19日
 */
@Slf4j
public class WebSocketInterceptor implements HandshakeInterceptor {

    @Autowired
    private JwtTool jwtTool;


    /**
     * 建立请求之前，可以用来做权限判断
     *
     * @param request    the current request
     * @param response   the current response
     * @param wsHandler  the target WebSocket handler
     * @param attributes the attributes from the HTTP handshake to associate with the WebSocket
     *                   session; the provided attributes are copied, the original map is not used.
     * @return
     * @throws Exception
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response, WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletServerHttpRequest = (ServletServerHttpRequest) request;
            // 模拟用户（通常利用JWT令牌解析用户信息）
            String token = servletServerHttpRequest.getServletRequest().getParameter("token");
            log.info("token: " + token);
            //log.info(Objects.requireNonNull(servletServerHttpRequest.getHeaders().get("authorization")).toString());
            try {
                Integer clientUserId = jwtTool.parseToken(token);
                // 设置当前这个session的属性，后续我们在发送消息时，可以通过 session.getAttributes().get("clientUserId")可以取出 clientUserId参数
                attributes.put("clientUserId", clientUserId);
            } catch (Exception e) {
                log.error("webSocket 认证失败 ", e);
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * 建立请求之后
     *
     * @param request   the current request
     * @param response  the current response
     * @param wsHandler the target WebSocket handler
     * @param exception an exception raised during the handshake, or {@code null} if none
     */
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {

    }
}


