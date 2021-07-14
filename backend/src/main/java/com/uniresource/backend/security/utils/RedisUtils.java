package com.uniresource.backend.security.utils;

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisUtils {
    
    @Autowired
    private StringRedisTemplate redisTemplate;

    private static RedisUtils redisUtils;

    @PostConstruct
    public void init(){
        redisUtils = this;
        redisUtils.redisTemplate = this.redisTemplate;
    }
    
	public static Object get(String key) {
		return redisUtils.redisTemplate.opsForValue().get(key);
	}

	public static void set(String key, String value) {
		redisUtils.redisTemplate.opsForValue().set(key, value);
	}

	
	public static void set(String key, String value, Long expire) {
		redisUtils.redisTemplate.opsForValue().set(key, value, expire, TimeUnit.SECONDS);
	}
    
	public static void delete(String key) {
		redisUtils.redisTemplate.opsForValue().getOperations().delete(key);
	}

	public static Boolean hasKey(String key){
        return redisUtils.redisTemplate.opsForValue().getOperations().hasKey(key);
    }

}
