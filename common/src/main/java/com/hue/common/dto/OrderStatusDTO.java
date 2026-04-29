package com.hue.common.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OrderStatusDTO {
    @NotBlank(message = "订单编号不能为空！")
    private String orderNo;
    private String action;
}
