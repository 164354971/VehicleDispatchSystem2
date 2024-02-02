package cn.lingbaocrisps.user.utils;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTValidator;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import cn.lingbaocrisps.common.exception.UnauthorizedException;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.time.Duration;
import java.util.Date;

@Component
public class JwtTool {
    private final JWTSigner jwtSigner;

    public JwtTool(KeyPair keyPair) {
        this.jwtSigner = JWTSignerUtil.createSigner("rs256", keyPair);
    }

    /**
     * 创建 access-token
     *
     * @param userId 用户信息
     * @return access-token
     */
    public String createToken(Integer userId, Duration ttl) {
        // 1.生成jws
        return JWT.create()
                .setPayload("user", userId)
                .setExpiresAt(new Date(System.currentTimeMillis() + ttl.toMillis()))
                .setSigner(jwtSigner)
                .sign();
    }

    /**
     * 解析token
     *
     * @param token token
     * @return 解析刷新token得到的用户信息
     */
    public Integer parseToken(String token){
        //1.校验token是否为空
        if (token == null)
            throw new UnauthorizedException("未登录");
        //2.校验并解析token
        JWT jwt;
        try {
            jwt = JWT.of(token).setSigner(jwtSigner);
        }catch (Exception e){
            throw new UnauthorizedException("无效的token");
        }
        //3.校验是否过期
        try {
            JWTValidator.of(jwt).validateDate();
        }catch (Exception e){
            throw new UnauthorizedException("token已过期");
        }
        //4.数据格式校验
        Object userPayload = jwt.getPayload("user");
        if(userPayload == null){
            throw new UnauthorizedException("无效的token");
        }
        //5.数据解析
        try {
            return Integer.valueOf(userPayload.toString());
        }catch (Exception e){
            throw new UnauthorizedException("无效的token");
        }
    }
}
