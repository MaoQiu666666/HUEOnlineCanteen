package com.hue.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hue.common.dto.OrderCreateDTO;
import com.hue.common.util.OrderNoUtil;
import com.hue.common.vo.OrderVO;
import com.hue.order.pojo.Orders;
import com.hue.order.service.OrdersService;
import com.hue.order.mapper.OrdersMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
* @author 21991
* @description 针对表【orders(订单表)】的数据库操作Service实现
* @createDate 2026-04-23 19:16:11
*/
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders>
    implements OrdersService{

    @Override
    public OrderVO createOrder(OrderCreateDTO orderCreateDTO) {
        Orders order = new Orders();
        BeanUtils.copyProperties(orderCreateDTO, order);
        order.setOrderNo(OrderNoUtil.generateOrderNo(orderCreateDTO.getUserId()));
        order.setStatus(1);
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        this.save(order);
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(order, orderVO);
        return orderVO;
    }
}




