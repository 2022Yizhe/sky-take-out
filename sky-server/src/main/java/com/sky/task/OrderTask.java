package com.sky.task;


import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;


@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;


    /**
     * 处理支付超时订单
     */
    @Scheduled(cron = "0 * * * * ? ")   // 每分钟执行一次
//    @Scheduled(cron = "1/5 * * * * ? ")   // 测试：每 5s 执行一次
    public void processTimeoutOrder() {

        // select * from orders where status = 1 and pay_time < (当前时间 - 15min)
        List<Orders> ordersList = orderMapper.getByStatusAndOrderTime(Orders.PENDING_PAYMENT, LocalDateTime.now().plusMinutes(-15));

        if (ordersList != null && !ordersList.isEmpty()) {
            for (Orders orders : ordersList) {
                orders.setStatus(Orders.CANCELLED);         // 设置订单状态为取消
                orders.setCancelReason("支付超时，取消订单");   // 设置取消原因
                orders.setCancelTime(LocalDateTime.now());  // 设置取消时间
                orderMapper.update(orders);
            }

            log.info("[Task] 已处理支付超时订单，完成 {} 个", ordersList.size());
        }
    }

    /**
     * 处理处于派送中的订单
     */
    @Scheduled(cron = "0 0 1 * * ? ")   // 每天凌晨 1 点执行一次
//    @Scheduled(cron = "0/5 * * * * ? ")   // 测试：每 5s 执行一次
    public void processDeliveryTimeoutOrder() {

        // select * from orders where status = 4 and delivery_time < (当前时间 - 60min)
        List<Orders> ordersList = orderMapper.getByStatusAndOrderTime(Orders.DELIVERY_IN_PROGRESS, LocalDateTime.now().plusMinutes(-60));

        if (ordersList != null && !ordersList.isEmpty()) {
            for (Orders orders : ordersList) {
                orders.setStatus(Orders.COMPLETED);             // 订单状态改为已完成
//                orders.setDeliveryTime(LocalDateTime.now());    // 订单完成时间
                orderMapper.update(orders);
            }

            log.info("[Task] 已处理处于派送中的订单，完成 {} 个", ordersList.size());
        }
    }
}
