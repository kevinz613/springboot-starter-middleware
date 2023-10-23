package cn.fuzhizhuang.middleware.hystrix.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author fuzhizhuang
 * @description 自定义hystrix注解，标识该方法需要被拦截
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
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
