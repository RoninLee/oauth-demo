/**
 * 
 */
package com.ronin.gateway.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description
 * @author 陈贵兵
 * @Date 2020年3月3日
 */
public class ServletUtil {
	/**
	 * 获取请求的真实ip
	 * 
	 * @param request
	 * @return
	 */
	public static String getIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if (ip == null) {
			ip = "";
		}
		return ip;
	}
	/**
	 * 判断是否局域网IP
	 * @author 陈贵兵
	 */
	public static boolean internalIp(String ip) {
		if ("127.0.0.1".equals(ip)||"0:0:0:0:0:0:0:1".equals(ip)) {
			return true;
		}
		byte[] addr = sun.net.util.IPAddressUtil.textToNumericFormatV4(ip);
		return internalIp(addr);
	}

	private static boolean internalIp(byte[] addr) {
		if (addr==null||addr.length<2) {
			return false;
		}
		final byte b0 = addr[0];
		final byte b1 = addr[1];
		// 10.x.x.x/8
		final byte SECTION_1 = 0x0A;
		// 172.16.x.x/12
		final byte SECTION_2 = (byte) 0xAC;
		final byte SECTION_3 = (byte) 0x10;
		final byte SECTION_4 = (byte) 0x1F;
		// 192.168.x.x/16
		final byte SECTION_5 = (byte) 0xC0;
		final byte SECTION_6 = (byte) 0xA8;
		switch (b0) {
		case SECTION_1:
			return true;
		case SECTION_2:
			if (b1 >= SECTION_3 && b1 <= SECTION_4) {
				return true;
			}
		case SECTION_5:
			switch (b1) {
			case SECTION_6:
				return true;
			}
		default:
			return false;
		}
	}
	
}
