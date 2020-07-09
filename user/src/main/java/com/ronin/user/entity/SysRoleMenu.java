package com.ronin.user.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author lizelong
 * @date Created on 2020/7/3 13:28
 * @description 角色菜单关系
 */
@Data
public class SysRoleMenu implements Serializable {
    private static final long serialVersionUID = -6893869248988729457L;

    /**
     * 主键id
     */
    private Long roleMenuId;

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 菜单id
     */
    private Long menuId;

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
