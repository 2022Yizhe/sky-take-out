package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;


public interface UserService {

    /**
     * 微信登录
     * @param userLoginDTO 微信登录信息 DTO 对象
     * @return 登录成功后用户信息 VO 对象
     */
    User wxlogin(UserLoginDTO userLoginDTO);
}
