package com.hue.common.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderCreateDTO {
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
     * 备注
     */
    private String remark;
}
