package cn.fuzhizhuang.middleware.govern.aop;

import cn.fuzhizhuang.middleware.govern.annotation.UseRateLimiter;
import cn.fuzhizhuang.middleware.govern.service.impl.RateLimiterServiceImpl;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author fuzhizhuang
 * @description Aop切面逻辑实现
 */
@Component
@Aspect
@Order(2)
public class DoRateLimiterPoint {

    @Pointcut("@annotation(cn.fuzhizhuang.middleware.govern.annotation.UseRateLimiter)")
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
