package com.ronin.user.dao;

import com.ronin.user.entity.SysRoleMenu;
import com.ronin.user.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author lizelong
 * @date Created on 2020/7/1 15:54
 * @description 角色菜单管理数据层
 */
@Mapper
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {

}
