package cn.fuzhizhuang.middleware.govern.service.impl;

import cn.fuzhizhuang.middleware.govern.annotation.UseMethodIntercept;
import cn.fuzhizhuang.middleware.govern.service.IValveService;
import com.alibaba.fastjson.JSON;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * @author fuzhizhuang
 * @description 封装白名单实现
 */
public class MethodInterceptImpl implements IValveService {

    private final Logger logger = LoggerFactory.getLogger(MethodInterceptImpl.class);


    @Override
    public Object access(ProceedingJoinPoint joinPoint, Method method, Object annotation, Object[] args) throws Throwable {
        if (!match(annotation)) {
            //放行
            joinPoint.proceed();
        }
        //具体业务逻辑实现
        UseMethodIntercept useMethodIntercept = (UseMethodIntercept) annotation;
        //获取拦截方法
        String methodName = useMethodIntercept.method();
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
        return invoke ? joinPoint.proceed() : JSON.parseObject(useMethodIntercept.returnJson(), method.getReturnType());
    }

    @Override
    public boolean match(Object annotation) {
        return annotation instanceof UseMethodIntercept;
    }

    private Class<? extends Object> getClass(JoinPoint joinPoint) {
        return joinPoint.getTarget().getClass();
    }


}
