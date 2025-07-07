package com.sky.controller.admin;


import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 菜品管理
 */
@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品相关接口")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    /**
     * 新增菜品
     * @param dishDTO 菜品信息 DTO 对象
     * @return REST 响应结果
     */
    @PostMapping
    @ApiOperation("新增菜品")
    public Result<String> save(@RequestBody DishDTO dishDTO) {

        log.info("新增菜品：{}", dishDTO);
        dishService.saveWithFlavor(dishDTO);

        return Result.success();
    }


    /**
     * 分页查询 菜品列表
     * @param dishPageQueryDTO 分页查询参数 DTO 对象
     * @return REST 响应结果
     */
    @GetMapping("/page")
    @ApiOperation("分页查询 菜品列表")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO) {

        log.info("[Controller] 分页查询菜品列表，查询信息: {}", dishPageQueryDTO);
        PageResult pageResult = dishService.page(dishPageQueryDTO);

        return Result.success(pageResult);
    }

    /**
     * 批量删除菜品
     * @param ids 菜品 id 数组 (解析路径参数为数组)
     * @return REST 响应结果
     */
    @DeleteMapping
    @ApiOperation("批量删除菜品")
    public Result<String> delete(@RequestParam List<Long> ids){

        log.info("[Controller] 批量删除菜品：{}", ids);
        dishService.deleteBatch(ids);

        return Result.success();
    }
}
