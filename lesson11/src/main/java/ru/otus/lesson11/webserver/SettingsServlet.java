package ru.otus.lesson11.webserver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by piphonom
 */
class SettingsServlet extends HttpServlet {

    private String username;
    private String password;

    private Object handledObject;

    public SettingsServlet(Object handledObject, String username, String password) {
        this.handledObject = handledObject;
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
        if (html == null) {
            html = new ObjectToWebMapper(handledObject).toString();
        }
        response.getWriter().println(html);
    }

    private String handleCurrentSession(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ServletException, IOException {
        String username = (String) session.getAttribute("username");
        if (username == null || !username.equals(this.username)) {
            return "<h1>Incorrect username!</h1>";
        }
        WebToObjectMapper.map(request, handledObject);
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
