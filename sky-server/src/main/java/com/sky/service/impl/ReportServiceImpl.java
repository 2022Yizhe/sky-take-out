package com.sky.service.impl;


import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;


@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * 营业额统计
     */
    @Override
    public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {

        /// 1.存放待统计的日期数据
        List<LocalDate> dateList = new ArrayList<>();

        // 加入起始日期
        dateList.add(begin);

        // 加入递增的日期
        while (!begin.equals(end)){
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

        // 按需求拼接日期
        StringJoiner dateJoiner = new StringJoiner(",", "", "");
        dateList.forEach(date -> dateJoiner.add(date.toString()));

        /// 2.存放待统计的营业额数据
        List<Double> turnoverList = new ArrayList<>();

        for (LocalDate localDate : dateList) {
            // 获取指定日期精确时间（订单已完成 - COMPLETED）
            LocalDateTime beginTime = LocalDateTime.of(localDate, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(localDate, LocalTime.MAX);

            // 构造查询参数
            Map<String, Object> map = new HashMap<>();
            map.put("beginTime", beginTime);
            map.put("endTime", endTime);
            map.put("status", Orders.COMPLETED);

            // 查询营业额
            Double turnover = orderMapper.sumByMap(map);
            turnover = turnover == null ? 0.0 : turnover;   // 当日没有营业额，三目运算符处理避免空指针异常
            turnoverList.add(turnover);
        }

        /// 3.封装返回结果 (按需求拼接营业额)
        StringJoiner turnoverJoiner = new StringJoiner(",");
        turnoverList.forEach(turnover -> turnoverJoiner.add(turnover.toString()));

        return TurnoverReportVO.builder()
                .dateList(dateJoiner.toString())
                .turnoverList(turnoverJoiner.toString())
                .build();
    }

    /**
     * 用户统计
     */
    @Override
    public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {

        /// 1.存放待统计的日期数据
        List<LocalDate> dateList = new ArrayList<>();

        // 加入起始日期
        dateList.add(begin);

        // 加入递增的日期
        while (!begin.equals(end)){
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

        // 按需求拼接日期
        StringJoiner dateJoiner = new StringJoiner(",", "", "");
        dateList.forEach(date -> dateJoiner.add(date.toString()));

        /// 2.存放每天的用户总量、新增的用户数量
        List<Integer> totalUserList = new ArrayList<>();
        List<Integer> newUserList = new ArrayList<>();

        for (LocalDate localDate : dateList) {
            // 获取指定日期精确时间
            LocalDateTime beginTime = LocalDateTime.of(localDate, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(localDate, LocalTime.MAX);

            // 构造查询参数
            Map<String, Object> map = new HashMap<>();
            map.put("endTime", endTime);

            // 查询用户总量
            Integer total = userMapper.countByMap(map);
            total = total == null ? 0 : total;   // 当日没有，三目运算符处理避免空指针异常
            totalUserList.add(total);

            // 构造查询参数
            map.put("beginTime", beginTime);

            // 查询用户增量
            Integer newUser = userMapper.countByMap(map);
            newUser = newUser == null ? 0 : newUser;
            newUserList.add(newUser);
        }

        /// 3.封装返回结果 (用户总量、新增的用户数量的时间函数字符串)
        StringJoiner totalUserJoiner = new StringJoiner(",");
        totalUserList.forEach(totalUser -> totalUserJoiner.add(totalUser.toString()));
        StringJoiner newUserJoiner = new StringJoiner(",");
        newUserList.forEach(newUser -> newUserJoiner.add(newUser.toString()));

        return UserReportVO.builder()
                .dateList(dateJoiner.toString())
                .totalUserList(totalUserJoiner.toString())
                .newUserList(newUserJoiner.toString())
                .build();
    }

    /**
     * 订单数据统计
     */
    @Override
    public OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end) {

        /// 1.存放待统计的日期数据
        List<LocalDate> dateList = new ArrayList<>();

        // 加入起始日期
        dateList.add(begin);

        // 加入递增的日期
        while (!begin.equals(end)){
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

        // 按需求拼接日期
        StringJoiner dateJoiner = new StringJoiner(",", "", "");
        dateList.forEach(date -> dateJoiner.add(date.toString()));

        /// 2.查询每天的有效订单数、总订单数
        List<Integer> validOrderCountList = new ArrayList<>();
        List<Integer> orderCountList = new ArrayList<>();

        for (LocalDate localDate : dateList) {
            // 获取指定日期精确时间
            LocalDateTime beginTime = LocalDateTime.of(localDate, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(localDate, LocalTime.MAX);

            // 构造查询参数
            Map<String, Object> map = new HashMap<>();
            map.put("beginTime", beginTime);
            map.put("endTime", endTime);

            // 查询订单总量
            Integer orderCount = orderMapper.countByMap(map);
            orderCount = orderCount == null ? 0 : orderCount;
            orderCountList.add(orderCount);

            // 构造查询参数
            map.put("status", Orders.COMPLETED);

            // 查询有效订单数量
            Integer validOrderCount = orderMapper.countByMap(map);
            validOrderCount = validOrderCount == null ? 0 : validOrderCount;
            validOrderCountList.add(validOrderCount);
        }

        /// 3.封装返回结果
        Integer total = orderCountList.stream().reduce(Integer::sum).get();
        Integer valid = validOrderCountList.stream().reduce(Integer::sum).get();
        Double CompletionRate = 0.0;
        if (total != 0){
            CompletionRate = 1.0 * valid / total;
        }

        StringJoiner orderCountJoiner = new StringJoiner(",");
        orderCountList.forEach(orderCount -> orderCountJoiner.add(orderCount.toString()));
        StringJoiner validOrderCountJoiner = new StringJoiner(",");
        validOrderCountList.forEach(validOrderCount -> validOrderCountJoiner.add(validOrderCount.toString()));

        return OrderReportVO.builder()
                .dateList(dateList.toString())
                .orderCountList(orderCountJoiner.toString())
                .validOrderCountList(validOrderCountJoiner.toString())
                .totalOrderCount(total)
                .validOrderCount(valid)
                .orderCompletionRate(CompletionRate)
                .build();
    }
}
