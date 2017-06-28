package ru.otus.lesson11.webserver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by piphonom
 */
public class RootServlet extends HttpServlet{

    private static String LOGIN_PAGE_TEMPLATE = "<!DOCTYPE html>" +
            "<html>" +
            "<body>" +
            "<h2>Login</h2>" +
            "<form action=\"/settings\" method=\"post\">" +
            "    <div>" +
            "    <label><b>Username</b></label>" +
            "    <input type=\"text\" placeholder=\"Enter Username\" name=\"username\" required>" +
            "    </div>" +
            "    <div>" +
            "    <label><b>Password</b></label>" +
            "    <input type=\"password\" placeholder=\"Enter Password\" name=\"password\" required>" +
            "    </div>" +
            "    <div>" +
            "    <button type=\"submit\">Login</button>" +
            "    </div>" +
            "</form>" +
            "</body>" +
            "</html>";

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(LOGIN_PAGE_TEMPLATE);
    }
}
