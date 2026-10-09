package com.lathika.lathikamart.controller;

import com.lathika.lathikamart.dao.ProductDAOImpl;
import com.lathika.lathikamart.dto.UserResponseDTO;
import com.lathika.lathikamart.exception.AppException;
import com.lathika.lathikamart.model.Product;
import com.lathika.lathikamart.model.Role;
import com.lathika.lathikamart.service.ProductService;
import com.lathika.lathikamart.service.ProductServiceImpl;
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
 * ProductServlet handles browsing, searching, creating, updating, and deleting product listings.
 */
@WebServlet(name = "ProductServlet", urlPatterns = {"/api/v1/products", "/api/v1/products/*"})
public class ProductServlet extends HttpServlet {

    private ProductService productService;

    @Override
    public void init() throws ServletException {
        this.productService = new ProductServiceImpl(new ProductDAOImpl());
    }

    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        try {
            if (pathInfo == null || "/".equals(pathInfo)) {
                String search = req.getParameter("search");
                if (search == null || search.isEmpty()) search = req.getParameter("q");
                String category = req.getParameter("category");
                String sellerIdStr = req.getParameter("sellerId");
                String minPriceStr = req.getParameter("minPrice");
                String maxPriceStr = req.getParameter("maxPrice");
                String sortBy = req.getParameter("sortBy");

                List<Product> products;
                if (sellerIdStr != null && !sellerIdStr.isEmpty()) {
                    products = productService.getProductsBySeller(Long.parseLong(sellerIdStr));
                } else {
                    java.math.BigDecimal minPrice = (minPriceStr != null && !minPriceStr.isEmpty()) ? new java.math.BigDecimal(minPriceStr) : null;
                    java.math.BigDecimal maxPrice = (maxPriceStr != null && !maxPriceStr.isEmpty()) ? new java.math.BigDecimal(maxPriceStr) : null;
                    products = productService.searchProductsWithFilters(search, category, minPrice, maxPrice, sortBy);
                }
                JsonUtil.sendSuccess(resp, HttpServletResponse.SC_OK, products);
            } else {
                Long id = Long.parseLong(pathInfo.substring(1));
                Product product = productService.getProductById(id);
                JsonUtil.sendSuccess(resp, HttpServletResponse.SC_OK, product);
            }
        } catch (AppException e) {
            JsonUtil.sendError(resp, e.getStatusCode(), "PRODUCT_ERROR", e.getMessage());
        } catch (Exception e) {
            JsonUtil.sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "INVALID_REQUEST", e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserResponseDTO user = getAuthenticatedUser(req);
        if (user == null) {
            JsonUtil.sendError(resp, HttpServletResponse.SC_UNAUTHORIZED, "UNAUTHORIZED", "Please log in to create products.");
            return;
        }

        try {
            Product product = parseRequestBody(req, Product.class);
            product.setSellerId(user.getId());
            Product created = productService.createProduct(product);
            JsonUtil.sendSuccess(resp, HttpServletResponse.SC_CREATED, created);
        } catch (AppException e) {
            JsonUtil.sendError(resp, e.getStatusCode(), "VALIDATION_ERROR", e.getMessage());
        } catch (Exception e) {
            JsonUtil.sendError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "SERVER_ERROR", e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserResponseDTO user = getAuthenticatedUser(req);
        if (user == null) {
            JsonUtil.sendError(resp, HttpServletResponse.SC_UNAUTHORIZED, "UNAUTHORIZED", "Login required.");
            return;
        }

        try {
            String pathInfo = req.getPathInfo();
            if (pathInfo == null || "/".equals(pathInfo)) {
                JsonUtil.sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "BAD_REQUEST", "Product ID required.");
                return;
            }
            Long id = Long.parseLong(pathInfo.substring(1));
            Product product = parseRequestBody(req, Product.class);
            product.setId(id);

            boolean isAdmin = user.getRole() == Role.ADMIN;
            Product updated = productService.updateProduct(product, user.getId(), isAdmin);
            JsonUtil.sendSuccess(resp, HttpServletResponse.SC_OK, updated);
        } catch (AppException e) {
            JsonUtil.sendError(resp, e.getStatusCode(), "PRODUCT_ERROR", e.getMessage());
        } catch (Exception e) {
            JsonUtil.sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "BAD_REQUEST", e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserResponseDTO user = getAuthenticatedUser(req);
        if (user == null) {
            JsonUtil.sendError(resp, HttpServletResponse.SC_UNAUTHORIZED, "UNAUTHORIZED", "Login required.");
            return;
        }

        try {
            String pathInfo = req.getPathInfo();
            if (pathInfo == null || "/".equals(pathInfo)) {
                JsonUtil.sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "BAD_REQUEST", "Product ID required.");
                return;
            }
            Long id = Long.parseLong(pathInfo.substring(1));
            boolean isAdmin = user.getRole() == Role.ADMIN;

            boolean deleted = productService.deleteProduct(id, user.getId(), isAdmin);
            if (deleted) {
                JsonUtil.sendSuccess(resp, HttpServletResponse.SC_OK, "Product deleted successfully.");
            } else {
                JsonUtil.sendError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "DELETE_FAILED", "Failed to delete product.");
            }
        } catch (AppException e) {
            JsonUtil.sendError(resp, e.getStatusCode(), "PRODUCT_ERROR", e.getMessage());
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
