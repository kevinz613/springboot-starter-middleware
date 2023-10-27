package cn.fuzhizhuang.middleware.govern.service.impl;

import cn.fuzhizhuang.middleware.govern.annotation.UseWhiteList;
import cn.fuzhizhuang.middleware.govern.service.IValveService;
import com.alibaba.fastjson.JSON;
import org.apache.commons.beanutils.BeanUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * @author fuzhizhuang
 * @description 封装白名单实现
 */
public class WhiteListImpl implements IValveService {

    private final Logger logger = LoggerFactory.getLogger(WhiteListImpl.class);

    private final String whiteListConfig;

    public WhiteListImpl(String whiteListConfig) {

        this.whiteListConfig = whiteListConfig;
    }

    @Override
    public Object access(ProceedingJoinPoint joinPoint, Method method, Object annotation, Object[] args) throws Throwable {
        if (!match(annotation)) {
            //放行
            joinPoint.proceed();
        }
        //具体业务逻辑实现
        UseWhiteList useWhiteList = (UseWhiteList) annotation;
        //获取字段信息
        String keyValue = getFieldValue(useWhiteList.key(), joinPoint.getArgs());
        logger.info("whitelist handler method:{} value:{}", method.getName(), keyValue);
        //解析配置信息
        String users = whiteListConfig;
        String[] userList = users.split(",");
        //白名单过滤
        for (String user : userList) {
            if (keyValue.equals(user)) {
                //放行
                return joinPoint.proceed();
            }
        }
        //拦截
        return returnObject(useWhiteList, method);
    }

    @Override
    public boolean match(Object annotation) {
        return annotation instanceof UseWhiteList;
    }

    /**
     * 获取字段值
     *
     * @param key  入参所需属性
     * @param args 参数
     * @return 字符串
     */
    private String getFieldValue(String key, Object[] args) {
        String fieldValue = null;
        for (Object arg : args) {
            try {
                if (fieldValue == null || "".equals(fieldValue)) {
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
}
