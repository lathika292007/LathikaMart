package com.lathika.lathikamart.controller;

import com.lathika.lathikamart.dao.CartDAOImpl;
import com.lathika.lathikamart.dao.OrderDAOImpl;
import com.lathika.lathikamart.dao.ProductDAOImpl;
import com.lathika.lathikamart.dto.UserResponseDTO;
import com.lathika.lathikamart.model.Order;
import com.lathika.lathikamart.model.OrderItem;
import com.lathika.lathikamart.model.Role;
import com.lathika.lathikamart.service.OrderService;
import com.lathika.lathikamart.service.OrderServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;

/**
 * InvoiceServlet renders a printable, professional HTML tax invoice for an order.
 */
@WebServlet(name = "InvoiceServlet", urlPatterns = {"/invoice"})
public class InvoiceServlet extends HttpServlet {

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
        HttpSession session = req.getSession(false);
        UserResponseDTO user = (session != null) ? (UserResponseDTO) session.getAttribute("currentUser") : null;

        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        String orderIdStr = req.getParameter("orderId");
        if (orderIdStr == null || orderIdStr.trim().isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing Order ID parameter.");
            return;
        }

        try {
            Long orderId = Long.parseLong(orderIdStr);
            boolean isAdmin = user.getRole() == Role.ADMIN;
            Order order = orderService.getOrderById(orderId, user.getId(), isAdmin);

            resp.setContentType("text/html;charset=UTF-8");
            PrintWriter out = resp.getWriter();
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, hh:mm a");

            out.println("<!DOCTYPE html>");
            out.println("<html><head><title>Invoice #" + order.getId() + " - LathikaMart</title>");
            out.println("<style>");
            out.println("body { font-family: 'Segoe UI', Tahoma, sans-serif; background: #f8fafc; color: #1e293b; padding: 40px; margin: 0; }");
            out.println(".invoice-card { max-width: 800px; margin: 0 auto; background: white; border-radius: 12px; box-shadow: 0 4px 20px rgba(0,0,0,0.08); padding: 40px; }");
            out.println(".header { display: flex; justify-content: space-between; border-bottom: 2px solid #e2e8f0; padding-bottom: 20px; }");
            out.println(".logo { font-size: 24px; font-weight: bold; color: #4f46e5; }");
            out.println(".invoice-title { text-align: right; }");
            out.println(".details { display: flex; justify-content: space-between; margin: 30px 0; }");
            out.println("table { width: 100%; border-collapse: collapse; margin-top: 20px; }");
            out.println("th, td { padding: 12px; text-align: left; border-bottom: 1px solid #e2e8f0; }");
            out.println("th { background: #f1f5f9; color: #475569; font-weight: 600; }");
            out.println(".total-row { font-size: 18px; font-weight: bold; color: #0f172a; text-align: right; padding-top: 20px; }");
            out.println(".print-btn { background: #4f46e5; color: white; border: none; padding: 10px 20px; border-radius: 6px; cursor: pointer; font-size: 14px; margin-top: 20px; }");
            out.println("@media print { .print-btn { display: none; } body { padding: 0; } .invoice-card { box-shadow: none; } }");
            out.println("</style></head><body>");

            out.println("<div class='invoice-card'>");
            out.println("<div class='header'>");
            out.println("<div><div class='logo'>✨ LathikaMart</div><div>Official Tax Invoice</div></div>");
            out.println("<div class='invoice-title'><h2 style='margin:0;color:#334155;'>INVOICE</h2><div>#INV-" + String.format("%06d", order.getId()) + "</div></div>");
            out.println("</div>");

            out.println("<div class='details'>");
            out.println("<div><strong>Billed To:</strong><br/>" + user.getName() + "<br/>" + user.getEmail() + "</div>");
            out.println("<div><strong>Order Date:</strong> " + (order.getCreatedAt() != null ? sdf.format(order.getCreatedAt()) : "N/A") + "<br/>");
            out.println("<strong>Order Status:</strong> <span style='color:#16a34a;font-weight:bold;'>" + order.getStatus() + "</span></div>");
            out.println("</div>");

            out.println("<table><thead><tr><th>#</th><th>Item Description</th><th>Qty</th><th>Unit Price</th><th>Subtotal</th></tr></thead><tbody>");
            int index = 1;
            for (OrderItem item : order.getItems()) {
                BigDecimal unitPrice = item.getUnitPrice() != null ? item.getUnitPrice() : BigDecimal.ZERO;
                BigDecimal subtotal = unitPrice.multiply(new BigDecimal(item.getQuantity()));
                out.println("<tr>");
                out.println("<td>" + (index++) + "</td>");
                out.println("<td>" + (item.getProductName() != null ? item.getProductName() : "Product #" + item.getProductId()) + "</td>");
                out.println("<td>" + item.getQuantity() + "</td>");
                out.println("<td>₹" + unitPrice + "</td>");
                out.println("<td>₹" + subtotal + "</td>");
                out.println("</tr>");
            }
            out.println("</tbody></table>");

            out.println("<div class='total-row'>Grand Total: ₹" + order.getTotalAmount() + "</div>");
            out.println("<button class='print-btn' onclick='window.print()'>🖨️ Print / Download Invoice PDF</button>");
            out.println("</div>");

            out.println("</body></html>");
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error generating invoice: " + e.getMessage());
        }
    }
}
