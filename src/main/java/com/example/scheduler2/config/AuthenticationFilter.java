package com.example.scheduler2.config;
// 로그인 상태 인증 필터


// jakarta.servlet.*; 하면 servlet 에 있는 모든(*) 항목을 임포트 함

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthenticationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpReq = (HttpServletRequest) servletRequest;
        HttpServletResponse httpRes = (HttpServletResponse) servletResponse;
        String path = httpReq.getRequestURI();

        // 회원가입, 로그인 요청은 인증 체크에서 제외
        if (path.startsWith("/users/signup") || path.startsWith("/users/login")) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        // 세션에 userId가 없다면 Unauthorized(401) 반환
        if (httpReq.getSession().getAttribute("userId") == null) {
            httpRes.sendError(HttpServletResponse.SC_UNAUTHORIZED, "로그인 해주세요.");
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);


    }
}
