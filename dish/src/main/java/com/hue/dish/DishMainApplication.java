package com.hue.dish;



import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@EnableDiscoveryClient //开启服务发现
@SpringBootApplication(scanBasePackages = {"com.hue.common", "com.hue.dish"})
@MapperScan("com.hue.dish.mapper")
public class DishMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(DishMainApplication.class, args);
    }
}
