package cn.fuzhizhuang.middleware.govern.aop;

import cn.fuzhizhuang.middleware.govern.annotation.UseHystrix;
import cn.fuzhizhuang.middleware.govern.service.impl.HystrixImpl;
import jakarta.annotation.Resource;
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
 * @description 切面逻辑实现
 */

@Aspect
@Component
@Order(10)
public class DoHystrixJointPoint {

    @Resource
    private int hystrixConfig;


    /**
     * 熔断切点
     */
    @Pointcut("@annotation(cn.fuzhizhuang.middleware.govern.annotation.UseHystrix)")
    public void aopPoint() {

    }


    /**
     * 熔断逻辑实现
     *
     * @param joinPoint  加入点
     * @param useHystrix 熔断自定义注解
     * @return 对象
     * @throws Throwable 抛出异常
     */
    @Around("aopPoint() && @annotation(useHystrix))")
    public Object doRouterForHystrix(ProceedingJoinPoint joinPoint, UseHystrix useHystrix) throws Throwable {
        HystrixImpl hystrixValve = null;
        if (useHystrix.timeoutValue() != 0) {
            //方法自定义熔断时间
            hystrixValve = new HystrixImpl(useHystrix.timeoutValue());
        } else {
            //配置文件通用熔断时间
            hystrixValve = new HystrixImpl(hystrixConfig);
        }
        return hystrixValve.access(joinPoint, getMethod(joinPoint), useHystrix, joinPoint.getArgs());
    }


    private Method getMethod(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        return joinPoint.getTarget().getClass().getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
    }


}
