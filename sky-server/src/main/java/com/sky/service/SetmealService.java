package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;

import java.util.List;


public interface SetmealService {

    /**
     * 新增套餐，同时需要保存套餐和菜品的关联关系
     * @param setmealDTO 套餐 DTO 对象
     */
    void saveWithDish(SetmealDTO setmealDTO);

    /**
     * 分页查询 套餐列表
     * @param setmealPageQueryDTO 分页参数 DTO 对象
     * @return 分页结果
     */
    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 批量删除套餐
     * @param ids 套餐 id 列表
     */
    void deleteBatch(List<Long> ids);

    /**
     * 根据套餐 id 查询套餐和关联的菜品数据（用于修改时套餐信息回显）
     * @param id 套餐 id
     * @return 套餐 VO 对象
     */
    SetmealVO getByIdWithDish(Long id);

    /**
     * 修改套餐
     * @param setmealDTO 套餐 DTO 对象
     */
    void update(SetmealDTO setmealDTO);

    /**
     * 条件查询 套餐列表
     * @param setmeal 套餐对象
     * @return 套餐列表
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * 根据套餐 id 查询包含的菜品列表
     * @param id 套餐 id
     * @return 菜品 VO 列表
     */
    List<DishItemVO> getDishItemById(Long id);

    /**
     * 套餐起售、停售
     * @param status 状态：1-起售 0-停售
     * @param id 套餐 id
     */
    void startOrStop(Integer status, Long id);
}