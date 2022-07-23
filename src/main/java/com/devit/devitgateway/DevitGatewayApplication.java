package com.devit.devitgateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
@Slf4j
public class DevitGatewayApplication {

    public static void main(String[] args) {
        log.info("ㅂㅐ포 테스트");
        SpringApplication.run(DevitGatewayApplication.class, args);
    }

}
