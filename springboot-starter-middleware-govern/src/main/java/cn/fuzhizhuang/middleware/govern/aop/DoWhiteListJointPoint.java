package cn.fuzhizhuang.middleware.govern.aop;

import cn.fuzhizhuang.middleware.govern.annotation.UseWhiteList;
import cn.fuzhizhuang.middleware.govern.service.impl.WhiteListImpl;
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
@Order(1)
public class DoWhiteListJointPoint {

    @Resource
    private String whiteListConfig;

    /**
     * 白名单切点，通过自定义注解的方式
     */
    @Pointcut("@annotation(cn.fuzhizhuang.middleware.govern.annotation.UseWhiteList)")
    public void aopPoint() {

    }


    /**
     * 白名单逻辑实现
     *
     * @param joinPoint    加入点
     * @param useWhiteList 白名单自定义注解
     * @return 对象
     * @throws Throwable 抛出异常
     */
    @Around("aopPoint() && @annotation(useWhiteList)")
    public Object doRouterForWhiteList(ProceedingJoinPoint joinPoint, UseWhiteList useWhiteList) throws Throwable {
        WhiteListImpl whiteListValve = new WhiteListImpl(whiteListConfig);
        return whiteListValve.access(joinPoint, getMethod(joinPoint), useWhiteList, joinPoint.getArgs());
    }


    private Method getMethod(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        return joinPoint.getTarget().getClass().getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
    }


}
