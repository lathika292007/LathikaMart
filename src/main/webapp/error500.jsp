<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>500 Internal Server Error — LathikaMart</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="bg-gray-100 flex items-center justify-center min-h-screen">
    <div class="error-container card text-center p-8 max-w-md mx-auto">
        <h1 class="text-6xl font-bold text-red-600 mb-4">500</h1>
        <h2 class="text-2xl font-semibold mb-2">Something Went Wrong</h2>
        <p class="text-gray-600 mb-6">An unexpected error occurred while processing your request. Please try again later.</p>
        <a href="${pageContext.request.contextPath}/" class="btn btn-primary">Return to Marketplace</a>
    </div>
</body>
</html>
