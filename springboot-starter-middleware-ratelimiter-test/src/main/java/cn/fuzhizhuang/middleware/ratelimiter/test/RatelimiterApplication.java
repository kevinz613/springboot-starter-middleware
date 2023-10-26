package cn.fuzhizhuang.middleware.ratelimiter.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("cn.fuzhizhuang.middleware.ratelimiter.*")
public class RatelimiterApplication {

    public static void main(String[] args) {
        SpringApplication.run(RatelimiterApplication.class, args);
    }

}
