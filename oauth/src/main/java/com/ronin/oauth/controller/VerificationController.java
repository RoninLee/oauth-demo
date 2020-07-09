package com.ronin.oauth.controller;

import com.ronin.common.AutoInfo;
import com.ronin.common.Result;
import com.ronin.common.util.RedisStringUtils;
import com.ronin.oauth.util.AesUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>Description: [验证码]</p >
 * Created on 2019年08月26日
 *
 * @author <a href="xienan@camelotchina.com">谢楠</a>
 * @version 1.0
 * Copyright (c) 2019 北京柯莱特科技有限公司
 */
@Slf4j
@RestController
public class VerificationController {

    @Resource
    private RedisStringUtils redisStringUtils;

    /**
     * Description: [获取验证码图片]
     *
     * @return: void
     * Created on 2019年08月26日
     * Copyright (c) 2019 北京柯莱特科技有限公司
     **/
    @PostMapping("/oauth/verification/code")
    public void verificationCode(String code, String uuid) {
        // 存redis 过期时间5分钟
        redisStringUtils.set(uuid, code, 300);
    }

    /**
     * Description: [校验验证码]
     *
     * @return: void
     * Created on 2019年08月26日
     * Copyright (c) 2019 北京柯莱特科技有限公司
     **/
    @PostMapping("/oauth/verification/check")
    public Result<Boolean> checkVerification(String uuid, String code) {
        if (StringUtils.isBlank(code)) {
            return Result.error("验证码错误");
        }
        code = code.toUpperCase();
        if (StringUtils.isBlank(uuid)) {
            return Result.error("uuid不能为空");
        }
        // 获取缓存中的验证码
        String redisCode = redisStringUtils.get(uuid);
        if (StringUtils.isBlank(redisCode)) {
            return Result.error("验证码过期");
        }
        if (!code.equals(redisCode)) {
            return Result.error("验证码错误");
        }
        return Result.success(true);
    }

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/oauth/verification/newUserPwd")
    public Result<String> newUserPwd(String password) {
        log.info("\n 密码：" + password);
        log.info("\n 解密密码：" + AesUtil.aesDecrypt(password));
        String encode = passwordEncoder.encode(AesUtil.aesDecrypt(password));
        Result<String> ret = new Result<>();
        ret.setData(encode);
        ret.setMsg("success");
        ret.setCode(200);
        return ret;
    }

    /**
     * Description: [存一个token]
     *
     * @return: void
     * Created on 2019年08月26日
     * Copyright (c) 2019 北京柯莱特科技有限公司
     **/
    @PostMapping("/oauth/verification/setToken")
    public void setToken(String key, String accessToken) {
        // 存redis 过期时间 12 个小时
        redisStringUtils.set(key, accessToken, 60 * 60 * 12);
        // redisStringUtils.set(key, accessToken, 60);
    }

    /**
     * Description: [校验token是否过期]
     *
     * @return: void
     * Created on 2019年08月26日
     * Copyright (c) 2019 北京柯莱特科技有限公司
     **/
    @PostMapping("/oauth/verification/checkToken")
    public Result<Boolean> checkToken(String key) {
        String val = redisStringUtils.get(key);
        if (StringUtils.isBlank(val)) {
            return Result.success(false);
        }
        return Result.success(true);
    }

    /**
     * Description: [删除token]
     *
     * @return: void
     * Created on 2019年08月26日
     * Copyright (c) 2019 北京柯莱特科技有限公司
     **/
    @PostMapping("/oauth/verification/deleteRedisToken")
    public void deleteRedisToken(@RequestBody AutoInfo autoInfo) {
        String refreshKey = "refresh:" + autoInfo.getRefreshToken();
        String refreshAuthKey = "refresh_auth:" + autoInfo.getRefreshToken();
        String refreshToAccessKey = "refresh_to_access:" + autoInfo.getRefreshToken();
        redisStringUtils.del(refreshKey);
        redisStringUtils.del(refreshAuthKey);
        redisStringUtils.del(refreshToAccessKey);
        String checkKey = "CHECK-" + autoInfo.getAccessToken();
        String access = "access:" + autoInfo.getAccessToken();
        String accessToRefresh = "access_to_refresh:" + autoInfo.getAccessToken();
        String auth = "auth:" + autoInfo.getAccessToken();
        redisStringUtils.del(checkKey);
        redisStringUtils.del(access);
        redisStringUtils.del(accessToRefresh);
        redisStringUtils.del(auth);
    }

}
