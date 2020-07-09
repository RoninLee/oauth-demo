package com.ronin.gateway.service.impl;

import com.alibaba.fastjson.JSON;
import com.ronin.common.AutoInfo;
import com.ronin.common.CredentialType;
import com.ronin.common.Result;
import com.ronin.common.SystemClientInfo;
import com.ronin.gateway.feign.Oauth2Client;
import com.ronin.gateway.feign.Verification;
import com.ronin.gateway.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Description: [类功能描述]</p>
 * Created on 2019/11/11
 *
 * @author <a href="mailto: xienan@camelotchina.com">谢楠</a>
 * @version 1.0
 * Copyright (c) 2019 北京柯莱特科技有限公司
 */
@Slf4j
@Service("loginService")
public class LoginServiceImpl implements LoginService {

    @Resource
    private Verification verification;
    @Resource
    private Oauth2Client oauth2Client;

    @Override
    public Result<Map<String, Object>> refreshToken(AutoInfo autoInfo) {
        log.info("\n 系统刷新refresh_token, 入参:{}", JSON.toJSONString(autoInfo));
        Map<String, String> parameters = assOAuth2Client(autoInfo);
        try {
            Map<String, Object> tokenInfo = oauth2Client.postAccessToken(parameters);
            log.info("\n 系统刷新refresh_token, 返回结果{}", JSON.toJSONString(tokenInfo));
            autoInfo.setAccessToken((String) tokenInfo.get("access_token"));
        } catch (Exception e) {
            log.error("\n  系统刷新refresh_token, 错误:", e);
            return Result.error("refresh_token超期");
        }
        // 获取当前用户名
        String userLogName = verification.queryNameByToken(autoInfo.getAccessToken());
        // 用户重新登录
        Map<String, String> oAuth2Login = assOAuth2Login(userLogName);
        Map<String, Object> tokenInfo = null;
        try {
            tokenInfo = oauth2Client.postAccessToken(oAuth2Login);
            tokenInfo.put("username", userLogName);
        } catch (Exception e) {
            log.error("\n 根据企业name登录, 用户信息校验失败", e);
            return Result.error(userLogName + "未绑定后台账户，请联系管理员");
        }
        // 删除从redis旧的的token信息
        verification.deleteRedisToken(autoInfo);
        return Result.success(tokenInfo);
    }

    /**
     * 刷新系统刷新refresh_tokencas
     *
     * @param autoInfo
     * @return
     */
    private Map<String, String> assOAuth2Client(AutoInfo autoInfo) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OAuth2Utils.GRANT_TYPE, "refresh_token");
        parameters.put(OAuth2Utils.CLIENT_ID, SystemClientInfo.CLIENT_ID);
        parameters.put("client_secret", SystemClientInfo.CLIENT_SECRET);
        parameters.put(OAuth2Utils.SCOPE, SystemClientInfo.CLIENT_SCOPE);
        parameters.put("refresh_token", autoInfo.getRefreshToken());
        return parameters;
    }


    /**
     * 刷新系统刷新refresh_tokencas
     *
     * @param username
     * @return
     */
    private Map<String, String> assOAuth2Login(String username) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OAuth2Utils.GRANT_TYPE, "password");
        parameters.put(OAuth2Utils.CLIENT_ID, SystemClientInfo.CLIENT_ID);
        parameters.put("client_secret", SystemClientInfo.CLIENT_SECRET);
        parameters.put(OAuth2Utils.SCOPE, SystemClientInfo.CLIENT_SCOPE);
        // 为了支持多类型登录，这里在username后拼装上登录类型
        parameters.put("username", username + "|" + CredentialType.WECHAT_OPENID.name());
        // 密码固定为123456
        parameters.put("password", "123456");
        return parameters;
    }

}
