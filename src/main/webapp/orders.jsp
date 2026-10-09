<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order History — LathikaMart</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <script>window.contextPath = '${pageContext.request.contextPath}';</script>
</head>
<body>
    <nav class="navbar">
        <a href="${pageContext.request.contextPath}/" class="brand-title">✨ LathikaMart</a>
        <div class="nav-links">
            <a href="${pageContext.request.contextPath}/" class="nav-link">Browse Products</a>
            <a href="${pageContext.request.contextPath}/orders.jsp" class="nav-link active">My Orders</a>
            <a href="${pageContext.request.contextPath}/cart.jsp" class="nav-link">🛒 Cart</a>
        </div>
    </nav>

    <main class="container">
        <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:1.5rem;">
            <h2 style="font-size: 1.75rem; font-weight: 800;">Order History & Status</h2>
            <c:if test="${sessionScope.currentUser.role eq 'SELLER' or sessionScope.currentUser.role eq 'ADMIN'}">
                <div>
                    <button id="mode-buyer" class="btn btn-primary" onclick="setMode('buyer')">My Buyer Orders</button>
                    <button id="mode-seller" class="btn btn-secondary" onclick="setMode('seller')">Incoming Seller Orders</button>
                </div>
            </c:if>
        </div>

        <div id="orders-list">
            <div class="glass-card text-center p-8"><p>Loading order history...</p></div>
        </div>
    </main>

    <script src="${pageContext.request.contextPath}/js/orders.js"></script>
</body>
</html>
