package com.ylzinfo.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

public class JndiUtil {
	static Logger logger = Logger.getLogger(JndiUtil.class);
	
	public static Connection getConnection(String provider_url, String jndi_name,
			String weblogic_user, String weblogic_pwd) throws Exception {
		Hashtable ht = new Hashtable();
		ht.put(Context.INITIAL_CONTEXT_FACTORY,
				"weblogic.jndi.WLInitialContextFactory");
		ht.put(Context.PROVIDER_URL, provider_url);
		Context context;
		try {
			context = new InitialContext(ht);
		} catch (NamingException e) {
			logger.error(e);
			throw e;
		} 
		// 注意：lookup 中的参数
		// 是你在weblogic中配置的JNDI名称
		DataSource ds = null;
		Connection conn = null;
		try {
			ds = (DataSource) context.lookup(jndi_name);// 配置的jndi名
			conn = ds.getConnection(weblogic_user, weblogic_pwd); // 登陆weblogic的用户名、密码
		} catch (NamingException e) {
			logger.error(e);
			throw e;
		} 
		catch (SQLException e) {
			logger.error(e);
			throw e;
		}
		
		return conn;
	}
}
