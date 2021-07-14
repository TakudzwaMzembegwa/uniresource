package com.uniresource.backend.security.filter;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.uniresource.backend.security.configuration.JWTConfig;
import com.uniresource.backend.security.utils.JWTTokenUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter{
    
    public static final Logger log = LoggerFactory.getLogger(JWTAuthorizationFilter.class);

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager){
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException{
        log.info("\n>>>>>>>>\n>>>\n>>>\n>>>\nTOKEH HEARDER:" + JWTConfig.tokenHeader);  
        String header = request.getHeader(JWTConfig.tokenHeader);
        if(header == null || !header.startsWith(JWTConfig.tokenPrefix)){
            chain.doFilter(request, response);
            return;
        }
       
        try{
            UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
            String token = request.getHeader(JWTConfig.tokenHeader);
            if(JWTTokenUtils.isBlackList(token.replace(JWTConfig.tokenPrefix, ""))){
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("Authentication error, Token`s Blacklisted");
                return;
            }
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        }
        catch(SignatureVerificationException e){
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().write("Authentication error, SignatureVerification fail");
        }
        catch(TokenExpiredException e){
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.getWriter().print("Authentication error, The Token`s Expired.");// might want to redirect to login page https://stackoverflow.com/a/58528297/9714469
        }
    }

    private UsernamePasswordAuthenticationToken getAuthentication( HttpServletRequest request){
        String token = request.getHeader(JWTConfig.tokenHeader);
        if(token != null){
            String user = null;
            try {
                user = 
                JWT.require(Algorithm.HMAC512(JWTConfig.secret.getBytes()))
                    .build()
                    .verify(token.replace(JWTConfig.tokenPrefix, ""))
                    .getSubject();

            } catch (Exception e) {
            }
            if(user != null)
                return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
        }
        return null;
    }
}