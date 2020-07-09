/**
 * 
 */
package com.ronin.oauth.service;

import java.util.List;

/**
 * @Description
 * @author    陈贵兵
 * @Date      2020年5月22日
 */
public interface RefreshUserCacheService {
	void refreshUserCache(List<String> accountList);
}
