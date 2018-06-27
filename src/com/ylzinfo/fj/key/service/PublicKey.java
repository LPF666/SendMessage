/**
 * 
 */
package com.ylzinfo.fj.key.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Map;

import net.sf.json.JSONArray;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;

import com.ylzinfo.cbp.IPersistence;
import com.ylzinfo.cbp.Platform;
import com.ylzinfo.esb.soap.ReaderSoapXmlOut;
import com.ylzinfo.fj.key.dto.KeyDTO;
import com.ylzinfo.fj.key.po.PublicKeyPO;
import com.ylzinfo.fj.utils.LoginKeyUtil;

/**
 * 获取公共查询的密钥
 * 
 * @author zero
 * 
 */
public class PublicKey {
	Logger logger = Logger.getLogger(PublicKey.class);
	IPersistence ip = Platform.getPersistence();

	/**
	 * 获取公共查询的密钥
	 * 
	 * @param esbuser
	 *            esb用户
	 * @param termid
	 *            终端编号
	 * @return
	 */
	public String getpublicKey(String esbuser, String termid) {
		KeyDTO dto = new KeyDTO();
		dto.setKey(LoginKeyUtil.getkey(esbuser, "public"));
		return ReaderSoapXmlOut.readerSoapXMlOut(dto.getColset(), dto.getStructsList(), "");
	}

	/**
	 * 校验登录密钥，用于ESB总线调用
	 * 
	 * @param json
	 *            esb总线传入的json字符串
	 * @return
	 */
	public String checkKey(String json) {
		logger.info("json:" + json);
		JSONArray array = JSONArray.fromObject(json);
		KeyDTO dto = new KeyDTO();
		for (int i = 0; i < array.size(); i++) {
			Map map = (Map) array.get(i);
			Iterator it = map.entrySet().iterator();
			while (it.hasNext()) {
				// entry的输出结果如key0=value0等
				Map.Entry entry = (Map.Entry) it.next();
				try {
					PropertyUtils.setProperty(dto, (String) entry.getKey(), ((String) entry.getValue()).trim());
				} catch (IllegalAccessException e) {
				} catch (InvocationTargetException e) {
				} catch (NoSuchMethodException e) {
				}
			}
		}
		PublicKeyPO po = new PublicKeyPO();
		return po.checkKey(dto);
	}

}
