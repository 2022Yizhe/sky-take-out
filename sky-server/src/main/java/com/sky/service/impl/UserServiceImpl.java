package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@Service
@Slf4j
public class UserServiceImpl implements UserService {

    // 微信登录服务的接口地址
    private static final String WX_LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session";

    // 注入微信登录属性类
    @Autowired
    private WeChatProperties weChatProperties;

    @Autowired
    private UserMapper userMapper;

    /**
     * 微信登录
     */
    @Override
    public User wxlogin(UserLoginDTO userLoginDTO) {

        // 1. 调用微信登录 API，获取微信用户唯一标识 openid
        String openid = getOpenId(userLoginDTO.getCode());

        // 2. 若 openid 为空，抛出业务异常
        if (openid == null) {
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }

        // 3. 判断当前用户是否为新用户
        User user = userMapper.getByOpenid(openid);

        // 4. 若是新用户，完成插入操作
        if (user == null) {
            user = User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();
            userMapper.insert(user);    // 插入新用户数据
        }

        // 5. 返回用户信息
        return user;
    }

    /**
     * 微信登录，获取 openid
     * @param js_code 微信登录成功后，微信会返回一个临时登录凭证 js_code
     * @return 微信登录成功后返回的 openid
     */
    private String getOpenId(String js_code){
        Map<String, String> param = new HashMap<>();    // 手动构造请求参数
        param.put("appid", weChatProperties.getAppid());
        param.put("secret", weChatProperties.getSecret());
        param.put("js_code", js_code);
        param.put("grant_type", "authorization_code");

        String json = HttpClientUtil.doGet(WX_LOGIN_URL, param);
        JSONObject jsonObject = JSON.parseObject(json); // 解析结果，获取 openid
        return jsonObject.getString("openid");
    }
}
