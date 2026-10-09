package com.lathika.lathikamart.controller;

import com.lathika.lathikamart.dao.CartDAOImpl;
import com.lathika.lathikamart.dao.ProductDAOImpl;
import com.lathika.lathikamart.dto.UserResponseDTO;
import com.lathika.lathikamart.exception.AppException;
import com.lathika.lathikamart.model.CartItem;
import com.lathika.lathikamart.service.CartService;
import com.lathika.lathikamart.service.CartServiceImpl;
import com.lathika.lathikamart.util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CartServlet handles shopping cart operations for buyers.
 */
@WebServlet(name = "CartServlet", urlPatterns = {"/api/v1/cart", "/api/v1/cart/*"})
public class CartServlet extends HttpServlet {

    private CartService cartService;

    @Override
    public void init() throws ServletException {
        this.cartService = new CartServiceImpl(new CartDAOImpl(), new ProductDAOImpl());
    }

    public void setCartService(CartService cartService) {
        this.cartService = cartService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserResponseDTO user = getAuthenticatedUser(req);
        if (user == null) {
            JsonUtil.sendError(resp, HttpServletResponse.SC_UNAUTHORIZED, "UNAUTHORIZED", "Please log in to view cart.");
            return;
        }

        List<CartItem> items = cartService.getCartItems(user.getId());
        BigDecimal total = cartService.getCartTotal(user.getId());

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("items", items);
        responseData.put("totalAmount", total);

        JsonUtil.sendSuccess(resp, HttpServletResponse.SC_OK, responseData);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserResponseDTO user = getAuthenticatedUser(req);
        if (user == null) {
            JsonUtil.sendError(resp, HttpServletResponse.SC_UNAUTHORIZED, "UNAUTHORIZED", "Please log in to add items.");
            return;
        }

        try {
            CartItem requestDto = parseRequestBody(req, CartItem.class);
            CartItem added = cartService.addToCart(user.getId(), requestDto.getProductId(), requestDto.getQuantity());
            JsonUtil.sendSuccess(resp, HttpServletResponse.SC_CREATED, added);
        } catch (AppException e) {
            JsonUtil.sendError(resp, e.getStatusCode(), "CART_ERROR", e.getMessage());
        } catch (Exception e) {
            JsonUtil.sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "BAD_REQUEST", e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserResponseDTO user = getAuthenticatedUser(req);
        if (user == null) {
            JsonUtil.sendError(resp, HttpServletResponse.SC_UNAUTHORIZED, "UNAUTHORIZED", "Please log in.");
            return;
        }

        try {
            String pathInfo = req.getPathInfo();
            if (pathInfo == null || "/".equals(pathInfo)) {
                JsonUtil.sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "BAD_REQUEST", "Cart Item ID required.");
                return;
            }
            Long cartItemId = Long.parseLong(pathInfo.substring(1));
            CartItem requestDto = parseRequestBody(req, CartItem.class);

            boolean updated = cartService.updateQuantity(cartItemId, requestDto.getQuantity(), user.getId());
            if (updated) {
                JsonUtil.sendSuccess(resp, HttpServletResponse.SC_OK, "Quantity updated.");
            } else {
                JsonUtil.sendError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "UPDATE_FAILED", "Failed to update quantity.");
            }
        } catch (AppException e) {
            JsonUtil.sendError(resp, e.getStatusCode(), "CART_ERROR", e.getMessage());
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

        try {
            String pathInfo = req.getPathInfo();
            if (pathInfo == null || "/".equals(pathInfo)) {
                cartService.clearCart(user.getId());
                JsonUtil.sendSuccess(resp, HttpServletResponse.SC_OK, "Cart cleared.");
                return;
            }
            Long cartItemId = Long.parseLong(pathInfo.substring(1));
            cartService.removeFromCart(cartItemId, user.getId());
            JsonUtil.sendSuccess(resp, HttpServletResponse.SC_OK, "Item removed from cart.");
        } catch (AppException e) {
            JsonUtil.sendError(resp, e.getStatusCode(), "CART_ERROR", e.getMessage());
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
