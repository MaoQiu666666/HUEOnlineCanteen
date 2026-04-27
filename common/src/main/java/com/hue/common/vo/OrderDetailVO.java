package com.hue.common.vo;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderDetailVO {
    // 订单号
    private String orderNo;
    // 订单金额
    @NotBlank(message = "订单金额不能为空")
    private BigDecimal totalAmount;
    @NotBlank(message = "优惠金额不能为空")
    private BigDecimal disAmount;
    @NotBlank(message = "支付金额不能为空")
    private BigDecimal payAmount;
}
