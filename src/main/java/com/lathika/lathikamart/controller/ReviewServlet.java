package com.lathika.lathikamart.controller;

import com.lathika.lathikamart.dao.ProductDAOImpl;
import com.lathika.lathikamart.dao.ReviewDAOImpl;
import com.lathika.lathikamart.dto.UserResponseDTO;
import com.lathika.lathikamart.exception.AppException;
import com.lathika.lathikamart.model.Review;
import com.lathika.lathikamart.service.ReviewService;
import com.lathika.lathikamart.service.ReviewServiceImpl;
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
 * ReviewServlet handles fetching and posting product reviews and ratings.
 */
@WebServlet(name = "ReviewServlet", urlPatterns = {"/api/v1/reviews", "/api/v1/reviews/*"})
public class ReviewServlet extends HttpServlet {

    private ReviewService reviewService;

    @Override
    public void init() throws ServletException {
        this.reviewService = new ReviewServiceImpl(new ReviewDAOImpl(), new ProductDAOImpl());
    }

    public void setReviewService(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String productIdStr = req.getParameter("productId");
        if (productIdStr == null || productIdStr.isEmpty()) {
            JsonUtil.sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "BAD_REQUEST", "productId parameter required.");
            return;
        }

        try {
            Long productId = Long.parseLong(productIdStr);
            List<Review> reviews = reviewService.getProductReviews(productId);
            JsonUtil.sendSuccess(resp, HttpServletResponse.SC_OK, reviews);
        } catch (NumberFormatException e) {
            JsonUtil.sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "BAD_REQUEST", "Invalid productId format.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserResponseDTO user = getAuthenticatedUser(req);
        if (user == null) {
            JsonUtil.sendError(resp, HttpServletResponse.SC_UNAUTHORIZED, "UNAUTHORIZED", "Please log in to leave a review.");
            return;
        }

        try {
            Review body = parseRequestBody(req, Review.class);
            Review created = reviewService.addReview(user.getId(), body.getProductId(), body.getRating(), body.getComment());
            JsonUtil.sendSuccess(resp, HttpServletResponse.SC_CREATED, created);
        } catch (AppException e) {
            JsonUtil.sendError(resp, e.getStatusCode(), "REVIEW_ERROR", e.getMessage());
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
