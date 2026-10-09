package com.lathika.lathikamart;

import com.lathika.lathikamart.dao.CartDAO;
import com.lathika.lathikamart.dao.OrderDAO;
import com.lathika.lathikamart.dao.ProductDAO;
import com.lathika.lathikamart.exception.AppException;
import com.lathika.lathikamart.exception.ValidationException;
import com.lathika.lathikamart.model.CartItem;
import com.lathika.lathikamart.model.Order;
import com.lathika.lathikamart.model.OrderStatus;
import com.lathika.lathikamart.model.Product;
import com.lathika.lathikamart.service.OrderService;
import com.lathika.lathikamart.service.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for OrderService checkout and status transition rules.
 */
@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderDAO orderDAO;

    @Mock
    private CartDAO cartDAO;

    @Mock
    private ProductDAO productDAO;

    private OrderService orderService;

    @BeforeEach
    public void setUp() {
        orderService = new OrderServiceImpl(orderDAO, cartDAO, productDAO);
    }

    @Test
    public void testCheckoutCartSuccess() throws AppException {
        CartItem item = new CartItem();
        item.setId(1L);
        item.setUserId(10L);
        item.setProductId(100L);
        item.setQuantity(2);

        Product product = new Product();
        product.setId(100L);
        product.setName("Test Laptop");
        product.setPrice(new BigDecimal("999.99"));
        product.setStockQty(10);

        when(cartDAO.findByUserId(10L)).thenReturn(List.of(item));
        when(productDAO.findById(100L)).thenReturn(Optional.of(product));
        when(orderDAO.createOrder(any(Order.class))).thenAnswer(invocation -> {
            Order o = invocation.getArgument(0);
            o.setId(50L);
            return o;
        });

        Order result = orderService.checkoutCart(10L);

        assertNotNull(result);
        assertEquals(50L, result.getId());
        assertEquals(OrderStatus.CONFIRMED, result.getStatus());
        assertEquals(new BigDecimal("1999.98"), result.getTotalAmount());
        assertEquals(8, product.getStockQty()); // Stock deducted
        verify(cartDAO, times(1)).clearCart(10L);
    }

    @Test
    public void testUpdateOrderStatusFromDeliveredThrowsValidationException() {
        Order deliveredOrder = new Order();
        deliveredOrder.setId(50L);
        deliveredOrder.setBuyerId(10L);
        deliveredOrder.setStatus(OrderStatus.DELIVERED);

        when(orderDAO.findById(50L)).thenReturn(Optional.of(deliveredOrder));

        assertThrows(ValidationException.class, () ->
                orderService.updateOrderStatus(50L, OrderStatus.PENDING, 10L, true)
        );
    }
}
