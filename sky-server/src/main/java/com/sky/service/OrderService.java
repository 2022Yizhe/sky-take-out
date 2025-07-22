package com.sky.service;

import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import org.springframework.transaction.annotation.Transactional;


public interface OrderService {

    /**
     * 用户提交订单
     * @param ordersSubmitDTO 订单数据 DTO 对象
     */
    @Transactional
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);

    /**
     * 订单支付
     * @param ordersPaymentDTO 支付信息 DTO 对象
     * @return 预支付交易单
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支付成功，修改订单状态
     * @param outTradeNo 微信支付返回的订单号
     */
    void paySuccess(String outTradeNo);

}
