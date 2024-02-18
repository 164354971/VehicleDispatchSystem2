package cn.lingbaocrisps.common.aop;

import cn.hutool.extra.servlet.ServletUtil;
import cn.lingbaocrisps.common.annotate.RepeatSubmit;
import cn.lingbaocrisps.common.exception.RepeatSubmitException;
import cn.lingbaocrisps.common.utils.RedisTools;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@Aspect
@RequiredArgsConstructor
public class NoRepeatSubmitAspect {

    private final RedisTools redisTools;

    @Pointcut("@annotation(repeatSubmit)")
    public void pointcutNoRepeatSubmit(RepeatSubmit repeatSubmit) {}

    @Around("pointcutNoRepeatSubmit(repeatSubmit)")
    public Object AroundNoRepeatSubmit(ProceedingJoinPoint pjp, RepeatSubmit repeatSubmit) throws Throwable {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attrs.getRequest();
        String reqSignature = buildRequestSignature(request, pjp);
        String key = buildRedisKey(reqSignature);
        long lockTime = repeatSubmit.lockTime();
        boolean flag = redisTools.setIfAbsent(key, "true", lockTime, TimeUnit.SECONDS);
        if(!flag){
            log.info("请求重复提交");
            throw new RepeatSubmitException("请求重复提交");
        }
        try {
            long beginTime = System.currentTimeMillis();
            log.info("{}执行开始时间:{}", pjp.getSignature().getName(), beginTime);
            long endTime = System.currentTimeMillis();
            log.info("{}执行结束时间:{}, 用时：{}", pjp.getSignature().getName(), endTime, endTime - beginTime);
            return pjp.proceed();
        } finally {
            redisTools.delete(key);
        }


    }

    /**
     * 构建请求签名（由客户端ip+类名+方法名+请求参数组成）
     * @param req
     * @param pjp
     * @return
     */
    private String buildRequestSignature(HttpServletRequest req, ProceedingJoinPoint pjp) {
        String clientIP = ServletUtil.getClientIP(req);
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        String className = method.getDeclaringClass().getName();
        StringBuilder sParams = new StringBuilder();
        for(var o : pjp.getArgs()){
            sParams.append("-").append(o.toString());
        }
        return String.format("%s-%s-%s-%s", clientIP, className, signature.getName(), sParams);
    }

    private String buildRedisKey(String reqSignature) {
        return String.format("vehicle:repeat-submit:%s", reqSignature);
    }
}
