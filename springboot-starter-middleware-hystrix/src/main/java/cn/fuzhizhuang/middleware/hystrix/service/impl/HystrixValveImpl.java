package cn.fuzhizhuang.middleware.hystrix.service.impl;

import cn.fuzhizhuang.middleware.hystrix.annotation.UseHystrix;
import cn.fuzhizhuang.middleware.hystrix.service.IValveService;
import com.alibaba.fastjson.JSON;
import com.netflix.hystrix.*;
import jakarta.annotation.Resource;
import org.aspectj.lang.ProceedingJoinPoint;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author fuzhizhuang
 * @description 封装熔断保护
 */
public class HystrixValveImpl extends HystrixCommand<Object> implements IValveService {

    /**
     * 加入点
     */
    private ProceedingJoinPoint joinPoint;

    /**
     * 方法
     */
    private Method method;

    /**
     * 自定义注解
     */
    private UseHystrix useHystrix;

    /**
     * <p>配置HystrixCommand的属性</p>
     * <p>groupKey：命令属于哪一个组，可以帮助我们更好的组织命令</p>
     * <p>CommandKey：该命令名称</p>
     * <p>ThreadPoolKey：该命令所属线程池的名称，同样配置的命令会共享同一线程池，若不配置，会默认使用GroupKey作为线程池名称</p>
     * <p>CommandProperties：该命令的一些设置，包括断路由器的配置，隔离策略、降级设置，以及一些监控指标等</p>
     * <p>ThreadPoolProperties：关于线程池的配置，包括线程池大小，排队队列的大小等</p>
     */
    public HystrixValveImpl(int timeout) {
        super(
                //设置命令属于哪一个组
                Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("GovernGroup"))
                        //配置命令名称
                        .andCommandKey(HystrixCommandKey.Factory.asKey("GovernKey"))
                        //配置线程池名称
                        .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("GovernThreadPool"))
                        //命令的配置
                        .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                                .withExecutionTimeoutInMilliseconds(timeout)
                                .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD)
                        )
                        //线程池的配置
                        .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
                                .withCoreSize(10)
                        )
        );
    }


    @Override
    public Object access(ProceedingJoinPoint joinPoint, Method method, UseHystrix useHystrix, Object[] args) throws Throwable {
        //获取参数信息
        this.joinPoint = joinPoint;
        this.method = method;
        this.useHystrix = useHystrix;
        //设置熔断时间
        Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("GovernGroup"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(useHystrix.timeoutValue()));
        return this.execute();
    }

    /**
     * 返回正确方法调用结果
     *
     * @return {@link Object}
     * @throws Exception 异常
     */
    @Override
    protected Object run() throws Exception {
        try {
            return joinPoint.proceed();
        } catch (Throwable e) {
           return null;
        }
    }

    /**
     * 返回熔断保护时的对象信息
     *
     * @return {@link Object}
     */
    @Override
    protected Object getFallback(){
        return JSON.parseObject(useHystrix.returnJson(),method.getReturnType());
    }
}
