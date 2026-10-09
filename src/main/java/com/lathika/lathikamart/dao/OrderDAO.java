package com.lathika.lathikamart.dao;

import com.lathika.lathikamart.model.Order;
import com.lathika.lathikamart.model.OrderStatus;

import java.util.List;
import java.util.Optional;

/**
 * Data Access Object interface for Order operations.
 */
public interface OrderDAO {
    Order createOrder(Order order);
    Optional<Order> findById(Long id);
    List<Order> findByBuyerId(Long buyerId);
    List<Order> findBySellerId(Long sellerId);
    List<Order> findAll();
    boolean updateStatus(Long orderId, OrderStatus status);
}
