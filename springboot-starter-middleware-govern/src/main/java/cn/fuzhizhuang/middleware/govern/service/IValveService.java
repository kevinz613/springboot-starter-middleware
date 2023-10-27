package cn.fuzhizhuang.middleware.govern.service;

import cn.fuzhizhuang.middleware.govern.annotation.UseHystrix;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;

/**
 * @author fuzhizhuang
 * @description 业务接口定义
 */
public interface IValveService {

    /**
     * 访问
     *
     * @param joinPoint  加入点
     * @param method     方法
     * @param useHystrix 自定义注解
     * @param args       参数
     * @return {@link Object}
     * @throws Throwable 抛出异常
     */
    Object access(ProceedingJoinPoint joinPoint, Method method, Object annotation, Object[] args) throws Throwable;

    boolean match(Object annotation);
}
