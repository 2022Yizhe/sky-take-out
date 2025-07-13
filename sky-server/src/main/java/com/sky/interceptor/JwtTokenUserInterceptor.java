package com.sky.interceptor;

import com.sky.constant.JwtClaimsConstant;
import com.sky.context.BaseContext;
import com.sky.properties.JwtProperties;
import com.sky.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * jwt 令牌校验的拦截器
 */
@Component
@Slf4j
public class JwtTokenUserInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 校验 jwt
     * 除了登录接口外，其他所有业务请求，都需要进行 jwt 令牌校验（可以在此处存储当前用户信息到 ThreadLocal）
     * @param request 客户端请求
     * @param response 响应
     * @param handler 处理器
     * @return 拦截器放行结果（true - 放行, false - 不放行）
     * @throws Exception 拦截器异常
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 判断当前拦截到的是 Controller 的方法还是其他资源
        if (!(handler instanceof HandlerMethod)) {
            // 当前拦截到的不是动态方法，直接放行
            return true;
        }

        //1、从请求头中获取令牌
        String token = request.getHeader(jwtProperties.getUserTokenName());

        //2、校验令牌
        try {
            log.info("[Interceptor] jwt 校验: {}", token);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getUserSecretKey(), token);
            Long userId = Long.valueOf(claims.get(JwtClaimsConstant.USER_ID).toString());

            log.info("[Interceptor] 当前用户 id: {}", userId);
            BaseContext.setCurrentId(userId);    // 存储当前用户 id 到 ThreadLocal 中

            return true;    // 放行
        } catch (Exception ex) {
            response.setStatus(401);
            return false;   // 不放行，响应 401 状态码
        }
    }
}
