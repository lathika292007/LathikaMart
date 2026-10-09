package com.lathika.lathikamart.filter;

import com.lathika.lathikamart.dto.UserResponseDTO;
import com.lathika.lathikamart.model.Role;
import com.lathika.lathikamart.util.JsonUtil;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * AuthFilter checks session state and enforces role-based access control.
 */
@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getRequestURI().substring(req.getContextPath().length());

        // Protected API checks
        if (path.startsWith("/api/v1/cart") || 
            path.startsWith("/api/v1/orders") || 
            path.startsWith("/api/v1/reviews") ||
            path.startsWith("/api/v1/seller") || 
            path.startsWith("/api/v1/admin")) {

            HttpSession session = req.getSession(false);
            if (session == null || session.getAttribute("currentUser") == null) {
                JsonUtil.sendError(res, HttpServletResponse.SC_UNAUTHORIZED, "UNAUTHORIZED", "Authentication required. Please log in.");
                return;
            }

            UserResponseDTO currentUser = (UserResponseDTO) session.getAttribute("currentUser");

            if (path.startsWith("/api/v1/seller") && currentUser.getRole() != Role.SELLER && currentUser.getRole() != Role.ADMIN) {
                JsonUtil.sendError(res, HttpServletResponse.SC_FORBIDDEN, "FORBIDDEN", "Seller or Admin access required.");
                return;
            }

            if (path.startsWith("/api/v1/admin") && currentUser.getRole() != Role.ADMIN) {
                JsonUtil.sendError(res, HttpServletResponse.SC_FORBIDDEN, "FORBIDDEN", "Admin access required.");
                return;
            }
        }

        // Protected view checks for JSP view paths
        if (path.startsWith("/views/seller") || path.startsWith("/seller-dashboard.jsp")) {
            HttpSession session = req.getSession(false);
            if (session == null || session.getAttribute("currentUser") == null) {
                res.sendRedirect(req.getContextPath() + "/login.jsp");
                return;
            }
            UserResponseDTO currentUser = (UserResponseDTO) session.getAttribute("currentUser");
            if (currentUser.getRole() != Role.SELLER && currentUser.getRole() != Role.ADMIN) {
                res.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied: Seller role required");
                return;
            }
        }

        if (path.startsWith("/views/admin") || path.startsWith("/admin.jsp")) {
            HttpSession session = req.getSession(false);
            if (session == null || session.getAttribute("currentUser") == null) {
                res.sendRedirect(req.getContextPath() + "/login.jsp");
                return;
            }
            UserResponseDTO currentUser = (UserResponseDTO) session.getAttribute("currentUser");
            if (currentUser.getRole() != Role.ADMIN) {
                res.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied: Admin role required");
                return;
            }
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
