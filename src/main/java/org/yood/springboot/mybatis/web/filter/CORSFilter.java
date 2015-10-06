package org.yood.springboot.mybatis.web.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CORSFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(CORSFilter.class);

    @Value("${cors.origin}")
    private String corsOrigin;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        LOGGER.info("Client origin = {}, HTTP Method={}", corsOrigin, request.getMethod());
        response.addHeader("Access-Control-Allow-Origin", corsOrigin);
        response.addHeader("Access-Control-Allow-Credentials", "true") ;
        if (request.getHeader("Access-Control-Request-Method") != null && "OPTIONS".equals(request.getMethod())) {
            response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
            response.addHeader("Access-Control-Allow-Headers", "Content-Type");
            response.addHeader("Access-Control-Max-Age", "3600");
        }
        filterChain.doFilter(request, response);
    }
}
