package com.ronin.user.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lizelong
 * @date Created on 2020/7/6 15:20
 * @description 基础<key, value>格式对象
 */
@ApiModel("键值对对象")
@Data
public class BaseVo<T> implements Serializable {
    private static final long serialVersionUID = -9128034407448456201L;

    private T key;

    private String value;

}
