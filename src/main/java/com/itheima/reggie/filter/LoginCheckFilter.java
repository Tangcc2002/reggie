package com.itheima.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 检查用户是否登录
 */
@Slf4j
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {

    /**
     * 路径匹配器，支持通配符
     */
    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        // 获取本次请求URL
        String url = request.getRequestURI();
        // 定义不需要处理的请求URL
        String[] urls = new String[] {"/employee/login", "/employee/logout", "/backend/**", "/front/**"};
        // 判断本次请求是否需要处理
        if (check(urls, url)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        // 判断用户登录状态，已登录则放行，否则跳转登录
        Long id = (Long) request.getSession().getAttribute("employee");
        if (id != null) {
            BaseContext.set((Long) request.getSession().getAttribute("employee"));
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        // 用户未登录，通过输出流方式向用户响应数据
        servletResponse.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
    }

    /**
     * 路径匹配，检查本次请求是否放行
     * @param urls  不需要处理的URL
     * @param requestUrl    请求URL
     * @return  检查结果
     */
    public boolean check(String[] urls, String requestUrl) {
        for (String s: urls) {
            if (PATH_MATCHER.match(s, requestUrl))
                return true;
        }
        return false;
    }
}
