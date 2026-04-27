package com.hue.dish.cotroller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hue.common.dto.DishCreateDTO;
import com.hue.common.feign.DishFeignClient;
import com.hue.common.result.Result;
import com.hue.dish.pojo.Dish;
import com.hue.dish.service.DishService;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/dish")
public class DishController implements DishFeignClient {

    @Autowired
    private DishService dishService;

    //==========     基础服务    ==========

    @GetMapping("/list")
    public Result<List<Dish>> getDishList() {
        return Result.success("菜品列表",dishService.list());
    }

    @GetMapping("/hotlist")
    public Result<List<Dish>> getSortedDishList() {
        return Result.success("热门菜品排序列表", dishService.list(new LambdaQueryWrapper<Dish>().orderByDesc(Dish::getIsHot)));
    }

    //==========     远程调用服务    ==========

    /**
     * 1. 下单时减库存
     * @param dishId 菜品id
     * @param account 下单数量
     */
    @Override
    @PostMapping("/minusStock")
    public void minusStock(Integer dishId, Integer account) {
        dishService.minusStock(dishId,account);
    }


    // ==========     商家端操作接口     ==========

    /**
     * 1. 创建菜品
     * @param dishCreateDTO 菜品创建参数
     * @return 新创建的菜品ID
     */
    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('merchant', 'admin')")
    public Result<Long> createDish(@Validated @RequestBody DishCreateDTO dishCreateDTO) {
        Long dishId = dishService.createDish(dishCreateDTO);
        return Result.success("菜品创建成功", dishId);
    }

    /**
     * 2. 菜品上下架
     * @param dishId 菜品ID
     * @param status 状态：0-下架 1-上架
     */
    @PutMapping("/status")
    @PreAuthorize("hasAnyRole('merchant', 'admin')")
    public Result<Void> setDishStatus(
            @NotNull(message = "菜品ID不能为空") @RequestParam Integer dishId,
            @NotNull(message = "状态不能为空") @RequestParam Integer status
    ) {
        // 状态值合法性校验
        if (status != 0 && status != 1) {
            return Result.error("状态值非法，仅支持0(下架)、1(上架)");
        }
        dishService.setDishStatus(dishId, status);
        return Result.success(status == 1 ? "菜品上架成功" : "菜品下架成功");
    }

    /**
     * 3. 删除菜品（逻辑删除）
     * @param dishId 菜品ID
     * @param idDeleted 删除标识：0-恢复 1-已删除
     */
    @PutMapping("/delete")
    @PreAuthorize("hasAnyRole('merchant', 'admin')")
    public Result<Void> deleteDish(
            @NotNull(message = "菜品ID不能为空") @RequestParam Integer dishId,
            @NotNull(message = "删除标识不能为空") @RequestParam Integer idDeleted
    ) {
        // 删除标识合法性校验
        if (idDeleted != 0 && idDeleted != 1) {
            return Result.error("删除标识非法，仅支持0(恢复)、1(删除)");
        }
        dishService.deleteDish(dishId, idDeleted);
        return Result.success(idDeleted == 1 ? "菜品删除成功" : "菜品恢复成功");
    }

    /**
     * 4. 修改菜品价格
     * @param dishId 菜品ID
     * @param price 新价格（必须大于0）
     */
    @PutMapping("/price")
    @PreAuthorize("hasAnyRole('merchant', 'admin')")
    public Result<Void> updateDishPrice(
            @NotNull(message = "菜品ID不能为空") @RequestParam Integer dishId,
            @NotNull(message = "菜品价格不能为空")
            @DecimalMin(value = "0.01", message = "菜品价格必须大于0") @RequestParam BigDecimal price
    ) {
        dishService.updateDishPrice(dishId, price);
        return Result.success("菜品价格修改成功");
    }

    /**
     * 5. 修改菜品库存
     * @param dishId 菜品ID
     * @param newStock 新库存（不能小于0）
     */
    @PutMapping("/stock")
    @PreAuthorize("hasAnyRole('merchant', 'admin')")
    public Result<Void> updateDishStock(
            @NotNull(message = "菜品ID不能为空") @RequestParam Integer dishId,
            @NotNull(message = "库存数量不能为空")
            @Min(value = 0, message = "库存不能小于0") @RequestParam Integer newStock
    ) {
        dishService.updateDishStock(dishId, newStock);
        return Result.success("菜品库存修改成功");
    }

}
