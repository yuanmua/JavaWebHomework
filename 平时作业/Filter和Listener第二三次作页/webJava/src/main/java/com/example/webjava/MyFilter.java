/*
package com.example.webjava;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;

import java.io.IOException;


@WebFilter(filterName = "MyFilter", urlPatterns = "/*")
public class MyFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("MyFilter 执行过滤前");
        filterChain.doFilter(servletRequest, servletResponse);
        System.out.println("MyFilter 执行过滤结束");
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("MyFilter创建中.........");
        Filter.super.init(filterConfig);
    }

    @Override
    public void destroy() {
        System.out.println("MyFilter销毁.........");

        Filter.super.destroy();
    }
}
*/
