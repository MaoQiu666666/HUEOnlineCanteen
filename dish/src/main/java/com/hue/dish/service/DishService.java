package com.hue.dish.service;

import com.hue.common.dto.DishCreateDTO;
import com.hue.dish.pojo.Dish;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;

/**
* @author 21991
* @description 针对表【dish(菜品表)】的数据库操作Service
* @createDate 2026-04-22 22:14:03
*/
public interface DishService extends IService<Dish> {
    //远程调用服务，下单时减库存
    void minusStock(Integer dishId, Integer account);
    //创建菜品
    Long createDish(DishCreateDTO dishCreateDTO);
    //上下架菜品
    void setDishStatus(Integer dishId, Integer status);
    //删除菜品
    void deleteDish(Integer dishId, Integer idDeleted);
    //修改菜品价格
    void updateDishPrice(Integer dishId, BigDecimal price);
    //修改菜品库存
    void updateDishStock(Integer dishId, Integer newStock);
}
