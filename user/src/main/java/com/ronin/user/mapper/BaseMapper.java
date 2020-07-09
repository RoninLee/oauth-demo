package com.ronin.user.mapper;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @author lizelong
 * @date Created on 2020/7/6 15:15
 * @description
 */
public interface BaseMapper<T> extends Mapper<T>, MySqlMapper<T> {
}
