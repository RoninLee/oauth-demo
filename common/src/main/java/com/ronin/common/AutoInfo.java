package com.ronin.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lizelong
 * @date Created on 2020/7/6 13:32
 * @description
 */
@Data
public class AutoInfo implements Serializable {

    private static final long serialVersionUID = 6139086989963137474L;
    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     **/
    private String password;

    /**
     * taken令牌
     **/
    private String accessToken;

    /**
     * taken令牌
     **/
    private String refreshToken;

    /**
     * Description: [验证码的uuid]
     **/
    private String uuid;

    /**
     * Description: [验证码]
     **/
    private String code;

    /**
     * 电脑的用户名
     */
    private String computerName;
}
