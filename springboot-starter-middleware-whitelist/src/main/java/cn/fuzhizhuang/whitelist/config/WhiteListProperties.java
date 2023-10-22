package cn.fuzhizhuang.whitelist.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author fuzhizhuang
 * @description 获取白名单配置,创建指定前缀（prefix = "middleware.whitelist"）的自定义配置信息，可以在yml或者properties中读取到我们自定义的配置信息
 */
@ConfigurationProperties(prefix = "middleware.whitelist")
@Data
public class WhiteListProperties {

    private String users;

}
