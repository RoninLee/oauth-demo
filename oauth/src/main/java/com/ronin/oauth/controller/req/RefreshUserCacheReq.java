package com.ronin.oauth.controller.req;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author lizelong
 * @date Created on 2020/7/6 14:46
 * @description
 */
@Data
public class RefreshUserCacheReq implements Serializable {

    private static final long serialVersionUID = -7863609121887671530L;
    private List<String> accountList;
}
