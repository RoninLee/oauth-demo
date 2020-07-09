package com.ronin.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author lizelong
 * @date Created on 2020/7/6 11:43
 * @description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseDTO implements Serializable {

    private Long id;

    private String name;
}
