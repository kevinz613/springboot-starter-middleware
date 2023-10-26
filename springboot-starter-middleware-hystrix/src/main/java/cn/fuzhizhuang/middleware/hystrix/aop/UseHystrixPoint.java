package cn.fuzhizhuang.middleware.hystrix.aop;

import cn.fuzhizhuang.middleware.hystrix.annotation.UseHystrix;
import cn.fuzhizhuang.middleware.hystrix.service.impl.HystrixValveImpl;
import jakarta.annotation.Resource;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author fuzhizhuang
 * @description Aop切面逻辑实现
 */

@Aspect
@Component
public class UseHystrixPoint {

    @Resource
    private int hystrixConfig;

    /**
     * AOP 切点
     */
    @Pointcut("@annotation(cn.fuzhizhuang.middleware.hystrix.annotation.UseHystrix)")
    public void aopPoint(){

    }

    @Around("aopPoint() && @annotation(useHystrix))")
    public Object doRouter(ProceedingJoinPoint joinPoint, UseHystrix useHystrix) throws Throwable {
        HystrixValveImpl hystrixValve = null;
        if (useHystrix.timeoutValue()!=0) {
            //方法自定义熔断时间
            hystrixValve = new HystrixValveImpl(useHystrix.timeoutValue());
        }else {
            //配置文件通用熔断时间
            hystrixValve = new HystrixValveImpl(hystrixConfig);
        }
        return hystrixValve.access(joinPoint,getMethod(joinPoint),useHystrix,joinPoint.getArgs());
    }

    private Method getMethod(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        return joinPoint.getTarget().getClass().getMethod(signature.getName(),methodSignature.getParameterTypes());
    }
}
