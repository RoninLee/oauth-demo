package com.ronin.user.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author lizelong
 * @date Created on 2020/7/3 13:26
 * @description 系统菜单
 */
@Data
public class SysMenu implements Serializable {
    private static final long serialVersionUID = 265564172283081138L;
    /**
     * 主键id
     */
    private Long menuId;

    /**
     * 菜单名称
     */
    private String menuName;

    /**
     * 菜单标识
     */
    private String menuCode;

    /**
     * 父节点id
     */
    private Long parentId;

    /**
     * 菜单路径
     */
    private String url;

    /**
     * 菜单排序
     */
    private Integer orderNo;

    /**
     * 菜单层级
     */
    private Integer level;

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
