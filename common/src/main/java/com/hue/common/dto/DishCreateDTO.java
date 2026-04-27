package com.hue.common.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DishCreateDTO {
    @NotBlank(message = "菜品名称不能为空")
    private String name;

    @NotNull(message = "菜品价格不能为空")
    @DecimalMin(value = "0.01", message = "菜品价格必须大于0")
    private BigDecimal price;

    @NotNull(message = "库存数量不能为空")
    @Min(value = 0, message = "库存不能小于0")
    private Integer stock;

    @NotNull(message = "所属窗口ID不能为空")
    private Long windowId;

    private String dishImage; // 菜品图片
    private String description; // 菜品描述
    private Integer categoryId; // 菜品分类ID
}
