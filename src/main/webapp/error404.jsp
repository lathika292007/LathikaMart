<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>404 Page Not Found — LathikaMart</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="bg-gray-100 flex items-center justify-center min-h-screen">
    <div class="error-container card text-center p-8 max-w-md mx-auto">
        <h1 class="text-6xl font-bold text-indigo-600 mb-4">404</h1>
        <h2 class="text-2xl font-semibold mb-2">Page Not Found</h2>
        <p class="text-gray-600 mb-6">The page or resource you are looking for could not be found.</p>
        <a href="${pageContext.request.contextPath}/" class="btn btn-primary">Return to Marketplace</a>
    </div>
</body>
</html>
