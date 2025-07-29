package com.sky.controller.user;

import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSON;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


@RestController(value = "userOrderController")
@RequestMapping("/user/order")
@Slf4j
@Api(tags = "C端-订单接口")
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * 用户提交订单
     * @param ordersSubmitDTO 订单数据 DTO 对象
     * @return 订单提交结果
     */
    @PostMapping("/submit")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO){

        log.info("[Controller] 订单提交，订单数据：{}", ordersSubmitDTO);
        OrderSubmitVO orderSubmitVO = orderService.submitOrder(ordersSubmitDTO);

        return Result.success(orderSubmitVO);
    }


    /**
     * 订单支付
     * @param ordersPaymentDTO 订单支付参数 DTO 对象
     * @return 预支付交易单
     */
    @PutMapping("/payment")
    @ApiOperation("订单支付")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {

        log.info("[Controller] 订单支付：{}", ordersPaymentDTO);
        OrderPaymentVO orderPaymentVO = orderService.payment(ordersPaymentDTO);
        log.info("[Controller] 生成预支付交易单：{}", orderPaymentVO);

        /// DEBUG.模拟微信支付成功 (实际生产需删除)
        {
            // 模拟支付成功的数据
            Map<String, Object> resource = new HashMap<>();
            resource.put("ciphertext", "your_ciphertext_here");  // 替换为实际的密文
            resource.put("nonce", "your_nonce_here");            // 替换为实际的nonce
            resource.put("associated_data", "your_associated_data_here");  // 替换为实际的associated_data

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("resource", resource);
            requestBody.put("out_trade_no", "your_order_number");     // 商户平台订单号
            requestBody.put("transaction_id", "your_transaction_id_here"); // 微信支付交易号

            // 将请求体转换为JSON字符串
            String jsonBody = JSON.toJSONString(requestBody);

            // 发送HTTP POST请求
            URL url = new URL("http://localhost:8080/notify/paySuccess");  // 替换为实际的URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            int responseCode = connection.getResponseCode();
            System.out.println("[Controller] (Test WX-PAY Success) Response Code: " + responseCode);
        }

        return Result.success(orderPaymentVO);
    }
}
