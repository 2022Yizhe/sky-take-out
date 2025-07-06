package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DishMapper {

    /**
     * 根据分类 id 查询菜品数量
     * @param categoryId 分类 id
     * @return 菜品数量
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * & 动态 SQL
     * 插入菜品数据
     * @param dish 菜品信息
     */
    @AutoFill(value = OperationType.INSERT)
    void insert(Dish dish);

    /**
     * & 动态 SQL
     * 分页查询 菜品列表 (基于 PageHelper 依赖补充动态 SQL 的 limit 参数)
     * @param dishPageQueryDTO 分页查询参数 DTO
     * @return 页面结果
     */
    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);
}
