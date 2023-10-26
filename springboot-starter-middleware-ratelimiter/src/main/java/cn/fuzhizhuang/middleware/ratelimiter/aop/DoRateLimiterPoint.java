package cn.fuzhizhuang.middleware.ratelimiter.aop;

import cn.fuzhizhuang.middleware.ratelimiter.annotation.UseRateLimiter;
import cn.fuzhizhuang.middleware.ratelimiter.service.impl.RateLimiterServiceImpl;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author fuzhizhuang
 * @description Aop切面逻辑实现
 */
@Component
@Aspect
public class DoRateLimiterPoint {

    @Pointcut("@annotation(cn.fuzhizhuang.middleware.ratelimiter.annotation.UseRateLimiter)")
    public void aopPoint() {

    }

    @Around("aopPoint() && @annotation(useRateLimiter)")
    public Object doRouter(ProceedingJoinPoint joinPoint, UseRateLimiter useRateLimiter) throws Throwable {
        RateLimiterServiceImpl rateLimiterService = new RateLimiterServiceImpl();
        return rateLimiterService.access(joinPoint, getMethod(joinPoint), useRateLimiter, joinPoint.getArgs());
    }

    private Method getMethod(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        return joinPoint.getTarget().getClass().getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
    }
}
