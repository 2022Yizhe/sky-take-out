package com.sky.service;


import com.sky.vo.TurnoverReportVO;

import java.time.LocalDate;


public interface ReportService {

    /**
     * 营业额统计
     * @param begin 统计开始日期
     * @param end 统计结束日期
     * @return 营业额数据
     */
    TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end);
}
