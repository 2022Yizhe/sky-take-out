package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;


@Mapper
public interface DishMapper {

    /**
     * & 动态 SQL
     * 条件查询菜品
     * @param dish 菜品查询条件
     * @return 菜品列表
     */
    List<Dish> list(Dish dish);

    /**
     * 根据分类 id 查询菜品数量
     * @param categoryId 分类 id
     * @return 菜品数量
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    /**
     * & 动态 SQL
     * 插入菜品
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

    /**
     * 根据 id 查询菜品
     * @param id 菜品 id
     * @return 菜品数据
     */
    @Select("select * from dish where id = #{id}")
    Dish getById(Long id);

    /**
     * 根据 id 删除菜品
     * @param id 菜品 id
     */
    @Delete("delete from dish where id = #{id}")
    void deleteById(Long id);

    /**
     * & 动态 SQL
     * 根据 id(列表) 删除菜品
     * @param ids 菜品 id 列表
     */
    void deleteByIds(List<Long> ids);

    /**
     * & 动态 SQL
     * 修改菜品
     * @param dish 菜品数据
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(Dish dish);

    /**
     * 根据套餐 id 查询菜品
     * @param setmealId 套餐 id
     * @return 菜品列表
     */
    @Select("select a.* from dish a left join setmeal_dish b on a.id = b.dish_id where b.setmeal_id = #{setmealId}")
    List<Dish> getBySetmealId(Long setmealId);

    /**
     * 修改菜品起售、停售状态
     * @param status 状态：1 起售 0 停售
     * @param id 菜品 id
     */
    @Update("update dish set status = #{status} where id = #{id}")
    void startOrStop(Integer status, Long id);
}
