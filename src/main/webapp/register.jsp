<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register — LathikaMart</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <script>window.contextPath = '${pageContext.request.contextPath}';</script>
</head>
<body class="flex flex-col min-h-screen">
    <nav class="navbar">
        <a href="${pageContext.request.contextPath}/" class="brand-title">✨ LathikaMart</a>
        <div class="nav-links">
            <a href="${pageContext.request.contextPath}/" class="nav-link">Browse Products</a>
            <a href="${pageContext.request.contextPath}/login.jsp" class="btn btn-secondary">Login</a>
        </div>
    </nav>

    <div class="container" style="max-width: 480px; margin-top: 3rem;">
        <div class="glass-card">
            <h2 style="font-size: 1.75rem; font-weight: 800; margin-bottom: 1.5rem; text-align: center;">Create Account</h2>
            
            <div id="error-alert" style="display:none; background:rgba(239,68,68,0.2); border:1px solid #ef4444; color:#f87171; padding:0.75rem; border-radius:8px; margin-bottom:1rem; font-size:0.9rem;"></div>

            <form id="register-form">
                <div class="form-group">
                    <label class="form-label" for="name">Full Name</label>
                    <input type="text" id="name" class="form-input" required placeholder="John Doe">
                </div>

                <div class="form-group">
                    <label class="form-label" for="email">Email Address</label>
                    <input type="email" id="email" class="form-input" required placeholder="john@example.com">
                </div>

                <div class="form-group">
                    <label class="form-label" for="password">Password</label>
                    <input type="password" id="password" class="form-input" required placeholder="At least 6 characters">
                </div>

                <div class="form-group">
                    <label class="form-label" for="role">Account Role</label>
                    <select id="role" class="form-select">
                        <option value="BUYER">Buyer (Browse & Purchase Products)</option>
                        <option value="SELLER">Seller (List & Manage Products)</option>
                    </select>
                </div>

                <button type="submit" class="btn btn-primary" style="width: 100%; margin-top: 0.5rem; padding: 0.75rem;">
                    Create Account
                </button>
            </form>

            <div style="margin-top: 1.5rem; text-align: center; font-size: 0.875rem; color: #94a3b8;">
                Already have an account? <a href="${pageContext.request.contextPath}/login.jsp" style="color: #6366f1; text-decoration: none; font-weight: 600;">Log in here</a>
            </div>
        </div>
    </div>

    <script>
        document.getElementById('register-form').addEventListener('submit', async (e) => {
            e.preventDefault();
            const alert = document.getElementById('error-alert');
            alert.style.display = 'none';

            const name = document.getElementById('name').value.trim();
            const email = document.getElementById('email').value.trim();
            const password = document.getElementById('password').value;
            const role = document.getElementById('role').value;

            try {
                const res = await fetch('${pageContext.request.contextPath}/api/v1/auth/register', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ name, email, password, role })
                });

                const data = await res.json();
                if (data.success) {
                    window.location.href = '${pageContext.request.contextPath}/';
                } else {
                    alert.textContent = data.error ? data.error.message : 'Registration failed.';
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
