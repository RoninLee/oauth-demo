package com.ronin.user.dao;

import com.ronin.user.dto.LoginUserInfo;
import com.ronin.user.entity.SysUser;
import com.ronin.user.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author lizelong
 * @date Created on 2020/7/1 15:54
 * @description 用户管理数据层
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 查询用户登录信息
     *
     * @param account 账号
     * @return 登录信息
     */
    LoginUserInfo queryLoginUserInfo(String account);
}
