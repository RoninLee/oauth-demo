package com.ronin.user.controller;

import com.ronin.common.util.AppUserUtil;
import com.ronin.user.feign.OauthServerFeign;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author lizelong
 * @date Created on 2020/7/9 17:53
 * @description
 */
@RestController
public class TestController {

    @Resource
    private OauthServerFeign oauthServerFeign;

    @GetMapping("test")
    public String test() {
        String test = oauthServerFeign.test();
        return AppUserUtil.getLoginAppUserName() + " <== ok ==> " + test;
    }
}
