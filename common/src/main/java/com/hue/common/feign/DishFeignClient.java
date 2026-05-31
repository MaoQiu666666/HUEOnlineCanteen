package com.hue.common.feign;



import com.hue.common.dto.DishStockDTO;
import com.hue.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "dish")
public interface DishFeignClient {
    @PostMapping("/dish/minusStock")
    Result<Void> minusStock(@RequestBody DishStockDTO dishStockDTO);

    @PostMapping("/dish/addStock")
    Result<Void> addStock(@RequestBody DishStockDTO dishStockDTO);
}
