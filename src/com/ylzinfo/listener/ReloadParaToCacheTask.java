package com.ylzinfo.listener;

import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.ylzinfo.cbp.IPersistence;
import com.ylzinfo.cbp.Platform;
import com.ylzinfo.fj.cache.po.CachePO;

/**
 * @description 定时重载参数到缓存
 * @author linxiaoling
 * @date 2014-8-11 下午02:42:35
 */
public class ReloadParaToCacheTask extends TimerTask {
	Logger logger = Logger.getLogger(ReloadParaToCacheTask.class);
	IPersistence ip = Platform.getPersistence();

	@Override
	public void run() {
		try {
			CachePO po = new CachePO();
			//重载所有永久参数到缓存
			po.reloadAllCache();
		} catch (Exception e) {
			logger.error(e);
		}
	}

}
