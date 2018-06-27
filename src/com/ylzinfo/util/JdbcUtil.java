package com.ylzinfo.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.ylzinfo.listener.datasource.DBDTO;
import com.ylzinfo.listener.datasource.DataSource;

public class JdbcUtil {
	static Logger logger = Logger.getLogger(JdbcUtil.class);

	public static Connection getConnection(String dbname) throws Exception {
		Connection conn = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			DBDTO dto = DataSource.dbs.get(dbname);
			if(null == dto){
				throw new Exception("找不到"+dbname+"数据源配置信息！");
			}

			conn = DriverManager.getConnection(dto.getUrl(), dto.getUsername(), dto.getPasswd());

		} catch (ClassNotFoundException e) {
			logger.error(e);
			throw e;
		} catch (SQLException e) {
			logger.error(e);
			throw e;
		} catch (Exception e) {
			logger.error(e);
			throw e;
		}
		return conn;
	}
}
