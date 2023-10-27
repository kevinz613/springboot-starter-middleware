package cn.fuzhizhuang.middleware.govern.annotation;

import java.lang.annotation.*;

/**
 * @author fuzhizhuang
 * @description 自定义hystrix注解，标识该方法需要被拦截
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface UseHystrix {

    /**
     * 失败结果JSON
     *
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
