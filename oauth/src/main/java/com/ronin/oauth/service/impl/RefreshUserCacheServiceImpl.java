package com.ronin.oauth.service.impl;

import com.ronin.common.LoginAppUser;
import com.ronin.oauth.feign.UserClient;
import com.ronin.oauth.security.MyRedisTokenStore;
import com.ronin.oauth.service.RefreshUserCacheService;
import com.ronin.oauth.util.AuthUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 刷新登录用户缓存
 */
@Slf4j
@Service("refreshUserCacheService")
public class RefreshUserCacheServiceImpl implements RefreshUserCacheService {
    @Autowired
    private UserClient userClient;
    @Autowired
	private MyRedisTokenStore redisTokenStore;
	@Override
	@Async
	public void refreshUserCache(List<String> accountList) {
		log.info("开始刷新用户缓存"+accountList);
		for (String acc : accountList) {
			try {
				refreshUserCache(acc);
			} catch (Exception e) {
				log.error("刷新用户缓存异常"+acc,e);
			}
		}
	}
	
	private void refreshUserCache(String username) {
		log.info("开始刷新用户缓存"+username);
	    LoginAppUser loginAppUser = userClient.findByAccount(username);
	    if (loginAppUser==null) {
	    	log.warn("未查到LoginAppUser,username="+username);
			return;
		}
	    String tokenValue=redisTokenStore.getToken(username);
	    OAuth2Authentication oAuth2Auth = redisTokenStore.readAuthentication(tokenValue);
	    LoginAppUser loginAppUserCache = AuthUtil.getUserFromAuth(oAuth2Auth);
	    if (loginAppUserCache==null) {
	    	log.warn("loginAppUserCache为空,username="+username);
			return;
		}
	    //由于principal被final修饰，只能拷贝属性
        BeanUtils.copyProperties(loginAppUser, loginAppUserCache);
        redisTokenStore.storeAuthentication(tokenValue,oAuth2Auth);
        log.info("结束刷新用户缓存"+username);
	}
}
