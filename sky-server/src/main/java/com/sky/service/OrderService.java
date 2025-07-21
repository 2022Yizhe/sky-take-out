package com.sky.service;

import com.sky.dto.OrdersSubmitDTO;
import com.sky.vo.OrderSubmitVO;
import org.springframework.transaction.annotation.Transactional;


public interface OrderService {

    /**
     * 用户提交订单
     * @param ordersSubmitDTO 订单数据 DTO 对象
     */
    @Transactional
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);
}
