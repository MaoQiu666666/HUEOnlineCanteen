package com.hue.order.controller;

import com.hue.common.dto.OrderCreateDTO;
import com.hue.common.dto.OrderStatusDTO;
import com.hue.common.result.Result;
import com.hue.common.vo.OrderVO;
import com.hue.order.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class orderController {
    @Autowired
    private OrdersService ordersService;

    //创建订单
    @PostMapping("/create")
    @PreAuthorize("hasRole('user')")
    public Result<OrderVO> createOrder(@RequestBody OrderCreateDTO orderCreateDTO) {
        return Result.success("创建订单成功", ordersService.createOrder(orderCreateDTO));
    }

    //修改订单状态
    @PostMapping("/status/update")
    @PreAuthorize("hasRole('user')")
    public Result<Void> setOrderStatus(@RequestBody OrderStatusDTO orderStatusDTO) {
        ordersService.setOrderStatus(orderStatusDTO);
        return Result.success("order is " + orderStatusDTO.getAction());
    }

}
