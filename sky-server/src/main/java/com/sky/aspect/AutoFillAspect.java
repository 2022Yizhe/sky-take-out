package com.sky.aspect;


import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 自定义切面类，用于实现公共字段自动填充
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {

    /**
     * 先提取切入点表达式
     * 'execution': 指定扫描的类
     * '@annotation': 指定扫描的类下具有特点注解的方法
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut(){}

    /**
     * 使用前置通知，用于实现公共字段填充
     * @param joinPoint 连接点
     * @throws Throwable 异常
     */
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) throws Throwable{
        log.info("[Aspect] 开始进行公共字段填充...");

        // 1. 获取被拦截的持久层业务方法的操作类型
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();   // 获取方法签名对象
        AutoFill autoFill = methodSignature.getMethod().getAnnotation(AutoFill.class);  // 获取方法上的注解对象
        OperationType operationType = autoFill.value();     // 获取操作类型 (INSERT、UPDATE、...)

        // 2. 获取当前被拦截方法的参数
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) return;

        Object entity = args[0];

        // 3. 准备好公共字段填充的数据
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();

        // 4. 根据操作类型，填充对应的字段
        if (operationType == OperationType.INSERT){
            // 采用反射，填充 4 个公共字段
            try {
                /// 拿方法
                Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                /// 调用方法
                setCreateTime.invoke(entity, now);
                setUpdateTime.invoke(entity, now);
                setCreateUser.invoke(entity, currentId);
                setUpdateUser.invoke(entity, currentId);
            } catch (Exception e){
                log.error("[Aspect] 自动填充公共字段失败：", e);
            }
        } else if (operationType == OperationType.UPDATE){
            // 填充 2 个公共字段
            try {
                /// 拿方法
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                /// 调用方法
                setUpdateTime.invoke(entity, now);
                setUpdateUser.invoke(entity, currentId);
            } catch (Exception e) {
                log.error("[Aspect] 自动填充公共字段失败：", e);
            }
        }

        log.info("[Aspect] 公共字段填充完毕...");
    }



}