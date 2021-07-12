package com.uniresource.backend.security.utils;

import javax.annotation.PostConstruct;

import com.auth0.jwt.JWT;
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
            token = token.replace(JWTAuthenticationFilter.TOKEN_PREFIX, "");
            RedisUtils.set(token, "blacklist", JWT.decode(token).getExpiresAt().getTime());
		}
	}

	public static void deleteRedisToken(String token) {
		if (!token.isBlank()) {
			token = token.replace(JWTAuthenticationFilter.TOKEN_PREFIX, "");
			RedisUtils.delete(token);
		}
	}

	public static boolean isBlackList(String token) {
		if (!token.isBlank()) {
			token = token.replace(JWTAuthenticationFilter.TOKEN_PREFIX, "");
			return RedisUtils.hasKey(token);
		}
		return false;
	}

}