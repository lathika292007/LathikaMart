-- LathikaMart Seed Data Migration V2__seed_data.sql

MERGE INTO users (id, name, email, password_hash, role) KEY(id) VALUES 
(1, 'Admin User', 'admin@lathikamart.com', '$2a$10$5Qv70cHNt38aSfAw65NfE.hcXoChUbJdNDI7Q1XA/Px.2r8aToKzi', 'ADMIN'),
(2, 'TechStore Seller', 'seller.tech@lathikamart.com', '$2a$10$5Qv70cHNt38aSfAw65NfE.hcXoChUbJdNDI7Q1XA/Px.2r8aToKzi', 'SELLER'),
(3, 'FashionTrend Seller', 'seller.fashion@lathikamart.com', '$2a$10$5Qv70cHNt38aSfAw65NfE.hcXoChUbJdNDI7Q1XA/Px.2r8aToKzi', 'SELLER'),
(4, 'John Buyer', 'john.buyer@gmail.com', '$2a$10$5Qv70cHNt38aSfAw65NfE.hcXoChUbJdNDI7Q1XA/Px.2r8aToKzi', 'BUYER');

MERGE INTO products (id, seller_id, name, description, price, stock_qty, category, image_url) KEY(id) VALUES
(1, 2, 'Wireless Noise-Canceling Headphones', 'High quality Bluetooth over-ear headphones with ANC and 30-hour battery life', 199.99, 50, 'Electronics', 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?auto=format&fit=crop&w=600&q=80'),
(2, 2, 'Smart Fitness Watch', 'Waterproof fitness tracker with continuous heart rate monitor and GPS', 89.50, 100, 'Electronics', 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?auto=format&fit=crop&w=600&q=80'),
(3, 2, '4K Ultra HD Smart TV 55"', 'Stunning 4K display with HDR10+ and built-in streaming apps', 449.99, 15, 'Electronics', 'https://images.unsplash.com/photo-1593784991095-a205069470b6?auto=format&fit=crop&w=600&q=80'),
(4, 2, 'Mechanical RGB Gaming Keyboard', 'Tactile mechanical switches with customizable RGB backlighting and wrist rest', 69.99, 60, 'Electronics', 'https://images.unsplash.com/photo-1587829741301-dc798b83add3?auto=format&fit=crop&w=600&q=80'),
(5, 3, 'Classic Leather Jacket', '100% Genuine lambskin leather jacket with premium quilted lining', 149.00, 25, 'Fashion', 'https://images.unsplash.com/photo-1551028719-00167b16eac5?auto=format&fit=crop&w=600&q=80'),
(6, 3, 'Ergonomic Running Shoes', 'Lightweight mesh sneakers engineered for maximum comfort and cushioning', 79.99, 40, 'Fashion', 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?auto=format&fit=crop&w=600&q=80'),
(7, 3, 'Designer Denim Slim Jeans', 'Premium stretch cotton slim-fit denim jeans in dark indigo wash', 59.50, 45, 'Fashion', 'https://images.unsplash.com/photo-1541099649105-f69ad21f3246?auto=format&fit=crop&w=600&q=80'),
(8, 3, 'Minimalist Waterproof Backpack', 'Durable water-resistant laptop backpack with USB charging port', 45.00, 80, 'Fashion', 'https://images.unsplash.com/photo-1553062407-98eeb64c6a62?auto=format&fit=crop&w=600&q=80'),
(9, 2, 'Stainless Steel Espresso Machine', '15-bar Italian pump espresso maker with integrated milk frother', 129.99, 30, 'Home & Kitchen', 'https://images.unsplash.com/photo-1517668808822-9ebb02f2a0e6?auto=format&fit=crop&w=600&q=80'),
(10, 2, 'Modern LED Desk Lamp with Wireless Charger', 'Eye-caring dimmable desk light with Qi fast wireless charging pad', 39.99, 70, 'Home & Kitchen', 'https://images.unsplash.com/photo-1534353473418-4cfa6c56fd38?auto=format&fit=crop&w=600&q=80'),
(11, 3, 'Pro Non-Slip Yoga & Exercise Mat', 'Eco-friendly 6mm extra-thick high-density TPE workout mat', 29.99, 90, 'Sports', 'https://images.unsplash.com/photo-1601925260368-ae2f83cf8b7f?auto=format&fit=crop&w=600&q=80'),
(12, 3, 'Organic Hydrating Face Serum 50ml', 'Hyaluronic acid and vitamin C anti-aging face oil serum', 24.50, 110, 'Beauty', 'https://images.unsplash.com/photo-1620916566398-39f1143ab7be?auto=format&fit=crop&w=600&q=80'),
(13, 2, 'Clean Code: A Handbook of Agile Software Craftsmanship', 'Essential principles, patterns, and practices of writing clean, maintainable Java code', 37.50, 40, 'Books', 'https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?auto=format&fit=crop&w=600&q=80'),
(14, 2, 'Design Patterns: Elements of Reusable Object-Oriented Software', 'The classic Gang of Four reference guide on software design patterns and architecture', 49.99, 30, 'Books', 'https://images.unsplash.com/photo-1512820790803-83ca734da794?auto=format&fit=crop&w=600&q=80'),
(15, 3, 'Atomic Habits by James Clear', 'An easy and proven way to build good habits and break bad ones through micro changes', 18.99, 85, 'Books', 'https://images.unsplash.com/photo-1543002588-bfa74002ed7e?auto=format&fit=crop&w=600&q=80'),
(16, 3, 'The Pragmatic Programmer: Your Journey To Mastery', 'Timeless wisdom and classic career advice for software developers and modern engineers', 42.00, 50, 'Books', 'https://images.unsplash.com/photo-1497633762265-9d179a990aa6?auto=format&fit=crop&w=600&q=80');

MERGE INTO orders (id, buyer_id, status, total_amount) KEY(id) VALUES
(1, 4, 'DELIVERED', 199.99);

MERGE INTO order_items (id, order_id, product_id, quantity, unit_price) KEY(id) VALUES
(1, 1, 1, 1, 199.99);

MERGE INTO reviews (id, product_id, user_id, rating, comment) KEY(id) VALUES
(1, 1, 4, 5, 'Amazing noise cancellation and battery life!'),
(2, 2, 4, 4, 'Great fitness watch, accurate steps and long battery.'),
(3, 6, 4, 5, 'Super comfortable shoes for long runs!'),
(4, 13, 4, 5, 'Must-read book for every software engineer!');
