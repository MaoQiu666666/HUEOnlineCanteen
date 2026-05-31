package com.hue.order.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.hue.common.dto.DishStockDTO;
import lombok.Data;

/**
 * 订单表
 * @TableName orders
 */
@TableName(value ="orders")
@Data
public class Orders {
    /**
     * 订单ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 窗口ID
     */
    private Long windowId;

    /**
     * 1-自提 2-堂食
     */
    private Integer orderType;

    /**
     * 总金额
     */
    private BigDecimal totalAmount;
    /**
     * 优惠金额
     */
    private BigDecimal disAmount;

    /**
     * 实付金额
     */
    private BigDecimal payAmount;

    /**
     * 1-待支付 2-待接单 3-制作中 4-待取餐 5-已完成 6-已取消
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    /**
     * 支付时间
     */
    private LocalDateTime payTime;
    /**
     * 接单时间
     */
    private LocalDateTime receivingTime;
    /**
     * 出餐时间
     */
    private LocalDateTime dispatchTime;
    /**
     * 订单完成时间
     */
    private LocalDateTime completionTime;
    /**
     * 订单取消时间
     */
    private LocalDateTime cancelTime;
    

}