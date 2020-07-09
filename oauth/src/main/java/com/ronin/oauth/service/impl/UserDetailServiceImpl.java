package com.ronin.oauth.service.impl;

import com.ronin.common.CredentialType;
import com.ronin.common.LoginAppUser;
import com.ronin.common.SysRedisConstant;
import com.ronin.common.util.RedisStringUtils;
import com.ronin.oauth.feign.UserClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 根据用户名获取用户<br>
 * <p>
 * 密码校验请看下面两个类
 *
 * @see org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider
 * @see org.springframework.security.authentication.dao.DaoAuthenticationProvider
 */
@Slf4j
@Service("userDetailsService")
public class UserDetailServiceImpl implements UserDetailsService {

    @Resource
    private UserClient userClient;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Resource
    private RedisStringUtils redisStringUtils;

    /**
     * 这里仅获取密码，在MyRedisTokenStore里往redis存储用户信息前获取完整的用户信息
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        long timestart = System.currentTimeMillis();
        // 为了支持多类型登录，这里username后面拼装上登录类型,如username|type
        String[] params = username.split("\\|");
        // 真正的用户名
        username = params[0];
        // 登录类型
        CredentialType credentialType = params.length > 1 ? CredentialType.valueOf(params[1]) : null;

        LoginAppUser loginAppUser = new LoginAppUser();
        loginAppUser.setUsername(username);
        // loginAppUser.setLoginType(credentialType.name());
        loginAppUser.setLoginType(CredentialType.USERNAME.name());
        log.info("获取密码,登陆用户名：" + username);

        // 短信登录
        if (null != credentialType && CredentialType.PHONE == credentialType) {
            handlerWechatLogin(loginAppUser);
        }// 微信登陆
        else if (null != credentialType && CredentialType.WECHAT_OPENID == credentialType) {
            handlerWechatLogin(loginAppUser);
        }
        // 电脑用户名登录
        else if (null != credentialType && CredentialType.COMPUTER == credentialType) {
            handlerWechatLogin(loginAppUser);
        } else {
            // 获取用户密码
            String pwd = redisStringUtils.get(SysRedisConstant.SAAS_ACCOUNT_PWD + username);
            if (StringUtils.isEmpty(pwd)) {
                pwd = userClient.verification(username);
            }
            loginAppUser.setPassword(pwd);
        }
        long timeend = System.currentTimeMillis();
        log.info("获取密码耗时" + (timeend - timestart));
        return loginAppUser;
    }

    /**
     * 重置密码，让oauth2校验通过
     *
     * @param loginAppUser
     */
    private void handlerWechatLogin(LoginAppUser loginAppUser) {
        loginAppUser.setPassword(passwordEncoder.encode("123456"));
        log.info("微信登陆，{},{}", loginAppUser);
    }

}
