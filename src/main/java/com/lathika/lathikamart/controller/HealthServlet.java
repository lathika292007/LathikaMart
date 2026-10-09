package com.lathika.lathikamart.controller;

import com.lathika.lathikamart.listener.DbContextListener;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

/**
 * HealthServlet endpoint checking database and application health status.
 * URL: /api/v1/health
 */
@WebServlet(name = "HealthServlet", urlPatterns = {"/api/v1/health"})
public class HealthServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String dbStatus = "DOWN";
        try {
            DataSource ds = DbContextListener.getDataSource();
            if (ds != null) {
                try (Connection conn = ds.getConnection()) {
                    if (conn.isValid(2)) {
                        dbStatus = "UP";
                    }
                }
            }
        } catch (Exception e) {
            dbStatus = "DOWN";
        }

        if ("UP".equals(dbStatus)) {
            resp.setStatus(HttpServletResponse.SC_OK);
            PrintWriter writer = resp.getWriter();
            writer.print("{\"status\":\"UP\",\"db\":\"UP\"}");
            writer.flush();
        } else {
            resp.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            PrintWriter writer = resp.getWriter();
            writer.print("{\"status\":\"DOWN\",\"db\":\"DOWN\"}");
            writer.flush();
        }
    }
}
