<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>LathikaMart — Multi-Seller E-Commerce Marketplace</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <script>window.contextPath = '${pageContext.request.contextPath}';</script>
</head>
<body>
    <!-- Navbar -->
    <nav class="navbar">
        <a href="${pageContext.request.contextPath}/" class="brand-title">✨ LathikaMart</a>
        
        <div class="nav-links">
            <a href="${pageContext.request.contextPath}/" class="nav-link active">Browse Products</a>
            
            <c:choose>
                <c:when test="${not empty sessionScope.currentUser}">
                    <c:if test="${sessionScope.currentUser.role eq 'SELLER' or sessionScope.currentUser.role eq 'ADMIN'}">
                        <a href="${pageContext.request.contextPath}/seller-dashboard.jsp" class="nav-link">Seller Dashboard</a>
                    </c:if>
                    <c:if test="${sessionScope.currentUser.role eq 'ADMIN'}">
                        <a href="${pageContext.request.contextPath}/admin.jsp" class="nav-link">Admin Panel</a>
                    </c:if>
                    <a href="${pageContext.request.contextPath}/orders.jsp" class="nav-link">My Orders</a>
                    <a href="${pageContext.request.contextPath}/wishlist.jsp" class="nav-link">❤️ Wishlist</a>
                    <a href="${pageContext.request.contextPath}/profile.jsp" class="nav-link">👤 Profile</a>
                    <a href="${pageContext.request.contextPath}/cart.jsp" class="nav-link">🛒 Cart</a>
                    
                    <span style="color:#38bdf8; font-weight:600; font-size:0.9rem;">
                        👤 <c:out value="${sessionScope.currentUser.name}"/> (<c:out value="${sessionScope.currentUser.role}"/>)
                    </span>
                    <button onclick="logout()" class="btn btn-secondary" style="padding:0.4rem 0.8rem; font-size:0.85rem;">Logout</button>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/cart.jsp" class="nav-link">🛒 Cart</a>
                    <a href="${pageContext.request.contextPath}/login.jsp" class="btn btn-secondary">Login</a>
                    <a href="${pageContext.request.contextPath}/register.jsp" class="btn btn-primary">Sign Up</a>
                </c:otherwise>
            </c:choose>
        </div>
    </nav>

    <!-- Main Container -->
    <main class="container">
        <!-- Hero Welcome Banner -->
        <div class="glass-card mb-6" style="background: linear-gradient(135deg, rgba(79,70,229,0.3) 0%, rgba(56,189,248,0.2) 100%); border: 1px solid rgba(99,102,241,0.4); padding: 2.25rem 1.5rem; border-radius: 16px; margin-bottom: 1.5rem; text-align: center;">
            <h1 style="font-size: 2.25rem; font-weight: 800; color: #ffffff; margin-bottom: 0.5rem; letter-spacing: -0.02em;">
                ✨ Welcome to LathikaMart!
            </h1>
            <p style="font-size: 1.1rem; color: #cbd5e1; max-width: 650px; margin: 0 auto 1.25rem auto;">
                India's Premier Multi-Seller E-Commerce Marketplace — Discover 55+ Smartphones, ACs, Kurtis, Sarees, Cosmetics & Snacks!
            </p>
            <div style="display:flex; justify-content:center; gap:1rem; flex-wrap:wrap;">
                <button onclick="document.getElementById('search-input').focus()" class="btn btn-primary" style="padding: 0.6rem 1.4rem;">
                    🛍️ Start Shopping Now
                </button>
                <c:if test="${not empty sessionScope.currentUser}">
                    <a href="${pageContext.request.contextPath}/wishlist.jsp" class="btn btn-secondary" style="padding: 0.6rem 1.4rem; text-decoration:none;">
                        ❤️ My Wishlist
                    </a>
                </c:if>
            </div>
        </div>

        <!-- Search & Advanced Filter Controls -->
        <div class="glass-card mb-6" style="margin-bottom: 1.5rem; padding: 1.25rem;">
            <div style="display:flex; gap:1rem; flex-wrap:wrap; align-items:center; margin-bottom:1rem;">
                <div style="flex:2; min-width:240px;">
                    <label style="font-size:0.8rem; color:#94a3b8; display:block; margin-bottom:0.25rem;">Search Query</label>
                    <input type="text" id="search-input" class="form-input" placeholder="Search products by name or description..." onkeyup="handleSearch()">
                </div>
                
                <div style="flex:1; min-width:160px;">
                    <label style="font-size:0.8rem; color:#94a3b8; display:block; margin-bottom:0.25rem;">Category</label>
                    <select id="category-filter" class="form-select" onchange="handleSearch()">
                        <option value="all">All Categories (55+ Items)</option>
                        <option value="Smartphones & ACs">📱 Phones & ❄️ ACs</option>
                        <option value="Men's Clothing">👔 Men's Shirts, Pants, T-Shirts & Jeans</option>
                        <option value="Women's Fashion">👗 Women's Kurtis, Sarees, Crop Tops & Sets</option>
                        <option value="Cosmetics & Beauty">💄 Cosmetics & 10 Makeup Items</option>
                        <option value="Snacks & Foods">🍿 Snacks, Murukku, Chocolates & Dry Fruits</option>
                    </select>
                </div>

                <div style="flex:1; min-width:160px;">
                    <label style="font-size:0.8rem; color:#94a3b8; display:block; margin-bottom:0.25rem;">Price Range ($)</label>
                    <div style="display:flex; gap:0.5rem; align-items:center;">
                        <input type="number" id="min-price" class="form-input" placeholder="Min" style="padding:0.4rem;" onchange="handleSearch()">
                        <span style="color:#94a3b8;">-</span>
                        <input type="number" id="max-price" class="form-input" placeholder="Max" style="padding:0.4rem;" onchange="handleSearch()">
                    </div>
                </div>

                <div style="flex:1; min-width:160px;">
                    <label style="font-size:0.8rem; color:#94a3b8; display:block; margin-bottom:0.25rem;">Sort By</label>
                    <select id="sort-by" class="form-select" onchange="handleSearch()">
                        <option value="newest">✨ Newest First</option>
                        <option value="price_asc">💵 Price: Low to High</option>
                        <option value="price_desc">💎 Price: High to Low</option>
                    </select>
                </div>
            </div>

            <!-- Quick Category Badges -->
            <div style="display:flex; gap:0.5rem; flex-wrap:wrap; align-items:center; border-top:1px solid rgba(255,255,255,0.08); padding-top:0.75rem;">
                <span style="font-size:0.8rem; color:#94a3b8; margin-right:0.5rem;">Quick Facets:</span>
                <button class="btn btn-secondary" style="padding:0.2rem 0.6rem; font-size:0.75rem;" onclick="setQuickCategory('all')">All</button>
                <button class="btn btn-secondary" style="padding:0.2rem 0.6rem; font-size:0.75rem;" onclick="setQuickCategory('Smartphones & ACs')">📱 Phones & ACs</button>
                <button class="btn btn-secondary" style="padding:0.2rem 0.6rem; font-size:0.75rem;" onclick="setQuickCategory('Men\'s Clothing')">👔 Men's Fashion</button>
                <button class="btn btn-secondary" style="padding:0.2rem 0.6rem; font-size:0.75rem;" onclick="setQuickCategory('Women\'s Fashion')">👗 Women's Ethnic</button>
                <button class="btn btn-secondary" style="padding:0.2rem 0.6rem; font-size:0.75rem;" onclick="setQuickCategory('Cosmetics & Beauty')">💄 Makeup</button>
                <button class="btn btn-secondary" style="padding:0.2rem 0.6rem; font-size:0.75rem;" onclick="setQuickCategory('Snacks & Foods')">🍿 Snacks</button>
            </div>
        </div>

        <!-- Catalog Product Grid -->
        <div id="product-list-container" class="product-grid">
            <!-- Rendered dynamically by app.js -->
        </div>
    </main>

    <!-- AI Chatbot Floating Widget (Only Available for Logged-In Users) -->
    <c:if test="${not empty sessionScope.currentUser}">
        <button id="chat-widget-btn" class="chat-widget-btn" title="Ask AI Assistant">🤖</button>
        
        <div id="chat-panel" class="chat-panel">
            <div class="chat-header">
                <span>✨ LathikaMart AI Assistant</span>
                <button id="close-chat-btn" style="background:none; border:none; color:white; font-size:1.2rem; cursor:pointer;">&times;</button>
            </div>
            <div id="chat-messages" class="chat-messages">
                <div class="chat-msg chat-msg-bot">
                    Hello <c:out value="${sessionScope.currentUser.name}"/>! I am your AI Assistant. Ask me about products, shipping, returns, or coupons!
                </div>
            </div>
            <form id="chat-form" class="chat-input-area">
                <input type="text" id="chat-input" class="chat-input" placeholder="Ask a question..." required maxlength="500">
                <button type="submit" class="btn btn-primary" style="padding:0.4rem 0.8rem;">Send</button>
            </form>
        </div>
    </c:if>

    <!-- Product Reviews Modal -->
    <div id="reviews-modal" class="modal-overlay">
        <div class="modal-container">
            <div class="modal-header">
                <h3 id="modal-product-title" style="font-size:1.2rem; font-weight:700; color:white;">Product Reviews</h3>
                <button onclick="closeReviewsModal()" style="background:none; border:none; color:white; font-size:1.5rem; cursor:pointer;">&times;</button>
            </div>
            <div class="modal-body">
                <!-- Write a Review Form -->
                <div class="glass-card mb-4" style="padding:1rem; margin-bottom:1.25rem;">
                    <h4 style="font-size:0.95rem; font-weight:700; margin-bottom:0.75rem; color:#38bdf8;">✍️ Write a Review</h4>
                    <form id="review-form" onsubmit="submitReview(event)">
                        <input type="hidden" id="review-product-id">
                        <div class="form-group" style="margin-bottom:0.75rem;">
                            <label class="form-label" style="font-size:0.8rem;">Rating (1 to 5 Stars)</label>
                            <select id="review-rating" class="form-select" style="padding:0.4rem;" required>
                                <option value="5">⭐⭐⭐⭐⭐ (5/5 Excellent)</option>
                                <option value="4">⭐⭐⭐⭐ (4/5 Very Good)</option>
                                <option value="3">⭐⭐⭐ (3/5 Good)</option>
                                <option value="2">⭐⭐ (2/5 Fair)</option>
                                <option value="1">⭐ (1/5 Poor)</option>
                            </select>
                        </div>
                        <div class="form-group" style="margin-bottom:0.75rem;">
                            <label class="form-label" style="font-size:0.8rem;">Your Comment</label>
                            <textarea id="review-comment" class="form-textarea" rows="2" placeholder="Write your experience with this product..." required style="padding:0.5rem; font-size:0.85rem;"></textarea>
                        </div>
                        <button type="submit" class="btn btn-primary" style="width:100%; padding:0.5rem;">Submit Review</button>
                    </form>
                </div>

                <h4 style="font-size:0.95rem; font-weight:700; margin-bottom:0.75rem; color:#94a3b8;">💬 Customer Reviews</h4>
                <div id="reviews-list-container">
                    <p style="color:#94a3b8; text-align:center;">Loading reviews...</p>
                </div>
            </div>
        </div>
    </div>

    <!-- Footer -->
    <footer>
        <p>&copy; 2026 LathikaMart E-Commerce Marketplace — Anna University R2025 Specification</p>
    </footer>

    <script src="${pageContext.request.contextPath}/js/app.js"></script>
    <script src="${pageContext.request.contextPath}/js/chat.js"></script>
    <script>
        function handleSearch() {
            const search = document.getElementById('search-input').value;
            const category = document.getElementById('category-filter').value;
            const minPrice = document.getElementById('min-price').value;
            const maxPrice = document.getElementById('max-price').value;
            const sortBy = document.getElementById('sort-by').value;
            loadProducts(search, category, minPrice, maxPrice, sortBy);
        }

        function setQuickCategory(cat) {
            document.getElementById('category-filter').value = cat;
            handleSearch();
        }

        async function logout() {
            await fetch('${pageContext.request.contextPath}/api/v1/auth/logout', { method: 'POST' });
            window.location.reload();
        }
    </script>
</body>
</html>
