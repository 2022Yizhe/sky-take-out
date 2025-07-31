package com.sky.controller.admin;


import com.sky.result.Result;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

/**
 * 数据统计相关接口
 */
@RestController
@RequestMapping("/admin/report")
@Api(tags = "数据统计相关接口")
@Slf4j
public class ReportController {

    @Autowired
    private ReportService reportService;

    /**
     * 营业额统计
     * @param begin 统计开始日期
     * @param end 统计结束日期
     * @return 营业额数据
     */
    @GetMapping("/turnoverStatistics")
    @ApiOperation("营业额统计")
    public Result<TurnoverReportVO> turnoverStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end
    ){
        log.info("[Controller] 营业额统计，开始日期：{}，结束日期：{}", begin, end);
        TurnoverReportVO turnoverReportVO = reportService.getTurnoverStatistics(begin, end);

        return Result.success(turnoverReportVO);
    }

    /**
     * 用户统计
     * @param begin 统计开始日期
     * @param end 统计结束日期
     * @return 用户统计结果
     */
    @GetMapping("/userStatistics")
    @ApiOperation("用户统计")
    public Result<UserReportVO> userStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end
    ){
        log.info("[Controller] 用户统计，开始日期：{}，结束日期：{}", begin, end);
        UserReportVO userReportVO = reportService.getUserStatistics(begin, end);

        return Result.success(userReportVO);
    }

    /**
     * 订单统计
     * @param begin 统计开始日期
     * @param end 统计结束日期
     * @return 订单统计结果
     */
    @GetMapping("/ordersStatistics")
    @ApiOperation("订单统计")
    public Result<OrderReportVO> orderStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end
    ){
        log.info("[Controller] 订单统计，开始日期：{}，结束日期：{}", begin, end);
        OrderReportVO orderReportVO = reportService.getOrderStatistics(begin, end);

        return Result.success(orderReportVO);
    }

    /**
     * 销量排名 Top10
     * @param begin 统计开始日期
     * @param end 统计结束日期
     * @return 订单统计结果
     */
    @GetMapping("/top10")
    @ApiOperation("销量排名 Top10")
    public Result<SalesTop10ReportVO> salesTop10Statistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end
    ){
        log.info("[Controller] 销量排名 Top10，开始日期：{}，结束日期：{}", begin, end);
        SalesTop10ReportVO salesTop10ReportVO = reportService.getsalesTop10Statistics(begin, end);

        return Result.success(salesTop10ReportVO);
    }


    /**
     * 导出营业数据 (Excel 报表)
     * @param response 响应对象
     */
    @GetMapping("/export")
    @ApiOperation("导出营业数据 (Excel 报表)")
    public void export(HttpServletResponse response){

        reportService.exportBusinessData(response);

    }
}
