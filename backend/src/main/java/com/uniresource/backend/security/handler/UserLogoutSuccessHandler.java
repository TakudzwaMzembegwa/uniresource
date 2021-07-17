package com.uniresource.backend.security.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.uniresource.backend.security.configuration.JWTConfig;
import com.uniresource.backend.security.utils.JWTTokenUtils;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class UserLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
                
        String token = request.getHeader(JWTConfig.tokenHeader);
        JWTTokenUtils.addBlackList(token);

        SecurityContextHolder.clearContext();
        response.setStatus(HttpStatus.OK.value());
    }

}
