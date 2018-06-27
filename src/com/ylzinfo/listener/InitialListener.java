/**
 * 
 */
package com.ylzinfo.listener;

import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.ylzinfo.cbp.IPersistence;
import com.ylzinfo.cbp.Platform;
import com.ylzinfo.cbp.exception.YLZCBPException;
import com.ylzinfo.common.GlobalConst;
import com.ylzinfo.listener.datasource.DBDTO;
import com.ylzinfo.listener.datasource.DataSource;
import com.ylzinfo.listener.esb.ESBConfig;
import com.ylzinfo.listener.esb.ESBConfigDTO;

/**
 * @author pc
 * 
 */
public class InitialListener implements ServletContextListener {

	/*
	 * (non-Javadoc)
	 * 
	 * @seejavax.servlet.ServletContextListener#contextDestroyed(javax.servlet.
	 * ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent sce) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.ServletContextListener#contextInitialized(javax.servlet
	 * .ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		init();

	}

	private void init() {
//		try {
//			Timer timer = new Timer();
//			//重载永久有效缓存
//			ReloadParaToCacheTask cacheTask = new ReloadParaToCacheTask();
//			//频率为28天
//			timer.schedule(cacheTask, 0, 600000 * 24 * 28);
//		} catch (Exception e) {
//			throw new YLZCBPException(e);
//		}
		try {
			java.io.InputStream is = InitialListener.class.getClassLoader().getResourceAsStream("config.cfg.xml");
			SAXReader reader = new SAXReader();
			Document doc = reader.read(is);
			Element root = doc.getRootElement();
			
			Element esbsEl = root.element("esbs");
			List esblist = esbsEl.elements("esb");
			Iterator esbit = esblist.iterator();
			do {
				if (!esbit.hasNext())
					break;
				Element el = (Element) esbit.next();
				Attribute attrName = el.attribute("name");
				Attribute attrurl = el.attribute("url");
				Attribute username = el.attribute("username");
				Attribute passwd = el.attribute("passwd");
				if (attrName != null && !attrName.getValue().trim().equals("")) {
					ESBConfigDTO dto = new ESBConfigDTO();
					dto.setName(attrName.getValue());
					dto.setUrl(attrurl.getValue());
					dto.setUsername(username.getValue());
					dto.setPasswd(passwd.getValue());
					ESBConfig.configs.put(attrName.getValue(), dto);
				}
			} while (true);

			Element paramsEl = root.element("datasource");
			List param = paramsEl.elements("db");
			Iterator it = param.iterator();
			do {
				if (!it.hasNext())
					break;
				Element el = (Element) it.next();
				Attribute attrName = el.attribute("name");
				Attribute attrurl = el.attribute("url");
				Attribute username = el.attribute("username");
				Attribute passwd = el.attribute("passwd");
				if (attrName != null && !attrName.getValue().trim().equals("")) {
					DBDTO dto = new DBDTO();
					dto.setName(attrName.getValue());
					dto.setUrl(attrurl.getValue());
					dto.setUsername(username.getValue());
					dto.setPasswd(passwd.getValue());
					DataSource.dbs.put(attrName.getValue(), dto);
				}
			} while (true);

			Element paramspath = root.element("pdf");
			Element path = paramspath.element("tmppath");
			GlobalConst.PDF_TMP_PATH = path.attributeValue("value");
			
			 Element paramsel = root.element("params");
		      List AESParam = paramsel.elements("param");
		      Iterator iterator = AESParam.iterator();

		      while (iterator.hasNext())
		      {
		        Element el = (Element)iterator.next();
		        Attribute attrName = el.attribute("name");
		        Attribute attrvalue = el.attribute("value");
		        if ((attrName != null) && (!(attrName.getValue().trim().equals(""))));
		        GlobalConst.globalvalue.put(attrName.getValue(), attrvalue.getValue());
		      }
		} catch (Exception e) {
			throw new YLZCBPException(e);
		}
	}
}
