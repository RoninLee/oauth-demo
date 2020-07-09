package com.ronin.gateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.ronin.gateway.utils.ServletUtil;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Set;

/**
 * 黑名单IP拦截<br>
 * 黑名单ip变化不会太频繁，<br>
 * 考虑到性能，我们不实时掉接口从别的服务获取了，<br>
 * 而是定时把黑名单ip列表同步到网关层,
 *
 * @author 崔春松
 */
@Component
public class BlackIPAccessFilter extends ZuulFilter {

    /**
     * 黑名单列表
     */
    private Set<String> blackIPs = new HashSet<>();

    @Override
    public boolean shouldFilter() {
        if (blackIPs.isEmpty()) {
            return false;
        }

        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        String ip = ServletUtil.getIpAddress(request);

        return blackIPs.contains(ip);// 判断ip是否在黑名单列表里
    }

    @Override
    public Object run() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        requestContext.setResponseStatusCode(HttpStatus.FORBIDDEN.value());
        requestContext.setResponseBody("black ip");
        requestContext.setSendZuulResponse(false);

        return null;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

}
