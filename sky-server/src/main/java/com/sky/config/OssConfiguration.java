package com.sky.config;


import com.sky.properties.AliOssProperties;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 配置类，用于创建阿里云 OSS Utils 对象
 */
@Configuration
@Slf4j
public class OssConfiguration {

    /**
     * 创建阿里云 OSS Utils 对象（工具类只创建一次）
     * @param aliOssProperties 阿里云 OSS 属性
     * @return 阿里云 OSS Utils 对象
     */
    @Bean
    @ConditionalOnMissingBean   // 如果容器中不存在 AliOssUtil 对象，则创建
    public AliOssUtil aliOssUtil(AliOssProperties aliOssProperties) {
        log.info("[Config] 开始创建阿里云 OSS Utils 对象，创建参数: {}", aliOssProperties);
        return new AliOssUtil(
                aliOssProperties.getEndpoint(),
                aliOssProperties.getAccessKeyId(),
                aliOssProperties.getAccessKeySecret(),
                aliOssProperties.getBucketName());
    }
}
