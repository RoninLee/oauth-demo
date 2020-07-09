package com.ronin.user.dao;

import com.ronin.user.entity.SysUserRole;
import com.ronin.user.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author lizelong
 * @date Created on 2020/7/1 15:54
 * @description 用户角色管理数据层
 */
@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

}
