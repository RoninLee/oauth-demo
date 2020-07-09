package com.ronin.user.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lizelong
 * @date Created on 2020/7/6 15:21
 * @description 用户信息
 */
@Data
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 5227139354406510574L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 账号
     */
    private String account;

    /**
     * 密码
     */
    private String password;

    /**
     * 用户姓名
     */
    private String username;

    /**
     * 手机号
     */
    private String phone;
}
