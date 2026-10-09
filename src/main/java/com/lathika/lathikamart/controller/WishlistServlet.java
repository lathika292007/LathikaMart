package com.lathika.lathikamart.controller;

import com.lathika.lathikamart.dao.ProductDAOImpl;
import com.lathika.lathikamart.dao.WishlistDAOImpl;
import com.lathika.lathikamart.dto.UserResponseDTO;
import com.lathika.lathikamart.exception.AppException;
import com.lathika.lathikamart.model.Wishlist;
import com.lathika.lathikamart.service.WishlistService;
import com.lathika.lathikamart.service.WishlistServiceImpl;
import com.lathika.lathikamart.util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "WishlistServlet", urlPatterns = {"/api/v1/wishlist", "/api/v1/wishlist/*"})
public class WishlistServlet extends HttpServlet {

    private WishlistService wishlistService;

    @Override
    public void init() throws ServletException {
        this.wishlistService = new WishlistServiceImpl(new WishlistDAOImpl(), new ProductDAOImpl());
    }

    public void setWishlistService(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserResponseDTO user = getAuthenticatedUser(req);
        if (user == null) {
            JsonUtil.sendError(resp, HttpServletResponse.SC_UNAUTHORIZED, "UNAUTHORIZED", "Please log in to view your wishlist.");
            return;
        }

        try {
            List<Wishlist> items = wishlistService.getUserWishlist(user.getId());
            JsonUtil.sendSuccess(resp, HttpServletResponse.SC_OK, items);
        } catch (Exception e) {
            JsonUtil.sendError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SERVER_ERROR", e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserResponseDTO user = getAuthenticatedUser(req);
        if (user == null) {
            JsonUtil.sendError(resp, HttpServletResponse.SC_UNAUTHORIZED, "UNAUTHORIZED", "Please log in to add items to your wishlist.");
            return;
        }

        try {
            Map<?, ?> body = parseRequestBody(req, Map.class);
            Long productId = ((Number) body.get("productId")).longValue();
            Wishlist item = wishlistService.addToWishlist(user.getId(), productId);
            JsonUtil.sendSuccess(resp, HttpServletResponse.SC_CREATED, item);
        } catch (AppException e) {
            JsonUtil.sendError(resp, e.getStatusCode(), "WISHLIST_ERROR", e.getMessage());
        } catch (Exception e) {
            JsonUtil.sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "BAD_REQUEST", e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserResponseDTO user = getAuthenticatedUser(req);
        if (user == null) {
            JsonUtil.sendError(resp, HttpServletResponse.SC_UNAUTHORIZED, "UNAUTHORIZED", "Please log in.");
            return;
        }

        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            JsonUtil.sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "BAD_REQUEST", "Product ID required.");
            return;
        }

        try {
            Long productId = Long.parseLong(pathInfo.substring(1));
            boolean removed = wishlistService.removeFromWishlist(user.getId(), productId);
            JsonUtil.sendSuccess(resp, HttpServletResponse.SC_OK, Map.of("removed", removed));
        } catch (Exception e) {
            JsonUtil.sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "BAD_REQUEST", e.getMessage());
        }
    }

    private UserResponseDTO getAuthenticatedUser(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return (session != null) ? (UserResponseDTO) session.getAttribute("currentUser") : null;
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
