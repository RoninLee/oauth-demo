package com.ronin.user.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author lizelong
 * @date Created on 2020/7/3 13:11
 * @description 资源
 */
@Data
public class SysResource implements Serializable {

    private static final long serialVersionUID = -2063977324237238285L;

    /**
     * 主键id
     */
    private Long resourceId;

    /**
     * 资源名称
     */
    private String resourceName;

    /**
     * 资源标识
     */
    private String resourceCode;

    /**
     * 资源路径
     */
    private String url;

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
