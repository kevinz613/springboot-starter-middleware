package cn.fuzhizhuang.middleware.hystrix.service;

import cn.fuzhizhuang.middleware.hystrix.annotation.UseHystrix;
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
    Object access(ProceedingJoinPoint joinPoint, Method method, UseHystrix useHystrix, Object[] args) throws Throwable;
}
