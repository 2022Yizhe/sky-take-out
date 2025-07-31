package com.sky.service.impl;


import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.service.WorkspaceService;
import com.sky.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WorkspaceService workspaceService;

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

    /**
     * 销量排名 Top 10
     */
    @Override
    public SalesTop10ReportVO getsalesTop10Statistics(LocalDate begin, LocalDate end) {

        // 1.获取指定范围的精确时间
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);

        // 2.查询销量 Top 10 商品 DTO 对象
        List<GoodsSalesDTO> salesTop10List = orderMapper.getSalesTop10(beginTime, endTime);

        // 3.组装响应结果
        List<String> nameList = salesTop10List.stream().map(GoodsSalesDTO::getName).collect(Collectors.toList());
        List<Integer> numberList = salesTop10List.stream().map(GoodsSalesDTO::getNumber).collect(Collectors.toList());

        StringJoiner nameListJoiner = new StringJoiner(",");
        nameList.forEach(name -> nameListJoiner.add(name));   // 等效于 'toString()'
        StringJoiner numberListJoiner = new StringJoiner(",");
        numberList.forEach(number -> numberListJoiner.add(number.toString()));

        return SalesTop10ReportVO.builder()
                .nameList(nameListJoiner.toString())
                .numberList(numberListJoiner.toString())
                .build();
    }

    /**
     * 导出营业数据
     */
    @Override
    public void exportBusinessData(HttpServletResponse response) {

        // 1.查询营业数据
        LocalDateTime begin = LocalDateTime.of(LocalDate.now().minusDays(30), LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.MIN);
        BusinessDataVO businessData = workspaceService.getBusinessData(begin, end);

        // 2.读取 resources 下的模板文件，创建 Excel 对象
        XSSFWorkbook excel = null;
        try {
            InputStream in = this.getClass().getClassLoader().getResourceAsStream("template/运营数据报表模板.xlsx");
            if (in != null) {
                excel = new XSSFWorkbook(in);
            }
        } catch (IOException e){
            log.error("[Server] 获取 Excel 模板文件失败！");
            e.printStackTrace();
        }

        // 3.通过 POI 导出 Excel 文件
        XSSFSheet sheet = excel.getSheet("Sheet1");
        sheet.getRow(1).getCell(1).setCellValue("(时间)" + begin + " 至 " + end);

        sheet.getRow(3).getCell(2).setCellValue("(营业额)" + businessData.getTurnover());
        sheet.getRow(3).getCell(4).setCellValue("(订单完成率)" + businessData.getOrderCompletionRate());
        sheet.getRow(3).getCell(6).setCellValue("(新增用户数)" + businessData.getNewUsers());

        sheet.getRow(4).getCell(2).setCellValue("(有效订单)" + businessData.getValidOrderCount());
        sheet.getRow(4).getCell(4).setCellValue("(平均客单价)" + businessData.getUnitPrice());

        sheet.getRow(7).getCell(1).setCellValue("(日期)" );

        // 填充明细数据时，重新查询每日的营业额数据
        for (int i = 0; i < 30; i++) {
            LocalDateTime dayNEnd = begin.plusDays(i);
            LocalDateTime dayNBegin = dayNEnd.minusDays(1);
            BusinessDataVO dayData = workspaceService.getBusinessData(dayNBegin, dayNEnd);

            XSSFRow row = sheet.getRow(7 + i);
            row.getCell(1).setCellValue(dayNBegin.toString());
            row.getCell(2).setCellValue(dayData.getTurnover());
            row.getCell(3).setCellValue(dayData.getValidOrderCount());
            row.getCell(4).setCellValue(dayData.getOrderCompletionRate());
            row.getCell(5).setCellValue(dayData.getUnitPrice());
            row.getCell(6).setCellValue(dayData.getNewUsers());
        }

        // 4.通过 Servlet 输出流传输 Excel 文件
        try {
            ServletOutputStream out = response.getOutputStream();
            excel.write(out);

            // 关闭流
            out.close();
            excel.close();
        } catch (IOException e){

            log.error("导出 Excel 到客户端浏览器失败！");
            e.printStackTrace();
        }
    }

}
