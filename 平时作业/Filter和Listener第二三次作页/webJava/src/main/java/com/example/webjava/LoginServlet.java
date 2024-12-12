package com.example.webjava;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    protected void doGet (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        HttpSession session = request.getSession();
        session.setAttribute("user", "AAA");
        System.out.println(session.getMaxInactiveInterval());
//        response.sendRedirect("hello-servlet");

        System.out.println(session.getAttribute("user"));

        response.sendRedirect("welcome.html");
//         response.forward()

        // 假设用户名和密码验证通过
//        if ("admin".equals(username) && "password".equals(password)) {
//            HttpSession session = request.getSession();
//           HttpSession session = request.getSession();
//            response.sendRedirect("welcome.html");
//        } else {
//            response.sendRedirect("login.jsp?error=true");
//        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        System.out.println(password);

        HttpSession session = request.getSession();
        session.setAttribute("user", username);

        // Hello
        PrintWriter out = response.getWriter();
        out.println("true");

//        response.sendRedirect("welcome.html");
        // 假设用户名和密码验证通过
//        if ("admin".equals(username) && "password".equals(password)) {
//            HttpSession session = request.getSession();
//           HttpSession session = request.getSession();
//            response.sendRedirect("welcome.html");
//        } else {
//            response.sendRedirect("login.jsp?error=true");
//        }


    }
}
