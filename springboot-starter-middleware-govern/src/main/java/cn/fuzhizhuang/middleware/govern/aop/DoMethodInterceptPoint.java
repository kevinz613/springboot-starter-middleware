package cn.fuzhizhuang.middleware.govern.aop;

import cn.fuzhizhuang.middleware.govern.annotation.UseMethodIntercept;
import cn.fuzhizhuang.middleware.govern.service.impl.MethodInterceptImpl;
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
 * @description AOP切面实现
 */

@Component
@Aspect
@Order(3)
public class DoMethodInterceptPoint {

    @Pointcut("@annotation(cn.fuzhizhuang.middleware.govern.annotation.UseMethodIntercept)")
    public void aopPoint() {

    }

    @Around("aopPoint() && @annotation(useMethodIntercept)")
    public Object doRouter(ProceedingJoinPoint joinPoint, UseMethodIntercept useMethodIntercept) throws Throwable {
        MethodInterceptImpl methodIntercept = new MethodInterceptImpl();
        return methodIntercept.access(joinPoint, getMethod(joinPoint), useMethodIntercept, joinPoint.getArgs());
    }

    private Method getMethod(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        return joinPoint.getTarget().getClass().getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
    }
}
