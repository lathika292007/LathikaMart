-- LathikaMart Comprehensive Catalog Migration V2__seed_data.sql (65+ Products)

MERGE INTO users (id, name, email, password_hash, role) KEY(id) VALUES 
(1, 'Admin User', 'admin@lathikamart.com', '$2a$10$5Qv70cHNt38aSfAw65NfE.hcXoChUbJdNDI7Q1XA/Px.2r8aToKzi', 'ADMIN'),
(2, 'TechStore Seller', 'seller.tech@lathikamart.com', '$2a$10$5Qv70cHNt38aSfAw65NfE.hcXoChUbJdNDI7Q1XA/Px.2r8aToKzi', 'SELLER'),
(3, 'FashionTrend Seller', 'seller.fashion@lathikamart.com', '$2a$10$5Qv70cHNt38aSfAw65NfE.hcXoChUbJdNDI7Q1XA/Px.2r8aToKzi', 'SELLER'),
(4, 'John Buyer', 'john.buyer@gmail.com', '$2a$10$5Qv70cHNt38aSfAw65NfE.hcXoChUbJdNDI7Q1XA/Px.2r8aToKzi', 'BUYER');

MERGE INTO products (id, seller_id, name, description, price, stock_qty, category, image_url) KEY(id) VALUES
-- 1. PHONES & AIR CONDITIONERS (IDs 1-10)
(1, 2, 'iPhone 15 Pro Max 256GB', 'Titanium design, A17 Pro chip, 48MP main camera system with 5x Telephoto', 1199.00, 15, 'Smartphones & ACs', 'https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?auto=format&fit=crop&w=600&q=80'),
(2, 2, 'Samsung Galaxy S24 Ultra 5G', '200MP camera, Snapdragon 8 Gen 3, integrated S-Pen and AI photo editing', 1299.00, 20, 'Smartphones & ACs', 'https://images.unsplash.com/photo-1610945265064-0e34e5519bbf?auto=format&fit=crop&w=600&q=80'),
(3, 2, 'OnePlus 12 5G (16GB RAM, 512GB)', 'Hasselblad camera for mobile, 100W SUPERVOOC charging, 2K 120Hz display', 799.00, 25, 'Smartphones & ACs', 'https://images.unsplash.com/photo-1598327105666-5b89351aff97?auto=format&fit=crop&w=600&q=80'),
(4, 2, 'Google Pixel 8 Pro 128GB', 'Google Tensor G3 chip, advanced AI camera, best-in-class night sight photography', 899.00, 12, 'Smartphones & ACs', 'https://images.unsplash.com/photo-1565849904461-04a58ad377e0?auto=format&fit=crop&w=600&q=80'),
(5, 2, 'Redmi Note 13 Pro+ 5G', '200MP OIS camera, 120W HyperCharge, curved AMOLED 1.5K 120Hz display', 349.99, 45, 'Smartphones & ACs', 'https://images.unsplash.com/photo-1546054454-aa26e2b734c7?auto=format&fit=crop&w=600&q=80'),
(6, 2, 'Voltas 1.5 Ton 5 Star Inverter Split AC', '100% copper condenser, 4-in-1 adjustable cooling mode, anti-dust filter', 499.00, 10, 'Smartphones & ACs', 'https://images.unsplash.com/photo-1621905251189-08b45d6a269e?auto=format&fit=crop&w=600&q=80'),
(7, 2, 'Daikin 1.5 Ton 3 Star Inverter Split AC', 'PM 2.5 filter, ECONO mode, 3D airflow for quick uniform cooling', 469.00, 8, 'Smartphones & ACs', 'https://images.unsplash.com/photo-1621905252507-b35492cc74b4?auto=format&fit=crop&w=600&q=80'),
(8, 2, 'LG 1.5 Ton 5 Star Dual Inverter Split AC', 'AI Convertible 6-in-1 cooling, HD filter with anti-virus protection', 529.00, 14, 'Smartphones & ACs', 'https://images.unsplash.com/photo-1614633833026-06203577d64c?auto=format&fit=crop&w=600&q=80'),
(9, 2, 'Blue Star 1 Ton 3 Star Fixed Speed Window AC', 'Turbo cooling, self-diagnosis, 100% copper condenser for small rooms', 299.00, 6, 'Smartphones & ACs', 'https://images.unsplash.com/photo-1581092160607-ee22621dd758?auto=format&fit=crop&w=600&q=80'),
(10, 2, 'Lloyd 1.5 Ton 3 Star Portable Air Conditioner', 'Feather touch control panel, 360-degree casters for easy mobility', 389.00, 3, 'Smartphones & ACs', 'https://images.unsplash.com/photo-1631545856760-4886b510c4d5?auto=format&fit=crop&w=600&q=80'),

-- 2. MEN'S APPAREL (SHIRTS, PANTS, T-SHIRTS, JEANS) (IDs 11-22)
(11, 3, 'Men Slim Fit Pure Linen Casual Shirt', '100% breathable pure linen casual shirt with spread collar in ocean blue', 39.99, 50, "Men's Clothing", 'https://images.unsplash.com/photo-1596755094514-f87e34085b2c?auto=format&fit=crop&w=600&q=80'),
(12, 3, 'Men Formal Cotton Button-Down Shirt', 'Wrinkle-free pure cotton formal shirt for office and corporate wear', 34.50, 60, "Men's Clothing", 'https://images.unsplash.com/photo-1602810318383-e386cc2a3ccf?auto=format&fit=crop&w=600&q=80'),
(13, 3, 'Men Washed Denim Casual Shirt', 'Classic indigo washed denim shirt with double chest button flap pockets', 42.00, 35, "Men's Clothing", 'https://images.unsplash.com/photo-1576995853123-5a10305d93c0?auto=format&fit=crop&w=600&q=80'),
(14, 3, 'Men Slim Fit Stretch Chino Pants', 'Versatile stretch cotton chino trousers with side pockets in beige', 45.00, 40, "Men's Clothing", 'https://images.unsplash.com/photo-1473966968600-fa801b869a1a?auto=format&fit=crop&w=600&q=80'),
(15, 3, 'Men Formal Flat-Front Trouser Pants', 'Tailored fit wrinkle-resistant formal dress trousers in charcoal grey', 38.99, 30, "Men's Clothing", 'https://images.unsplash.com/photo-1506629082955-511b1aa562c8?auto=format&fit=crop&w=600&q=80'),
(16, 3, 'Men Tactical Multi-Pocket Cargo Pants', 'Durable cotton twill relaxed fit cargo pants with utility pockets', 49.50, 25, "Men's Clothing", 'https://images.unsplash.com/photo-1624378439575-d8705ad7ae80?auto=format&fit=crop&w=600&q=80'),
(17, 3, 'Men Oversized Graphic Crewneck T-Shirt', '100% combed cotton heavy-gauge streetwear graphic print t-shirt', 24.99, 80, "Men's Clothing", 'https://images.unsplash.com/photo-1521572267360-ee0c2909d518?auto=format&fit=crop&w=600&q=80'),
(18, 3, 'Men Classic Pique Cotton Polo T-Shirt', 'Ribbed collar and sleeve hems classic fit polo t-shirt in navy blue', 29.50, 70, "Men's Clothing", 'https://images.unsplash.com/photo-1581655353564-df123a1eb820?auto=format&fit=crop&w=600&q=80'),
(19, 3, 'Men Pack of 3 Essential V-Neck T-Shirts', 'Ultra-soft combed cotton everyday V-neck t-shirts in Black, White, Grey', 35.00, 90, "Men's Clothing", 'https://images.unsplash.com/photo-1583743814966-8936f5b7be1a?auto=format&fit=crop&w=600&q=80'),
(20, 3, 'Men Slim Fit Dark Denim Jeans', 'Premium stretch denim slim-fit jeans with 5-pocket styling', 54.99, 45, "Men's Clothing", 'https://images.unsplash.com/photo-1541099649105-f69ad21f3246?auto=format&fit=crop&w=600&q=80'),
(21, 3, 'Men Tapered Fit Light Wash Denim Jeans', 'Faded distressed light blue denim jeans with comfort stretch waist', 49.99, 38, "Men's Clothing", 'https://images.unsplash.com/photo-1582552938357-32b906df40cb?auto=format&fit=crop&w=600&q=80'),
(22, 3, 'Men Relaxed Straight Fit Jeans', 'Classic 100% cotton heavy denim straight fit indigo blue jeans', 44.50, 50, "Men's Clothing", 'https://images.unsplash.com/photo-1604176354204-9268737828e4?auto=format&fit=crop&w=600&q=80'),

-- 3. WOMEN'S FASHION (KURTIS, SAREES, CROP TOPS, KURTA SETS, WOMEN'S JEANS) (IDs 23-34)
(23, 3, 'Women Anarkali Flared Printed Kurti', 'Flowy rayon Anarkali kurti with intricate floral embroidery and foil print', 39.99, 40, "Women's Fashion", 'https://images.unsplash.com/photo-1610030469983-98e550d6193c?auto=format&fit=crop&w=600&q=80'),
(24, 3, 'Women Straight Cotton Block Print Kurti', '100% pure cotton daily wear straight kurti with Mandarin neck', 27.50, 65, "Women's Fashion", 'https://images.unsplash.com/photo-1583391733956-3750e0ff4e8b?auto=format&fit=crop&w=600&q=80'),
(25, 3, 'Women Rayon Chikankari Embroidery Kurti', 'Handcrafted lucknowi chikankari embroidery A-line tunic kurti', 34.00, 45, "Women's Fashion", 'https://images.unsplash.com/photo-1617627143750-d86bc21e42bb?auto=format&fit=crop&w=600&q=80'),
(26, 3, 'Kanjeevaram Soft Silk Woven Saree', 'Traditional Kanchipuram zari border soft silk saree with unstitched blouse piece', 99.00, 20, "Women's Fashion", 'https://images.unsplash.com/photo-1610030469983-98e550d6193c?auto=format&fit=crop&w=600&q=80'),
(27, 3, 'Handloom Chanderi Cotton Silk Saree', 'Lightweight woven golden motif chanderi saree for festive celebrations', 65.00, 25, "Women's Fashion", 'https://images.unsplash.com/photo-1617627143750-d86bc21e42bb?auto=format&fit=crop&w=600&q=80'),
(28, 3, 'Designer Georgette Floral Printed Saree', 'Elegant drape georgette saree with scalloped lace border and designer blouse', 49.99, 30, "Women's Fashion", 'https://images.unsplash.com/photo-1583391733956-3750e0ff4e8b?auto=format&fit=crop&w=600&q=80'),
(29, 3, 'Women Ribbed Knit Fitted Crop Top', 'Stretchable cotton ribbed short sleeve crop top in pastel peach', 19.99, 85, "Women's Fashion", 'https://images.unsplash.com/photo-1503342217505-b0a15ec3261c?auto=format&fit=crop&w=600&q=80'),
(30, 3, 'Women Floral Summer Short Crop Top', 'Chiffon puff sleeve square neck floral print casual summer crop top', 22.50, 75, "Women's Fashion", 'https://images.unsplash.com/photo-1515886657613-9f3515b0c78f?auto=format&fit=crop&w=600&q=80'),
(31, 3, 'Women High-Waisted Mom Fit Jeans', 'Vintage blue washed 100% cotton high-rise relaxed mom fit denim jeans', 49.99, 50, "Women's Fashion", 'https://images.unsplash.com/photo-1541099649105-f69ad21f3246?auto=format&fit=crop&w=600&q=80'),
(32, 3, 'Women Wide Leg High Rise Jeans', 'Trendy wide leg flared denim jeans with ankle length in light wash blue', 52.00, 35, "Women's Fashion", 'https://images.unsplash.com/photo-1582552938357-32b906df40cb?auto=format&fit=crop&w=600&q=80'),
(33, 3, 'Women Embroidered Straight Kurta Pants Set', 'Chanderi silk embroidered straight kurta with trousers and organza dupatta', 75.00, 18, "Women's Fashion", 'https://images.unsplash.com/photo-1610030469983-98e550d6193c?auto=format&fit=crop&w=600&q=80'),
(34, 3, 'Women Festive Printed Kurta Palazzo Dupatta Set', '3-piece rayon flared kurta with matching palazzo pants and printed chiffon dupatta', 59.99, 22, "Women's Fashion", 'https://images.unsplash.com/photo-1617627143750-d86bc21e42bb?auto=format&fit=crop&w=600&q=80'),

-- 4. COSMETICS & MAKEUP (10 PRODUCTS) (IDs 35-44)
(35, 3, 'Matte Liquid Lipstick Longwear 5ml', 'Smudge-proof 12-hour transfer-proof matte liquid lipstick in Ruby Red', 14.99, 120, 'Cosmetics & Beauty', 'https://images.unsplash.com/photo-1586495777744-4413f21062fa?auto=format&fit=crop&w=600&q=80'),
(36, 3, 'Waterproof Volume Express Mascara', 'Smear-proof instant lash lengthening and volumizing black mascara', 12.50, 100, 'Cosmetics & Beauty', 'https://images.unsplash.com/photo-1591360236480-4ed861025fa1?auto=format&fit=crop&w=600&q=80'),
(37, 3, 'Full Coverage Liquid Foundation 30ml', 'Hydrating oil-free natural matte liquid foundation with SPF 15', 24.99, 80, 'Cosmetics & Beauty', 'https://images.unsplash.com/photo-1631729371254-42c2892f0e6e?auto=format&fit=crop&w=600&q=80'),
(38, 3, 'Radiant Pressed Compact Powder', 'Oil-control weightless setting compact powder with mirror applicator', 16.00, 95, 'Cosmetics & Beauty', 'https://images.unsplash.com/photo-1512496015851-a90fb38ba796?auto=format&fit=crop&w=600&q=80'),
(39, 3, 'Precision Waterproof Eyeliner Pen', 'Intense black quick-dry felt tip liquid eyeliner for wing lines', 9.99, 150, 'Cosmetics & Beauty', 'https://images.unsplash.com/photo-1620916566398-39f1143ab7be?auto=format&fit=crop&w=600&q=80'),
(40, 3, '12-Color Nude & Shimmer Eyeshadow Palette', 'Highly pigmented blendable matte and metallic glitter eyeshadow palette', 28.50, 60, 'Cosmetics & Beauty', 'https://images.unsplash.com/photo-1512496015851-a90fb38ba796?auto=format&fit=crop&w=600&q=80'),
(41, 3, 'Soft Cheek Blush & Glow Highlighter Duo', 'Velvety smooth powder blush with luminous pearl illuminator highlighter', 18.00, 70, 'Cosmetics & Beauty', 'https://images.unsplash.com/photo-1522337360788-8b13dee7a37e?auto=format&fit=crop&w=600&q=80'),
(42, 3, 'Gel Nail Polish Set (Pack of 4 shades)', 'High-gloss long-lasting quick dry salon finish gel nail enamel kit', 19.99, 85, 'Cosmetics & Beauty', 'https://images.unsplash.com/photo-1604654894610-df63bc536371?auto=format&fit=crop&w=600&q=80'),
(43, 3, 'Hydrating Makeup Setting Spray 100ml', 'Dewy finish long-lasting weightless makeup fixer setting mist spray', 15.50, 110, 'Cosmetics & Beauty', 'https://images.unsplash.com/photo-1608248597349-f5127d142d59?auto=format&fit=crop&w=600&q=80'),
(44, 3, 'Micellar Cleansing Makeup Remover Water 400ml', 'Gentle non-greasy facial cleanser and waterproof makeup remover', 11.99, 130, 'Cosmetics & Beauty', 'https://images.unsplash.com/photo-1556228720-195a672e8a03?auto=format&fit=crop&w=600&q=80'),

-- 5. SNACKS & GOURMET FOODS (IDs 45-55)
(45, 2, 'South Indian Special Murukku & Mixture Pack 500g', 'Authentic crunchy handmade butter murukku and spicy garlic mixture box', 8.99, 200, 'Snacks & Foods', 'https://images.unsplash.com/photo-1621996346565-e3d5d6281290?auto=format&fit=crop&w=600&q=80'),
(46, 2, 'Roasted & Salted Premium Cashew & Almonds 400g', 'Crunchy jumbo roasted cashews and California almonds healthy snack jar', 19.50, 120, 'Snacks & Foods', 'https://images.unsplash.com/photo-1509440159596-0249088772ff?auto=format&fit=crop&w=600&q=80'),
(47, 2, '70% Cocoa Belgian Dark Chocolate Bars (Pack of 3)', 'Rich smooth bittersweet artisan dark chocolate bars with sea salt', 12.99, 150, 'Snacks & Foods', 'https://images.unsplash.com/photo-1511381939415-e44015466834?auto=format&fit=crop&w=600&q=80'),
(48, 2, 'Gourmet Potato Chips Combo Pack (6 Flavors)', 'Crispy kettle cooked potato chips in Cream & Onion, Peri Peri, Salted', 9.50, 180, 'Snacks & Foods', 'https://images.unsplash.com/photo-1566478989037-eec170784d0b?auto=format&fit=crop&w=600&q=80'),
(49, 2, 'Organic Kashmiri Green Tea & Jasmine Box', 'Pure whole leaf green tea bags rich in antioxidants for morning freshness', 14.00, 90, 'Snacks & Foods', 'https://images.unsplash.com/photo-1576092768241-dec231879fc3?auto=format&fit=crop&w=600&q=80'),
(50, 2, 'Movie Theater Butter Salted Popcorn 300g', 'Instant microwave crunchy jumbo kernels butter salted gourmet popcorn', 6.99, 250, 'Snacks & Foods', 'https://images.unsplash.com/photo-1578849278619-e73505e9610f?auto=format&fit=crop&w=600&q=80'),
(51, 2, 'Roasted Peri Peri Makhana Foxnuts 200g', 'Low-calorie crunchy roasted lotus seeds seasoned with spicy peri peri', 11.50, 140, 'Snacks & Foods', 'https://images.unsplash.com/photo-1599490659213-e2b9527bd087?auto=format&fit=crop&w=600&q=80'),
(52, 2, 'Crunchy Peanut Butter Creamy Spread 1kg', '100% roasted peanuts high protein zero trans-fat peanut butter jar', 13.99, 80, 'Snacks & Foods', 'https://images.unsplash.com/photo-1584308666744-24d5c474f2ae?auto=format&fit=crop&w=600&q=80'),
(53, 2, 'Assorted Indian Mithai Sweet Box 500g', 'Fresh Kaju Katli, Besan Ladoo, and Milk Peda festive sweet gift hamper', 22.00, 60, 'Snacks & Foods', 'https://images.unsplash.com/photo-1599490659213-e2b9527bd087?auto=format&fit=crop&w=600&q=80'),
(54, 2, 'Cold Pressed Sparkling Mango Drink 6-Pack', 'Real Alphonso mango pulp sparkling refreshment cans with natural fruit juice', 10.99, 110, 'Snacks & Foods', 'https://images.unsplash.com/photo-1622483767028-3f66f32aef97?auto=format&fit=crop&w=600&q=80'),
(55, 2, 'Multigrain Digestive Biscuit Family Pack 750g', 'High-fiber oats and wheat digestive biscuits ideal for evening tea', 7.50, 160, 'Snacks & Foods', 'https://images.unsplash.com/photo-1558961363-fa8fdf82db35?auto=format&fit=crop&w=600&q=80');

MERGE INTO orders (id, buyer_id, status, total_amount) KEY(id) VALUES
(1, 4, 'DELIVERED', 1199.00);

MERGE INTO order_items (id, order_id, product_id, quantity, unit_price) KEY(id) VALUES
(1, 1, 1, 1, 1199.00);

MERGE INTO reviews (id, product_id, user_id, rating, comment) KEY(id) VALUES
(1, 1, 4, 5, 'Super fast iPhone 15 Pro Max, camera is incredible!'),
(2, 6, 4, 5, 'Voltas AC cools the room in under 5 minutes.'),
(3, 23, 4, 5, 'Beautiful Anarkali kurti, perfect fit and soft fabric.'),
(4, 35, 4, 5, 'Lipstick color stays all day without drying lips!');
