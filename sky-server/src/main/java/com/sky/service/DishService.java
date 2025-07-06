package com.sky.service;


import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import org.springframework.transaction.annotation.Transactional;


public interface DishService {

    /**
     * 新增菜品和对应的口味数据
     * @param dishDTO 菜品信息 DTO 对象
     */
    @Transactional  // 开启事务控制 （涉及多表操作）
    void saveWithFlavor(DishDTO dishDTO);

    /**
     * 分页查询 菜品列表
     * @param dishPageQueryDTO 分页查询参数 DTO 对象
     * @return 菜品分页列表
     */
    PageResult page(DishPageQueryDTO dishPageQueryDTO);
}
