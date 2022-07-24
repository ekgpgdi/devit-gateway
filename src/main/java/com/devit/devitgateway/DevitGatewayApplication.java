package com.devit.devitgateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class DevitGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevitGatewayApplication.class, args);
    }

}
