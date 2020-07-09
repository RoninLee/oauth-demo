package com.ronin.oauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author lizelong
 * @date Created on 2020/7/6 10:50
 * @description
 */
@EnableFeignClients
@SpringBootApplication
@ComponentScan({"com.ronin.common", "com.ronin.oauth"})
public class OauthApplication {
    public static void main(String[] args) {
        SpringApplication.run(OauthApplication.class, args);
    }
}
