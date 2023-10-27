package cn.fuzhizhuang.middleware.govern.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("cn.fuzhizhuang.middleware.govern.*")
public class GovernApplication {

    public static void main(String[] args) {
        SpringApplication.run(GovernApplication.class, args);
    }

}
