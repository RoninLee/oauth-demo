package com.ronin.oauth.security;

import com.ronin.common.CredentialType;
import com.ronin.common.LoginAppUser;
import com.ronin.common.ServiceException;
import com.ronin.common.SysConstant;
import com.ronin.oauth.feign.UserClient;
import com.ronin.oauth.util.AuthUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.ExpiringOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.JdkSerializationStrategy;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStoreSerializationStrategy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * <Description> 重写tokenStore .因为最新版中RedisTokenStore的set已经被弃用了，
 * 所以我就只能自定义一个，代码和RedisTokenStore一样，
 * 只是把所有conn.set(…)都换成conn..stringCommands().set(…)，
 * <br>
 */
@Slf4j
@Component
public class MyRedisTokenStore implements TokenStore {
    private static final String ACCESS = "access:";
    // private static final String AUTH_TO_ACCESS = "auth_to_access:";
    private static final String AUTH = "auth:";
    private static final String REFRESH_AUTH = "refresh_auth:";
    private static final String ACCESS_TO_REFRESH = "access_to_refresh:";
    private static final String REFRESH = "refresh:";
    private static final String REFRESH_TO_ACCESS = "refresh_to_access:";
    // private static final String CLIENT_ID_TO_ACCESS = "client_id_to_access:";
    private static final String UNAME_TO_ACCESS = "uname_to_access:";
    // private static final String UNAME_TO_ACCESS = "uname_to_access:";
    private static final String UNAME_TO_TOKEN = "uname_to_token:";
    private static final String TOKEN_CACHED = "token_cached";
    private final RedisConnectionFactory connectionFactory;
    private AuthenticationKeyGenerator authenticationKeyGenerator = new DefaultAuthenticationKeyGenerator();
    private RedisTokenStoreSerializationStrategy serializationStrategy = new JdkSerializationStrategy();
    private String prefix = "";
    @Resource
    private UserClient userClient;

    public MyRedisTokenStore(RedisConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public void setAuthenticationKeyGenerator(AuthenticationKeyGenerator authenticationKeyGenerator) {
        this.authenticationKeyGenerator = authenticationKeyGenerator;
    }

    public void setSerializationStrategy(RedisTokenStoreSerializationStrategy serializationStrategy) {
        this.serializationStrategy = serializationStrategy;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    private RedisConnection getConnection() {
        return this.connectionFactory.getConnection();
    }

    private byte[] serialize(Object object) {
        return this.serializationStrategy.serialize(object);
    }

    private byte[] serializeKey(String object) {
        return this.serialize(this.prefix + object);
    }

    private OAuth2AccessToken deserializeAccessToken(byte[] bytes) {
        return (OAuth2AccessToken) this.serializationStrategy.deserialize(bytes, OAuth2AccessToken.class);
    }

    private OAuth2Authentication deserializeAuthentication(byte[] bytes) {
        return (OAuth2Authentication) this.serializationStrategy.deserialize(bytes, OAuth2Authentication.class);
    }

    private OAuth2RefreshToken deserializeRefreshToken(byte[] bytes) {
        return (OAuth2RefreshToken) this.serializationStrategy.deserialize(bytes, OAuth2RefreshToken.class);
    }

    private byte[] serialize(String string) {
        return this.serializationStrategy.serialize(string);
    }

    private String deserializeString(byte[] bytes) {
        return this.serializationStrategy.deserializeString(bytes);
    }

    @Override
    public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
        long timestart = System.currentTimeMillis();
        //根据username查询token,没有就返回空
        String username = authentication.getName();
        byte[] serializedKey = this.serializeKey(UNAME_TO_TOKEN + username);
        byte[] bytes = null;
        RedisConnection conn = this.getConnection();
        try {
            bytes = conn.get(serializedKey);
        } finally {
            conn.close();
        }
        DefaultOAuth2AccessToken accessToken = (DefaultOAuth2AccessToken) this.deserializeAccessToken(bytes);
        if (accessToken != null) {
            Map<String, Object> map = accessToken.getAdditionalInformation();
            //表示token是从缓存中取的
            if (map == null || map.isEmpty()) {
                map = new HashMap<String, Object>();
                //表示token是从缓存中取的
                map.put(TOKEN_CACHED, SysConstant.Y);
                accessToken.setAdditionalInformation(map);
            } else {
                //表示token是从缓存中取的
                map.put(TOKEN_CACHED, SysConstant.Y);
            }
        }
        long timeend = System.currentTimeMillis();
        log.info("获取token耗时" + (timeend - timestart));
        return accessToken;
    }

    public String getToken(String username) {
        byte[] serializedKey = this.serializeKey(UNAME_TO_TOKEN + username);
        byte[] bytes = null;
        RedisConnection conn = this.getConnection();
        try {
            bytes = conn.get(serializedKey);
        } finally {
            conn.close();
        }
        OAuth2AccessToken accessToken = this.deserializeAccessToken(bytes);
        if (accessToken != null) {
            return accessToken.getValue();
        }
        return null;
    }

    private OAuth2Authentication loadUser(OAuth2Authentication authentication) {
        LoginAppUser loginAppUser = AuthUtil.getUserFromAuth(authentication);
        String username = loginAppUser.getUsername();
        CredentialType credentialType = null;
        if (loginAppUser.getLoginType() != null) {
            credentialType = CredentialType.valueOf(loginAppUser.getLoginType());
        }
        LoginAppUser loginAppUserNew = null;
        // 手机号登录
        if (null != credentialType && CredentialType.PHONE == credentialType) {
            loginAppUserNew = userClient.findByPhone(username);
        } else if (null != credentialType && CredentialType.COMPUTER == credentialType) {
            // 电脑用户名登录
            loginAppUserNew = userClient.findByAccount(username);
        } else {
            // 默认是用户密码登录
            loginAppUserNew = userClient.findByAccount(username);
        }
        if (loginAppUserNew == null) {
            throw new ServiceException("加载用户信息失败username:" + username);
        }
        //由于principal被final修饰，只能拷贝属性
        BeanUtils.copyProperties(loginAppUserNew, loginAppUser);
        return authentication;
    }

    @Override
    public OAuth2Authentication readAuthentication(OAuth2AccessToken token) {
        return this.readAuthentication(token.getValue());
    }

    @Override
    public OAuth2Authentication readAuthentication(String token) {
        byte[] bytes = null;
        RedisConnection conn = this.getConnection();
        try {
            bytes = conn.get(this.serializeKey(AUTH + token));
        } finally {
            conn.close();
        }
        OAuth2Authentication auth = this.deserializeAuthentication(bytes);
        return auth;
    }

    @Override
    public OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken token) {
        return this.readAuthenticationForRefreshToken(token.getValue());
    }

    public OAuth2Authentication readAuthenticationForRefreshToken(String token) {
        RedisConnection conn = getConnection();
        try {
            byte[] bytes = conn.get(serializeKey(REFRESH_AUTH + token));
            OAuth2Authentication auth = deserializeAuthentication(bytes);
            return auth;
        } finally {
            conn.close();
        }
    }

    /**
     * 先token是否从缓存中取的,如果是就返回，
     * 否则就获取AppLoginUser并塞到OAuth2Authentication对象里, 然后存到redis
     */
    @Override
    public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
        Map<String, Object> map = token.getAdditionalInformation();
        //判断token是否从缓存中取的
        String isCache = (String) map.get(TOKEN_CACHED);
        if (SysConstant.Y.equals(isCache)) {
            log.debug("token是缓存中的，不存储OAuth2Authentication,token=" + token.getValue());
            return;
        }
        //加载用户信息
        if (!authentication.isClientOnly()) {
            authentication = loadUser(authentication);
        }
        byte[] unameTokenKey = this.serializeKey(UNAME_TO_TOKEN + authentication.getName());
        byte[] serializedAccessToken = serialize(token);
        byte[] serializedAuth = serialize(authentication);
        byte[] accessKey = serializeKey(ACCESS + token.getValue());
        byte[] authKey = serializeKey(AUTH + token.getValue());
        //byte[] authToAccessKey = serializeKey(AUTH_TO_ACCESS + authenticationKeyGenerator.extractKey(authentication));
        byte[] approvalKey = serializeKey(UNAME_TO_ACCESS + getApprovalKey(authentication));
        //byte[] clientId = serializeKey(CLIENT_ID_TO_ACCESS + authentication.getOAuth2Request().getClientId());

        RedisConnection conn = getConnection();
        try {
            conn.openPipeline();
            conn.stringCommands().set(accessKey, serializedAccessToken);
            conn.stringCommands().set(authKey, serializedAuth);
            conn.stringCommands().set(unameTokenKey, serializedAccessToken);
            //conn.stringCommands().set(authToAccessKey, serializedAccessToken);
            if (!authentication.isClientOnly()) {
                conn.rPush(approvalKey, serializedAccessToken);
            }
            //conn.rPush(clientId, serializedAccessToken);
            if (token.getExpiration() != null) {
                int seconds = token.getExpiresIn();
                conn.expire(accessKey, seconds);
                conn.expire(authKey, seconds);
                //conn.expire(authToAccessKey, seconds);
                //conn.expire(clientId, seconds);
                conn.expire(approvalKey, seconds);
                //token过期时间提前10秒，避免用户信息先于token失效
                conn.expire(unameTokenKey, seconds - 10);
            }
            OAuth2RefreshToken refreshToken = token.getRefreshToken();
            if (refreshToken != null && refreshToken.getValue() != null) {
                byte[] refresh = serialize(token.getRefreshToken().getValue());
                byte[] auth = serialize(token.getValue());
                byte[] refreshToAccessKey = serializeKey(REFRESH_TO_ACCESS + token.getRefreshToken().getValue());
                conn.stringCommands().set(refreshToAccessKey, auth);
                byte[] accessToRefreshKey = serializeKey(ACCESS_TO_REFRESH + token.getValue());
                conn.stringCommands().set(accessToRefreshKey, refresh);
                if (refreshToken instanceof ExpiringOAuth2RefreshToken) {
                    ExpiringOAuth2RefreshToken expiringRefreshToken = (ExpiringOAuth2RefreshToken) refreshToken;
                    Date expiration = expiringRefreshToken.getExpiration();
                    if (expiration != null) {
                        int seconds = Long.valueOf((expiration.getTime() - System.currentTimeMillis()) / 1000L)
                                .intValue();
                        conn.expire(refreshToAccessKey, seconds);
                        conn.expire(accessToRefreshKey, seconds);
                    }
                }
            }
            conn.closePipeline();
        } finally {
            conn.close();
        }
    }

    public void storeAuthentication(String token, OAuth2Authentication authentication) {
        byte[] serializedAuth = serialize(authentication);
        byte[] authKey = serializeKey(AUTH + token);
        RedisConnection conn = getConnection();
        try {
            conn.openPipeline();
            conn.stringCommands().set(authKey, serializedAuth);
            conn.closePipeline();
        } finally {
            conn.close();
        }
    }

    private static String getApprovalKey(OAuth2Authentication authentication) {
        String userName = authentication.getUserAuthentication() == null ? "" : authentication.getUserAuthentication().getName();
        return getApprovalKey(authentication.getOAuth2Request().getClientId(), userName);
    }

    private static String getApprovalKey(String clientId, String userName) {
        return clientId + (userName == null ? "" : ":" + userName);
    }

    @Override
    public void removeAccessToken(OAuth2AccessToken accessToken) {
        this.removeAccessToken(accessToken.getValue());
    }

    @Override
    public OAuth2AccessToken readAccessToken(String tokenValue) {
        byte[] key = serializeKey(ACCESS + tokenValue);
        byte[] bytes = null;
        RedisConnection conn = getConnection();
        try {
            bytes = conn.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.close();
        }
        OAuth2AccessToken accessToken = deserializeAccessToken(bytes);
        return accessToken;
    }

    public void removeTokenByUsername(String username) {
        byte[] serializedKey = this.serializeKey(UNAME_TO_TOKEN + username);
        RedisConnection conn = this.getConnection();
        try {
            conn.del(serializedKey);
            conn.closePipeline();
        } finally {
            conn.close();
        }
    }

    public void removeAccessToken(String tokenValue) {
        byte[] accessKey = serializeKey(ACCESS + tokenValue);
        byte[] authKey = serializeKey(AUTH + tokenValue);
        byte[] accessToRefreshKey = serializeKey(ACCESS_TO_REFRESH + tokenValue);
        RedisConnection conn = getConnection();
        try {
            conn.openPipeline();
            conn.get(accessKey);
            conn.get(authKey);
            conn.del(accessKey);
            conn.del(accessToRefreshKey);
            // Don't remove the refresh token - it's up to the caller to do that
            conn.del(authKey);
            List<Object> results = conn.closePipeline();
            byte[] access = (byte[]) results.get(0);
            byte[] auth = (byte[]) results.get(1);

            OAuth2Authentication authentication = deserializeAuthentication(auth);
            if (authentication != null) {
                String key = authenticationKeyGenerator.extractKey(authentication);
                //byte[] authToAccessKey = serializeKey(AUTH_TO_ACCESS + key);
                byte[] unameKey = serializeKey(UNAME_TO_ACCESS + getApprovalKey(authentication));
                //byte[] clientId = serializeKey(CLIENT_ID_TO_ACCESS + authentication.getOAuth2Request().getClientId());
                conn.openPipeline();
                //conn.del(authToAccessKey);
                conn.lRem(unameKey, 1, access);
                //conn.lRem(clientId, 1, access);
                conn.del(serialize(ACCESS + key));
                conn.closePipeline();
            }
        } finally {
            conn.close();
        }
    }

    @Override
    public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication authentication) {
        byte[] refreshKey = serializeKey(REFRESH + refreshToken.getValue());
        byte[] refreshAuthKey = serializeKey(REFRESH_AUTH + refreshToken.getValue());
        byte[] serializedRefreshToken = serialize(refreshToken);
        RedisConnection conn = getConnection();
        try {
            conn.openPipeline();
            conn.stringCommands().set(refreshKey, serializedRefreshToken);
            conn.stringCommands().set(refreshAuthKey, serialize(authentication));
            if (refreshToken instanceof ExpiringOAuth2RefreshToken) {
                ExpiringOAuth2RefreshToken expiringRefreshToken = (ExpiringOAuth2RefreshToken) refreshToken;
                Date expiration = expiringRefreshToken.getExpiration();
                if (expiration != null) {
                    int seconds = Long.valueOf((expiration.getTime() - System.currentTimeMillis()) / 1000L)
                            .intValue();
                    conn.expire(refreshKey, seconds);
                    conn.expire(refreshAuthKey, seconds);
                }
            }
            conn.closePipeline();
        } finally {
            conn.close();
        }
    }

    @Override
    public OAuth2RefreshToken readRefreshToken(String tokenValue) {
        byte[] key = serializeKey(REFRESH + tokenValue);
        byte[] bytes = null;
        RedisConnection conn = getConnection();
        try {
            bytes = conn.get(key);
        } finally {
            conn.close();
        }
        OAuth2RefreshToken refreshToken = deserializeRefreshToken(bytes);
        return refreshToken;
    }

    @Override
    public void removeRefreshToken(OAuth2RefreshToken refreshToken) {
        this.removeRefreshToken(refreshToken.getValue());
    }

    public void removeRefreshToken(String tokenValue) {
        byte[] refreshKey = serializeKey(REFRESH + tokenValue);
        byte[] refreshAuthKey = serializeKey(REFRESH_AUTH + tokenValue);
        byte[] refresh2AccessKey = serializeKey(REFRESH_TO_ACCESS + tokenValue);
        byte[] access2RefreshKey = serializeKey(ACCESS_TO_REFRESH + tokenValue);
        RedisConnection conn = getConnection();
        try {
            conn.openPipeline();
            conn.del(refreshKey);
            conn.del(refreshAuthKey);
            conn.del(refresh2AccessKey);
            conn.del(access2RefreshKey);
            conn.closePipeline();
        } finally {
            conn.close();
        }
    }

    @Override
    public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken refreshToken) {
        this.removeAccessTokenUsingRefreshToken(refreshToken.getValue());
    }

    private void removeAccessTokenUsingRefreshToken(String refreshToken) {
        byte[] key = serializeKey(REFRESH_TO_ACCESS + refreshToken);
        List<Object> results = null;
        RedisConnection conn = getConnection();
        try {
            conn.openPipeline();
            conn.get(key);
            conn.del(key);
            results = conn.closePipeline();
        } finally {
            conn.close();
        }
        if (results == null) {
            return;
        }
        byte[] bytes = (byte[]) results.get(0);
        String accessToken = deserializeString(bytes);
        if (accessToken != null) {
            removeAccessToken(accessToken);
        }
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId, String userName) {
        byte[] approvalKey = serializeKey(UNAME_TO_ACCESS + getApprovalKey(clientId, userName));
        List<byte[]> byteList = null;
        RedisConnection conn = getConnection();
        try {
            byteList = conn.lRange(approvalKey, 0, -1);
        } finally {
            conn.close();
        }
        if (byteList == null || byteList.size() == 0) {
            return Collections.<OAuth2AccessToken>emptySet();
        }
        List<OAuth2AccessToken> accessTokens = new ArrayList<OAuth2AccessToken>(byteList.size());
        for (byte[] bytes : byteList) {
            OAuth2AccessToken accessToken = deserializeAccessToken(bytes);
            accessTokens.add(accessToken);
        }
        return Collections.<OAuth2AccessToken>unmodifiableCollection(accessTokens);
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientId(String clientId) {
        return null;
//        byte[] key = serializeKey(CLIENT_ID_TO_ACCESS + clientId);
//        List<byte[]> byteList = null;
//        RedisConnection conn = getConnection();
//        try {
//            byteList = conn.lRange(key, 0, -1);
//        } finally {
//            conn.close();
//        }
//        if (byteList == null || byteList.size() == 0) {
//            return Collections.<OAuth2AccessToken> emptySet();
//        }
//        List<OAuth2AccessToken> accessTokens = new ArrayList<OAuth2AccessToken>(byteList.size());
//        for (byte[] bytes : byteList) {
//            OAuth2AccessToken accessToken = deserializeAccessToken(bytes);
//            accessTokens.add(accessToken);
//        }
//        return Collections.<OAuth2AccessToken> unmodifiableCollection(accessTokens);
    }

}


