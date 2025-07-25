package com.sky.controller.user;


import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/user/shoppingCart")
@Slf4j
@Api(tags = "C端-购物车接口")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 添加商品到购物车
     * @return REST 响应结果
     */
    @PostMapping("/add")
    @ApiOperation("添加商品到购物车")
    public Result<String> add(@RequestBody ShoppingCartDTO shoppingCartDTO) {

        log.info("[Controller] 添加商品到购物车，商品为: {}", shoppingCartDTO);
        shoppingCartService.add2ShoppingCart(shoppingCartDTO);

        return Result.success();
    }

    /**
     * 查看购物车 (列表)
     * @return REST 响应结果
     */
    @GetMapping("/list")
    @ApiOperation("查看购物车")
    public Result<List<ShoppingCart>> list() {

        log.info("[Controller] 用户查看了购物车");
        List<ShoppingCart> list = shoppingCartService.showShoppingCart();

        return Result.success(list);
    }

    /**
     * 清空购物车所有 item
     * @return REST 响应结果
     */
    @DeleteMapping("/clean")
    @ApiOperation("清空购物车")
    public Result<String> clean() {

        log.info("[Controller] 用户清空了购物车");
        shoppingCartService.cleanShoppingCart();

        return Result.success();
    }
}
