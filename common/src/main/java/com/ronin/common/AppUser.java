package com.ronin.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lizelong
 * @date Created on 2020/7/6 11:42
 * @description 用户信息
 */
@Data
public class AppUser implements Serializable {

    private static final long serialVersionUID = 5495237365809597018L;

    /* 账号信息 */
    /**
     * 账号ID
     */
    private Long id;

    /**
     * 账号
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 头像图片地址
     */
    private String headImgUrl;

    /* 人员信息 */
    /**
     * 用户姓名
     */
    private String name;

    /**
     * 手机号
     */
    private String phone;

}
