package com.sky.controller.admin;


import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController(value = "adminShopController")  // 重名的 Bean 需指定 value
@RequestMapping("/admin/shop")
@Api(tags = "店铺相关接口")
public class ShopController {

    public static final String SHOP_STATUS = "SHOP_STATUS";

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 修改营业状态
     * @param status 营业状态 (0-打烊，1-营业)
     * @return REST 响应结果
     */
    @PutMapping("/{status}")
    @ApiOperation("设置营业状态")
    public Result<String> setStatus(@PathVariable Integer status){

        log.info("[Controller] 店铺营业状态，设置状态为: {} (0-打烊，1-营业)", status);
        redisTemplate.opsForValue().set(SHOP_STATUS, status);

        return Result.success();
    }

    /**
     * 获取营业状态
     * @return REST 响应结果
     */
    @GetMapping("/status")
    @ApiOperation("获取营业状态")
    public Result<Integer> getStatus(){

        Integer status = (Integer)redisTemplate.opsForValue().get(SHOP_STATUS);
        log.info("[Controller] 获取营业状态: {}",  status);

        return Result.success(status);
    }
}
