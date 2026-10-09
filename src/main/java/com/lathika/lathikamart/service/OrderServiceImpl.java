package com.lathika.lathikamart.service;

import com.lathika.lathikamart.dao.CartDAO;
import com.lathika.lathikamart.dao.OrderDAO;
import com.lathika.lathikamart.dao.ProductDAO;
import com.lathika.lathikamart.exception.AppException;
import com.lathika.lathikamart.exception.AuthException;
import com.lathika.lathikamart.exception.NotFoundException;
import com.lathika.lathikamart.exception.ValidationException;
import com.lathika.lathikamart.model.CartItem;
import com.lathika.lathikamart.model.Order;
import com.lathika.lathikamart.model.OrderItem;
import com.lathika.lathikamart.model.OrderStatus;
import com.lathika.lathikamart.model.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of OrderService managing cart checkout and status transitions.
 */
public class OrderServiceImpl implements OrderService {

    private final OrderDAO orderDAO;
    private final CartDAO cartDAO;
    private final ProductDAO productDAO;

    public OrderServiceImpl(OrderDAO orderDAO, CartDAO cartDAO, ProductDAO productDAO) {
        this.orderDAO = orderDAO;
        this.cartDAO = cartDAO;
        this.productDAO = productDAO;
    }

    @Override
    public Order checkoutCart(Long buyerId) throws AppException {
        if (buyerId == null || buyerId <= 0) {
            throw new ValidationException("Invalid buyer ID.");
        }

        List<CartItem> cartItems = cartDAO.findByUserId(buyerId);
        if (cartItems.isEmpty()) {
            throw new ValidationException("Cart is empty. Add items to cart before placing an order.");
        }

        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem ci : cartItems) {
            Product p = productDAO.findById(ci.getProductId())
                    .orElseThrow(() -> new NotFoundException("Product not found: " + ci.getProductId()));

            if (p.getStockQty() < ci.getQuantity()) {
                throw new ValidationException("Insufficient stock for product '" + p.getName() + "'. Available: " + p.getStockQty());
            }

            BigDecimal itemTotal = p.getPrice().multiply(new BigDecimal(ci.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);

            OrderItem oi = new OrderItem();
            oi.setProductId(p.getId());
            oi.setQuantity(ci.getQuantity());
            oi.setUnitPrice(p.getPrice());
            orderItems.add(oi);

            // Deduct stock quantity
            p.setStockQty(p.getStockQty() - ci.getQuantity());
            productDAO.update(p);
        }

        Order order = new Order();
        order.setBuyerId(buyerId);
        order.setStatus(OrderStatus.CONFIRMED); // Mock payment confirmed
        order.setTotalAmount(totalAmount);
        order.setItems(orderItems);

        Order createdOrder = orderDAO.createOrder(order);

        // Clear buyer's cart after successful order creation
        cartDAO.clearCart(buyerId);

        return createdOrder;
    }

    @Override
    public Order getOrderById(Long id, Long requestingUserId, boolean isAdmin) throws AppException {
        Order order = orderDAO.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found with ID: " + id));

        if (!isAdmin && !order.getBuyerId().equals(requestingUserId)) {
            // Check if seller owns any product in this order
            List<Order> sellerOrders = orderDAO.findBySellerId(requestingUserId);
            boolean isSellerForOrder = sellerOrders.stream().anyMatch(o -> o.getId().equals(id));
            if (!isSellerForOrder) {
                throw new AuthException("Access denied to view order " + id, 403);
            }
        }
        return order;
    }

    @Override
    public List<Order> getBuyerOrders(Long buyerId) {
        return orderDAO.findByBuyerId(buyerId);
    }

    @Override
    public List<Order> getSellerOrders(Long sellerId) {
        return orderDAO.findBySellerId(sellerId);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderDAO.findAll();
    }

    @Override
    public boolean updateOrderStatus(Long orderId, OrderStatus newStatus, Long requestingUserId, boolean isAdmin) throws AppException {
        Order order = getOrderById(orderId, requestingUserId, isAdmin);
        OrderStatus current = order.getStatus();

        if (current == newStatus) {
            return true;
        }

        if (current == OrderStatus.DELIVERED) {
            throw new ValidationException("Cannot change status of an already DELIVERED order.");
        }

        if (current == OrderStatus.CANCELLED) {
            throw new ValidationException("Cannot change status of a CANCELLED order.");
        }

        return orderDAO.updateStatus(order.getId(), newStatus);
    }
}
