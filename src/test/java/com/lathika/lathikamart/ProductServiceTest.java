package com.lathika.lathikamart;

import com.lathika.lathikamart.dao.ProductDAO;
import com.lathika.lathikamart.exception.AppException;
import com.lathika.lathikamart.exception.AuthException;
import com.lathika.lathikamart.exception.ValidationException;
import com.lathika.lathikamart.model.Product;
import com.lathika.lathikamart.service.ProductService;
import com.lathika.lathikamart.service.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ProductService seller listing management and authorization.
 */
@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductDAO productDAO;

    private ProductService productService;

    @BeforeEach
    public void setUp() {
        productService = new ProductServiceImpl(productDAO);
    }

    @Test
    public void testCreateProductSuccess() throws AppException {
        Product p = new Product(null, 2L, "Laptop", "High performance", new BigDecimal("999.99"), 10, "Electronics", "http://img.jpg", null);

        when(productDAO.create(any(Product.class))).thenAnswer(inv -> {
            Product created = inv.getArgument(0);
            created.setId(100L);
            return created;
        });

        Product created = productService.createProduct(p);
        assertNotNull(created);
        assertEquals(100L, created.getId());
        assertEquals("Laptop", created.getName());
    }

    @Test
    public void testCreateProductInvalidPriceThrowsValidationException() {
        Product p = new Product(null, 2L, "Laptop", "High performance", new BigDecimal("-10.00"), 10, "Electronics", null, null);
        assertThrows(ValidationException.class, () -> productService.createProduct(p));
    }

    @Test
    public void testUpdateProductUnauthorizedSellerThrowsAuthException() {
        Product existing = new Product(100L, 2L, "Laptop", "High performance", new BigDecimal("999.99"), 10, "Electronics", null, null);
        Product updateReq = new Product(100L, 2L, "Laptop Updated", "High performance", new BigDecimal("999.99"), 10, "Electronics", null, null);

        when(productDAO.findById(100L)).thenReturn(Optional.of(existing));

        // Requesting user ID 5L is not seller 2L nor admin
        assertThrows(AuthException.class, () -> productService.updateProduct(updateReq, 5L, false));
    }
}
