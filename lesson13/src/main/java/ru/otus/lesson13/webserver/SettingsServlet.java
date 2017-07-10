package ru.otus.lesson13.webserver;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import ru.otus.lesson13.base.DBService;
import ru.otus.lesson13.base.datasets.UserDataSet;
import ru.otus.lesson13.cache.CacheEngine;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by piphonom
 */
public class SettingsServlet extends HttpServlet {

    private String username = "admin";
    private String password = "nimda";

    private Object handledObject;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(this.getServletContext());
        @SuppressWarnings("unchecked")
        CacheEngine<String, UserDataSet> namesCache = (CacheEngine<String, UserDataSet>)ctx.getBean("namesCache");
        this.handledObject = namesCache;
        DBService dbService = (DBService)ctx.getBean("dbService");

        /* It's no good idea to run thread here. But it's done for demo visibility */
        new Thread(new DbServiceRunner(dbService)).start();
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

    private class DbServiceRunner implements Runnable {
        DBService dbService;

        public DbServiceRunner(DBService dbService) {
            this.dbService = dbService;
        }

        @Override
        public void run() {
            UserDataSet firstUser = new UserDataSet("Michael Jackson", 25);
            dbService.save(firstUser);
            while (true) {
                UserDataSet dbUser = dbService.readByName("Michael Jackson");
                try {
                    Thread.sleep(3_000);
                } catch (InterruptedException e) {
                }
            }
        }
    }
}
