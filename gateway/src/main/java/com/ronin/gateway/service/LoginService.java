package com.ronin.gateway.service;


import com.ronin.common.AutoInfo;
import com.ronin.common.Result;

import java.util.Map;

/**
 * <p>Description: [类功能描述]</p>
 * Created on 2019/11/11
 *
 * @author <a href="mailto: xienan@camelotchina.com">谢楠</a>
 * @version 1.0
 * Copyright (c) 2019 北京柯莱特科技有限公司
 */
public interface LoginService {

	/***
	 * <p>Description:[系统刷新refresh_token]</p>
	 * Created on 2019/11/11
	 * @param autoInfo
	 * @return com.fehorizon.commonService.model.common.Result<java.util.Map < java.lang.String, java.lang.Object>>
	 * @author 谢楠
	 */
	Result<Map<String, Object>> refreshToken(AutoInfo autoInfo);
}
