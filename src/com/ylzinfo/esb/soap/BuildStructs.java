package com.ylzinfo.esb.soap;


import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.map.ListOrderedMap;

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
		          .append(struct.getColumnname())
		          .append("\" typename=\"")
		          .append(type)
		          .append("\" />");
	    }
	    	return StrStructs.toString();
    }


}
