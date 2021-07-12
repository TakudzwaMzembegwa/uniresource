package com.uniresource.backend.security.handler;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.util.UrlPathHelper;


public class JWTAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private static final  UrlPathHelper urlPathHelper = new UrlPathHelper();
    
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {
        response.setStatus(401);
        response.setContentType("application/json"); 
        response.getWriter().append(json(request));
    }

    private String json(HttpServletRequest request) {
        long date = new Date().getTime();
        return "{\"timestamp\": " + date + ", "
            + "\"status\": 401, "
            + "\"error\": \"Unauthorized\", "
            + "\"message\": \"Authentication failed: bad credentials\", "
            + "\"path\": \"" + urlPathHelper.getRequestUri(request)+ "\"}";
    }
    
}
