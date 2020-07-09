package com.ronin.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

/**
 * @author lizelong
 * @date Created on 2020/7/6 15:22
 * @description 登录用户信息
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class LoginUserInfo extends UserInfo {
    private static final long serialVersionUID = 2828785297554299819L;

    @ApiModelProperty("资源权限")
    private Set<BaseVo<String>> resource;

    @ApiModelProperty("菜单权限")
    private Set<BaseVo<String>> menu;

}
