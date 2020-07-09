package com.ronin.user.controller;

import com.google.common.collect.Sets;

import com.ronin.common.LoginAppUser;
import com.ronin.common.Result;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

/**
 * @author lizelong
 * @date Created on 2020/7/6 15:26
 * @description
 */
@RestController
public class UserController {


    /**
     * 查询账号信息
     *
     * @param account 账号
     * @return 账号信息
     */
    @GetMapping(value = "/user/info")
    LoginAppUser findByAccount(@RequestParam("account") String account) {
        LoginAppUser loginAppUser = new LoginAppUser();
        loginAppUser.setAscription(0L);
        loginAppUser.setPersonId(0L);
        loginAppUser.setAvatar("");
        loginAppUser.setSuperAdmin(false);
        loginAppUser.setOrgAdmin(false);
        loginAppUser.setAttributeType(0);
        loginAppUser.setApplications(Sets.newHashSet());
        loginAppUser.setAppIds(Sets.newHashSet());
        loginAppUser.setPermissions(Sets.newHashSet());
        loginAppUser.setSysRoles(Sets.newHashSet());
        loginAppUser.setDeptOrg(Sets.newHashSet());
        loginAppUser.setStoreOrg(Sets.newHashSet());
        loginAppUser.setDeptScope(Sets.newHashSet());
        loginAppUser.setStoreScope(Sets.newHashSet());
        loginAppUser.setAllowOvertime(180000);
        loginAppUser.setAllowSeconded(180000);
        loginAppUser.setPosition("");
        loginAppUser.setCompanyPhone("");
        loginAppUser.setLoginType("USERNAME");
        loginAppUser.setId(233L);
        loginAppUser.setUsername("lisi");
        loginAppUser.setPassword("$2a$10$ew5diqp0CliQ4F6JyZ2iEOOFpVSKSzsLfCdZYnd30DzypDBIHewKW");
        loginAppUser.setHeadImgUrl("");
        loginAppUser.setName("lisi");
        loginAppUser.setPhone("16602105233");
        return loginAppUser;
    }

    /**
     * 查询账号信息
     *
     * @param phone 手机号
     * @return 账号信息
     */
    @GetMapping(value = "/user/phone")
    LoginAppUser findByPhone(@RequestParam("phone") String phone) {
        LoginAppUser loginAppUser = new LoginAppUser();
        loginAppUser.setAscription(0L);
        loginAppUser.setPersonId(0L);
        loginAppUser.setAvatar("");
        loginAppUser.setSuperAdmin(false);
        loginAppUser.setOrgAdmin(false);
        loginAppUser.setAttributeType(0);
        loginAppUser.setApplications(Sets.newHashSet());
        loginAppUser.setAppIds(Sets.newHashSet());
        loginAppUser.setPermissions(Sets.newHashSet());
        loginAppUser.setSysRoles(Sets.newHashSet());
        loginAppUser.setDeptOrg(Sets.newHashSet());
        loginAppUser.setStoreOrg(Sets.newHashSet());
        loginAppUser.setDeptScope(Sets.newHashSet());
        loginAppUser.setStoreScope(Sets.newHashSet());
        loginAppUser.setAllowOvertime(180000);
        loginAppUser.setAllowSeconded(180000);
        loginAppUser.setPosition("");
        loginAppUser.setCompanyPhone("");
        loginAppUser.setLoginType("USERNAME");
        loginAppUser.setId(0L);
        loginAppUser.setUsername("lisi");
        loginAppUser.setPassword("$2a$10$ew5diqp0CliQ4F6JyZ2iEOOFpVSKSzsLfCdZYnd30DzypDBIHewKW");
        loginAppUser.setHeadImgUrl("");
        loginAppUser.setName("lisi");
        loginAppUser.setPhone("16602105233");
        return loginAppUser;
    }

    @PostMapping("/user/account/getAll")
    public Result<List<String>> getAllAccount() {

        List<String> list = new LinkedList<>();
        list.add("lisi");
        list.add("zs");
        list.add("hbx");
        list.add("lzl");
        return Result.success(list);
    }

    /**
     * 获取验证密码
     *
     * @param account 账号
     * @return 加密过的密码串
     */
    @GetMapping(value = "/api/saas/user/verification")
    String verification(@RequestParam("account") String account) {
        return "$2a$10$fBh04Pu27S3z7rGjf4.2jeuOMVYOOm0PrP64dJJ5fC8K8OLgOmBOy";
    }

}
