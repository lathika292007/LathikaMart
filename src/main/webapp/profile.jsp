<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Profile — LathikaMart</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <header class="header">
        <div class="container header-container">
            <a href="${pageContext.request.contextPath}/" class="logo">✨ LathikaMart</a>
            <nav class="nav">
                <a href="${pageContext.request.contextPath}/">Browse Products</a>
                <a href="${pageContext.request.contextPath}/wishlist.jsp">❤️ Wishlist</a>
                <a href="${pageContext.request.contextPath}/cart.jsp">🛒 Cart</a>
                <a href="${pageContext.request.contextPath}/orders.jsp">📦 Orders</a>
                <a href="${pageContext.request.contextPath}/profile.jsp" class="active">👤 Profile</a>
                <a href="${pageContext.request.contextPath}/api/v1/auth/logout">Logout</a>
            </nav>
        </div>
    </header>

    <main class="container main-content">
        <div class="card" style="max-width: 600px; margin: 40px auto; padding: 30px;">
            <h2>👤 My Account Profile</h2>
            <div id="profileAlert"></div>
            
            <form id="profileForm">
                <div class="form-group">
                    <label for="profileName">Full Name</label>
                    <input type="text" id="profileName" class="form-control" required>
                </div>
                <div class="form-group">
                    <label for="profileEmail">Email Address (Read Only)</label>
                    <input type="email" id="profileEmail" class="form-control" readonly style="background: #e2e8f0;">
                </div>

                <hr style="margin: 25px 0; border: none; border-top: 1px solid #e2e8f0;">

                <h3>🔒 Change Password</h3>
                <div class="form-group">
                    <label for="currentPassword">Current Password</label>
                    <input type="password" id="currentPassword" class="form-control" placeholder="Enter current password">
                </div>
                <div class="form-group">
                    <label for="newPassword">New Password</label>
                    <input type="password" id="newPassword" class="form-control" placeholder="Enter new password">
                </div>

                <button type="submit" class="btn btn-primary btn-block" style="margin-top: 20px;">Save Profile Changes</button>
            </form>
        </div>
    </main>

    <script src="${pageContext.request.contextPath}/js/profile.js"></script>
</body>
</html>
