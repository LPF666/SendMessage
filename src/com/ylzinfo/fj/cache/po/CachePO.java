package com.ylzinfo.fj.cache.po;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.zreport.util.XmlBeanUtil;

import com.ylz.ggyw.cache.CacheFactory;
import com.ylzinfo.cbp.IPersistence;
import com.ylzinfo.cbp.Platform;
import com.ylzinfo.cbp.exception.YLZCBPException;
import com.ylzinfo.common.GlobalConst;
import com.ylzinfo.fj.key.entity.Se35;
import com.ylzinfo.listener.EsbUserKeyDTO;
import com.ylzinfo.listener.NoCheckServiceDTO;

/**
 * @description 缓存重载
 * @author linxiaoling
 * @date 2014-8-11 下午02:52:46
 */
public class CachePO {
	Logger logger = Logger.getLogger(CachePO.class);
	IPersistence ip = Platform.getPersistence();

	/**
	 * 重载所有参数到缓存
	 */
	public void reloadAllCache() {
		try {
			// 服务重载到缓存
			List<Se35> se35list = ip.executeSQLQuery("select a.*,b.yse151 from se35 a,se44 b where a.ysz060=b.ysz060(+) and a.ysf016='"+GlobalConst.YSF016_FW+"'", Se35.class);
			if (se35list != null && se35list.size() > 0) {
				for (int i = 0; i < se35list.size(); i++) {
					Map<String, String> se35Map = XmlBeanUtil.beanToMap(se35list.get(i));
					CacheFactory.getCacheClient("forevercache").put("se35_" + se35list.get(i).getYse073(), se35Map);
				}
			}
			// 固定key重载到缓存
			List<EsbUserKeyDTO> ss04list = ip.executeSQLQuery("select * from ss04", EsbUserKeyDTO.class);
			if (ss04list != null && ss04list.size() > 0) {
				for (int i = 0; i < ss04list.size(); i++) {
					Map<String, String> ss04Map = XmlBeanUtil.beanToMap(ss04list.get(i));
					CacheFactory.getCacheClient("forevercache").put("ss04_" + ss04list.get(i).getYse100(), ss04Map);
				}
			}
			// 不校验登录密钥重载到缓存
			List<NoCheckServiceDTO> ss02list = ip.executeSQLQuery("select yse073,yse079 from ss02", NoCheckServiceDTO.class);
			if (ss02list != null && ss02list.size() > 0) {
				for (int i = 0; i < ss02list.size(); i++) {
					CacheFactory.getCacheClient("forevercache").put("ss02_" + ss02list.get(i).getYse073(), ss02list.get(i).getYse079());
				}
			}
		} catch (Exception e) {
			logger.error(e);
			throw new YLZCBPException(e);
		}
	}

	/**
	 * 根据服务id重载服务到缓存
	 * 
	 * @param ysz053
	 *            服务id
	 */
	public String reloadSe35ToCache(String ysz053) {
		try {
			List<Se35> se35list = ip.executeSQLQuery("select a.*,b.yse151 from se35 a,se44 b where a.ysz060=b.ysz060(+) and a.ysz053=" + Long.valueOf(ysz053), Se35.class);
			if (se35list != null && se35list.size() > 0) {
				Map<String, String> se35Map = XmlBeanUtil.beanToMap(se35list.get(0));
				CacheFactory.getCacheClient("forevercache").put("se35_" + se35list.get(0).getYse073(), se35Map);
				return "OK";
			}else{
				return "表se35不存在ysz053为"+ysz053+"的记录";
			}
		} catch (Exception e) {
			logger.error(e);
			throw new YLZCBPException(e);
		}
	}

	/**
	 * 根据渠道固定key的id重载缓存
	 * 
	 * @param ysz056
	 *            渠道固定key的id
	 */
	public String reloadSs04ToCache(String ysz065) {
		try {
			List<EsbUserKeyDTO> ss04list = ip.executeSQLQuery("select * from ss04 where ysz065=" + Long.valueOf(ysz065), EsbUserKeyDTO.class);
			if (ss04list != null && ss04list.size() > 0) {
				Map<String, String> ss04Map = XmlBeanUtil.beanToMap(ss04list.get(0));
				CacheFactory.getCacheClient("forevercache").put("ss04_" + ss04list.get(0).getYse100(), ss04Map);
				return "OK";
			}else{
				return "表ss04不存在ysz038为"+ysz065+"的记录！";
			}
		} catch (Exception e) {
			logger.error(e);
			throw new YLZCBPException(e);
		}
	}

	/**
	 * 根据不校验登录密钥id重载缓存
	 * 
	 * @param ysz038
	 *            不校验登录密钥id
	 */
	public String reloadSs02ToCache(String ysz038) {
		try {
			List<NoCheckServiceDTO> ss02list = ip.executeSQLQuery("select yse073,yse079 from ss02 where ysz038=" + Long.valueOf(ysz038), NoCheckServiceDTO.class);
			if (ss02list != null && ss02list.size() > 0) {
				CacheFactory.getCacheClient("forevercache").put("ss02_" + ss02list.get(0).getYse073(), ss02list.get(0).getYse079());
				return "OK";
			}else{
				return "表ss02不存在ysz038为"+ysz038+"的记录！";
			}
		} catch (Exception e) {
			logger.error(e);
			throw new YLZCBPException(e);
		}
	}

}
