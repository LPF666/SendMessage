package com.ylzinfo.fj.cache.service;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.ylzinfo.cbp.IPersistence;
import com.ylzinfo.cbp.Platform;
import com.ylzinfo.esb.soap.ReaderSoapXmlOut;
import com.ylzinfo.fj.cache.dto.CacheReturnDTO;
import com.ylzinfo.fj.cache.po.CachePO;

/**
 * @description 缓存重载
 * @author linxiaoling
 * @date 2014-8-11 下午02:51:49
 */
public class CacheService {
	Logger logger = Logger.getLogger(CacheService.class);
	IPersistence ip = Platform.getPersistence();

	/**
	 * 重载所有缓存
	 */
	public String reloadAllCache() {
		CacheReturnDTO dto = new CacheReturnDTO();
		try {
			CachePO po = new CachePO();
			po.reloadAllCache();
			dto.setAae314("1");
			dto.setAae317("重载缓存成功！");
		} catch (Exception e) {
			dto.setAae314("0");
			dto.setAae317("重载缓存失败！");
			logger.error(e);
		}
		return ReaderSoapXmlOut.readerSoapXMlOut(dto.getColset(), dto.getStructsList(), "");
	}

	/**
	 * 单条服务重载到缓存
	 */
	public String reloadSe35ToCache(String ysz053) {
		CacheReturnDTO dto = new CacheReturnDTO();
		if (StringUtils.isEmpty(ysz053)) {
			dto.setAae314("0");
			dto.setAae317("服务ID不能为空！");
			return ReaderSoapXmlOut.readerSoapXMlOut(dto.getColset(), dto.getStructsList(), "");
		}
		try {
			CachePO po = new CachePO();
			String result = po.reloadSe35ToCache(ysz053);
			if(result.equals("OK")){
				dto.setAae314("1");
				dto.setAae317("服务重载成功！");
			}else{
				dto.setAae314("0");
				dto.setAae317(result);
			}
			
		} catch (Exception e) {
			dto.setAae314("0");
			dto.setAae317("服务重载失败！");
			logger.error(e);
		}
		return ReaderSoapXmlOut.readerSoapXMlOut(dto.getColset(), dto.getStructsList(), "");
	}

	/**
	 * 单个固定key重载到缓存
	 */
	public String reloadSs04ToCache(String ysz065) {
		CacheReturnDTO dto = new CacheReturnDTO();
		if (StringUtils.isEmpty(ysz065)) {
			dto.setAae314("0");
			dto.setAae317("渠道固定keyd的ID不能为空！");
			return ReaderSoapXmlOut.readerSoapXMlOut(dto.getColset(), dto.getStructsList(), "");
		}
		try {
			CachePO po = new CachePO();
			String result = po.reloadSs04ToCache(ysz065);
			if (result.endsWith("OK")) {
				dto.setAae314("1");
				dto.setAae317("渠道固定key重载成功！");
			} else {
				dto.setAae314("0");
				dto.setAae317(result);
			}

		} catch (Exception e) {
			dto.setAae314("0");
			dto.setAae317("渠道固定key重载失败！");
			logger.error(e);
		}
		return ReaderSoapXmlOut.readerSoapXMlOut(dto.getColset(), dto.getStructsList(), "");
	}

	/**
	 * 单个不校验登录密钥重载到缓存
	 */
	public String reloadSs02ToCache(String ysz038) {
		CacheReturnDTO dto = new CacheReturnDTO();
		if (StringUtils.isEmpty(ysz038)) {
			dto.setAae314("0");
			dto.setAae317("不校验登录密钥ID不能为空！");
			return ReaderSoapXmlOut.readerSoapXMlOut(dto.getColset(), dto.getStructsList(), "");
		}
		try {
			CachePO po = new CachePO();
			String result = po.reloadSs02ToCache(ysz038);
			if (result.equals("OK")) {
				dto.setAae314("1");
				dto.setAae317("不校验登录密钥重载成功！");
			} else {
				dto.setAae314("0");
				dto.setAae317(result);
			}
		} catch (Exception e) {
			dto.setAae314("0");
			dto.setAae317("不校验登录密钥重载失败！");
			logger.error(e);
		}
		return ReaderSoapXmlOut.readerSoapXMlOut(dto.getColset(), dto.getStructsList(), "");
	}
}
