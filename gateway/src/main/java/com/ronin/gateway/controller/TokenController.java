package com.ronin.gateway.controller;

import com.ronin.common.AutoInfo;
import com.ronin.common.CredentialType;
import com.ronin.common.Result;
import com.ronin.common.SystemClientInfo;
import com.ronin.common.constants.INTEGER;
import com.ronin.gateway.feign.Oauth2Client;
import com.ronin.gateway.service.LoginService;
import com.ronin.gateway.utils.AesUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.util.OAuth2Utils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
/**
 * 登陆、刷新token、退出
 *
 * @author 崔春松
 */

/**
 * <p>Description: [类功能描述]</p>
 * Created on 2019/9/20
 *
 * @author <a href="mailto: xienan@camelotchina.com">谢楠 </a>
 * @version 1.0
 * Copyright (c) 2019 北京柯莱特科技有限公司
 */
@Slf4j
@Controller
public class TokenController {

    @Resource
    private Oauth2Client oauth2Client;

    /**
     * 系统登陆<br>
     * 根据用户名登录<br>
     * 采用oauth2密码模式获取access_token和refresh_token
     *
     * @param autoInfo
     * @return
     */
    @ResponseBody
    @PostMapping("/sys/login")
    public Result<Map<String, Object>> login(@RequestBody AutoInfo autoInfo) {
        // 验证码处理
        //        Result<Boolean> verif = verification.checkVerification(autoInfo.getUuid(), autoInfo.getCode());
        //        if (!verif.isSuccess()) {
        //            return Result.error(verif.getMsg());
        //        }
        long startTime = System.currentTimeMillis();
        // 解密密码
        if (StringUtils.isNotBlank(autoInfo.getPassword())) {
            autoInfo.setPassword(AesUtil.aesDecrypt(autoInfo.getPassword()));
        }
        // 登录处理
        Map<String, String> parameters = new HashMap<>();
        parameters.put(OAuth2Utils.GRANT_TYPE, "password");
        parameters.put(OAuth2Utils.CLIENT_ID, SystemClientInfo.CLIENT_ID);
        parameters.put("client_secret", SystemClientInfo.CLIENT_SECRET);
        parameters.put(OAuth2Utils.SCOPE, SystemClientInfo.CLIENT_SCOPE);
        // 为了支持多类型登录，这里在username后拼装上登录类型
        parameters.put("username", autoInfo.getUsername() + "|" + CredentialType.USERNAME.name());
        parameters.put("password", autoInfo.getPassword());
        // 校验并获取token
        Map<String, Object> tokenInfo = null;
        try {
            long stayan = System.currentTimeMillis();
            tokenInfo = oauth2Client.postAccessToken(parameters);
            long endyan = System.currentTimeMillis();
            log.info("鉴权用时：{}", endyan - stayan);
            //String access_token = (String) tokenInfo.get("access_token");
            //verification.setToken("CHECK-" + access_token, "access_token");
        } catch (Exception e) {
            // 不打印异常信息，请在oauth项目中查看
            log.error("请求token失败", e);
            return Result.error("用户名密码错误");
        }
        long middleTime = System.currentTimeMillis();
        // 密码是否原始密码
        if (autoInfo.getPassword().equals("2307d08fc4fee06228d1a84a13ceeadb")) {
            // 0代表原始密码
            tokenInfo.put("password", INTEGER.ZERO);
        } else {
            // 1代表已经修改过
            tokenInfo.put("password", INTEGER.ONE);
        }
        log.info("{} 登录", autoInfo.getUsername());
        long endTime = System.currentTimeMillis();
        log.info("用户：{}开始时间{},鉴权完时间{},结束时间：{}，用时：{}", autoInfo.getUsername(), startTime, middleTime, endTime, endTime - startTime);
        return Result.success(tokenInfo);
    }

    /*

     */
/**
 * 系统登陆<br>
 * 企业微信认证后的登录<br>
 * 采用oauth2密码模式获取access_token和refresh_token
 *
 * @param autoInfo 微信code
 * @return
 *//*

	@ResponseBody @PostMapping("/sys/wechat/login")
	public Result<Map<String, Object>> wechatLogin(@RequestBody AutoInfo autoInfo) {
		log.info("\n 根据企业微信code获取用户, code:{}", autoInfo.getCode());
		// 通过code获取微信用户名
		Result<WeChatResp> weChaRes = weChat.getUserId(autoInfo.getCode());
		if (!weChaRes.isSuccess()) {
			return Result.error("获取企业微信用户名失败，请联系管理员");
		}
		log.info("\n 企业微信登录, 企业微信用户名:{}", weChaRes.getData().getUserId());
		String username = weChaRes.getData().getUserId();
		Map<String, String> parameters = new HashMap<>();
		parameters.put(OAuth2Utils.CLIENT_ID, SystemClientInfo.CLIENT_ID);
		parameters.put(OAuth2Utils.GRANT_TYPE, "password");
		parameters.put("client_secret", SystemClientInfo.CLIENT_SECRET);
		parameters.put(OAuth2Utils.SCOPE, SystemClientInfo.CLIENT_SCOPE);
		// 为了支持多类型登录，这里在username后拼装上登录类型
		parameters.put("username", username + "|" + CredentialType.WECHAT_OPENID.name());
		// 密码固定为123456
		parameters.put("password", "123456");
		Map<String, Object> tokenInfo = null;
		try {
			tokenInfo = oauth2Client.postAccessToken(parameters);
			tokenInfo.put("username", username);
		} catch (Exception e) {
			log.error("\n 根据企业name登录, 用户信息校验失败");
			// 不打印异常信息，请在oauth项目中查看
			return Result.error(username + "未绑定后台账户，请联系管理员");
		}
		// 当前用户access_token
		String access_token = (String) tokenInfo.get("access_token");
		saveLoginLog(username, "微信用户名登陆");
		return Result.success(tokenInfo);
	}


	*/
/**
 * 点击消息登录
 * 成功后跳转到消息指定页面
 *
 * @param code 微信code
 * @return
 *//*

	@GetMapping("/sys/wechat/message/login") public String wechatMessageLogin(@RequestParam String state, @RequestParam String code) {
		// 通过code获取微信用户名
		Result<WeChatResp> weChaRes = weChat.getUserId(code);
		if (!weChaRes.isSuccess()) {
			// TODO 失败页面
			log.error("\n 点击消息登录, code转用户名失败");
			return "redirect:error.html";
		}
		String username = weChaRes.getData().getUserId();
		Map<String, String> parameters = new HashMap<>();
		parameters.put(OAuth2Utils.CLIENT_ID, SystemClientInfo.CLIENT_ID);
		parameters.put(OAuth2Utils.GRANT_TYPE, "password");
		parameters.put("client_secret", SystemClientInfo.CLIENT_SECRET);
		parameters.put(OAuth2Utils.SCOPE, SystemClientInfo.CLIENT_SCOPE);
		// 为了支持多类型登录，这里在username后拼装上登录类型
		parameters.put("username", username + "|" + CredentialType.WECHAT_OPENID.name());
		// 密码固定为123456
		parameters.put("password", "123456");
		Map<String, Object> tokenInfo = null;
		try {
			tokenInfo = oauth2Client.postAccessToken(parameters);
			String access_token = (String) tokenInfo.get("access_token");
			// verification.setToken("CHECK-" + access_token, "access_token");
		} catch (Exception e) {
			// 不打印异常信息，请在oauth项目中查看
			log.error("\n 点击消息登录, 用户信息校验失败");
			// TODO 失败页面
			return "error";
		}
		// 当前用户access_token
		String access_token = (String) tokenInfo.get("access_token");
		log.info("\n 解析前的URL:{}", state);
		// 解析url
		String url = urlReduction(state);
		log.info("\n 解析后的URL:{}", url);
		saveLoginLog(username, "微信用户名登陆, 成功后跳转到消息指定页面");
		// 跳转的UTL是否携带参数
		String toUrl = "";
		if (url.indexOf("?") != -1) {
			toUrl = "redirect:" + url + "&access_token=" + access_token;
		} else {
			toUrl = "redirect:" + url + "?access_token=" + access_token;
		}
		log.info("\n 点击消息登录跳转的url:{}", toUrl);
		return toUrl;
	}


	*/
/**
 * 系统登陆<br>
 * 根据当前电脑用户名登录<br>
 * 获取access_token和refresh_token
 *
 * @param autoInfo
 * @return
 *//*

	@ResponseBody @PostMapping("/sys/computer/login") public Result<Map<String, Object>> computerLogin(@RequestBody AutoInfo autoInfo) {
		Map<String, String> parameters = new HashMap<>();
		parameters.put(OAuth2Utils.CLIENT_ID, SystemClientInfo.CLIENT_ID);
		parameters.put(OAuth2Utils.GRANT_TYPE, "password");
		parameters.put(OAuth2Utils.SCOPE, SystemClientInfo.CLIENT_SCOPE);
		parameters.put("client_secret", SystemClientInfo.CLIENT_SECRET);
		// 为了支持多类型登录，这里在username后拼装上登录类型
		parameters.put("username", autoInfo.getComputerName() + "|" + CredentialType.COMPUTER.name());
		// 密码固定为123456
		parameters.put("password", "123456");
		Map<String, Object> tokenInfo = null;

		try {
			tokenInfo = oauth2Client.postAccessToken(parameters);
			String access_token = (String) tokenInfo.get("access_token");
			// verification.setToken("CHECK-" + access_token, "access_token");
		} catch (Exception e) {
			log.error("\n 根据当前电脑用户名登录, 用户信息校验失败");
			// 不打印异常信息，请在oauth项目中查看
			return Result.error("用户不存在");
		}

		saveLoginLog(autoInfo.getComputerName(), "根据当前电脑用户名登录");
		return Result.success(tokenInfo);
	}

	*/
/***
 * <p>Description:[发送短信验证码]</p>
 * Created on 2019/12/3
 * @param req
 * @return com.fehorizon.commonService.model.common.Result
 * @author 谢楠
 *//*

	@ResponseBody @PostMapping("/sys/phone/sendPhoneCode")
	public Result sendPhoneCode(@RequestBody PhoneLoginReq req) {
		if (null == req || StringUtils.isBlank(req.getPhone())) {
			return Result.error("手机号不能为空");
		}
		log.info("\n 发送登录验证码, 手机号:{}", req.getPhone());
		return verification.sendPhoneCode(req);
	}

	*/
/***
 * <p>Description:[手机号登录]</p>
 * Created on 2019/12/3
 * @param req
 * @return com.fehorizon.commonService.model.common.Result
 * @author 谢楠
 *//*

	@ResponseBody @PostMapping("/sys/phone/login")
	public Result phoneLogin(@RequestBody PhoneLoginReq req) {
		log.info("\n 手机号登录, 参数:{}", JSON.toJSONString(req));
		if (null == req || StringUtils.isBlank(req.getPhone())) {
			return Result.error("手机号不能为空");
		}
		if (null == req || StringUtils.isBlank(req.getCode())) {
			return Result.error("验证码不能为空");
		}
		// 验证验证码
		Result result = verification.checkPhoneCode(req);
		if (!result.isSuccess()) {
			return Result.error(result.getMsg());
		}
		Map<String, String> parameters = new HashMap<>();
		parameters.put(OAuth2Utils.CLIENT_ID, SystemClientInfo.CLIENT_ID);
		parameters.put(OAuth2Utils.GRANT_TYPE, "password");
		parameters.put(OAuth2Utils.SCOPE, SystemClientInfo.CLIENT_SCOPE);
		parameters.put("client_secret", SystemClientInfo.CLIENT_SECRET);
		// 为了支持多类型登录，这里在username后拼装上登录类型
		parameters.put("username", req.getPhone()+ "|" + CredentialType.PHONE.name());
		// 密码固定为123456
		parameters.put("password", "123456");
		Map<String, Object> tokenInfo = null;

		try {
			tokenInfo = oauth2Client.postAccessToken(parameters);
			String access_token = (String) tokenInfo.get("access_token");
		} catch (Exception e) {
			log.error("\n 根据当前电脑用户名登录, 用户信息校验失败");
			// 不打印异常信息，请在oauth项目中查看
			return Result.error("未绑定后台账户，请联系管理员");
		}

		saveLoginLog(req.getPhone(), "手机号登录");
		return Result.success(tokenInfo);
	}


	*/
    /**
     * 登陆日志
     *
     * @param username
     *//*

	private void saveLoginLog(String username, String remark) {
		log.info("{}登陆", username);
		// 异步
//		CompletableFuture.runAsync(() -> {
//			try {
//				Log log = Log.builder().username(username).module("登陆").remark(remark).createTime(new Date()).build();
//				logClient.save(log);
//			} catch (Exception e) {
//				// do nothing
//			}
//		});
	}
*/
    @Autowired
    private LoginService loginService;


    /**
     * 系统刷新refresh_token
     *
     * @param autoInfo
     * @return
     */
    @ResponseBody
    @PostMapping("/sys/refresh_token")
    Result<Map<String, Object>> refresh_token(@RequestBody AutoInfo autoInfo) {
        return loginService.refreshToken(autoInfo);
    }


    /**
     * 登出
     */
    @ResponseBody
    @PostMapping("/sys/logout")
    public Result<Boolean> logout(@RequestHeader(required = false, value = "Authorization") String token) {
        String accessToken = "";
        if (StringUtils.isNoneBlank(token)) {
            accessToken = token.substring(OAuth2AccessToken.BEARER_TYPE.length() + 1);
        }
        try {
            oauth2Client.removeToken(accessToken);
        } catch (Exception e) {
            // 异常是正常的，已经从redis中删除了accessToken
            log.error("\n 删除toaken + " + accessToken);
        }
        return Result.success(true);
    }

    /**
     * Description: [微信参数只能是[a-zA-Z0-9], 所以将特殊字符转义]
     *
     * @return: java.lang.String
     * Created on 2019年09月09日
     * Copyright (c) 2019 北京柯莱特科技有限公司
     **/
    private static String urlReduction(String url) {
        url = url.replace("zdy100001", "http://");
        url = url.replace("zdy100002", "https://");
        url = url.replace("zdy100003", "/");
        url = url.replace("zdy100004", "?");
        url = url.replace("zdy100005", "=");
        url = url.replace("zdy100006", "&");
        url = url.replace("zdy100007", ".");
        url = url.replace("zdy100008", "#");
        return url;
    }
}
