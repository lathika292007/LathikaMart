package com.lathika.lathikamart.service;

import com.lathika.lathikamart.exception.AppException;
import com.lathika.lathikamart.model.Order;
import com.lathika.lathikamart.model.OrderStatus;

import java.util.List;

/**
 * Service interface for order creation, status tracking, and checkout operations.
 */
public interface OrderService {
    Order checkoutCart(Long buyerId) throws AppException;
    Order getOrderById(Long id, Long requestingUserId, boolean isAdmin) throws AppException;
    List<Order> getBuyerOrders(Long buyerId);
    List<Order> getSellerOrders(Long sellerId);
    List<Order> getAllOrders();
    boolean updateOrderStatus(Long orderId, OrderStatus status, Long requestingUserId, boolean isAdmin) throws AppException;
}
