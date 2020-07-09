/**
 * 
 */
package com.ronin.oauth.util;

import com.alibaba.fastjson.JSONObject;
import com.ronin.common.LoginAppUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.Map;

/**
 * @Description
 * @author    陈贵兵
 * @Date      2020年5月20日
 */
@Slf4j
public class AuthUtil {
	@SuppressWarnings("rawtypes")
	public static LoginAppUser getUserFromAuth(OAuth2Authentication oAuth2Auth) {
		Authentication authentication = oAuth2Auth.getUserAuthentication();
		log.debug(authentication.toString());
		if (authentication instanceof UsernamePasswordAuthenticationToken) {
			Object principal = authentication.getPrincipal();
			if (principal instanceof LoginAppUser) {
				LoginAppUser la = (LoginAppUser)principal;
				return la;
			}
			UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken)authentication;
			Map map = (Map)authenticationToken.getDetails();
			map = (Map)map.get("principal");
			LoginAppUser la = JSONObject.parseObject(JSONObject.toJSONString(map), LoginAppUser.class);
			log.info("用户信息从map获取的"+la.toString());
			return la;
		}else if(authentication instanceof PreAuthenticatedAuthenticationToken){
			PreAuthenticatedAuthenticationToken authenticationToken = (PreAuthenticatedAuthenticationToken)authentication;
			Object principal = authenticationToken.getPrincipal();
			if (principal instanceof LoginAppUser) {
				LoginAppUser la = (LoginAppUser)principal;
				return la;
			}
		}
		return null;
	}
}
