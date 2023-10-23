package cn.fuzhizhuang.middleware.hystrix.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author fuzhizhuang
 * @description hystrix测试启动类
 */

@SpringBootApplication
@ComponentScan("cn.fuzhizhuang.middleware.hystrix.*")
public class HystrixApplication {
    public static void main(String[] args) {
        SpringApplication.run(HystrixApplication.class,args);
    }
}
