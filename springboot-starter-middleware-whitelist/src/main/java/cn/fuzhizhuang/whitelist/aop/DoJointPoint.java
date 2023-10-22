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

    private Logger logger = LoggerFactory.getLogger(DoJointPoint.class);

    @Resource
    private String whiteListConfig;

    @Pointcut("@annotation(cn.fuzhizhuang.whitelist.annotation.UseWhiteList)")
    public void aopPoint(){

    }

    @Around("aopPoint()")
    public Object doRouter(ProceedingJoinPoint joinPoint) throws Throwable {
        //获取内容
        Method method = getMethod(joinPoint);
        UseWhiteList whiteList = method.getAnnotation(UseWhiteList.class);
        //获取字段值
        String keyValue = getFieldValue(whiteList.key(),joinPoint.getArgs());
        logger.info("whitelist handler method-{} value-{}",method.getName(),keyValue);
        if (null == keyValue || "".equals(keyValue)){
            return joinPoint.proceed();
        }
        //解析配置信息
        String[] users = whiteListConfig.split(",");
        //白名单过滤
        for (String user:users){
            if (keyValue.equals(user)){
                return joinPoint.proceed();
            }
        }
        //拦截
        return returnObject(whiteList,method);
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
        if ("".equals(returnJson)){
            return returnType.newInstance();
        }
        return JSON.parseObject(returnJson,returnType);
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
        for (Object arg : args){
            try {
                if (null == fieldValue || "".equals(fieldValue)){
                    BeanUtils.getProperty(arg,fieldValue);
                }else {
                    break;
                }
            } catch (Exception e) {
                if (args.length == 1){
                    return args[0].toString();
                }
            }
        }
        return fieldValue;
    }

    private Method getMethod(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        return joinPoint.getTarget().getClass().getMethod(methodSignature.getName(),methodSignature.getParameterTypes());
    }



}
