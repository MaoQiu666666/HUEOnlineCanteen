package com.hue.order;




import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


@EnableDiscoveryClient//开启服务发现
@EnableFeignClients(basePackages = "com.hue.common.feign")
@SpringBootApplication(scanBasePackages = {"com.hue.common", "com.hue.order"})
@MapperScan("com.hue.order.mapper")
public class OrderMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderMainApplication.class, args);
    }
}