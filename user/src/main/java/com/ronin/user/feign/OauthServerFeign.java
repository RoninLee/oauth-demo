package com.ronin.user.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author lizelong
 * @date Created on 2020/7/9 18:30
 * @description
 */
@FeignClient("oauth-server233")
public interface OauthServerFeign {
    @GetMapping("/lzlTest/test")
    String test();
}
