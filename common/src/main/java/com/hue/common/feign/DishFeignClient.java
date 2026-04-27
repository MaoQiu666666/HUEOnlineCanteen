package com.hue.common.feign;



import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("dish")
public interface DishFeignClient {
    @PostMapping("dish/minusStock")
    void minusStock(@RequestParam Integer dishId, @RequestParam Integer account);
}
