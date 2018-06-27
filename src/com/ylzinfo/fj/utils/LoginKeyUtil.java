package com.ylzinfo.fj.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.ylz.ggyw.cache.CacheFactory;
import com.ylzinfo.cbp.IPersistence;
import com.ylzinfo.cbp.Platform;
import com.ylzinfo.jmb.AES;

/**
 * 登录校验密钥的生成和校验
 * 
 * @author zero
 * 
 */
public class LoginKeyUtil {
	static Logger logger = Logger.getLogger(LoginKeyUtil.class);
	/**
	 * 生成登录服务访问密钥
	 * 
	 * @param esbuser
	 *            esb用户
	 * @param aac002
	 *            身份证号码，未登录的传入public
	 * @return 密钥
	 */
	public static String getkey(String esbuser, String aac002) {
		logger.info("getKey:esbuser>>" + esbuser + "   aac002>>" + aac002);
		String source = "";
		String keyid = "";// 保存到缓存的key开头，保证唯一标识
		if ("public".equals(aac002)) {// 判断是否是未登录用户
			source = "public" + esbuser;
			keyid = "publickey_" + source;
		} else {
			source = aac002;
			keyid = "login_" + source;
		}
		DateFormat format = new SimpleDateFormat("yyyyMMdd");
		String key = format.format(new Date());
		// 从缓存中查找是否有记录，有则获取返回回去，无则生成key
		Object object = CacheFactory.getCacheClient("key").getAsSession(keyid);
		if (object == null) {
			String target = AES.encrypttoStr(source, key);// key用AES加密
			CacheFactory.getCacheClient("key").putAsSession(keyid, target);
			return target;
		} else {
			return (String) object;
		}
	}

	/**
	 * 校验用户登录的密钥，判断用户是否登录
	 * 
	 * @param esbuser
	 *            ESB用户
	 * @param aac002
	 *            身份证号码，公共查询传入“public”
	 * @param key
	 *            校验的密钥，
	 * @return 成功 OK； 失败，格式为“#失败原因编号,失败原因中文说明”
	 * @throws ParseException
	 */
	public static String checkKey(String esbuser, String aac002, String key) throws ParseException {
		logger.info(esbuser + ">>" + aac002 + ">>" + key);
		IPersistence ip = Platform.getPersistence();
		Date today = ip.getDBTimestamp();
		DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		Object object = CacheFactory.getCacheClient("forevercache").get("ss04_" + esbuser);
		if (object != null) {// 渠道固定key校验
			Map ss04Map = (Map) object;
			// 校验key是否一致及key的截止有效日期
			if (today.after(format.parse((String) ss04Map.get("yse077")))) {
				return "#2,未登录3";
			} else if (!key.equals((String) ss04Map.get("yse072"))) {
				return "#2,未登录2";
			} else {
				return "OK";
			}
		} else {
			String keyid = "";
			String keyvalue = "";
			if (aac002.equals("public")) {
				//只校验公共key
				keyid = "publickey_" + aac002 + esbuser;
			} else if (aac002.indexOf("public_") != -1) {            
				//校验公共key或登陆key
				if (aac002.length() == 25) {
					keyid = "login_" + aac002.substring(7, 25);
				} else {
					keyid = "publickey_" + "public" + esbuser;
				}
			} else {
				//校验登陆key
				keyid = "login_" + aac002;
			}
			keyvalue = (String) CacheFactory.getCacheClient("key").getAsSession(keyid);
			if (StringUtils.isEmpty(keyvalue)) {
				if (aac002.indexOf("public_") != -1) {
					//校验公共key或登陆key
					if (keyid.equals("login_" + aac002.substring(7, 25))) {
						keyid = "publickey_" + "public" + esbuser;
					} else {
						keyid = "login_" + aac002.substring(7, 25);
					}
					keyvalue = (String) CacheFactory.getCacheClient("key").getAsSession("publickey_" + "public" + esbuser);
					if (StringUtils.isEmpty(keyvalue)) {
						return "#2,未登录1";
					} else if (!keyvalue.equals(key)) {
						return "#2,未登录2";
					}
					return "OK";
				}
				return "#2,未登录1";
			} else if (!keyvalue.equals(key)) {
				if (aac002.indexOf("public_") != -1) {
					//校验公共key或登陆key
					if (keyid.equals("login_" + aac002.substring(7, 25))) {
						keyid = "publickey_" + "public" + esbuser;
					} else {
						keyid = "login_" + aac002.substring(7, 25);
					}
					keyvalue = (String) CacheFactory.getCacheClient("key").getAsSession("publickey_" + "public" + esbuser);
					if (StringUtils.isEmpty(keyvalue)) {
						return "#2,未登录1";
					} else if (!keyvalue.equals(key)) {
						return "#2,未登录2";
					}
					return "OK";
				}
				return "#2,未登录2";
			} else {
				return "OK";
			}
		}
	}
	public static void main(String[] args) {
	//	System.out.println(getkey("fjweb", "350784196907184243"));//35011119730223052X
	}
}
