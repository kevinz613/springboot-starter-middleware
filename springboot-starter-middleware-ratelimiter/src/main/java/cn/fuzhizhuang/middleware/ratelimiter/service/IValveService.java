package cn.fuzhizhuang.middleware.ratelimiter.service;

import cn.fuzhizhuang.middleware.ratelimiter.annotation.UseRateLimiter;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;

/**
 * @author fuzhizhuang
 * @description 服务接口定义
 */
public interface IValveService {
    /**
     * 访问
     *
     * @param joinPoint      加入点
     * @param method         方法
     * @param useRateLimiter 自定义限流注解
     * @param args           参数
     * @return 对象
     * @throws Throwable 抛出异常
     */
    Object access(ProceedingJoinPoint joinPoint, Method method, UseRateLimiter useRateLimiter, Object[] args) throws Throwable;
}
