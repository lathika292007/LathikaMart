package com.lathika.lathikamart.service;

import com.lathika.lathikamart.dao.ProductDAO;
import com.lathika.lathikamart.dao.WishlistDAO;
import com.lathika.lathikamart.exception.AppException;
import com.lathika.lathikamart.model.Product;
import com.lathika.lathikamart.model.Wishlist;

import java.util.List;

public class WishlistServiceImpl implements WishlistService {

    private final WishlistDAO wishlistDAO;
    private final ProductDAO productDAO;

    public WishlistServiceImpl(WishlistDAO wishlistDAO, ProductDAO productDAO) {
        this.wishlistDAO = wishlistDAO;
        this.productDAO = productDAO;
    }

    @Override
    public Wishlist addToWishlist(Long userId, Long productId) throws AppException {
        Product p = productDAO.findById(productId).orElseThrow(() -> 
            new AppException("Product #" + productId + " not found.", 404));
        return wishlistDAO.add(userId, productId);
    }

    @Override
    public boolean removeFromWishlist(Long userId, Long productId) {
        return wishlistDAO.remove(userId, productId);
    }

    @Override
    public List<Wishlist> getUserWishlist(Long userId) {
        return wishlistDAO.findByUserId(userId);
    }

    @Override
    public boolean isWishlisted(Long userId, Long productId) {
        return wishlistDAO.exists(userId, productId);
    }
}
