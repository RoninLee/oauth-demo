package com.ronin.gateway.feign;

import com.ronin.common.AutoInfo;
import com.ronin.common.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("oauth-server233")
public interface Verification {


    /**
     * Description: [存储验证码]
     * @return: java.lang.String
     * @param code
     * Created on 2019年08月26日
     * Copyright (c) 2019 北京柯莱特科技有限公司
     **/
    @PostMapping(path = "/oauth/verification/code")
    void verificationCode(@RequestParam("code") String code, @RequestParam("uuid") String uuid);

    /**
     * Description: [校验验证码]
     * @return: java.lang.String
     * @param uuid
     * @param code
     * Created on 2019年08月26日
     * Copyright (c) 2019 北京柯莱特科技有限公司
     **/
    @PostMapping(path = "/oauth/verification/check")
    Result<Boolean> checkVerification(@RequestParam("uuid") String uuid, @RequestParam("code") String code);


    /**
     * Description: [存token]
     * @return: java.lang.String
     * @param key
     * @param accessToken
     * Created on 2019年08月26日
     * Copyright (c) 2019 北京柯莱特科技有限公司
     **/
    @PostMapping(path = "/oauth/verification/setToken")
    void setToken(@RequestParam("key") String key, @RequestParam("accessToken") String accessToken);

    /**
     * Description: [校验token]
     * @return: java.lang.String
     * @param key
     * Created on 2019年08月26日
     * Copyright (c) 2019 北京柯莱特科技有限公司
     **/
    @PostMapping(path = "/oauth/verification/checkToken")
    Result<Boolean> checkToken(@RequestParam("key") String key);

    /***
     * <p>Description:[根据token 获取用户名]</p>
     * Created on 2019/11/11
     * @param token
     * @return java.lang.String
     * @author 谢楠
     */
    @PostMapping("/oauth/verification/user-token")
    String queryNameByToken(String token);

    /***
     * <p>Description:[删除不用的token]</p>
     * Created on 2019/11/11
     * @param autoInfo
     * @return void
     * @author 谢楠
     */
    @PostMapping("/oauth/verification/deleteRedisToken")
    void deleteRedisToken(@RequestBody AutoInfo autoInfo);

}
