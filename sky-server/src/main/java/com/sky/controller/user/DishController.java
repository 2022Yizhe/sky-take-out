package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;


@RestController("userDishController")
@RequestMapping("/user/dish")
@Slf4j
@Api(tags = "C端-菜品浏览接口")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private RedisTemplate redisTemplate;    // Redis Java 客户端

    /**
     * 根据分类 id 查询菜品
     * & 使用 Redis 缓存
     * @param categoryId 分类 id
     * @return 菜品列表
     */
    @GetMapping("/list")
    @ApiOperation("根据分类 id 查询菜品")
    public Result<List<DishVO>> list(Long categoryId) {

        String key = "dish_" + categoryId;  // 'dish_1'

        // 1. 查询 Redis 中是否存在菜品数据
        List<DishVO> list = (List<DishVO>) redisTemplate.opsForValue().get(key);    // 放进去什么类型，取出来就是什么类型（无需考虑类型转换）
        if (list != null && !list.isEmpty()) {

            return Result.success(list);    // 如果存在，则返回
        }

        // 2. 如果不存在，则查询数据库，并将新页写入 Redis 中
        Dish dish = new Dish();
        dish.setCategoryId(categoryId);
        dish.setStatus(StatusConstant.ENABLE);      // 标记只查询 '起售中' 的菜品

        list = dishService.listWithFlavor(dish);    // 查询数据库
        redisTemplate.opsForValue().set(key, list); // 缓存数据到 Redis 中

        return Result.success(list);
    }
}
