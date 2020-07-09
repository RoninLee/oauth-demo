package com.ronin.oauth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 注入HashOperations简化hash结构操作
 * @author liubin3
 *
 */
@Configuration
public class RedisConfig {

	@Bean
	@Scope("prototype")
	public HashOperations<String, String, String> hashOperations(RedisTemplate<String, String> redisTemplate) {
		return redisTemplate.opsForHash();
	}
}
