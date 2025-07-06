package com.sky.service;


import com.sky.dto.DishDTO;
import org.springframework.transaction.annotation.Transactional;

public interface DishService {

    /**
     * 新增菜品和对应的口味数据
     * @param dishDTO 菜品信息 DTO 对象
     */
    @Transactional  // 开启事务控制 （涉及多表操作）
    void saveWithFlavor(DishDTO dishDTO);
}
