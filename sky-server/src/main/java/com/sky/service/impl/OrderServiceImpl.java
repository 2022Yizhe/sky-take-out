package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.*;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.OrderBusinessException;
import com.sky.mapper.*;
import com.sky.service.OrderService;
import com.sky.utils.WeChatPayUtil;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.websocket.WebSocketServer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;


@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private AddressBookMapper addressBookMapper;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WeChatPayUtil weChatPayUtil;

    @Autowired
    private WebSocketServer webSocketServer;

    /**
     * 用户提交订单
     */
    @Override
    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {

        // 1. 异常处理（包括地址簿为空、购物车为空，提高代码健壮性）
        AddressBook addressBook = addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
        if (addressBook == null) {  // 地址簿为空
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }

        ShoppingCart shoppingCart = new ShoppingCart(); /// 创建查询条件
        shoppingCart.setUserId(BaseContext.getCurrentId());

        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);
        if (list == null || list.isEmpty()) {   // 购物车为空
            throw new AddressBookBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        // 2. 向订单表插入 1 条数据
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, orders);
        orders.setOrderTime(LocalDateTime.now());   // 设置其他订单信息
        orders.setPayStatus(Orders.UN_PAID);
        orders.setStatus(Orders.PENDING_PAYMENT);
        orders.setUserId(BaseContext.getCurrentId());
        orders.setNumber(String.valueOf(System.currentTimeMillis()));   /// 订单号，直接使用时间戳字符串，不会重复
        orders.setPhone(addressBook.getPhone());
        orders.setConsignee(addressBook.getConsignee());    /// 收货人
        orderMapper.insert(orders);

        // 3. 向订单明细表插入 n 条数据
        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (ShoppingCart cart : list) {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(cart, orderDetail);
            orderDetail.setOrderId(orders.getId()); // 已经主键返回
            orderDetailList.add(orderDetail);
        }
        orderDetailMapper.insertBatch(orderDetailList);

        // 4. 清空用户的购物车
        shoppingCartMapper.deleteAll(BaseContext.getCurrentId());

        // 5. 组装返回结果
        return OrderSubmitVO.builder()
                .id(orders.getId())
                .orderNumber(orders.getNumber())
                .orderAmount(orders.getAmount())
                .orderTime(orders.getOrderTime())
                .build();
    }

    /**
     * 订单支付
     * @param ordersPaymentDTO 订单支付参数 DTO 对象
     * @return 预支付交易单
     */
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        // 当前登录用户id
        Long userId = BaseContext.getCurrentId();
        User user = userMapper.getById(userId);

        /// DEBUG.无法认证商户，此处省略实际的微信支付 (实际生产需删除)
        if (true){
            JSONObject jo = new JSONObject();
            jo.put("timeStamp", "20230521110000000001");
            jo.put("nonceStr", "20230521110000000001");
            jo.put("package", "prepay_id=" + 123456L);
            jo.put("signType", "RSA");
            jo.put("paySign", "20230521110000000001");
            return jo.toJavaObject(OrderPaymentVO.class);
        }

        /// MAIN.调用微信支付接口，生成预支付交易单
        JSONObject jsonObject = weChatPayUtil.pay(
                ordersPaymentDTO.getOrderNumber(), //商户订单号
                new BigDecimal("0.01"), //支付金额，单位 元
                "苍穹外卖订单", //商品描述
                user.getOpenid() //微信用户的openid
        );

        if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
            throw new OrderBusinessException("该订单已支付");
        }

        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        vo.setPackageStr(jsonObject.getString("package"));

        return vo;
    }

    /**
     * 支付成功，修改订单状态
     * @param outTradeNo 微信支付返回的订单号
     */
    public void paySuccess(String outTradeNo) {

        // 根据订单号查询订单
        Orders ordersDB = orderMapper.getByNumber(outTradeNo);

        // 根据订单 id 更新订单的状态、支付方式、支付状态、结账时间
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();

        orderMapper.update(orders);

        // 通过 WebSocket 推送订单状态消息 (Web Server 处理)
        Map<String, Object> message = new HashMap<>();
        message.put("type", 1);     // 1-来单提醒，2-客户催单
        message.put("orderId", ordersDB.getId());
        message.put("content", "订单号：" + outTradeNo);

        String jsonMessage = JSON.toJSONString(message);
        webSocketServer.sendToAllClient(jsonMessage);
    }
}
