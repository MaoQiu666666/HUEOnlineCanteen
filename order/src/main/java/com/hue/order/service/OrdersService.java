package com.hue.order.service;

import com.hue.common.dto.OrderCreateDTO;
import com.hue.common.dto.OrderStatusDTO;
import com.hue.common.vo.OrderVO;
import com.hue.order.pojo.Orders;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 21991
* @description 针对表【orders(订单表)】的数据库操作Service
* @createDate 2026-04-23 19:16:11
*/
public interface OrdersService extends IService<Orders> {
    OrderVO createOrder(OrderCreateDTO orderCreateDTO);
    Boolean setOrderStatus(OrderStatusDTO orderStatusDTO);

}
