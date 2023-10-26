package cn.fuzhizhuang.middleware.methodintercept.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author fuzhizhuang
 * @description 自定义拦截注解
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface UseMethodIntercept {

    String method() default "";

    String returnJson() default "";
}
