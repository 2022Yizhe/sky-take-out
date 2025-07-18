package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.ShoppingCart;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface ShoppingCartMapper {

    /**
     * 根据 '用户id/菜品id/套餐id/菜品口味'(直接封装为 ShoppingCart) 来查询购物车
     * & 动态 SQL
     * @param shoppingCart 购物车对象 (作为查询条件)
     * @return 符合查询条件的购物车列表
     */
    List<ShoppingCart> list(ShoppingCart shoppingCart);

    /**
     * 根据 id 修改购物车对象
     * & 动态 SQL
     * @param cart 新的购物车对象
     */
    void updateById(ShoppingCart cart);

    /**
     * 插入购物车对象
     * & 动态 SQL
     * @param cart 新的购物车对象
     */
//    @AutoFill(value = OperationType.INSERT)   // 用不了，只有 Create_Time 字段
    void save(ShoppingCart cart);
}
