package com.hue.dish.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;

/**
 * 菜品表
 * @TableName dish
 */
@TableName(value ="dish")
@Data
public class Dish {
    /**
     * 菜品ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 窗口ID
     */
    private Long windowId;

    /**
     * 菜品名称
     */
    private String name;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 图片URL
     */
    private String imageUrl;

    /**
     * 库存
     */
    private Integer stock;

    /**
     * 是否热销 0-否 1-是
     */
    private Integer isHot;

    /**
     * 上架状态 0-下架 1-上架
     */
    private Integer status;

    /**
     * 日销量
     */
    private Integer dailySales;

    /**
     * 月销量
     */
    private Integer monthlySales;

    /**
     * 总销量
     */
    private Integer totalSales;

    /**
     * 创建时间
     * 更新时间
     */
    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    /**
     * 是否删除
     */
    private Integer isDeleted;
}