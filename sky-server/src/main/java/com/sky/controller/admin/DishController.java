package com.sky.controller.admin;


import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;


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

    @Autowired
    private RedisTemplate redisTemplate;    // Redis Java 客户端


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

        // 清理缓存 (添加菜品信息，指定分类更新)
        clearCache("DISH_" + dishDTO.getCategoryId());

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

        // 清理缓存 (删除菜品，所有分类菜品都更新)
        clearCache("dish_*");

        return Result.success();
    }

    /**
     * 根据 id 查询菜品
     * @param id 菜品 id
     * @return REST 响应结果
     */
    @GetMapping("/{id}")
    @ApiOperation("根据 id 查询菜品")
    public Result<DishVO> getById(@PathVariable Long id){

        log.info("[Controller] 根据 id 获取菜品信息：{}", id);
        DishVO dishVO = dishService.getByIdWithFlavor(id);

        return Result.success(dishVO);
    }

    /**
     * 根据分类 id 查询菜品列表
     * @param categoryId 分类 id
     * @return REST 响应结果
     */
    @GetMapping("/list")
    @ApiOperation("根据分类 id 查询菜品列表")
    public Result<List<Dish>> list(Long categoryId){
        List<Dish> list = dishService.list(categoryId);
        return Result.success(list);
    }

    /**
     * 修改菜品及口味
     * @param dishDTO 菜品数据 DTO 对象
     * @return REST 响应结果
     */
    @PutMapping
    @ApiOperation("修改菜品")
    public Result<String> update(@RequestBody DishDTO dishDTO){

        log.info("[Controller] 修改菜品，菜品数据: {}", dishDTO);
        dishService.updateWithFlavor(dishDTO);

        // 清理缓存 (修改菜品信息，影响多个分类的缓存记录，不如直接全部删除)
        clearCache("dish_*");

        return Result.success();
    }

    /**
     * 起售、停售菜品
     * @param status 1起售 0停售
     * @param id 菜品 id
     * @return REST 响应结果
     */
    @PostMapping("/status/{status}")
    @ApiOperation("起售、停售菜品")
    public Result<String> startOrStop(@PathVariable Integer status, Long id){

        log.info("[Controller] 修改菜品状态，状态: {}, 菜品id: {}", status, id);
        dishService.startOrStop(status, id);

        // 清理缓存 (修改菜品信息，影响多个分类的缓存记录，不如直接全部删除)
        clearCache("dish_*");

        return Result.success();
    }

    /**
     * 清理 Redis 缓存
     * @param pattern 缓存的 key 的模式（支持通配符）
     */
    private void clearCache(String pattern){
        Set keys = redisTemplate.keys(pattern);
        if (keys != null) redisTemplate.delete(keys);
    }
}
