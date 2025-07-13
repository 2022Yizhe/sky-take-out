package com.sky.controller.admin;

import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/admin/setmeal")
@Api(tags = "S端-套餐管理接口")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    /**
     * 分页查询 套餐列表
     * @param setmealPageQueryDTO 分页参数 DTO 对象
     * @return REST 响应结果
     */
    @GetMapping("/page")
    @ApiOperation("分页查询 套餐列表")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageResult pageResult = setmealService.pageQuery(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 根据套餐 id 查询指定套餐，用于修改页面回显数据
     * @param id 套餐 id
     * @return REST 响应结果
     */
    @GetMapping("/{id}")
    @ApiOperation("根据套餐 id 查询指定套餐")
    public Result<SetmealVO> getById(@PathVariable Long id) {
        SetmealVO setmealVO = setmealService.getByIdWithDish(id);
        return Result.success(setmealVO);
    }

    /**
     * 新增套餐
     * @param setmealDTO 套餐 DTO 对象
     * @return REST 响应结果
     */
    @PostMapping
    @ApiOperation("新增套餐")
    public Result<String> save(@RequestBody SetmealDTO setmealDTO) {
        setmealService.saveWithDish(setmealDTO);
        return Result.success();
    }

    /**
     * 修改套餐
     * @param setmealDTO 套餐 DTO 对象
     * @return REST 响应结果
     */
    @PutMapping
    @ApiOperation("修改套餐")
    public Result<String> update(@RequestBody SetmealDTO setmealDTO) {
        setmealService.update(setmealDTO);
        return Result.success();
    }

    /**
     * 批量删除套餐
     * @param ids 套餐 id 列表
     * @return REST 响应结果
     */
    @DeleteMapping
    @ApiOperation("批量删除套餐")
    public Result<String> delete(@RequestParam List<Long> ids){
        setmealService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 套餐起售停售
     * @param status 状态，1-起售 0-停售
     * @param id 套餐 id
     * @return REST 响应结果
     */
    @PostMapping("/status/{status}")
    @ApiOperation("套餐起售停售")
    public Result<String> startOrStop(@PathVariable Integer status, Long id) {
        setmealService.startOrStop(status, id);
        return Result.success();
    }




    /**
     * 条件查询 套餐列表
     * @param categoryId 分类 id
     * @return REST 响应结果
     */
    @GetMapping("/list")
    @ApiOperation("根据分类 id 查询套餐")
    public Result<List<Setmeal>> list(Long categoryId) {
        Setmeal setmeal = new Setmeal();
        setmeal.setCategoryId(categoryId);
        setmeal.setStatus(StatusConstant.ENABLE);

        List<Setmeal> list = setmealService.list(setmeal);
        return Result.success(list);
    }

    /**
     * 根据套餐 id 查询包含的菜品列表
     * @param id 套餐 id
     * @return REST 响应结果
     */
    @GetMapping("/dish/{id}")
    @ApiOperation("根据套餐 id 查询包含的菜品列表")
    public Result<List<DishItemVO>> dishList(@PathVariable("id") Long id) {
        List<DishItemVO> list = setmealService.getDishItemById(id);
        return Result.success(list);
    }
}

