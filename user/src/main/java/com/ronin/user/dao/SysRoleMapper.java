package com.ronin.user.dao;

import com.ronin.user.entity.SysRole;
import com.ronin.user.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author lizelong
 * @date Created on 2020/7/1 15:54
 * @description 角色管理数据层
 */
@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

}
