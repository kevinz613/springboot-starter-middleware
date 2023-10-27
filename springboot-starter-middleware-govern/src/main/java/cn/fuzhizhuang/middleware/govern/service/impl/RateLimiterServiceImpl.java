package cn.fuzhizhuang.middleware.govern.service.impl;

import cn.fuzhizhuang.middleware.govern.annotation.UseRateLimiter;
import cn.fuzhizhuang.middleware.govern.common.RateLimiterConstants;
import cn.fuzhizhuang.middleware.govern.service.IValveService;
import com.alibaba.fastjson.JSON;
import com.google.common.util.concurrent.RateLimiter;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;

/**
 * @author fuzhizhuang
 * @description 限流业务实现类
 */
public class RateLimiterServiceImpl implements IValveService {

    @Override
    public Object access(ProceedingJoinPoint joinPoint, Method method, Object annotation, Object[] args) throws Throwable {
        if (!match(annotation)) {
            //放行
            joinPoint.proceed();
        }
        UseRateLimiter useRateLimiter = (UseRateLimiter) annotation;
        //判断是否开启限流
        if (useRateLimiter.permitsPerSecond() == 0) {
            //未开启限流，放行
            return joinPoint.proceed();
        }
        String clazzName = joinPoint.getTarget().getClass().getName();
        String methodName = method.getName();
        String key = clazzName + "." + methodName;
        if (null == RateLimiterConstants.rateLimiterMap.get(key)) {
            RateLimiterConstants.rateLimiterMap.put(key, RateLimiter.create(useRateLimiter.permitsPerSecond()));
        }
        RateLimiter rateLimiter = RateLimiterConstants.rateLimiterMap.get(key);
        if (rateLimiter.tryAcquire()) {
            return joinPoint.proceed();
        }
        return JSON.parseObject(useRateLimiter.returnJson(), method.getReturnType());
    }

    @Override
    public boolean match(Object annotation) {
        return annotation instanceof UseRateLimiter;
    }
}
