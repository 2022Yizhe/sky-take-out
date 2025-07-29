package com.sky.service;

import com.sky.dto.*;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import org.springframework.transaction.annotation.Transactional;


public interface OrderService {

    /**
     * 用户提交订单
     *
     * @param ordersSubmitDTO 订单数据 DTO 对象
     */
    @Transactional
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO 支付信息 DTO 对象
     * @return 预支付交易单
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支付成功，修改订单状态
     *
     * @param outTradeNo 微信支付返回的订单号
     */
    void paySuccess(String outTradeNo);

    /**
     * 历史订单查询（分页）
     *
     * @param pageNum  页码
     * @param pageSize 每页数量
     * @param status   订单状态
     * @return 订单分页数据
     */
    PageResult pageQuery4User(int pageNum, int pageSize, Integer status);

    /**
     * 订单详情
     *
     * @param id 订单 id
     * @return 订单详情
     */
    OrderVO details(Long id);

    /**
     * 用户取消订单
     *
     * @param id 订单 id
     */
    void userCancelById(Long id) throws Exception;

    /**
     * 再来一单
     *
     * @param id 原订单 id
     */
    void repetition(Long id);

    /**
     * 客户催单
     *
     * @param id 订单 id
     */
    void remainder(Long id);

    /**
     * 根据条件 分页查询订单
     *
     * @param ordersPageQueryDTO 条件参数 DTO 对象
     * @return 订单分页数据
     */
    PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 各个状态的订单数量统计
     *
     * @return 订单数量统计结果
     */
    OrderStatisticsVO statistics();

    /**
     * 接单
     *
     * @param ordersConfirmDTO 接单参数 DTO 对象
     */
    void confirm(OrdersConfirmDTO ordersConfirmDTO);

    /**
     * 拒单
     *
     * @param ordersRejectionDTO 拒单参数 DTO 对象
     */
    void rejection(OrdersRejectionDTO ordersRejectionDTO) throws Exception;

    /**
     * 商家取消订单
     *
     * @param ordersCancelDTO 取消订单参数 DTO 对象
     */
    void cancel(OrdersCancelDTO ordersCancelDTO) throws Exception;

    /**
     * 派送订单
     *
     * @param id 订单 id
     */
    void delivery(Long id);

    /**
     * 完成订单
     * @param id 订单 id
     */
    void complete(Long id);
}
