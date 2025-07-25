package com.sky.mapper;


import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderDetailMapper {

    /**
     * 批量插入订单明细表
     * @param orderDetailList 订单明细 列表
     */
    void insertBatch(List<OrderDetail> orderDetailList);
}
