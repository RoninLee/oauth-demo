package com.ronin.oauth.controller;

import com.ronin.common.LoginAppUser;
import com.ronin.common.Result;
import com.ronin.common.util.ListUtil;
import com.ronin.oauth.controller.req.RefreshUserCacheReq;
import com.ronin.oauth.security.MyRedisTokenStore;
import com.ronin.oauth.service.RefreshTokenService;
import com.ronin.oauth.service.RefreshUserCacheService;
import com.ronin.oauth.util.AuthUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping
public class OAuth2Controller {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;
    @Autowired
    private RefreshUserCacheService refreshUserCacheService;
    @Autowired
    private RefreshTokenService refreshTokenService;

    /**
     * 当前登陆用户信息<br>
     * <p>
     * security获取当前登录用户的方法是SecurityContextHolder.getContext().getAuthentication()<br>
     * 返回值是接口org.springframework.security.core.Authentication，又继承了Principal<br>
     * 这里的实现类是org.springframework.security.oauth2.provider.OAuth2Authentication<br>
     * </p>
     *
     * @return
     */
    @GetMapping("/user-me")
    public Authentication principal(Authentication authentication) {
        log.debug("user-me:{}", authentication.getName());
        return authentication;
    }

    /**
     * 根据token 获取用户名
     *
     * @param token
     * @return
     */
    @PostMapping("/oauth/verification/user-token")
    public String queryNameByToken(@RequestBody String token) {
        MyRedisTokenStore redisTokenStore = new MyRedisTokenStore(redisConnectionFactory);
        OAuth2Authentication oAuth2Authentication = redisTokenStore.readAuthentication(token);
        return oAuth2Authentication.getName();
    }

    @PostMapping("/oauth/verification/queryAppUserByToken")
    public LoginAppUser queryAppUserByToken() {
        MyRedisTokenStore redisTokenStore = new MyRedisTokenStore(redisConnectionFactory);
        String tokenValue = getToken();
        OAuth2Authentication oAuth2Auth = redisTokenStore.readAuthentication(tokenValue);
        return AuthUtil.getUserFromAuth(oAuth2Auth);
    }

    private String getToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object details = authentication.getDetails();
        OAuth2AuthenticationDetails details1 = (OAuth2AuthenticationDetails) details;
        return details1.getTokenValue();
    }

    @Resource
    private ConsumerTokenServices tokenServices;

    /**
     * 注销登陆/退出
     * 移除access_token和refresh_token<br>
     * 改为用ConsumerTokenServices，该接口的实现类DefaultTokenServices已有相关实现，我们不再重复造轮子
     *
     * @param access_token
     */
    @DeleteMapping(value = "/remove_token", params = "access_token")
    public void removeToken(String access_token) {
        boolean flag = tokenServices.revokeToken(access_token);
        if (flag) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            saveLogoutLog(authentication.getName());
        }
    }

    /**
     * 退出日志
     *
     * @param username
     */
    private void saveLogoutLog(String username) {
        log.info("{}退出", username);
    }

    /**
     * @author 陈贵兵
     */
    @ApiOperation("刷新登录用户的缓存")
    @PostMapping("/oauth/refreshUserCache")
    public Result<String> refreshUserCache(@RequestBody RefreshUserCacheReq req) {
        if (ListUtil.isEmpty(req.getAccountList())) {
            log.info("刷新登录用户的缓存账户列表为空");
            return Result.success("刷新登录用户的缓存成功");
        }
        refreshUserCacheService.refreshUserCache(req.getAccountList());
        return Result.success("刷新登录用户的缓存成功");
    }

    /**
     * @author 陈贵兵
     * 这是一个定时任务接口
     */
    @ApiOperation("定时刷新所有TOKEN")
    @PostMapping("/oauth/refreshAllToken")
    public Result<String> refreshAllToken() {
        refreshTokenService.refreshToken();
        return Result.success("刷新TOKEN成功");
    }

}
