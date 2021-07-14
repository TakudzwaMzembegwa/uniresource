package com.uniresource.backend.security.utils;

import java.util.Date;

import javax.annotation.PostConstruct;

import com.auth0.jwt.JWT;
import com.uniresource.backend.security.configuration.JWTConfig;
import com.uniresource.backend.security.filter.JWTAuthenticationFilter;

import org.springframework.stereotype.Component;

@Component
public class JWTTokenUtils{

    private static JWTTokenUtils jwtTokenUtils;

    @PostConstruct
    public void init(){
        jwtTokenUtils = this;
    }
    
    public static void addBlackList(String token) {
		if (!token.isBlank()) {
            token = token.replace(JWTConfig.tokenPrefix, "");
            Long expire = JWT.decode(token).getExpiresAt().getTime() - new Date().getTime();
            RedisUtils.set(token, "blacklist", expire);
		}
	}

	public static void deleteRedisToken(String token) {
		if (!token.isBlank()) {
			token = token.replace(JWTConfig.tokenPrefix, "");
			RedisUtils.delete(token);
		}
	}

	public static boolean isBlackList(String token) {
		if (!token.isBlank()) {
			token = token.replace(JWTConfig.tokenPrefix, "");
			return RedisUtils.hasKey(token);
		}
		return false;
	}

}