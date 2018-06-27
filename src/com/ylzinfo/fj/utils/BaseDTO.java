package com.ylzinfo.fj.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.map.ListOrderedMap;

import org.apache.commons.beanutils.PropertyUtils;

import com.ylzinfo.esb.webservice.gen.Structs;

/**
 * 查询服务基本DTO
 * @author zero
 *
 */
public class BaseDTO {
	
	private String msg;

	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @param msg the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * 获取返回参数字段信息
	 * @return
	 */
	public List<ListOrderedMap> getColset() {
		Field[] fieldlist = this.getClass().getDeclaredFields();
		ListOrderedMap map = new ListOrderedMap();
		for(int i = 0;i < fieldlist.length; i ++) {
			OutPara para = fieldlist[i].getAnnotation(OutPara.class);
			if(null != para) {
				String name = fieldlist[i].getName();
				try {
					Object value = PropertyUtils.getProperty(this, name);
					map.put(name, null == value?"":value.toString());
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				}
			}
		}
		List<ListOrderedMap> list = new ArrayList<ListOrderedMap>();
		list.add(map);
		return list;
	}
	
	public List<Structs> getStructsList() {
		Field[] fieldlist = this.getClass().getDeclaredFields();
		List<Structs> list = new ArrayList<Structs>();
		for(int i = 0;i < fieldlist.length; i ++) {
			OutPara para = fieldlist[i].getAnnotation(OutPara.class);
			if(null != para) {
				Structs struct = new Structs();
				struct.setColumnname(fieldlist[i].getName());
				struct.setLabel(para.label());
				list.add(struct);
			}
		}
		return list;
	}
}
