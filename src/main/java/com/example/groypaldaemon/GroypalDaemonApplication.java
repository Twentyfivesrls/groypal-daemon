package com.example.groypaldaemon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.twentyfive.authorizationflow", "com.example.groypaldaemon"})
@EnableFeignClients
public class GroypalDaemonApplication {

    public static void main(String[] args) {
        SpringApplication.run(GroypalDaemonApplication.class, args);
    }

}
