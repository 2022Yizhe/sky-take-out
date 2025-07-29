package com.sky.mapper;


import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OrderDetailMapper {

    /**
     * 批量插入订单明细表
     * @param orderDetailList 订单明细 列表
     */
    void insertBatch(List<OrderDetail> orderDetailList);

    /**
     * 根据订单 id 查询订单明细
     * @param orderId 订单 id
     * @return 订单明细
     */
    @Select("select * from order_detail where order_id = #{orderId}")
    List<OrderDetail> getByOrderId(Long orderId);
}
