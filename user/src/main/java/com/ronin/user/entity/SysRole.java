package com.ronin.user.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author lizelong
 * @date Created on 2020/7/3 13:19
 * @description 系统角色
 */
@Data
public class SysRole implements Serializable {

    private static final long serialVersionUID = 4902294252169074430L;

    /**
     * 主键id
     */
    private Long roleId;

    /**
     * 角色名
     */
    private String roleName;

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
