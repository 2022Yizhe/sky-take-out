package com.sky.service;


import com.sky.vo.OrderReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

import java.time.LocalDate;


public interface ReportService {

    /**
     * 营业额统计
     * @param begin 统计开始日期
     * @param end 统计结束日期
     * @return 营业额数据
     */
    TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end);

    /**
     * 用户统计
     * @param begin 统计开始日期
     * @param end 统计结束日期
     * @return 用户数据
     */
    UserReportVO getUserStatistics(LocalDate begin, LocalDate end);

    /**
     * 订单统计
     * @param begin 统计开始日期
     * @param end 统计结束日期
     * @return 订单数据
     */
    OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end);
}
