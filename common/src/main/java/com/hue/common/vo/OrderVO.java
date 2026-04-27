package com.hue.common.vo;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderVO {
    //订单id
    private Long id;
    // 订单号
    private String orderNo;
    // 订单金额
    @NotBlank(message = "订单总金额不能为空")
    private BigDecimal totalAmount;
    @NotBlank(message = "优惠金额不能为空")
    private BigDecimal disAmount;
    @NotBlank(message = "支付金额不能为空")
    private BigDecimal payAmount;

    // 订单状态 0-待支付 1-已支付 2-已取消
    private Integer Status;
    // 下单时间
    private LocalDateTime createTime;
    // 模拟支付跳转链接
    private String payUrl;
}
