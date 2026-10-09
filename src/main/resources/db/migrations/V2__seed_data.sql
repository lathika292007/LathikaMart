-- LathikaMart Seed Data Migration V2__seed_data.sql (52 Products across 10 Categories)

MERGE INTO users (id, name, email, password_hash, role) KEY(id) VALUES 
(1, 'Admin User', 'admin@lathikamart.com', '$2a$10$5Qv70cHNt38aSfAw65NfE.hcXoChUbJdNDI7Q1XA/Px.2r8aToKzi', 'ADMIN'),
(2, 'TechStore Seller', 'seller.tech@lathikamart.com', '$2a$10$5Qv70cHNt38aSfAw65NfE.hcXoChUbJdNDI7Q1XA/Px.2r8aToKzi', 'SELLER'),
(3, 'FashionTrend Seller', 'seller.fashion@lathikamart.com', '$2a$10$5Qv70cHNt38aSfAw65NfE.hcXoChUbJdNDI7Q1XA/Px.2r8aToKzi', 'SELLER'),
(4, 'John Buyer', 'john.buyer@gmail.com', '$2a$10$5Qv70cHNt38aSfAw65NfE.hcXoChUbJdNDI7Q1XA/Px.2r8aToKzi', 'BUYER');

MERGE INTO products (id, seller_id, name, description, price, stock_qty, category, image_url) KEY(id) VALUES
-- Category 1: Electronics (IDs 1-6)
(1, 2, '4K Ultra HD Smart TV 55"', 'Stunning 4K display with HDR10+ and built-in streaming apps', 449.99, 15, 'Electronics', 'https://images.unsplash.com/photo-1593784991095-a205069470b6?auto=format&fit=crop&w=600&q=80'),
(2, 2, 'Ultra Slim Laptop 15.6"', 'Intel i7 processor, 16GB RAM, 512GB SSD ultra portable laptop', 899.99, 12, 'Electronics', 'https://images.unsplash.com/photo-1496181133206-80ce9b88a853?auto=format&fit=crop&w=600&q=80'),
(3, 2, 'Flagship Smartphone 5G', '6.7-inch OLED 120Hz display, 108MP camera with fast charging', 699.00, 25, 'Electronics', 'https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?auto=format&fit=crop&w=600&q=80'),
(4, 2, 'Pro Tablet 11" 256GB', 'Retina display with M2 chip, stylus support, and all-day battery', 599.50, 20, 'Electronics', 'https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0?auto=format&fit=crop&w=600&q=80'),
(5, 2, '4K Gaming Monitor 27"', '144Hz 1ms IPS gaming display with G-Sync support and slim bezel', 329.99, 18, 'Electronics', 'https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?auto=format&fit=crop&w=600&q=80'),
(6, 2, 'DSLR Mirrorless Camera 24MP', '4K video recording, 3-inch flip touchscreen with 18-55mm lens', 749.00, 8, 'Electronics', 'https://images.unsplash.com/photo-1516035069371-29a1b244cc32?auto=format&fit=crop&w=600&q=80'),

-- Category 2: Wearables (IDs 7-11)
(7, 2, 'Smart Fitness Watch Pro', 'Waterproof fitness tracker with continuous heart rate monitor and GPS', 89.50, 4, 'Wearables', 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?auto=format&fit=crop&w=600&q=80'),
(8, 2, 'Smart Health Ring', 'Titanium sleep tracking ring with SPO2 and body temperature sensor', 199.00, 15, 'Wearables', 'https://images.unsplash.com/photo-1605100804763-247f67b3557e?auto=format&fit=crop&w=600&q=80'),
(9, 2, 'VR Headset System 128GB', 'Standalone immersive virtual reality glasses with wireless controllers', 299.99, 10, 'Wearables', 'https://images.unsplash.com/photo-1622979135225-d2ba269bc1bd?auto=format&fit=crop&w=600&q=80'),
(10, 2, 'GPS Outdoor Sports Watch', 'Rugged outdoor smartwatch with solar charging and topographic maps', 249.50, 14, 'Wearables', 'https://images.unsplash.com/photo-1508685096489-7aacd43bd3b1?auto=format&fit=crop&w=600&q=80'),
(11, 2, 'Smart Audio Sunglasses', 'Polarized UV protection sunglasses with open-ear directional speakers', 129.99, 3, 'Wearables', 'https://images.unsplash.com/photo-1511499767150-a48a237f0083?auto=format&fit=crop&w=600&q=80'),

-- Category 3: Audio (IDs 12-16)
(12, 2, 'Wireless ANC Headphones', 'High quality Bluetooth over-ear headphones with ANC and 30-hour battery life', 199.99, 50, 'Audio', 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?auto=format&fit=crop&w=600&q=80'),
(13, 2, 'True Wireless Earbuds Pro', 'Active noise cancellation, IPX7 waterproof, with wireless charging case', 119.00, 65, 'Audio', 'https://images.unsplash.com/photo-1590658268037-6bf12165a8df?auto=format&fit=crop&w=600&q=80'),
(14, 2, 'Portable Bluetooth Speaker', '360-degree surround sound with deep bass and 20-hour battery', 59.99, 45, 'Audio', 'https://images.unsplash.com/photo-1608043152269-423dbba4e7e1?auto=format&fit=crop&w=600&q=80'),
(15, 2, 'Dolby Atmos Soundbar 3.1', 'Subwoofer soundbar system with Bluetooth 5.0 and HDMI eARC', 219.00, 2, 'Audio', 'https://images.unsplash.com/photo-1545454675-3531b543be5d?auto=format&fit=crop&w=600&q=80'),
(16, 2, 'Studio Condenser USB Microphone', 'Professional podcasting and streaming microphone with shock mount', 79.50, 30, 'Audio', 'https://images.unsplash.com/photo-1590602847861-f357a9332bbc?auto=format&fit=crop&w=600&q=80'),

-- Category 4: Home & Kitchen (IDs 17-21)
(17, 2, 'Stainless Steel Espresso Machine', '15-bar Italian pump espresso maker with integrated milk frother', 129.99, 30, 'Home & Kitchen', 'https://images.unsplash.com/photo-1517668808822-9ebb02f2a0e6?auto=format&fit=crop&w=600&q=80'),
(18, 2, 'Modern LED Desk Lamp', 'Eye-caring dimmable desk light with Qi fast wireless charging pad', 39.99, 70, 'Home & Kitchen', 'https://images.unsplash.com/photo-1534353473418-4cfa6c56fd38?auto=format&fit=crop&w=600&q=80'),
(19, 2, 'Smart Robot Vacuum Cleaner', 'LIDAR navigation, automatic self-emptying base, 3000Pa suction', 279.00, 11, 'Home & Kitchen', 'https://images.unsplash.com/photo-1558317374-067fb5f30001?auto=format&fit=crop&w=600&q=80'),
(20, 2, 'HEPA Air Purifier for Home', 'Filters 99.97% dust, pollen, smoke, and odors in rooms up to 500 sq ft', 89.99, 22, 'Home & Kitchen', 'https://images.unsplash.com/photo-1585771724684-38269d6639fd?auto=format&fit=crop&w=600&q=80'),
(21, 2, 'Digital Air Fryer 5.8 Qt', '8-in-1 touchscreen air fryer with rapid hot air circulation', 74.50, 5, 'Home & Kitchen', 'https://images.unsplash.com/photo-1584269600464-37b1b58a9fe7?auto=format&fit=crop&w=600&q=80'),

-- Category 5: Footwear (IDs 22-26)
(22, 3, 'Ergonomic Running Shoes', 'Lightweight mesh sneakers engineered for maximum comfort and cushioning', 79.99, 40, 'Footwear', 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?auto=format&fit=crop&w=600&q=80'),
(23, 3, 'Classic Leather Oxford Shoes', 'Handcrafted genuine leather formal shoes with anti-slip rubber sole', 110.00, 25, 'Footwear', 'https://images.unsplash.com/photo-1614252235316-8c857d38b5f4?auto=format&fit=crop&w=600&q=80'),
(24, 3, 'High-Top Canvas Sneakers', 'Retro casual high-top canvas sneakers with durable rubber toe cap', 49.99, 50, 'Footwear', 'https://images.unsplash.com/photo-1525966222134-fcfa99b8ae77?auto=format&fit=crop&w=600&q=80'),
(25, 3, 'Waterproof Hiking Boots', 'All-terrain suede hiking boots with breathable waterproof membrane', 95.00, 18, 'Footwear', 'https://images.unsplash.com/photo-1520639888713-7851133b1ed0?auto=format&fit=crop&w=600&q=80'),
(26, 3, 'Comfort Cushion Sandals', 'Ergonomic leather strap sandals with contoured arch support footbed', 34.99, 35, 'Footwear', 'https://images.unsplash.com/photo-1603808033192-082d6919d3e1?auto=format&fit=crop&w=600&q=80'),

-- Category 6: Fashion & Apparel (IDs 27-31)
(27, 3, 'Classic Lambskin Leather Jacket', '100% Genuine lambskin leather jacket with premium quilted lining', 149.00, 25, 'Fashion', 'https://images.unsplash.com/photo-1551028719-00167b16eac5?auto=format&fit=crop&w=600&q=80'),
(28, 3, 'Designer Slim Fit Jeans', 'Premium stretch cotton slim-fit denim jeans in dark indigo wash', 59.50, 45, 'Fashion', 'https://images.unsplash.com/photo-1541099649105-f69ad21f3246?auto=format&fit=crop&w=600&q=80'),
(29, 3, 'Minimalist Waterproof Backpack', 'Durable water-resistant laptop backpack with USB charging port', 45.00, 80, 'Fashion', 'https://images.unsplash.com/photo-1553062407-98eeb64c6a62?auto=format&fit=crop&w=600&q=80'),
(30, 3, 'Organic Cotton Fleece Hoodie', 'Heavyweight 100% organic cotton pullover hoodie with kangaroo pocket', 49.99, 60, 'Fashion', 'https://images.unsplash.com/photo-1556905055-8f358a7a47b2?auto=format&fit=crop&w=600&q=80'),
(31, 3, 'Classic Chronograph Analog Watch', 'Stainless steel quartz watch with genuine leather strap and date display', 85.00, 1, 'Fashion', 'https://images.unsplash.com/photo-1524805444758-089113d48a6d?auto=format&fit=crop&w=600&q=80'),

-- Category 7: Gaming (IDs 32-36)
(32, 2, 'Mechanical RGB Gaming Keyboard', 'Tactile mechanical switches with customizable RGB backlighting', 69.99, 60, 'Gaming', 'https://images.unsplash.com/photo-1587829741301-dc798b83add3?auto=format&fit=crop&w=600&q=80'),
(33, 2, 'Wireless Ergonomic Gaming Mouse', '26K DPI optical sensor with 68g lightweight design and PTFE feet', 54.99, 40, 'Gaming', 'https://images.unsplash.com/photo-1615663245857-ac93bb7c39e7?auto=format&fit=crop&w=600&q=80'),
(34, 2, 'Ergonomic Gaming Chair', 'High-density foam gaming chair with lumbar support and 180-degree recline', 189.00, 8, 'Gaming', 'https://images.unsplash.com/photo-1598550476439-6847785fcea6?auto=format&fit=crop&w=600&q=80'),
(35, 2, 'Wireless Gamepad Controller', 'Dual vibration motors with zero-latency 2.4GHz wireless connection', 39.99, 50, 'Gaming', 'https://images.unsplash.com/photo-1600080972464-8e5f35f63d08?auto=format&fit=crop&w=600&q=80'),
(36, 2, 'RGB Extended Mouse Pad', 'Extra large desk mat with 14 RGB lighting modes and anti-slip rubber base', 24.50, 75, 'Gaming', 'https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?auto=format&fit=crop&w=600&q=80'),

-- Category 8: Books & Stationery (IDs 37-41)
(37, 2, 'Clean Code: Handbook of Software Craftsmanship', 'Essential principles, patterns, and practices of writing clean Java code', 37.50, 40, 'Books', 'https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?auto=format&fit=crop&w=600&q=80'),
(38, 2, 'Design Patterns: Elements of Reusable Software', 'Classic Gang of Four reference guide on software design patterns', 49.99, 30, 'Books', 'https://images.unsplash.com/photo-1512820790803-83ca734da794?auto=format&fit=crop&w=600&q=80'),
(39, 3, 'Atomic Habits by James Clear', 'Proven way to build good habits and break bad ones through micro changes', 18.99, 85, 'Books', 'https://images.unsplash.com/photo-1543002588-bfa74002ed7e?auto=format&fit=crop&w=600&q=80'),
(40, 3, 'The Pragmatic Programmer 20th Anniversary', 'Timeless wisdom and classic career advice for software developers', 42.00, 50, 'Books', 'https://images.unsplash.com/photo-1497633762265-9d179a990aa6?auto=format&fit=crop&w=600&q=80'),
(41, 3, 'Executive Leather Journal & Fountain Pen Set', 'Refillable A5 hardcover leather notebook with iridium nib fountain pen', 29.99, 60, 'Books', 'https://images.unsplash.com/photo-1583485088034-697b5bc54ccd?auto=format&fit=crop&w=600&q=80'),

-- Category 9: Beauty & Personal Care (IDs 42-46)
(42, 3, 'Organic Hydrating Face Serum 50ml', 'Hyaluronic acid and vitamin C anti-aging face oil serum', 24.50, 110, 'Beauty', 'https://images.unsplash.com/photo-1620916566398-39f1143ab7be?auto=format&fit=crop&w=600&q=80'),
(43, 3, 'Electric Rotary Shaver 4-in-1', 'IPX7 3D floating head electric razor with beard trimmer attachment', 45.00, 35, 'Beauty', 'https://images.unsplash.com/photo-1621607512214-68297480165e?auto=format&fit=crop&w=600&q=80'),
(44, 3, 'Ionic Hair Dryer 1800W', 'Professional negative ion fast drying blow dryer with diffuser', 39.99, 40, 'Beauty', 'https://images.unsplash.com/photo-1522337360788-8b13dee7a37e?auto=format&fit=crop&w=600&q=80'),
(45, 3, 'Deep Tissue Massage Gun', '20-speed brushless motor percussion muscle massager with 6 heads', 59.99, 25, 'Beauty', 'https://images.unsplash.com/photo-1544367567-0f2fcb009e0b?auto=format&fit=crop&w=600&q=80'),
(46, 3, 'Sonic Electric Toothbrush', '40,000 VPM ultrasonic toothbrush with 5 modes and 4 brush heads', 29.50, 80, 'Beauty', 'https://images.unsplash.com/photo-1559591937-e68fb3305e43?auto=format&fit=crop&w=600&q=80'),

-- Category 10: Sports & Fitness (IDs 47-52)
(47, 3, 'Pro Non-Slip Yoga & Exercise Mat', 'Eco-friendly 6mm extra-thick high-density TPE workout mat', 29.99, 90, 'Sports', 'https://images.unsplash.com/photo-1601925260368-ae2f83cf8b7f?auto=format&fit=crop&w=600&q=80'),
(48, 3, 'Adjustable Rubber Dumbbell Set 20kg', 'Versatile cast iron dumbbell pair with quick-adjust weight plates', 65.00, 20, 'Sports', 'https://images.unsplash.com/photo-1584735935682-2f2b69dff9d2?auto=format&fit=crop&w=600&q=80'),
(49, 3, 'Insulated Stainless Steel Water Bottle 1L', 'Double-wall vacuum insulated flask keeps drinks cold for 24 hours', 19.99, 120, 'Sports', 'https://images.unsplash.com/photo-1602143407151-7111542de6e8?auto=format&fit=crop&w=600&q=80'),
(50, 3, 'Heavy Duty Resistance Loop Bands', 'Set of 5 natural latex resistance exercise bands for home fitness', 14.99, 150, 'Sports', 'https://images.unsplash.com/photo-1598289431512-b97b0917affc?auto=format&fit=crop&w=600&q=80'),
(51, 3, 'Speed Jump Rope with Ball Bearings', 'Tangle-free steel cable speed jump rope with anti-slip aluminum handles', 12.50, 95, 'Sports', 'https://images.unsplash.com/photo-1518611012118-696072aa579a?auto=format&fit=crop&w=600&q=80'),
(52, 3, 'Aerobic Fitness Step Platform', '4-inch to 6-inch adjustable stepper platform for cardio workouts', 32.00, 18, 'Sports', 'https://images.unsplash.com/photo-1517838277536-f5f99be501cd?auto=format&fit=crop&w=600&q=80');

MERGE INTO orders (id, buyer_id, status, total_amount) KEY(id) VALUES
(1, 4, 'DELIVERED', 199.99);

MERGE INTO order_items (id, order_id, product_id, quantity, unit_price) KEY(id) VALUES
(1, 1, 12, 1, 199.99);

MERGE INTO reviews (id, product_id, user_id, rating, comment) KEY(id) VALUES
(1, 12, 4, 5, 'Amazing noise cancellation and battery life!'),
(2, 7, 4, 4, 'Great fitness watch, accurate steps and long battery.'),
(3, 22, 4, 5, 'Super comfortable shoes for long runs!'),
(4, 37, 4, 5, 'Must-read book for every software engineer!');
