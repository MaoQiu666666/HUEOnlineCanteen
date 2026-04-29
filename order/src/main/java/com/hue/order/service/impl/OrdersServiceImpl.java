package com.hue.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hue.common.dto.OrderCreateDTO;
import com.hue.common.dto.OrderStatusDTO;
import com.hue.common.exception.BusinessErrorCode;
import com.hue.common.exception.BusinessException;
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

    //设置订单状态；

    @Override
    public Boolean setOrderStatus(OrderStatusDTO orderStatusDTO) {
        LocalDateTime now = LocalDateTime.now();
        LambdaUpdateWrapper<Orders> wrapper = new LambdaUpdateWrapper<>();
        wrapper
                .eq(Orders::getOrderNo, orderStatusDTO .getOrderNo());
        switch (orderStatusDTO.getAction()) {
            case "PAYED":
                wrapper.set(Orders::getPayTime, now)
                        .set(Orders::getStatus, 2);
                break;
            case "RECEiVED":
                wrapper.set(Orders::getReceivingTime, now)
                        .set(Orders::getStatus, 3);
                break;
            case "DISPATCHED":
                wrapper.set(Orders::getDispatchTime, now)
                        .set(Orders::getStatus, 4);
                break;
            case "COMPLETED":
                wrapper.set(Orders::getCompletionTime, now)
                        .set(Orders::getStatus, 5);
                break;
            case "CANCEL":
                wrapper.set(Orders::getCancelTime, now)
                        .set(Orders::getStatus, 6);
                break;
            default:
                throw new BusinessException(BusinessErrorCode.UNKNOWN_ORDER_ACTION);
        }
        return baseMapper.update(null,wrapper) > 0;
    }
}




