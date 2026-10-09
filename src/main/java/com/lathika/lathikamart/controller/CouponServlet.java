package com.lathika.lathikamart.controller;

import com.lathika.lathikamart.dto.UserResponseDTO;
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
import java.util.Map;

/**
 * CouponServlet handles discount promo codes for buyers.
 */
@WebServlet(name = "CouponServlet", urlPatterns = {"/api/v1/coupon/apply"})
public class CouponServlet extends HttpServlet {

    private static final Map<String, BigDecimal> COUPON_DISCOUNTS = new HashMap<>();

    static {
        COUPON_DISCOUNTS.put("LATHIKA10", new BigDecimal("10.00")); // 10% OFF
        COUPON_DISCOUNTS.put("WELCOME20", new BigDecimal("20.00")); // 20% OFF
        COUPON_DISCOUNTS.put("FESTIVE15", new BigDecimal("15.00")); // 15% OFF
        COUPON_DISCOUNTS.put("FLAT500", new BigDecimal("500.00"));  // Flat ₹500 OFF
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        UserResponseDTO user = (session != null) ? (UserResponseDTO) session.getAttribute("currentUser") : null;
        if (user == null) {
            JsonUtil.sendError(resp, HttpServletResponse.SC_UNAUTHORIZED, "UNAUTHORIZED", "Please log in to apply coupons.");
            return;
        }

        try {
            Map<String, String> body = parseRequestBody(req);
            String code = (body != null && body.containsKey("code")) ? body.get("code").trim().toUpperCase() : "";

            if (!COUPON_DISCOUNTS.containsKey(code)) {
                JsonUtil.sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "INVALID_COUPON", "Invalid or expired coupon code.");
                return;
            }

            BigDecimal discountVal = COUPON_DISCOUNTS.get(code);
            boolean isPercentage = !code.equals("FLAT500");

            Map<String, Object> result = new HashMap<>();
            result.put("code", code);
            result.put("discountValue", discountVal);
            result.put("isPercentage", isPercentage);
            result.put("message", "Coupon " + code + " applied successfully!");

            session.setAttribute("appliedCoupon", result);
            JsonUtil.sendSuccess(resp, HttpServletResponse.SC_OK, result);
        } catch (Exception e) {
            JsonUtil.sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "BAD_REQUEST", "Failed to apply coupon.");
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
