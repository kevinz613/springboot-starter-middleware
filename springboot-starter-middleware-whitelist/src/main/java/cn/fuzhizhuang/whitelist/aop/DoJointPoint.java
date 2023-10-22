package cn.fuzhizhuang.whitelist.aop;

import cn.fuzhizhuang.whitelist.annotation.UseWhiteList;
import com.alibaba.fastjson.JSON;
import jakarta.annotation.Resource;
import org.apache.commons.beanutils.BeanUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author fuzhizhuang
 * @description 切面逻辑实现
 */

@Aspect
@Component
public class DoJointPoint {

    private final Logger logger = LoggerFactory.getLogger(DoJointPoint.class);

    @Resource
    private String whiteListConfig;

    /**
     * 切点，通过自定义注解的方式
     */
    @Pointcut("@annotation(cn.fuzhizhuang.whitelist.annotation.UseWhiteList)")
    public void aopPoint() {

    }

    /**
     * 在doRouter中拦截方法后，获取方法的自定义注解
     *
     * @param joinPoint 加入点
     * @return {@link Object}
     * @throws Throwable 可投掷
     * @Around("aopPoint()") 可以理解为是对方法增强的织入动作，注解的效果：在调用加了自定义注解@UseWhiteList的方法时，会先进入到此切点增强的方法，我们可以对该方法进行操作，比如实现白名单用户的拦截还是放行
     */
    @Around("aopPoint()")
    public Object doRouter(ProceedingJoinPoint joinPoint) throws Throwable {
        //获取内容
        Method method = getMethod(joinPoint);
        UseWhiteList whiteList = method.getAnnotation(UseWhiteList.class);
        //获取字段值
        String keyValue = getFieldValue(whiteList.key(), joinPoint.getArgs());
        logger.info("whitelist handler method-{} value-{}", method.getName(), keyValue);
        if (null == keyValue || "".equals(keyValue)) {
            //放行
            return joinPoint.proceed();
        }
        //解析配置信息
        String[] users = whiteListConfig.split(",");
        //白名单过滤
        for (String user : users) {
            if (keyValue.equals(user)) {
                //放行
                return joinPoint.proceed();
            }
        }
        //拦截
        return returnObject(whiteList, method);
    }

    /**
     * 返回对象
     *
     * @param whiteList 白名单
     * @param method    方法
     * @return 对象
     */
    private Object returnObject(UseWhiteList whiteList, Method method) throws InstantiationException, IllegalAccessException {
        Class<?> returnType = method.getReturnType();
        String returnJson = whiteList.returnJson();
        if ("".equals(returnJson)) {
            return returnType.newInstance();
        }
        return JSON.parseObject(returnJson, returnType);
    }

    /**
     * 获取字段值
     *
     * @param key  key
     * @param args 参数
     * @return 字符串
     */
    private String getFieldValue(String key, Object[] args) {
        String fieldValue = null;
        for (Object arg : args) {
            try {
                if (null == fieldValue || "".equals(fieldValue)) {
                    BeanUtils.getProperty(arg, fieldValue);
                } else {
                    break;
                }
            } catch (Exception e) {
                if (args.length == 1) {
                    return args[0].toString();
                }
            }
        }
        return fieldValue;
    }

    private Method getMethod(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        return joinPoint.getTarget().getClass().getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
    }


}
