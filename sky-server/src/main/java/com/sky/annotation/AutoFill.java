package com.sky.annotation;


import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 自定义注解，用于某个方法标识需要自动填充的字段
 */
@Target(ElementType.METHOD)         // 作用地点
@Retention(RetentionPolicy.RUNTIME) // 作用时间
public @interface AutoFill {

    /**
     * 填充方式
     * @return 填充方式枚举值
     */
    OperationType value();
}
