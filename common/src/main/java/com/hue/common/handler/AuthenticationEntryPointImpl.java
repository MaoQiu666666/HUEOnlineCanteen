package com.hue.common.handler;

import com.hue.common.result.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用户认证处理器，一是捕获登录时的异常，比如BadCredentialsException 专门用来标记登录时凭证错误的子类，
 * 此外UsernameNotFoundException，DisabledException需要手动抛出。
 * 二是带 token 访问接口，但 token 无效、过期、被篡改的情况，框架会抛 AuthenticationException 被它捕获；
 * 三是登录后访问需要权限的接口，但当前用户没对应角色的话，会触发 AccessDeniedException，这时候也会轮到 AccessDeniedHandler处理
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        Result<String> result = Result.error(401,"未登录或Token无效");

        if (authException instanceof BadCredentialsException) {
            result = Result.error(401, "用户不存在或密码错误");
        }
        //抛出的异常可能会被包装，getCause()拿到具体异常
        if (authException.getCause() instanceof DisabledException) {
            result = Result.error(401, "账号禁用");
        }

        response.getWriter().write(new ObjectMapper().writeValueAsString(result));
    }
}
