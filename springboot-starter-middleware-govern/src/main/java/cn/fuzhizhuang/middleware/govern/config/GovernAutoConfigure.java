package cn.fuzhizhuang.middleware.govern.config;

import cn.fuzhizhuang.middleware.govern.annotation.UseHystrix;
import cn.fuzhizhuang.middleware.govern.annotation.UseWhiteList;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author fuzhizhuang
 * @description 中间件自动配置类
 */

@Configuration
@ConditionalOnClass(value = {UseWhiteList.class, UseHystrix.class}) //当WhiteListProperties位于当前类的路径上才去实例化一个类
@EnableConfigurationProperties(value = {WhiteListProperties.class, HystrixProperties.class})
public class GovernAutoConfigure {

    /**
     * 白名单配置
     *
     * @param whiteListProperties 白名单属性
     * @return 字符串
     * @ConditionalOnMissingBean 方法会在配置信息和Bean注册完成后，开始被实例化加载到Spring中
     */
    @Bean("whiteListConfig")
    @ConditionalOnMissingBean
    public String whiteListConfig(WhiteListProperties whiteListProperties) {
        return whiteListProperties.getUsers();
    }

    @Bean("hystrixConfig")
    public int hystrixConfig(HystrixProperties hystrixProperties) {
        return hystrixProperties.getTimeout();
    }

}
