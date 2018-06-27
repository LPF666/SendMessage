package com.ylzinfo.util;


import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.map.ListOrderedMap;
import org.springframework.jdbc.core.JdbcTemplate;

import com.ylzinfo.esb.webservice.gen.Structs;

/**
 * 构建表头结构体
 * Copyright ylzinfo Corporation. All rights reserved.
 * @author:    LvRongLin
 * History:  2009-12-1 Created.
 * Version: 1.0
 */

public class BuildStructs {
	
	public static String getStructsData(List<Structs> structsList) {
	    StringBuffer StrStructs=new StringBuffer(); 
	    String type="Varchar2";
	    for(Structs struct:structsList){
	    	type=struct.getType()==null?type:struct.getType();
	    	StrStructs.append("<row ");
		          StrStructs.append(" label=\"").append(struct.getLabel()).append("\"" );
		          StrStructs.append(" columnname=\"")
		          .append(struct.getColumnname().toUpperCase())
		          .append("\" typename=\"")
		          .append(type)
		          .append("\" />");
	    }
	    	return StrStructs.toString();
    }

	
	
	 public static String getStructsData(JdbcTemplate jdbctemplate,Boolean isView,String  tableName,String columnName) {
		    StringBuffer StrStructs=new StringBuffer(""); 
		    if(isView)
		    {
		    	String[] arrayColumn=columnName.split(",");
		    	for(int i=0;i<arrayColumn.length;i++){
		    		String[] temp=arrayColumn[i].split(":");
		    		StrStructs.append("<row ");
	       		          StrStructs.append(" label=\"").append(temp[0].toUpperCase()).append("\"" );
	       		          StrStructs.append(" columnname=\"")
	       		          .append(temp[1])
	       		          .append("\" typename=\"Varchar2\" displaysize=\"20\"")
	       		          .append("/>");
	   	       		     
		    	}
		    	return StrStructs.toString();
		    }
	    	//根据其他查询条件获取字段名 
	       	String sql="select a.comments comments, b.data_length length,b.data_type type,b.column_name name from user_col_comments a ,user_tab_columns b where a.table_name = upper('"+tableName+"') "+
	       		        " and a.column_name = b.column_name and b.table_name=upper(?) and b.column_name in ("+columnName.toUpperCase()+")";

	       	List list=jdbctemplate.queryForList(sql, new Object[]{tableName.toUpperCase()}); 		
	       		if(list!=null)
	       		{
	       		  for(Iterator it=list.iterator();it.hasNext();)
	       		  {
	       			ListOrderedMap bean=(ListOrderedMap)it.next();
	       		    String name=(String)bean.get("name");
	       		    String type=(String)bean.get("type");
	       		    StrStructs.append("<row ");
	       		     if(bean.get("comments")!=null)
	       		          StrStructs.append(" label=\"").append((String)bean.get("comments")).append("\"" );
	       		     else
	       		    	  StrStructs.append(" label=\"").append(name.toLowerCase()).append("\"" );
	       		     
	       		          StrStructs.append(" columnname=\"")
	       		          .append(name.toLowerCase())
	       		          .append("\" typename=\"")
	       		          .append(type.toLowerCase())
	       		          .append("\" displaysize=\"")
	   	       		      .append(bean.get("length"))
	   	       		      .append("\"/>");
	       		  }
	       		}
	       		
	    	return StrStructs.toString();
	    }


}
