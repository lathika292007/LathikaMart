package com.lathika.lathikamart.controller;

import com.google.gson.JsonObject;
import com.lathika.lathikamart.dao.AuditLogDAOImpl;
import com.lathika.lathikamart.dao.OrderDAOImpl;
import com.lathika.lathikamart.dao.ProductDAOImpl;
import com.lathika.lathikamart.dao.UserDAOImpl;
import com.lathika.lathikamart.dto.UserResponseDTO;
import com.lathika.lathikamart.exception.AppException;
import com.lathika.lathikamart.model.AuditLog;
import com.lathika.lathikamart.model.Order;
import com.lathika.lathikamart.model.Product;
import com.lathika.lathikamart.model.Role;
import com.lathika.lathikamart.service.AuditLogService;
import com.lathika.lathikamart.service.AuditLogServiceImpl;
import com.lathika.lathikamart.service.ProductService;
import com.lathika.lathikamart.service.ProductServiceImpl;
import com.lathika.lathikamart.service.UserService;
import com.lathika.lathikamart.service.UserServiceImpl;
import com.lathika.lathikamart.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AdminServlet handles system administrative actions:
 * user role management, moderation removal of violating listings, audit log inspection, and stats.
 */
@WebServlet(name = "AdminServlet", urlPatterns = {
        "/api/v1/admin/users",
        "/api/v1/admin/users/*",
        "/api/v1/admin/stats",
        "/api/v1/admin/audit-logs",
        "/api/v1/admin/products/*"
})
public class AdminServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(AdminServlet.class);

    private UserService userService;
    private ProductService productService;
    private AuditLogService auditLogService;
    private OrderDAOImpl orderDAO;

    @Override
    public void init() throws ServletException {
        this.userService = new UserServiceImpl(new UserDAOImpl());
        this.productService = new ProductServiceImpl(new ProductDAOImpl());
        this.auditLogService = new AuditLogServiceImpl(new AuditLogDAOImpl());
        this.orderDAO = new OrderDAOImpl();
    }

    public AdminServlet() {
    }

    public AdminServlet(UserService userService, ProductService productService, AuditLogService auditLogService) {
        this.userService = userService;
        this.productService = productService;
        this.auditLogService = auditLogService;
        this.orderDAO = new OrderDAOImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserResponseDTO admin = getAuthenticatedAdmin(req, resp);
        if (admin == null) return;

        String path = req.getRequestURI();

        if (path.endsWith("/api/v1/admin/users") || path.endsWith("/api/v1/admin/users/")) {
            List<UserResponseDTO> users = userService.getAllUsers();
            JsonUtil.sendSuccess(resp, HttpServletResponse.SC_OK, users);
        } else if (path.endsWith("/api/v1/admin/stats")) {
            List<UserResponseDTO> users = userService.getAllUsers();
            List<Product> products = productService.getAllProducts();
            List<Order> orders = orderDAO.findAll();

            long sellerCount = users.stream().filter(u -> u.getRole() == Role.SELLER).count();
            BigDecimal totalRevenue = orders.stream()
                    .map(Order::getTotalAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            Map<String, Object> stats = new HashMap<>();
            stats.put("totalUsers", users.size());
            stats.put("totalSellers", sellerCount);
            stats.put("totalProducts", products.size());
            stats.put("totalOrders", orders.size());
            stats.put("totalRevenue", totalRevenue);

            JsonUtil.sendSuccess(resp, HttpServletResponse.SC_OK, stats);
        } else if (path.endsWith("/api/v1/admin/audit-logs")) {
            List<AuditLog> logs = auditLogService.getAllAuditLogs();
            JsonUtil.sendSuccess(resp, HttpServletResponse.SC_OK, logs);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserResponseDTO admin = getAuthenticatedAdmin(req, resp);
        if (admin == null) return;

        String path = req.getRequestURI();
        if (path.contains("/api/v1/admin/users/") && path.endsWith("/role")) {
            try {
                String[] parts = path.split("/");
                Long userId = Long.parseLong(parts[parts.length - 2]);

                JsonObject json = JsonUtil.getGson().fromJson(req.getReader(), JsonObject.class);
                if (json == null || !json.has("role")) {
                    JsonUtil.sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "INVALID_INPUT", "Role parameter is required.");
                    return;
                }
                String newRole = json.get("role").getAsString();

                UserResponseDTO updatedUser = userService.updateUserRole(userId, newRole);

                // Create Audit Log
                auditLogService.logAction(
                        admin.getId(),
                        admin.getEmail(),
                        "USER_ROLE_UPDATE",
                        "USER",
                        userId,
                        "Updated user '" + updatedUser.getEmail() + "' role to " + updatedUser.getRole()
                );

                JsonUtil.sendSuccess(resp, HttpServletResponse.SC_OK, updatedUser);
            } catch (AppException e) {
                JsonUtil.sendError(resp, e.getStatusCode(), "UPDATE_ROLE_FAILED", e.getMessage());
            } catch (Exception e) {
                logger.error("Error updating user role", e);
                JsonUtil.sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "BAD_REQUEST", "Invalid request body or user ID.");
            }
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserResponseDTO admin = getAuthenticatedAdmin(req, resp);
        if (admin == null) return;

        String path = req.getRequestURI();
        if (path.contains("/api/v1/admin/products/")) {
            try {
                String[] parts = path.split("/");
                Long productId = Long.parseLong(parts[parts.length - 1]);

                Product product = productService.getProductById(productId);
                boolean deleted = productService.deleteProduct(productId, admin.getId(), true);

                if (deleted) {
                    auditLogService.logAction(
                            admin.getId(),
                            admin.getEmail(),
                            "LISTING_REMOVAL",
                            "PRODUCT",
                            productId,
                            "Removed product listing '" + product.getName() + "' (Seller ID #" + product.getSellerId() + ")"
                    );
                    JsonUtil.sendSuccess(resp, HttpServletResponse.SC_OK, Map.of("message", "Product listing removed successfully."));
                } else {
                    JsonUtil.sendError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "DELETE_FAILED", "Could not remove product listing.");
                }
            } catch (AppException e) {
                JsonUtil.sendError(resp, e.getStatusCode(), "MODERATION_FAILED", e.getMessage());
            } catch (Exception e) {
                logger.error("Error moderating product deletion", e);
                JsonUtil.sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "BAD_REQUEST", "Invalid product ID format.");
            }
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private UserResponseDTO getAuthenticatedAdmin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);
        UserResponseDTO currentUser = (session != null) ? (UserResponseDTO) session.getAttribute("currentUser") : null;
        if (currentUser == null || currentUser.getRole() != Role.ADMIN) {
            JsonUtil.sendError(resp, HttpServletResponse.SC_FORBIDDEN, "FORBIDDEN", "Admin access required.");
            return null;
        }
        return currentUser;
    }
}
