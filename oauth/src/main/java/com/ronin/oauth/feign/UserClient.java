package com.ronin.oauth.feign;

import com.ronin.common.LoginAppUser;
import com.ronin.common.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("user-center233")
public interface UserClient {

    /**
     * 查询账号信息
     *
     * @param account 账号
     * @return 账号信息
     */
    @GetMapping(value = "/user/info")
    LoginAppUser findByAccount(@RequestParam("account") String account);

    /**
     * 查询账号信息
     *
     * @param phone 手机号
     * @return 账号信息
     */
    @GetMapping(value = "/user/phone")
    LoginAppUser findByPhone(@RequestParam("phone") String phone);

    @PostMapping("/user/account/getAll")
    public Result<List<String>> getAllAccount();

    /**
     * 获取验证密码
     *
     * @param account 账号
     * @return 加密过的密码串
     */
    @GetMapping(value = "/api/saas/user/verification")
    String verification(@RequestParam("account") String account);


}
