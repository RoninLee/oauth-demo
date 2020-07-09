package com.ronin.oauth.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;

import java.util.UUID;

/**
 * 解决同一username每次登陆access_token都相同的问题,看下RedisTokenStore的方法getAccessToken便知<br>
 *
 * @author lizelong
 * @see org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator
 * @see org.springframework.security.oauth2.provider.token.TokenStore
 */
@Slf4j
public class RandomAuthenticationKeyGenerator implements AuthenticationKeyGenerator {

    @Override
    public String extractKey(OAuth2Authentication authentication) {
        String token = UUID.randomUUID().toString();
        log.info("生成token:" + token);
        return token;
    }
}
