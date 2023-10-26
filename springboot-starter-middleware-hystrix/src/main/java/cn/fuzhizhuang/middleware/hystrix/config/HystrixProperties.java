package cn.fuzhizhuang.middleware.hystrix.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author fuzhizhuang
 * @description TODO
 */

@ConfigurationProperties(prefix = "middleware.hystrix")
@Data
public class HystrixProperties {

    private int timeout;

}
