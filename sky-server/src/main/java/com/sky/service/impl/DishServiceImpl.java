package com.sky.service.impl;

import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    /**
     * 新增菜品及口味
     */
    @Override
    public void saveWithFlavor(DishDTO dishDTO) {

        // 创建一个菜品数据对象
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        // 向菜品表中插入 1 条数据
        dishMapper.insert(dish);
        Long dishId = dish.getId(); // 获取新插入的菜品的 id (需提前主键返回)

        // 向口味表中插入 n 条数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && flavors.size() > 0) {
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishId);   // 将返回的主键赋值给每一个 DishFlavor 对象
            });

            // 批量插入口味数据
            dishFlavorMapper.insertBatch(flavors);
        }
    }
}
