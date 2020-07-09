package com.ronin.oauth.controller;

import com.ronin.common.util.AppUserUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lizelong
 * @date Created on 2020/7/9 18:27
 * @description 测试类
 */
@RestController
@RequestMapping("/lzlTest/")
public class TestController {

    @GetMapping("test")
    public String test() {
        return AppUserUtil.getLoginAppUserId() + " Oauth2 Test Success";
    }

}
