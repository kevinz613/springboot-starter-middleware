package cn.fuzhizhuang.whitelist.annotation;

import java.lang.annotation.*;

/**
 * @author fuzhizhuang
 * @description 在需要使用到白名单服务接口上添加该注解并配置必要信息，接口入参提取字段属性名称、拦截后的返回信息
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface UseWhiteList {

    /**
     * 配置当前接口入参所需要提取的属性
     */
    String key() default "";

    /**
     * 拦截用户请求后给出一个返回信息
     */
    String returnJson() default "";
}
