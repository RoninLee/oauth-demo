package com.ronin.oauth.service.impl;

import com.ronin.common.CredentialType;
import com.ronin.common.Result;
import com.ronin.common.SystemClientInfo;
import com.ronin.common.util.ListUtil;
import com.ronin.oauth.feign.UserClient;
import com.ronin.oauth.security.MyRedisTokenStore;
import com.ronin.oauth.service.RefreshTokenService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.stereotype.Service;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 刷新TOKEN
 */
@Slf4j
@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
    @Autowired
    private UserClient userClient;
    @Autowired
	private MyRedisTokenStore redisTokenStore;
    @Autowired
    private TokenEndpoint tokenEndpoint;
    /**
     * 刷新TOKEN
     */
	@Override
	@Async
	public void refreshToken() {
		//查询用户
		Result<List<String>> result=userClient.getAllAccount();
		List<String> list=result.getData();
		if (ListUtil.isEmpty(list)) {
			log.info("刷新TOKEN用户为空");
			return;
		}
		for (String  username: list) {
			try {
				loopRefreshToken(username);
			} catch (Exception e) {
				log.error("刷新TOKEN异常"+username,e);
			}
		}
	}
	private void loopRefreshToken(String username) {
		//删除token
		String tokenValue=redisTokenStore.getToken(username);
		
		if (StringUtils.isNotEmpty(tokenValue)) {
			log.info("删除TOKEN,username:"+username);
			redisTokenStore.removeTokenByUsername(username);
			log.info("删除登录用户认证信息,username:"+username);
			redisTokenStore.removeAccessToken(tokenValue);
		}
		
		createToken(username);
	}
	private void createToken(String username) {
		log.info("创建TOKEN开始"+username);
		//模拟用户请求TokenEndpoint
		UsernamePasswordAuthenticationToken principal=new UsernamePasswordAuthenticationToken(
				SystemClientInfo.CLIENT_ID,SystemClientInfo.CLIENT_ID,null);
		Map<String, String> parameters = new HashMap<>();
        parameters.put(OAuth2Utils.CLIENT_ID, SystemClientInfo.CLIENT_ID);
        parameters.put(OAuth2Utils.GRANT_TYPE, "password");
        parameters.put("client_secret", SystemClientInfo.CLIENT_SECRET);
        parameters.put(OAuth2Utils.SCOPE, SystemClientInfo.CLIENT_SCOPE);
        parameters.put("username", username + "|" + CredentialType.COMPUTER.name());
        // 密码固定为123456
        parameters.put("password", "123456");
		try {
			tokenEndpoint.postAccessToken(principal, parameters);
		} catch (HttpRequestMethodNotSupportedException e) {
			log.error("创建TOKEN异常",e);
		}
		log.info("创建TOKEN结束"+username);
	}
}
