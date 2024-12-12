package com.example.webjava;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

@WebFilter(filterName = "LoginFilter", urlPatterns = "/*")

public class LoginFilter implements Filter {


    private String encoding = "UTF-8"; // 默认编码
    // 排除列表，不需要登录就能访问的路径
    private static final List<String> EXCLUDED_PATHS = Arrays.asList("/login", "/register", "/public");

    private static final List<String> STATIC_EXTENSIONS = Arrays.asList(
            ".css", ".js", ".jpg", ".png", ".gif", ".ico", ".html", ".jsp"
    );
    // 检查当前请求路径是否在排除列表中
    private boolean isExcludedPath(String path) {
        return EXCLUDED_PATHS.stream().anyMatch(path::startsWith);
    }
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("EncodingFilter 初始化");
        String encodingParam = filterConfig.getInitParameter("encoding");
        if (encodingParam != null && !encodingParam.isEmpty()) {
            encoding = encodingParam;
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        System.out.println("MyFilter 执行过滤前1");

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI().toLowerCase();

        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);


        boolean isStaticResource = STATIC_EXTENSIONS.stream()
                .anyMatch(extension -> requestURI.endsWith(extension));

        if (isStaticResource) {
            chain.doFilter(request, response);
            return;
        }


        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());
        System.out.println(path);

        // 检查当前请求路径是否在排除列表中
        if (isExcludedPath(path)) {
            chain.doFilter(request, response);
            return;
        }
        // 检查用户是否已登录
        if (session != null && session.getAttribute("user") != null) {
            System.out.println("EncodingFilter 执行过滤前");
            // 处理 POST 请求
            request.setCharacterEncoding(encoding);

            // 处理 GET 请求
            if ("GET".equalsIgnoreCase(httpRequest.getMethod())) {
                httpRequest = new GetRequestWrapper(httpRequest, encoding);
            }
            // 设置响应编码
            response.setCharacterEncoding(encoding);
            response.setContentType("text/html;charset=" + encoding);

            // 传递给下一个过滤器或者目标资源
            chain.doFilter(httpRequest, response);
            System.out.println("EncodingFilter 执行过滤结束");


        } else {
            // 用户未登录，重定向到登录页面
            System.out.println("检测到未登录用户");
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.html");
        }





    }

    @Override
    public void destroy() {
        // 不需要特别的清理操作
        System.out.println("EncodingFilter 销毁");
    }

    // 内部类，用于包装 GET 请求
    private class GetRequestWrapper extends HttpServletRequestWrapper {

        private String encoding;

        public GetRequestWrapper(HttpServletRequest request, String encoding) {
            super(request);
            this.encoding = encoding;
        }

        @Override
        public String getParameter(String name) {
            String value = super.getParameter(name);
            if (value == null) {
                return null;
            }
            try {
                // 先用 ISO-8859-1 解码，再用指定的编码重新编码
                return new String(value.getBytes("ISO-8859-1"), encoding);
            } catch (UnsupportedEncodingException e) {
                return value;
            }
        }

        @Override
        public String[] getParameterValues(String name) {
            String[] values = super.getParameterValues(name);
            if (values == null) {
                return null;
            }
            for (int i = 0; i < values.length; i++) {
                try {
                    values[i] = new String(values[i].getBytes("ISO-8859-1"), encoding);
                } catch (UnsupportedEncodingException e) {
                    // 如果转换失败，保留原值
                }
            }
            return values;
        }
    }
}
