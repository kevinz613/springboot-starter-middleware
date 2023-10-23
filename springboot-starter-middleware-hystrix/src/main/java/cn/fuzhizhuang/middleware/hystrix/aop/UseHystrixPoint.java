package cn.fuzhizhuang.middleware.hystrix.aop;

import cn.fuzhizhuang.middleware.hystrix.annotation.UseHystrix;
import cn.fuzhizhuang.middleware.hystrix.service.impl.HystrixValveImpl;
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

@Aspect
@Component
public class UseHystrixPoint {

    /**
     * AOP 切点
     */
    @Pointcut("@annotation(cn.fuzhizhuang.middleware.hystrix.annotation.UseHystrix)")
    public void aopPoint(){

    }

    @Around("aopPoint() && @annotation(useHystrix))")
    public Object doRouter(ProceedingJoinPoint joinPoint, UseHystrix useHystrix) throws Throwable {
        HystrixValveImpl hystrixValve = new HystrixValveImpl();
        return hystrixValve.access(joinPoint,getMethod(joinPoint),useHystrix,joinPoint.getArgs());
    }

    private Method getMethod(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        return joinPoint.getTarget().getClass().getMethod(signature.getName(),methodSignature.getParameterTypes());
    }
}
