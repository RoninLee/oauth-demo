package com.ronin.user.dao;

import com.ronin.user.dto.BaseVo;
import com.ronin.user.entity.SysMenu;
import com.ronin.user.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.Set;

/**
 * @author lizelong
 * @date Created on 2020/7/1 15:54
 * @description 菜单管理数据层
 */
@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    /**
     * 根据用户查询菜单
     *
     * @param userId 用户id
     * @return 菜单列表
     */
    Set<BaseVo<String>> queryMenuByUserId(String userId);
}
