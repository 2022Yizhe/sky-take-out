package com.sky.mapper;


import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;


@Mapper
public interface OrderMapper {

    /**
     * 插入订单数据
     * & 动态 SQL
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
     * & 动态 SQL
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

    /**
     * 根据订单 id 查询订单
     * @param id 订单 id
     * @return 订单对象
     */
    @Select("select * from orders where id = #{id}")
    Orders getById(Long id);

    /**
     * 分页条件查询并按下单时间排序
     * & 动态 SQL
     * @param ordersPageQueryDTO 查询参数 DTO 对象
     */
    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 根据订单状态统计订单数量
     * @param status 订单状态
     */
    @Select("select count(id) from orders where status = #{status}")
    Integer countStatus(Integer status);
}
