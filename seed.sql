-- LathikaMart Seed Data
-- Default Passwords: "password123" (bcrypt hashed with cost factor 10)
-- Hash: $2a$10$5Qv70cHNt38aSfAw65NfE.hcXoChUbJdNDI7Q1XA/Px.2r8aToKzi

MERGE INTO users (id, name, email, password_hash, role) KEY(id) VALUES 
(1, 'Admin User', 'admin@lathikamart.com', '$2a$10$5Qv70cHNt38aSfAw65NfE.hcXoChUbJdNDI7Q1XA/Px.2r8aToKzi', 'ADMIN'),
(2, 'TechStore Seller', 'seller.tech@lathikamart.com', '$2a$10$5Qv70cHNt38aSfAw65NfE.hcXoChUbJdNDI7Q1XA/Px.2r8aToKzi', 'SELLER'),
(3, 'FashionTrend Seller', 'seller.fashion@lathikamart.com', '$2a$10$5Qv70cHNt38aSfAw65NfE.hcXoChUbJdNDI7Q1XA/Px.2r8aToKzi', 'SELLER'),
(4, 'John Buyer', 'john.buyer@gmail.com', '$2a$10$5Qv70cHNt38aSfAw65NfE.hcXoChUbJdNDI7Q1XA/Px.2r8aToKzi', 'BUYER');

MERGE INTO products (id, seller_id, name, description, price, stock_qty, category, image_url) KEY(id) VALUES
(1, 2, 'Wireless Noise-Canceling Headphones', 'High quality Bluetooth over-ear headphones with ANC', 199.99, 50, 'Electronics', 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?auto=format&fit=crop&w=600&q=80'),
(2, 2, 'Smart Fitness Watch', 'Waterproof fitness tracker with heart rate monitor', 89.50, 100, 'Electronics', 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?auto=format&fit=crop&w=600&q=80'),
(3, 3, 'Classic Leather Jacket', '100% Genuine leather jacket with premium lining', 149.00, 25, 'Fashion', 'https://images.unsplash.com/photo-1551028719-00167b16eac5?auto=format&fit=crop&w=600&q=80'),
(4, 3, 'Ergonomic Running Shoes', 'Lightweight mesh sneakers for daily running', 79.99, 40, 'Fashion', 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?auto=format&fit=crop&w=600&q=80');

MERGE INTO orders (id, buyer_id, status, total_amount) KEY(id) VALUES
(1, 4, 'DELIVERED', 199.99);

MERGE INTO order_items (id, order_id, product_id, quantity, unit_price) KEY(id) VALUES
(1, 1, 1, 1, 199.99);

MERGE INTO reviews (id, product_id, user_id, rating, comment) KEY(id) VALUES
(1, 1, 4, 5, 'Amazing noise cancellation and battery life!');
