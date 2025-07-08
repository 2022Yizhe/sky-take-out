package com.sky.service;


import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


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

    /**
     * 批量删除菜品
     * @param ids 菜品 id 列表
     */
    @Transactional  // 开启事务控制 （涉及多表操作）
    void deleteBatch(List<Long> ids);

    /**
     * 修改菜品及口味
     * @param dishDTO 修改的菜品数据 DTO 对象
     */
    @Transactional  // 开启事务控制 （涉及多表操作）
    void updateWithFlavor(DishDTO dishDTO);

    /**
     * 根据 id 查询菜品数据
     * @param id 菜品 id
     * @return 菜品数据 VO 对象
     */
    @Transactional
    DishVO getByIdWithFlavor(Long id);
}
