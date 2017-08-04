package ru.otus.lesson15.webserver.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by piphonom
 */
public class LoginServlet extends HttpServlet{

    private static String MAIN_PAGE_TEMPLATE = "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <title>Index</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "<div class=\"top\">\n" +
            "    <br/>\n" +
            "    <a href=\"/settings\">Cache settings</a> <a href=\"dbservice.html\">DBMessageService</a>\n" +
            "</div>\n" +
            "</body>";

    private String username;
    private String password;

    public LoginServlet(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        HttpSession session = request.getSession(false);
        String html;
        if (session != null) {
            html = handleCurrentSession(request, response, session);
        } else {
            html = handleNewSession(request, response);
        }

        if (html == null)
            html = MAIN_PAGE_TEMPLATE;

        response.getWriter().println(html);
    }

    private String handleCurrentSession(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ServletException, IOException {
        String username = (String) session.getAttribute("username");
        if (username == null || !username.equals(this.username)) {
            return "<h1>Incorrect username!</h1>";
        }
        return null;
    }

    private String handleNewSession(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if (username == null || password == null ||
                !username.equals(this.username) || !password.equals(this.password)) {
            return "<h1>Incorrect username or password!</h1>";
        }
        HttpSession session = request.getSession(true);
        synchronized (session) {
            session.setAttribute("username", username);
        }
        return null;
    }
}
