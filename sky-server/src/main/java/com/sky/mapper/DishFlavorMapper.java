package com.sky.mapper;


import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {

    /**
     * & 动态 SQL
     * 批量插入口味数据
     * @param flavors 菜品口味列表
     */
    void insertBatch(List<DishFlavor> flavors);

    /**
     * 根据菜品 id 删除菜品口味
     * @param dishId 菜品 id
     */
    @Delete("delete from dish_flavor where dish_id = #{dishId}")
    void deleteByDishId(Long dishId);

    /**
     * & 动态 SQL
     * 根据菜品 id(列表) 删除菜品口味
     * @param dishIds 菜品 id 列表
     */
    void deleteByDishIds(List<Long> dishIds);

    /**
     * 根据菜品 id 查询菜品口味
     * @param dishId 菜品 id
     * @return 菜品口味 (列表)
     */
    @Select("select * from dish_flavor where dish_id = #{dishId}")
    List<DishFlavor> getByDishId(Long dishId);
}
