package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


@Mapper
public interface SetmealMapper {

    /**
     * 根据分类 id 查询套餐的数量
     * @param id 分类 id
     * @return 套餐数量
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);

    /**
     * & 动态 SQL
     * 条件查询 套餐列表
     * @param setmeal 套餐对象
     * @return 套餐列表
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * & 动态 SQL
     * 分页查询
     * @param setmealPageQueryDTO 分页参数 DTO 对象
     * @return 套餐分页数据
     */
    Page<SetmealVO> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 根据 id 查询套餐
     * @param id 套餐 id
     * @return 套餐
     */
    @Select("select * from setmeal where id = #{id}")
    Setmeal getById(Long id);

    /**
     * 根据 id 删除套餐
     * @param setmealId 套餐 id
     */
    @Delete("delete from setmeal where id = #{id}")
    void deleteById(Long setmealId);

    /**
     * & 动态 SQL
     * 修改套餐
     * @param setmeal 套餐对象
     */
    @AutoFill(OperationType.UPDATE)
    void update(Setmeal setmeal);

    /**
     * & 动态 SQL
     * 新增套餐
     * @param setmeal 套餐对象
     */
    @AutoFill(OperationType.INSERT)
    void insert(Setmeal setmeal);
}
