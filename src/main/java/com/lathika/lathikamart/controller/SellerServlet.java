package com.lathika.lathikamart.controller;

import com.lathika.lathikamart.dao.CartDAOImpl;
import com.lathika.lathikamart.dao.OrderDAOImpl;
import com.lathika.lathikamart.dao.ProductDAOImpl;
import com.lathika.lathikamart.dto.UserResponseDTO;
import com.lathika.lathikamart.model.Order;
import com.lathika.lathikamart.model.OrderItem;
import com.lathika.lathikamart.model.Product;
import com.lathika.lathikamart.model.Role;
import com.lathika.lathikamart.service.OrderService;
import com.lathika.lathikamart.service.OrderServiceImpl;
import com.lathika.lathikamart.service.ProductService;
import com.lathika.lathikamart.service.ProductServiceImpl;
import com.lathika.lathikamart.util.JsonUtil;

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
 * SellerServlet provides sales dashboard metrics, revenue totals, and seller product analytics.
 */
@WebServlet(name = "SellerServlet", urlPatterns = {"/api/v1/seller/stats"})
public class SellerServlet extends HttpServlet {

    private ProductService productService;
    private OrderService orderService;

    @Override
    public void init() throws ServletException {
        this.productService = new ProductServiceImpl(new ProductDAOImpl());
        this.orderService = new OrderServiceImpl(new OrderDAOImpl(), new CartDAOImpl(), new ProductDAOImpl());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserResponseDTO user = getAuthenticatedUser(req);
        if (user == null || (user.getRole() != Role.SELLER && user.getRole() != Role.ADMIN)) {
            JsonUtil.sendError(resp, HttpServletResponse.SC_FORBIDDEN, "FORBIDDEN", "Seller access required.");
            return;
        }

        try {
            List<Product> sellerProducts = productService.getProductsBySeller(user.getId());
            List<Order> sellerOrders = orderService.getSellerOrders(user.getId());

            BigDecimal totalRevenue = BigDecimal.ZERO;
            int totalItemsSold = 0;
            int lowStockCount = 0;

            for (Product p : sellerProducts) {
                if (p.getStockQty() != null && p.getStockQty() <= 5) {
                    lowStockCount++;
                }
            }

            for (Order o : sellerOrders) {
                if (o.getItems() != null) {
                    for (OrderItem item : o.getItems()) {
                        if (item.getUnitPrice() != null && item.getQuantity() != null) {
                            BigDecimal itemRev = item.getUnitPrice().multiply(new BigDecimal(item.getQuantity()));
                            totalRevenue = totalRevenue.add(itemRev);
                            totalItemsSold += item.getQuantity();
                        }
                    }
                }
            }

            Map<String, Object> stats = new HashMap<>();
            stats.put("totalProducts", sellerProducts.size());
            stats.put("totalOrders", sellerOrders.size());
            stats.put("totalItemsSold", totalItemsSold);
            stats.put("totalRevenue", totalRevenue);
            stats.put("lowStockCount", lowStockCount);

            JsonUtil.sendSuccess(resp, HttpServletResponse.SC_OK, stats);
        } catch (Exception e) {
            JsonUtil.sendError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SERVER_ERROR", e.getMessage());
        }
    }

    private UserResponseDTO getAuthenticatedUser(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return (session != null) ? (UserResponseDTO) session.getAttribute("currentUser") : null;
    }
}
