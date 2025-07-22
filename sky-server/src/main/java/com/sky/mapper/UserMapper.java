package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;


@Mapper
public interface UserMapper {

    /**
     * 根据 openid 查询用户
     * @param openid 用户唯一标识 openid
     * @return 用户对象
     */
    @Select("select * from user where openid = #{openid}")
    User getByOpenid(String openid);

    /**
     * & 动态 SQL
     * 插入新用户
     * @param user 用户对象
     */
    void insert(User user);

    /**
     * 根据 id 查询用户
     * @param userId 用户id
     * @return 用户对象
     */
    @Select("select * from user where id = #{userId}")
    User getById(Long userId);
}
