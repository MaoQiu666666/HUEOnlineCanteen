package com.hue.dish.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hue.common.dto.DishCreateDTO;
import com.hue.common.dto.DishStockDTO;
import com.hue.dish.pojo.Dish;
import com.hue.dish.service.DishService;
import com.hue.dish.mapper.DishMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
* @author 21991
* @description 针对表【dish(菜品表)】的数据库操作Service实现
* @createDate 2026-04-22 22:14:03
*/
@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish>
    implements DishService {

    //下单减库存，远程调用服务
    @Override
    public void minusStock(DishStockDTO dishStockDTO) {
        Dish dish = this.getById(dishStockDTO.getDishId());
        if(dish == null) {
            throw new RuntimeException("菜品不存在！");
        }
        int newStock = dish.getStock() - dishStockDTO.getAccount();
        if (newStock < 0) {
            throw new RuntimeException("库存不足!");
        }
        dish.setStock(newStock);
        this.updateById(dish);
    }

    @Override
    public void addStock(DishStockDTO dishStockDTO) {
        Dish dish = this.getById(dishStockDTO.getDishId());
        if(dish == null) {
            throw new RuntimeException("菜品不存在！");
        }
        int newStock = dish.getStock() + dishStockDTO.getAccount();
        dish.setStock(newStock);
        this.updateById(dish);
    }

    @Override
    public Long createDish(DishCreateDTO dishCreateDTO) {
        // Service层兜底参数校验
        Assert.notNull(dishCreateDTO, "菜品创建参数不能为空");
        Assert.hasText(dishCreateDTO.getName(), "菜品名称不能为空");
        Assert.notNull(dishCreateDTO.getPrice(), "菜品价格不能为空");
        Assert.isTrue(dishCreateDTO.getPrice().compareTo(java.math.BigDecimal.ZERO) > 0, "菜品价格必须大于0");

        // 对象转换（加注释说明，避免复制敏感字段）
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishCreateDTO, dish); // 只复制DTO中存在的字段，不会覆盖默认值
        // 显式设置业务默认值和自动填充字段
        dish.setStatus(0); // 0=下架（新创建的菜品默认下架，审核后上架）
        dish.setIsDeleted(0); // 0=未删除
        dish.setCreateTime(LocalDateTime.now()); // 创建时间
        dish.setUpdateTime(LocalDateTime.now()); // 更新时间


        boolean saveSuccess = this.save(dish);
        Assert.isTrue(saveSuccess, "创建菜品失败，请稍后重试");

        // 记录日志
        log.info("创建菜品成功：dishId={}, dishName={}, price={}",
                dish.getId(), dish.getName(), dish.getPrice());

        //返回新创建的菜品ID
        return dish.getId();
    }
    @Override
    public void setDishStatus(Integer dishId, Integer status) {
        // 1. 参数校验（Spring自带Assert，简单高效）
        Assert.notNull(dishId, "菜品ID不能为空");
        Assert.notNull(status, "状态不能为空");
        Assert.isTrue(status == 0 || status == 1, "状态只能是0或1");

        // 2. 构建更新条件（MyBatis-Plus LambdaUpdateWrapper，避免硬编码字段名）
        LambdaUpdateWrapper<Dish> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Dish::getId, dishId)
                .set(Dish::getStatus, status)
                .set(Dish::getUpdateTime, LocalDateTime.now()); // 自动填充更新时间

        // 3. 执行更新
        int affectedRows = baseMapper.update(null, wrapper);

        // 4. 校验更新结果（防止dishId不存在）
        Assert.isTrue(affectedRows > 0, "更新菜品状态失败，菜品不存在");

        // 5. 记录日志
        log.info("更新菜品状态成功：dishId={}, status={}", dishId, status);
    }
    @Override
    public void deleteDish(Integer dishId, Integer isDeleted) {
        // 1. 参数校验
        Assert.notNull(dishId, "菜品ID不能为空");
        Assert.notNull(isDeleted, "删除标记不能为空");
        Assert.isTrue(isDeleted == 0 || isDeleted == 1, "删除标记只能是0或1");

        // 2. 构建更新条件
        LambdaUpdateWrapper<Dish> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Dish::getId, dishId)
                .set(Dish::getStatus, isDeleted)
                .set(Dish::getUpdateTime, LocalDateTime.now());

        // 3. 执行更新
        int affectedRows = baseMapper.update(null, wrapper);

        // 4. 校验更新结果
        Assert.isTrue(affectedRows > 0, "逻辑删除/恢复菜品失败，菜品不存在");

        // 5. 记录日志
        String operation = isDeleted == 1 ? "逻辑删除" : "恢复";
        log.info("{}菜品成功：dishId={}", operation, dishId);
    }
    @Override
    public void updateDishPrice(Integer dishId, BigDecimal price){
        // 1. 参数校验
        Assert.notNull(dishId, "菜品ID不能为空");
        Assert.notNull(price, "价格不能为空");
        Assert.isTrue(price.compareTo(BigDecimal.ZERO) > 0, "价格必须大于0");

        // 2. 构建更新条件
        LambdaUpdateWrapper<Dish> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Dish::getId, dishId)
                .set(Dish::getPrice, price)
                .set(Dish::getUpdateTime, LocalDateTime.now());

        // 3. 执行更新
        int affectedRows = baseMapper.update(null, wrapper);

        // 4. 校验更新结果
        Assert.isTrue(affectedRows > 0, "更新菜品价格失败，菜品不存在");

        // 5. 记录日志
        log.info("更新菜品价格成功：dishId={}, newPrice={}", dishId, price);
    }
    @Override
    public void updateDishStock(Integer dishId, Integer newStock){
        // 1. 参数校验
        Assert.notNull(dishId, "菜品ID不能为空");
        Assert.notNull(newStock, "库存不能为空");
        Assert.isTrue(newStock >= 0, "库存不能为负数");

        // 2. 构建更新条件
        LambdaUpdateWrapper<Dish> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Dish::getId, dishId)
                .set(Dish::getStock, newStock)
                .set(Dish::getUpdateTime, LocalDateTime.now());

        // 3. 执行更新
        int affectedRows = baseMapper.update(null, wrapper);

        // 4. 校验更新结果
        Assert.isTrue(affectedRows > 0, "更新菜品库存失败，菜品不存在");

        // 5. 记录日志
        log.info("调整菜品库存成功：dishId={}, newStock={}", dishId, newStock);
    }

}




