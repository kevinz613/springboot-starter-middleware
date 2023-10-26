package cn.fuzhizhuang.middleware.methodintercept.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("cn.fuzhizhuang.middleware.methodintercept.*")
public class MethodInterceptApplication {

    public static void main(String[] args) {
        SpringApplication.run(MethodInterceptApplication.class, args);
    }

}
