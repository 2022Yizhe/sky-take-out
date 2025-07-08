package com.sky.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;


@Configuration
@Slf4j
public class RedisConfiguration {

    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
        log.info("[Config] 开始创建 RedisTemplate 对象...");

        // 1. 创建 RedisTemplate 对象
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();

        // 2. 将 RedisTemplate 对象关联到 RedisConnectionFactory
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // 3. 设置 RedisTemplate 的序列化器 (此步骤是方便 redis-desktop 查看 key 名称)
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }
}
