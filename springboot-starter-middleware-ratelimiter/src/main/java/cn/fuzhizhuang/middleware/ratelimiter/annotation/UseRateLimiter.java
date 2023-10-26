package cn.fuzhizhuang.middleware.ratelimiter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author fuzhizhuang
 * @description 标记限流注解
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface UseRateLimiter {

    /**
     * 限流许可量
     *
     * @return 每秒限流量
     */
    double permitsPerSecond() default 0D;

    /**
     * 失败结果 JSON，超过permitsPerSecond的量就会返回这里设置的信息
     *
     * @return 失败结果JSON信息
     */
    String returnJson() default "";
}
