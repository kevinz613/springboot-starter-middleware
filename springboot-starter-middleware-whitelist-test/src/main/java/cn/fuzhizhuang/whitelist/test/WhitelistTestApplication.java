package cn.fuzhizhuang.whitelist.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("cn.fuzhizhuang.whitelist.*")
public class WhitelistTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(WhitelistTestApplication.class, args);
    }

}
