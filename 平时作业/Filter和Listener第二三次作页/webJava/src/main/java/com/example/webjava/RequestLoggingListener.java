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
//        HttpServletRequest request = (HttpServletRequest) sre.getServletRequest();
//        long startTime = (Long) request.getAttribute("startTime");
//        long endTime = System.currentTimeMillis();
//        long duration = endTime - startTime;
//
//        logger.info("Request Destroyed: " +
//                "Duration=" + duration + "ms");
    }


}

