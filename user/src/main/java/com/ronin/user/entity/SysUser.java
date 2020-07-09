package com.ronin.user.entity;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author lizelong
 * @date Created on 2020/7/1 15:45
 * @description 用户信息实体类
 */
@Data
public class SysUser implements Serializable {

    private static final long serialVersionUID = -7853406323485915305L;

    /**
     * 主键id
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private Long userId;
    /**
     * 用户名
     */
    private String username;

    /**
     * 账号
     */
    private String account;

    /**
     * 密码
     */
    private String password;

    /**
     * 版本号（乐观锁）
     */
    private Integer version;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 最近更新时间
     */
    private LocalDateTime editTime;

    /**
     * 更新人
     */
    private String editUser;

    /**
     * 删除标志，-1为删除，1为正常
     */
    private Integer deleteFlag;

}
