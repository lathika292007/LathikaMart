<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Seller Sales & Listing Dashboard — LathikaMart</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <script>window.contextPath = '${pageContext.request.contextPath}';</script>
</head>
<body>
    <nav class="navbar">
        <a href="${pageContext.request.contextPath}/" class="brand-title">✨ LathikaMart Seller Hub</a>
        <div class="nav-links">
            <a href="${pageContext.request.contextPath}/" class="nav-link">Marketplace</a>
            <a href="${pageContext.request.contextPath}/seller-dashboard.jsp" class="nav-link active">Seller Dashboard</a>
            <a href="${pageContext.request.contextPath}/orders.jsp" class="nav-link">Incoming Orders</a>
        </div>
    </nav>

    <main class="container">
        <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:1.5rem;">
            <div>
                <h2 style="font-size: 1.75rem; font-weight: 800;">Seller Analytics & Listing Manager</h2>
                <p class="text-muted" style="font-size:0.9rem;">Manage product catalog, track sales performance, and process customer orders.</p>
            </div>
            <button onclick="showProductModal()" class="btn btn-primary">+ Create New Product Listing</button>
        </div>

        <!-- Requirement O3: Seller Sales Dashboard Metrics -->
        <div style="display:grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap:1rem; margin-bottom:2rem;">
            <div class="glass-card">
                <div class="text-muted" style="font-size:0.85rem; font-weight:600;">Total Sales Revenue</div>
                <div id="stat-revenue" style="font-size:2rem; font-weight:800; color:#38bdf8;">$0.00</div>
            </div>
            <div class="glass-card">
                <div class="text-muted" style="font-size:0.85rem; font-weight:600;">Total Orders</div>
                <div id="stat-orders" style="font-size:2rem; font-weight:800; color:#818cf8;">0</div>
            </div>
            <div class="glass-card">
                <div class="text-muted" style="font-size:0.85rem; font-weight:600;">Units Sold</div>
                <div id="stat-items" style="font-size:2rem; font-weight:800; color:#c084fc;">0</div>
            </div>
            <div class="glass-card">
                <div class="text-muted" style="font-size:0.85rem; font-weight:600;">Active Listings</div>
                <div id="stat-products" style="font-size:2rem; font-weight:800; color:#4ade80;">0</div>
            </div>
            <div class="glass-card">
                <div class="text-muted" style="font-size:0.85rem; font-weight:600;">Low Stock Alerts (<=5)</div>
                <div id="stat-lowstock" style="font-size:2rem; font-weight:800; color:#f87171;">0</div>
            </div>
        </div>

        <!-- Product Listings Management Table -->
        <div class="glass-card">
            <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:1rem;">
                <h3 style="font-size:1.25rem; font-weight:700;">My Product Listings</h3>
                <input type="text" id="filter-seller-products" class="form-input" style="width:240px; padding:0.4rem 0.8rem;" 
                       placeholder="Filter listings..." onkeyup="filterTable()">
            </div>
            <div id="seller-products-table">
                <div class="text-center p-8"><p>Loading your products...</p></div>
            </div>
        </div>
    </main>

    <!-- Create/Edit Product Modal -->
    <div id="product-modal" style="display:none; position:fixed; inset:0; background:rgba(0,0,0,0.8); backdrop-filter:blur(8px); z-index:2000; align-items:center; justify-content:center;">
        <div class="glass-card" style="width:100%; max-width:520px; max-height:90vh; overflow-y:auto;">
            <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:1rem;">
                <h3 id="modal-title" style="font-size:1.5rem; font-weight:800;">Add Product Listing</h3>
                <button onclick="hideProductModal()" style="background:none; border:none; color:white; font-size:1.5rem; cursor:pointer;">&times;</button>
            </div>
            
            <form id="product-form">
                <input type="hidden" id="prod-id">
                
                <div class="form-group">
                    <label class="form-label" for="prod-name">Product Name *</label>
                    <input type="text" id="prod-name" class="form-input" required placeholder="e.g. Wireless ANC Headphones">
                </div>

                <div class="form-group">
                    <label class="form-label" for="prod-category">Category *</label>
                    <select id="prod-category" class="form-select" required>
                        <option value="Smartphones & ACs">📱 Smartphones & ACs</option>
                        <option value="Men's Clothing">👔 Men's Clothing (Shirt, Pant, T-Shirt, Jeans)</option>
                        <option value="Women's Fashion">👗 Women's Fashion (Kurti, Saree, Crop Top, Kurta Set)</option>
                        <option value="Cosmetics & Beauty">💄 Cosmetics & Beauty (Makeup Products)</option>
                        <option value="Snacks & Foods">🍿 Snacks & Foods (Murukku, Sweets, Nuts)</option>
                    </select>
                </div>

                <div style="display:flex; gap:1rem;">
                    <div class="form-group" style="flex:1;">
                        <label class="form-label" for="prod-price">Price ($) *</label>
                        <input type="number" step="0.01" min="0.01" id="prod-price" class="form-input" required placeholder="199.99">
                    </div>
                    <div class="form-group" style="flex:1;">
                        <label class="form-label" for="prod-stock">Stock Quantity *</label>
                        <input type="number" min="0" id="prod-stock" class="form-input" required placeholder="50">
                    </div>
                </div>

                <div class="form-group">
                    <label class="form-label" for="prod-image">Image URL (Optional)</label>
                    <input type="text" id="prod-image" class="form-input" placeholder="https://images.unsplash.com/... (Leaves blank for default image)">
                </div>

                <div class="form-group">
                    <label class="form-label" for="prod-desc">Description</label>
                    <textarea id="prod-desc" class="form-textarea" rows="3" placeholder="Item highlights, specs, and details..."></textarea>
                </div>

                <div style="display:flex; justify-content:flex-end; gap:0.5rem; margin-top:1.5rem;">
                    <button type="button" class="btn btn-secondary" onclick="hideProductModal()">Cancel</button>
                    <button type="submit" class="btn btn-primary">Save Product Listing</button>
                </div>
            </form>
        </div>
    </div>

    <script>
        window.currentUserId = ${not empty sessionScope.currentUser ? sessionScope.currentUser.id : 'null'};
    </script>
    <script src="${pageContext.request.contextPath}/js/seller.js"></script>
</body>
</html>
