package com.lathika.lathikamart.controller;

import com.lathika.lathikamart.dao.UserDAOImpl;
import com.lathika.lathikamart.dto.UserResponseDTO;
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
import java.util.Map;

/**
 * ProfileServlet handles buyer/seller user profile settings & password changes.
 */
@WebServlet(name = "ProfileServlet", urlPatterns = {"/api/v1/profile"})
public class ProfileServlet extends HttpServlet {

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
        HttpSession session = req.getSession(false);
        UserResponseDTO currentUser = (session != null) ? (UserResponseDTO) session.getAttribute("currentUser") : null;

        if (currentUser == null) {
            JsonUtil.sendError(resp, HttpServletResponse.SC_UNAUTHORIZED, "UNAUTHORIZED", "Please log in.");
            return;
        }

        try {
            UserResponseDTO user = userService.getUserById(currentUser.getId());
            JsonUtil.sendSuccess(resp, HttpServletResponse.SC_OK, user);
        } catch (Exception e) {
            JsonUtil.sendError(resp, HttpServletResponse.SC_NOT_FOUND, "NOT_FOUND", e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        UserResponseDTO currentUser = (session != null) ? (UserResponseDTO) session.getAttribute("currentUser") : null;

        if (currentUser == null) {
            JsonUtil.sendError(resp, HttpServletResponse.SC_UNAUTHORIZED, "UNAUTHORIZED", "Please log in.");
            return;
        }

        try {
            Map<String, String> body = parseRequestBody(req);
            String name = body.get("name");
            String currentPassword = body.get("currentPassword");
            String newPassword = body.get("newPassword");

            UserResponseDTO updated = userService.updateProfile(currentUser.getId(), name, currentPassword, newPassword);
            session.setAttribute("currentUser", updated);
            JsonUtil.sendSuccess(resp, HttpServletResponse.SC_OK, updated);
        } catch (Exception e) {
            JsonUtil.sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "BAD_REQUEST", e.getMessage());
        }
    }

    private Map<String, String> parseRequestBody(HttpServletRequest req) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        return JsonUtil.fromJson(sb.toString(), Map.class);
    }
}
