<!-- TOC -->
* [服务治理：超时熔断](#服务治理超时熔断)
* [方案设计](#方案设计)
* [技术实现](#技术实现)
  * [1.工程结构](#1工程结构)
  * [2.自定义注解 UseHystrix](#2自定义注解-usehystrix)
  * [3.熔断配置获取](#3熔断配置获取)
  * [4.熔断服务包装](#4熔断服务包装)
  * [5.切面逻辑实现](#5切面逻辑实现)
  * [6.spring.factories](#6springfactories)
<!-- TOC -->

# 服务治理：超时熔断

> 需求背景：<br>
> 如果说你开发过交易或者支付系统中的核心服务，可能有这样的场景：用户在电商平台下单后开始跳转到在线收银台进行支付。由于支付渠道和网络环境随时都有可能发生问题，那么如何保证支付系统的可靠性？
> <br>
> <br>
> 保证可靠性需要考虑的点有很多，但是最直接和重点的就是<em style="color: salmon">支付响应时长</em>,如果支付时间过长，那么暴增的支付请求可能就会把整个系统拖垮，最终导致所有服务瘫痪
> <br>
> <br>
> 所以我们可以使用超时熔断组件hystrix。如何使用？所有接口都加一个这样的功能组件，不太好；一般类似这样的组件可以嵌入到RPC接口或者自研的网关上，也可以在整个服务治理层的功能编排上。总之，它不会轻易暴露给你，让你硬编码到业务逻辑实现中。

# 方案设计
> 我们只想方便、简单并且不需要关心如如何创建和返回结果，我们就可以使用hystrix的框架包装到中间件中，屏蔽调用逻辑。

设计方案如下图：<br>
<img src="https://raw.githubusercontent.com/zhuangfuzhi/notes-images/main/imgs/image-20231026201151923.png">
- 使用自定义注解和切面技术，拦截需要倍熔断保护的方法
- 拦截到方法后，就可以通过hystrix给方法设定已经配置好的超时熔断处理。

# 技术实现
## 1.工程结构
<img src="https://raw.githubusercontent.com/zhuangfuzhi/notes-images/main/imgs/image-20231026201819781.png">

## 2.自定义注解 UseHystrix
```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface UseHystrix {

    /**
     * 失败结果JSON
     * @return
     */
    String returnJson() default "";

    /**
     * 超时时间，超过改时间执行超时熔断
     *
     * @return
     */
    int timeoutValue() default 0;
}

```
## 3.熔断配置获取
```java
@ConfigurationProperties(prefix = "middleware.hystrix")
@Data
public class HystrixProperties {

    private int timeout;

}

```
```java
@Configuration
@ConditionalOnClass(HystrixProperties.class)
@EnableConfigurationProperties(HystrixProperties.class)
public class HystrixAutoConfigure {

    @Bean("hystrixConfig")
    public int hystrixConfig(HystrixProperties properties) {
        return properties.getTimeout();
    }

}

```
## 4.熔断服务包装
```java
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

```
```java
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
    protected Object getFallback() {
        return JSON.parseObject(useHystrix.returnJson(), method.getReturnType());
    }
}

```
## 5.切面逻辑实现
```java
@Aspect
@Component
public class UseHystrixPoint {

    @Resource
    private int hystrixConfig;

    /**
     * AOP 切点
     */
    @Pointcut("@annotation(cn.fuzhizhuang.middleware.hystrix.annotation.UseHystrix)")
    public void aopPoint() {

    }

    @Around("aopPoint() && @annotation(useHystrix))")
    public Object doRouter(ProceedingJoinPoint joinPoint, UseHystrix useHystrix) throws Throwable {
        HystrixValveImpl hystrixValve = null;
        if (useHystrix.timeoutValue() != 0) {
            //方法自定义熔断时间
            hystrixValve = new HystrixValveImpl(useHystrix.timeoutValue());
        } else {
            //配置文件通用熔断时间
            hystrixValve = new HystrixValveImpl(hystrixConfig);
        }
        return hystrixValve.access(joinPoint, getMethod(joinPoint), useHystrix, joinPoint.getArgs());
    }

    private Method getMethod(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        return joinPoint.getTarget().getClass().getMethod(signature.getName(), methodSignature.getParameterTypes());
    }
}

```
## 6.spring.factories
```properties
org.springframework.boot.autoconfigure.EnableAutoConfiguration=cn.fuzhizhuang.middleware.hystrix.config.HystrixAutoConfigure

```
