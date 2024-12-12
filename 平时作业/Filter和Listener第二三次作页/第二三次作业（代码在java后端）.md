# 《第二次作业》
学院：省级示范性软件学院

题目：《实验一：第04章 会话技术》

姓名：赵文胤

学号：2200770084

班级：软工2202

日期：2024-10-13

## 作业一：Filter练习
### 题目: 实现一个登录验证过滤器
目标: 创建一个 Servlet的 过滤器,用于验证用户是否已登录。对于未登录的用户,将其重定向到登录页面。
要求:
创建一个名为 LoginFilter 的类, 实现 javax.servlet.Filter 接口。

使用 @WebFilter 注解配置过滤器,使其应用于所有 URL 路径 ("/*")。 在 doFilter 方法中实现以下逻辑:

检查当前请求是否是对登录页面、注册页面或公共资源的请求。如果是,则允许请求通过。

如果不是上述情况,检查用户的 session 中是否存在表示已登录的属性(如 "user" 属性)。

如果用户已登录,允许请求继续。

如果用户未登录,将请求重定向到登录页面。

创建一个排除列表,包含不需要登录就能访问的路径(如 "/login", "/register", "/public")。

实现一个方法来检查当前请求路径是否在排除列表中。

添加适当的注释,解释代码的主要部分。

打开界面
![img_1.png](java后端servlet/img/img_1.png)
没登录
![img.png](java后端servlet/img/img.png)
登陆了
![img_2.png](java后端servlet/img/img_2.png)
```java
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
}
```

## 作业二：Listener练习
题目：完成请求日志记录（ServletRequestListener）功能
要求：

实现一个 ServletRequestListener 来记录每个 HTTP 请求的详细信息。

记录的信息应包括但不限于：

请求时间

客户端 IP 地址

请求方法（GET, POST 等）

请求 URI

查询字符串（如果有）

User-Agent

请求处理时间（从请求开始到结束的时间）

在请求开始时记录开始时间，在请求结束时计算处理时间。

使用适当的日志格式，确保日志易于阅读和分析。

实现一个简单的测试 Servlet，用于验证日志记录功能。

提供简要说明，解释你的实现方式和任何需要注意的事项。


图片
![img_5.png](java后端servlet/img/img_5.png)
```
Request Initialized: Time=1728526167369, IP=127.0.0.1, Method=POST, URI=/webJava_war_exploded/login, QueryString=, User-Agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/129.0.0.0 Safari/537.36

```
```java
package com.example.webjava;

import jakarta.servlet.ServletRequestEvent;
import jakarta.servlet.ServletRequestListener;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpServletRequest;
import java.util.logging.Logger;

@WebListener
public class RequestLoggingListener implements ServletRequestListener {
    private static final Logger logger = Logger.getLogger(RequestLoggingListener.class.getName());

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        HttpServletRequest request = (HttpServletRequest) sre.getServletRequest();
        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);

        String clientIp = request.getRemoteAddr();
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        String userAgent = request.getHeader("User-Agent");
        String aaa = "Request Initialized: " +
                "Time=" + startTime +
                ", IP=" + clientIp +
                ", Method=" + method +
                ", URI=" + uri +
                ", QueryString=" + (queryString != null ? queryString : "") +
                ", User-Agent=" + userAgent;
        System.out.println(aaa);


        logger.info(aaa);
    }

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
    }


}


```
