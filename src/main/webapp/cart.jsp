<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Shopping Cart — LathikaMart</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <script>window.contextPath = '${pageContext.request.contextPath}';</script>
</head>
<body>
    <nav class="navbar">
        <a href="${pageContext.request.contextPath}/" class="brand-title">✨ LathikaMart</a>
        <div class="nav-links">
            <a href="${pageContext.request.contextPath}/" class="nav-link">Browse Products</a>
            <a href="${pageContext.request.contextPath}/orders.jsp" class="nav-link">My Orders</a>
            <a href="${pageContext.request.contextPath}/cart.jsp" class="nav-link active">🛒 Cart</a>
        </div>
    </nav>

    <main class="container">
        <h2 style="font-size: 1.75rem; font-weight: 800; margin-bottom: 1.5rem;">Your Shopping Cart</h2>

        <div id="cart-content">
            <div class="glass-card text-center p-8"><p>Loading cart items...</p></div>
        </div>
    </main>

    <script src="${pageContext.request.contextPath}/js/cart.js"></script>
</body>
</html>
