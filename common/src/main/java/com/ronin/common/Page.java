package com.ronin.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author lizelong
 * @date Created on 2020/7/6 13:46
 * @description 分页对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Page<T> implements Serializable {

    private static final long serialVersionUID = -275582248840137389L;
    private int total;
    private List<T> data;
}
