package cn.fuzhizhuang.middleware.methodintercept.aop;

import cn.fuzhizhuang.middleware.methodintercept.annotation.UseMethodIntercept;
import com.alibaba.fastjson.JSON;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author fuzhizhuang
 * @description AOP切面实现
 */

@Component
@Aspect
public class DoMethodInterceptPoint {

    private Logger logger = LoggerFactory.getLogger(DoMethodInterceptPoint.class);

    @Pointcut("@annotation(cn.fuzhizhuang.middleware.methodintercept.annotation.UseMethodIntercept)")
    public void aopPoint() {

    }

    @Around("aopPoint()")
    public Object doRouter(ProceedingJoinPoint joinPoint) throws Throwable {
        //获取内容
        Method method = getMethod(joinPoint);
        UseMethodIntercept annotation = method.getAnnotation(UseMethodIntercept.class);
        //获取拦截方法
        String methodName = annotation.method();
        //功能处理
        Method methodIntercept = getClass(joinPoint).getMethod(methodName, method.getParameterTypes());
        Class<?> returnType = methodIntercept.getReturnType();
        //判断方法返回类型
        if (!returnType.getName().equals("boolean")) {
            throw new RuntimeException("annotation @UseMethodIntercept set method:" + methodName + "returnType is not boolean");
        }
        //判断拦截正常，继续
        boolean invoke = (boolean) methodIntercept.invoke(joinPoint.getThis(), joinPoint.getArgs());
        //返回结果
        return invoke ? joinPoint.proceed() : JSON.parseObject(annotation.returnJson(), method.getReturnType());
    }

    private Class<? extends Object> getClass(JoinPoint joinPoint){
        return joinPoint.getTarget().getClass();
    }

    private Method getMethod(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        return joinPoint.getTarget().getClass().getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
    }
}
