package com.ylzinfo.util;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;

public class DBQueryUtil {
	static Logger logger = Logger.getLogger(DBQueryUtil.class);

	/**
	 * 通用查询
	 * @param dbname 数据源名称，需要在config.cfg.xml配置
	 * @param sql 查询sql语句
	 * @param clazz 返回list的DTO类型
	 * @return
	 * @throws Exception 
	 */
	public static List query(String dbname, String sql, Class clazz) throws Exception{
		ResultSet rs = null;
		Statement stmt = null;
		Connection conn = null;
		List list = new ArrayList();
		try {
			conn = JdbcUtil.getConnection(dbname);
			logger.error("1.数据库连接创建成功！");
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			logger.error("2.执行查询成功！");
			while (rs.next()) {

				Object row = clazz.newInstance();
				Field[] fields = clazz.getDeclaredFields();
				for(Field f:fields){
					try{
						String value = rs.getString(f.getName());
						PropertyUtils.setProperty(row, f.getName(), value.trim());
					}
					catch(Exception e){
						
					}
				}
				list.add(row);
			}
		} catch (Exception e) {
			logger.error(e);
			throw e;
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException e) {
				logger.error(e);
			}
		}
		logger.error("3.获取数据成功返回！");
		return list;
	}
}
