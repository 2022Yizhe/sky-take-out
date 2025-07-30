package com.sky.service.impl;


import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.service.ReportService;
import com.sky.vo.TurnoverReportVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderMapper orderMapper;

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
            // 获取指定日期的营业额（订单已完成 - COMPLETED）
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

        // 按需求拼接营业额
        StringJoiner turnoverJoiner = new StringJoiner(",");
        turnoverList.forEach(turnover -> turnoverJoiner.add(turnover.toString()));

        return TurnoverReportVO.builder()
                .dateList(dateJoiner.toString())
                .turnoverList(turnoverJoiner.toString())
                .build();
    }
}
