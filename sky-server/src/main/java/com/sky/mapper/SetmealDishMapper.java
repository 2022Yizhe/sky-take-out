package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    /**
     * & 动态 SQL
     * 根据菜品 id(列表) 查询套餐 id(列表)
     * @param dishIds 菜品 ids
     * @return 套餐 ids
     */
    List<Long> getSetmealIdsByDishIds(List<Long> dishIds);
}
