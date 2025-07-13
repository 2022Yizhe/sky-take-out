package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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

    /**
     * & 动态 SQL
     * 批量保存套餐和菜品的关联关系
     * @param setmealDishes 套餐和菜品关系 列表
     */
    void insertBatch(List<SetmealDish> setmealDishes);

    /**
     * 根据套餐 id 删除套餐和菜品的关联关系
     * @param setmealId 套餐 id
     */
    @Delete("delete from setmeal_dish where setmeal_id = #{setmealId}")
    void deleteBySetmealId(Long setmealId);

    /**
     * 根据套餐 id 查询套餐和菜品的关联关系
     * @param setmealId 套餐 id
     * @return 套餐和菜品的关联关系
     */
    @Select("select * from setmeal_dish where setmeal_id = #{setmealId}")
    List<SetmealDish> getBySetmealId(Long setmealId);
}
