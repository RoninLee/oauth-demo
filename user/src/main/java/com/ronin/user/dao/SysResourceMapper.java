package com.ronin.user.dao;

import com.ronin.user.dto.BaseVo;
import com.ronin.user.entity.SysResource;
import com.ronin.user.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * @author lizelong
 * @date Created on 2020/7/1 15:54
 * @description 资源管理数据层
 */
@Mapper
public interface SysResourceMapper extends BaseMapper<SysResource> {

    /**
     * 根据用户查询资源列表
     *
     * @param userId 用户id
     * @return 资源列表
     */
    Set<BaseVo<String>> queryResourceByUserId(@Param("userId") Long userId);
}
