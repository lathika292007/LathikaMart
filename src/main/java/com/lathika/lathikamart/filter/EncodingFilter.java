package com.lathika.lathikamart.filter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * Filter to enforce UTF-8 character encoding on all requests and responses.
 */
@WebFilter("/*")
public class EncodingFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        if (response instanceof javax.servlet.http.HttpServletResponse) {
            javax.servlet.http.HttpServletResponse httpRes = (javax.servlet.http.HttpServletResponse) response;
            httpRes.setHeader("X-Frame-Options", "DENY");
            httpRes.setHeader("X-Content-Type-Options", "nosniff");
            httpRes.setHeader("X-XSS-Protection", "1; mode=block");
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
