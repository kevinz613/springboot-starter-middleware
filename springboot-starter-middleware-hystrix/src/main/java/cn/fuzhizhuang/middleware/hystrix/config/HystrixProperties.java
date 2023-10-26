package cn.fuzhizhuang.middleware.hystrix.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author fuzhizhuang
 * @description 获取熔断配置
 */

@ConfigurationProperties(prefix = "middleware.hystrix")
@Data
public class HystrixProperties {

    private int timeout;

}
