<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Moderation & Governance — LathikaMart</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <script>window.contextPath = '${pageContext.request.contextPath}';</script>
</head>
<body>
    <nav class="navbar">
        <a href="${pageContext.request.contextPath}/" class="brand-title">✨ LathikaMart Admin</a>
        <div class="nav-links">
            <a href="${pageContext.request.contextPath}/" class="nav-link">Marketplace</a>
            <a href="${pageContext.request.contextPath}/admin.jsp" class="nav-link active">Admin Panel</a>
        </div>
    </nav>

    <main class="container" style="max-width:1200px; margin: 2rem auto; padding: 0 1rem;">
        <h2 style="font-size: 1.75rem; font-weight: 800; margin-bottom: 1.5rem;">🛡️ System Administration & Governance</h2>

        <!-- Stats Overview -->
        <div style="display:grid; grid-template-columns: repeat(auto-fit, minmax(180px, 1fr)); gap:1rem; margin-bottom:1.5rem;">
            <div class="glass-card">
                <div class="text-muted" style="font-size:0.85rem;">Total Users</div>
                <div id="stat-users" style="font-size:2rem; font-weight:800; color:#38bdf8;">--</div>
            </div>
            <div class="glass-card">
                <div class="text-muted" style="font-size:0.85rem;">Sellers</div>
                <div id="stat-sellers" style="font-size:2rem; font-weight:800; color:#a78bfa;">--</div>
            </div>
            <div class="glass-card">
                <div class="text-muted" style="font-size:0.85rem;">Active Products</div>
                <div id="stat-products" style="font-size:2rem; font-weight:800; color:#4ade80;">--</div>
            </div>
            <div class="glass-card">
                <div class="text-muted" style="font-size:0.85rem;">Total Orders</div>
                <div id="stat-orders" style="font-size:2rem; font-weight:800; color:#fbbf24;">--</div>
            </div>
            <div class="glass-card">
                <div class="text-muted" style="font-size:0.85rem;">Platform Revenue</div>
                <div id="stat-revenue" style="font-size:2rem; font-weight:800; color:#f43f5e;">--</div>
            </div>
        </div>

        <!-- User Role Governance Table -->
        <div class="glass-card" style="margin-bottom:1.5rem;">
            <h3 style="font-size:1.25rem; font-weight:700; margin-bottom:1rem;">👥 User Management & Role Promotion</h3>
            <div id="users-table-container">Loading users...</div>
        </div>

        <!-- Product Listing Moderation Table -->
        <div class="glass-card" style="margin-bottom:1.5rem;">
            <h3 style="font-size:1.25rem; font-weight:700; margin-bottom:1rem;">📦 Product Listing Moderation</h3>
            <div id="products-table-container">Loading listings...</div>
        </div>

        <!-- Governance Audit Logs -->
        <div class="glass-card">
            <h3 style="font-size:1.25rem; font-weight:700; margin-bottom:1rem;">📜 Admin Governance Audit Logs</h3>
            <div id="audit-table-container">Loading audit history...</div>
        </div>
    </main>

    <script src="${pageContext.request.contextPath}/js/admin.js"></script>
</body>
</html>
