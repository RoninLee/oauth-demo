package com.ronin.oauth.service.impl;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 将oauth_client_details表数据缓存到redis，毕竟该表改动非常小，而且数据很少，这里做个缓存优化<br>
 * 如果有通过界面修改client的需求的话，不要用JdbcClientDetailsService了，请用该类，否则redis里有缓存<br>
 * 如果手动修改了该表的数据，请注意清除redis缓存，是hash结构，key是client_details
 *
 * @author 崔春松 cuichunsong@camelotchina.com
 */
@Slf4j
@Service
public class RedisClientDetailsService extends JdbcClientDetailsService implements InitializingBean {

    @Resource
    private HashOperations<String, String, String> hashOperations;

    public RedisClientDetailsService(DataSource dataSource) {
        super(dataSource);
    }

    /**
     * 缓存client的redis key，这里是hash结构存储
     */
    private static final String CACHE_CLIENT_KEY = "client_details";

    @Override
    public ClientDetails loadClientByClientId(String clientId) throws InvalidClientException {
        ClientDetails clientDetails = null;

        // 先从redis获取
        String value = hashOperations.get(CACHE_CLIENT_KEY, clientId);
        if (StringUtils.isBlank(value)) {
            clientDetails = cacheAndGetClient(clientId);
        } else {
            clientDetails = JSONObject.parseObject(value, BaseClientDetails.class);
        }

        return clientDetails;
    }

    /**
     * 缓存client并返回client
     *
     * @param clientId
     */
    private ClientDetails cacheAndGetClient(String clientId) {
        // 从数据库读取
        ClientDetails clientDetails = super.loadClientByClientId(clientId);
        if (clientDetails != null) {// 写入redis缓存
            hashOperations.putIfAbsent(CACHE_CLIENT_KEY, clientId, JSONObject.toJSONString(clientDetails));
            log.info("缓存clientId:{},{}", clientId, clientDetails);
        }

        return clientDetails;
    }

    @Override
    public void updateClientDetails(ClientDetails clientDetails) throws NoSuchClientException {
        super.updateClientDetails(clientDetails);
        cacheAndGetClient(clientDetails.getClientId());
    }

    @Override
    public void updateClientSecret(String clientId, String secret) throws NoSuchClientException {
        super.updateClientSecret(clientId, secret);
        cacheAndGetClient(clientId);
    }

    @Override
    public void removeClientDetails(String clientId) throws NoSuchClientException {
        super.removeClientDetails(clientId);
        removeRedisCache(clientId);
    }

    /**
     * 删除redis缓存
     *
     * @param clientId
     */
    private void removeRedisCache(String clientId) {
        hashOperations.delete(CACHE_CLIENT_KEY, clientId);
    }

    /**
     * 将oauth_client_details全表刷入redis
     */
    void loadAllClientToCache() {
        List<ClientDetails> list = super.listClientDetails();
        if (CollectionUtils.isEmpty(list)) {
            log.error("oauth_client_details表数据为空，请检查");
            return;
        }
        Map<String, String> clientsMap = list.stream().collect(Collectors.toMap(ClientDetails::getClientId, JSONObject::toJSONString));
        hashOperations.putAll(CACHE_CLIENT_KEY, clientsMap);
        log.info("将oauth_client_details全表刷入redis");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        loadAllClientToCache();
    }
}
