package com.lathika.lathikamart.controller;

import com.lathika.lathikamart.dao.UserDAOImpl;
import com.lathika.lathikamart.dto.LoginRequestDTO;
import com.lathika.lathikamart.dto.RegisterRequestDTO;
import com.lathika.lathikamart.dto.UserResponseDTO;
import com.lathika.lathikamart.exception.AppException;
import com.lathika.lathikamart.service.UserService;
import com.lathika.lathikamart.service.UserServiceImpl;
import com.lathika.lathikamart.util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * AuthServlet handles user registration, login, session inspection, and logout.
 */
@WebServlet(name = "AuthServlet", urlPatterns = {"/api/v1/auth/register", "/api/v1/auth/login", "/api/v1/auth/logout", "/api/v1/auth/me"})
public class AuthServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init() throws ServletException {
        this.userService = new UserServiceImpl(new UserDAOImpl());
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        if ("/api/v1/auth/me".equals(path)) {
            HttpSession session = req.getSession(false);
            if (session != null && session.getAttribute("currentUser") != null) {
                UserResponseDTO user = (UserResponseDTO) session.getAttribute("currentUser");
                JsonUtil.sendSuccess(resp, HttpServletResponse.SC_OK, user);
            } else {
                JsonUtil.sendError(resp, HttpServletResponse.SC_UNAUTHORIZED, "UNAUTHORIZED", "Not logged in");
            }
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getServletPath();
        try {
            if ("/api/v1/auth/register".equals(path)) {
                RegisterRequestDTO registerDTO = parseRequestBody(req, RegisterRequestDTO.class);
                UserResponseDTO registered = userService.register(registerDTO);

                // Initialize session
                HttpSession session = req.getSession(true);
                session.setMaxInactiveInterval(1800); // 30 mins
                session.setAttribute("currentUser", registered);

                JsonUtil.sendSuccess(resp, HttpServletResponse.SC_CREATED, registered);

            } else if ("/api/v1/auth/login".equals(path)) {
                LoginRequestDTO loginDTO = parseRequestBody(req, LoginRequestDTO.class);
                UserResponseDTO loggedIn = userService.login(loginDTO);

                // Session Management Rule: Regenerate session ID on login
                HttpSession oldSession = req.getSession(false);
                if (oldSession != null) {
                    oldSession.invalidate();
                }
                HttpSession newSession = req.getSession(true);
                newSession.setMaxInactiveInterval(1800); // 30 mins explicit timeout
                newSession.setAttribute("currentUser", loggedIn);

                JsonUtil.sendSuccess(resp, HttpServletResponse.SC_OK, loggedIn);

            } else if ("/api/v1/auth/logout".equals(path)) {
                HttpSession session = req.getSession(false);
                if (session != null) {
                    session.invalidate();
                }
                JsonUtil.sendSuccess(resp, HttpServletResponse.SC_OK, "Logged out successfully");
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (AppException e) {
            JsonUtil.sendError(resp, e.getStatusCode(), "AUTH_ERROR", e.getMessage());
        } catch (Exception e) {
            JsonUtil.sendError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SERVER_ERROR", e.getMessage());
        }
    }

    private <T> T parseRequestBody(HttpServletRequest req, Class<T> clazz) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        return JsonUtil.fromJson(sb.toString(), clazz);
    }
}
