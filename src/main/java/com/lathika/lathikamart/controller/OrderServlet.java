package com.lathika.lathikamart.controller;

import com.lathika.lathikamart.dao.CartDAOImpl;
import com.lathika.lathikamart.dao.OrderDAOImpl;
import com.lathika.lathikamart.dao.ProductDAOImpl;
import com.lathika.lathikamart.dto.UserResponseDTO;
import com.lathika.lathikamart.exception.AppException;
import com.lathika.lathikamart.model.Order;
import com.lathika.lathikamart.model.OrderStatus;
import com.lathika.lathikamart.model.Role;
import com.lathika.lathikamart.service.OrderService;
import com.lathika.lathikamart.service.OrderServiceImpl;
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

/**
 * OrderServlet handles order placement, buyer order history, and seller order management.
 */
@WebServlet(name = "OrderServlet", urlPatterns = {"/api/v1/orders", "/api/v1/orders/*"})
public class OrderServlet extends HttpServlet {

    private OrderService orderService;

    @Override
    public void init() throws ServletException {
        this.orderService = new OrderServiceImpl(new OrderDAOImpl(), new CartDAOImpl(), new ProductDAOImpl());
    }

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserResponseDTO user = getAuthenticatedUser(req);
        if (user == null) {
            JsonUtil.sendError(resp, HttpServletResponse.SC_UNAUTHORIZED, "UNAUTHORIZED", "Please log in.");
            return;
        }

        String pathInfo = req.getPathInfo();
        try {
            if (pathInfo == null || "/".equals(pathInfo)) {
                String mode = req.getParameter("mode");
                List<Order> orders;
                if ("seller".equalsIgnoreCase(mode) && (user.getRole() == Role.SELLER || user.getRole() == Role.ADMIN)) {
                    orders = orderService.getSellerOrders(user.getId());
                } else if ("admin".equalsIgnoreCase(mode) && user.getRole() == Role.ADMIN) {
                    orders = orderService.getAllOrders();
                } else {
                    orders = orderService.getBuyerOrders(user.getId());
                }
                JsonUtil.sendSuccess(resp, HttpServletResponse.SC_OK, orders);
            } else {
                Long id = Long.parseLong(pathInfo.substring(1));
                boolean isAdmin = user.getRole() == Role.ADMIN;
                Order order = orderService.getOrderById(id, user.getId(), isAdmin);
                JsonUtil.sendSuccess(resp, HttpServletResponse.SC_OK, order);
            }
        } catch (AppException e) {
            JsonUtil.sendError(resp, e.getStatusCode(), "ORDER_ERROR", e.getMessage());
        } catch (Exception e) {
            JsonUtil.sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "BAD_REQUEST", e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserResponseDTO user = getAuthenticatedUser(req);
        if (user == null) {
            JsonUtil.sendError(resp, HttpServletResponse.SC_UNAUTHORIZED, "UNAUTHORIZED", "Please log in to place an order.");
            return;
        }

        try {
            Order order = orderService.checkoutCart(user.getId());
            JsonUtil.sendSuccess(resp, HttpServletResponse.SC_CREATED, order);
        } catch (AppException e) {
            JsonUtil.sendError(resp, e.getStatusCode(), "CHECKOUT_ERROR", e.getMessage());
        } catch (Exception e) {
            JsonUtil.sendError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SERVER_ERROR", e.getMessage());
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
                JsonUtil.sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "BAD_REQUEST", "Order ID required.");
                return;
            }
            Long orderId = Long.parseLong(pathInfo.substring(1));
            Order body = parseRequestBody(req, Order.class);

            if (body == null || body.getStatus() == null) {
                JsonUtil.sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "BAD_REQUEST", "New order status required.");
                return;
            }

            boolean isAdmin = user.getRole() == Role.ADMIN;
            boolean updated = orderService.updateOrderStatus(orderId, body.getStatus(), user.getId(), isAdmin);
            if (updated) {
                JsonUtil.sendSuccess(resp, HttpServletResponse.SC_OK, "Order status updated to " + body.getStatus());
            } else {
                JsonUtil.sendError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "UPDATE_FAILED", "Failed to update order status.");
            }
        } catch (AppException e) {
            JsonUtil.sendError(resp, e.getStatusCode(), "ORDER_ERROR", e.getMessage());
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
