package com.sky.mapper;


import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderMapper {

    /**
     * 插入订单数据
     * @param orders 订单对象
     */
    void insert(Orders orders);

    /**
     * 根据订单号查询订单
     * @param orderNumber 订单号
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     * @param orders 订单对象
     */
    void update(Orders orders);

    /**
     * 根据状态查询订单（支付、派送超时订单）
     * @param status 订单状态
     * @param endTime 理论结束时间（deadline）
     */
    @Select("select * from orders where status = #{status} and order_time < #{endTime}")
    List<Orders> getByStatusAndOrderTime(Integer status, LocalDateTime endTime);
}
