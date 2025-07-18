package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;


public interface ShoppingCartService {

    /**
     * 添加商品到购物车（若已存在则修改商品数量 +1）
     * @param shoppingCartDTO 商品数据 DTO 对象
     */
    void add2ShoppingCart(ShoppingCartDTO shoppingCartDTO);

    /**
     * 获取当前用户的购物车数据（列表）
     * @return 购物车列表
     */
    List<ShoppingCart> showShoppingCart();
}
