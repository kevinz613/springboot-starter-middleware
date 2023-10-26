package cn.fuzhizhuang.middleware.hystrix.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author fuzhizhuang
 * @description 自动配置Hystrix
 */

@Configuration
@ConditionalOnClass(HystrixProperties.class)
@EnableConfigurationProperties(HystrixProperties.class)
public class HystrixAutoConfigure {

    @Bean("hystrixConfig")
    public int hystrixConfig(HystrixProperties properties) {
        return properties.getTimeout();
    }

}
