package com.sky.service;

import com.sky.dto.ShoppingCartDTO;


public interface ShoppingCartService {

    /**
     * 添加商品到购物车（若已存在则修改商品数量 +1）
     * @param shoppingCartDTO 商品数据 DTO 对象
     */
    void add2ShoppingCart(ShoppingCartDTO shoppingCartDTO);
}
