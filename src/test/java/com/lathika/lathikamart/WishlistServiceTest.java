package com.lathika.lathikamart;

import com.lathika.lathikamart.dao.ProductDAO;
import com.lathika.lathikamart.dao.WishlistDAO;
import com.lathika.lathikamart.model.Product;
import com.lathika.lathikamart.model.Wishlist;
import com.lathika.lathikamart.service.WishlistService;
import com.lathika.lathikamart.service.WishlistServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class WishlistServiceTest {

    private WishlistDAO wishlistDAO;
    private ProductDAO productDAO;
    private WishlistService wishlistService;

    @BeforeEach
    void setUp() {
        wishlistDAO = Mockito.mock(WishlistDAO.class);
        productDAO = Mockito.mock(ProductDAO.class);
        wishlistService = new WishlistServiceImpl(wishlistDAO, productDAO);
    }

    @Test
    void testAddToWishlistSuccess() throws Exception {
        Product sample = new Product();
        sample.setId(10L);
        sample.setName("Test Product");
        sample.setPrice(new BigDecimal("99.99"));

        when(productDAO.findById(10L)).thenReturn(java.util.Optional.of(sample));
        when(wishlistDAO.add(4L, 10L)).thenReturn(new Wishlist(1L, 4L, 10L, null));

        Wishlist w = wishlistService.addToWishlist(4L, 10L);
        assertNotNull(w);
        assertEquals(4L, w.getUserId());
        assertEquals(10L, w.getProductId());
    }

    @Test
    void testGetUserWishlist() {
        Wishlist item = new Wishlist(1L, 4L, 10L, null);
        when(wishlistDAO.findByUserId(4L)).thenReturn(List.of(item));

        List<Wishlist> result = wishlistService.getUserWishlist(4L);
        assertEquals(1, result.size());
        assertEquals(10L, result.get(0).getProductId());
    }
}
