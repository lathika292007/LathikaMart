<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login — LathikaMart</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <script>window.contextPath = '${pageContext.request.contextPath}';</script>
</head>
<body class="flex flex-col min-h-screen">
    <nav class="navbar">
        <a href="${pageContext.request.contextPath}/" class="brand-title">✨ LathikaMart</a>
        <div class="nav-links">
            <a href="${pageContext.request.contextPath}/" class="nav-link">Browse Products</a>
            <a href="${pageContext.request.contextPath}/register.jsp" class="btn btn-primary">Sign Up</a>
        </div>
    </nav>

    <div class="container" style="max-width: 440px; margin-top: 4rem;">
        <div class="glass-card">
            <h2 style="font-size: 1.75rem; font-weight: 800; margin-bottom: 1.5rem; text-align: center;">Welcome Back</h2>
            
            <div id="error-alert" style="display:none; background:rgba(239,68,68,0.2); border:1px solid #ef4444; color:#f87171; padding:0.75rem; border-radius:8px; margin-bottom:1rem; font-size:0.9rem;"></div>

            <form id="login-form">
                <div class="form-group">
                    <label class="form-label" for="email">Email Address</label>
                    <input type="email" id="email" class="form-input" required placeholder="buyer@example.com">
                </div>

                <div class="form-group">
                    <label class="form-label" for="password">Password</label>
                    <input type="password" id="password" class="form-input" required placeholder="••••••••">
                </div>

                <button type="submit" class="btn btn-primary" style="width: 100%; margin-top: 0.5rem; padding: 0.75rem;">
                    Sign In
                </button>
            </form>

            <div style="margin-top: 1.5rem; text-align: center; font-size: 0.875rem; color: #94a3b8;">
                Don't have an account? <a href="${pageContext.request.contextPath}/register.jsp" style="color: #6366f1; text-decoration: none; font-weight: 600;">Sign up here</a>
            </div>

            <!-- Demo Quick Login Accounts for evaluation -->
            <div style="margin-top: 1.5rem; padding-top: 1rem; border-top: 1px solid rgba(255,255,255,0.1); font-size: 0.8rem; color: #94a3b8;">
                <p style="font-weight: 700; margin-bottom: 0.5rem; color: white;">Quick Demo Credentials (Password: password123):</p>
                <div style="display: flex; gap: 0.5rem; flex-wrap: wrap;">
                    <button type="button" class="btn btn-secondary" style="font-size: 0.75rem; padding: 0.25rem 0.5rem;" onclick="fillCreds('john.buyer@gmail.com', 'password123')">Buyer</button>
                    <button type="button" class="btn btn-secondary" style="font-size: 0.75rem; padding: 0.25rem 0.5rem;" onclick="fillCreds('seller.tech@lathikamart.com', 'password123')">Seller</button>
                    <button type="button" class="btn btn-secondary" style="font-size: 0.75rem; padding: 0.25rem 0.5rem;" onclick="fillCreds('admin@lathikamart.com', 'password123')">Admin</button>
                </div>
            </div>
        </div>
    </div>

    <script>
        function fillCreds(email, pass) {
            document.getElementById('email').value = email;
            document.getElementById('password').value = pass;
        }

        document.getElementById('login-form').addEventListener('submit', async (e) => {
            e.preventDefault();
            const alert = document.getElementById('error-alert');
            alert.style.display = 'none';

            const email = document.getElementById('email').value.trim();
            const password = document.getElementById('password').value;

            try {
                const res = await fetch('${pageContext.request.contextPath}/api/v1/auth/login', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ email, password })
                });

                const data = await res.json();
                if (data.success) {
                    window.location.href = '${pageContext.request.contextPath}/';
                } else {
                    alert.textContent = data.error ? data.error.message : 'Login failed.';
                    alert.style.display = 'block';
                }
            } catch (err) {
                alert.textContent = 'Network error. Please try again.';
                alert.style.display = 'block';
            }
        });
    </script>
</body>
</html>
