package com.ylzinfo.esb.soap;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.collections.map.ListOrderedMap;
import sun.misc.BASE64Encoder;
import com.ylzinfo.esb.util.StringUtil;
import com.ylzinfo.esb.webservice.gen.Structs;

/***
 * 解析soap输出规范
 * Copyright ylzinfo Corporation. All rights reserved.
 * @author:    LvRongLin
 * History:  2009-11-17 Created.
 * Version: 1.0
 */
public class ReaderSoapXmlOut {

	/***
	 *   
	 * @param colset
	 * @param structsList
	 * @param msgInfo
	 * @return
	 */
	public static String readerSoapXMlOut(List colset,List<Structs> structsList,String msgInfo){
		//构建标准Soap输出规范
		StringBuffer reXML = new StringBuffer();
		reXML.append(SoapEnvelop.XML_PI)
		.append(SoapEnvelop.ENVELOP_START) //信封
		.append(SoapEnvelop.HEAD_START)    //soap:header
		.append(SoapEnvelop.HEAD_END)      
		.append(SoapEnvelop.BODY_START);   //soap:body
		String structs=BuildStructs.getStructsData(structsList);//结构体
		String[] structsRows=structs.split("<row");//结构体的行数
		String[] structsColumnName=new String[structsRows.length-1];//结构体的字段名称
		String[] structsColumnType=new String[structsRows.length-1];//结构体的字段类型
		for(int i=1;i<structsRows.length;i++)
		{
			structsColumnName[i-1]=structsRows[i].split("columnname=\"")[1].split("\"")[0];//字段名称
			structsColumnType[i-1]=structsRows[i].split("typename=\"")[1].split("\"")[0];//字段类型
		}
		if(colset==null||colset.size()==0)  //找不到结果
		{   
			reXML.append(SoapEnvelop.FAULT_START)   //soap:fault
			.append(SoapEnvelop.FAULT_CODE_START) //faultcode
			.append("700")   //500：client端出错;600:Esb端出错;700:service端出错
			.append(SoapEnvelop.FAULT_CODE_END)
			.append(SoapEnvelop.FAULT_STR_START) //faultstring
			.append(SoapEnvelop.ERROR_MSG_START) //error msg
			.append(msgInfo)
			.append(SoapEnvelop.ERROR_MSG_END)
			.append(SoapEnvelop.FAULT_STR_END)
			.append(SoapEnvelop.FAULT_END);
			reXML.append(SoapEnvelop.BODY_END)
			.append(SoapEnvelop.ENVELOP_END);
			return reXML.toString();

		}
		else if(colset.size()==1)  //单条记录集
		{	 reXML.append(SoapEnvelop.BUSINESS_START); //out:business
		reXML.append(SoapEnvelop.Single_Row);
		}
		else if(colset.size()>1)
		{
			reXML.append(SoapEnvelop.BUSINESS_START); //out:business
			reXML.append(SoapEnvelop.Multilateral_Row); //多条记录集
		}
		reXML.append(SoapEnvelop.Structs_START) 
		.append(structs)
		.append(SoapEnvelop.Structs_END)
		.append(SoapEnvelop.Retrieve_START);
		try {     		
			for(Iterator it=colset.iterator();it.hasNext();)
			{
				ListOrderedMap bean=(ListOrderedMap)it.next(); 
				reXML.append("<row ");
				for(int i=0;i<structsColumnName.length;i++)
				{ 
					reXML.append(structsColumnName[i])
					.append("=\"");
					//判断字段类型 如果是blob类型 则需要用64位加码
					if("blob".equalsIgnoreCase(structsColumnType[i]))
					{
						byte[] photograph=(byte[])bean.get(structsColumnName[i]);
						String base64Data=null;
						if(photograph!=null)
							base64Data=getBase64Data(photograph);
						reXML.append(base64Data).append("\" ") ;
					}
					else if("clob".equalsIgnoreCase(structsColumnType[i]))
					{
						String photograph=(String)bean.get(structsColumnName[i]);
						reXML.append(photograph==null ? "":photograph.toString()).append("\" ") ;;
					}
					else if(bean.get(structsColumnName[i])!=null)
						reXML.append(bean.get(structsColumnName[i]).toString()).append("\" ") ;
					else
						reXML.append("\" ") ;  
					if(i==structsColumnName.length-1)
						reXML.append("/>") ;  
				}
			}		
		} catch (Exception e) {
			return BuildSoapXMlError("700",e.getMessage()); //返回出错信息
		}
		reXML.append(SoapEnvelop.Retrieve_END)
		.append(SoapEnvelop.BUSINESS_END)
		.append(SoapEnvelop.BODY_END)
		.append(SoapEnvelop.ENVELOP_END);
		return reXML.toString();
	}


	/**
	 * @param colset
	 * @param totalnum
	 * @param structsList
	 * @param msgInfo
	 * @return
	 */
	public static String readerSoapXMlOut(List colset, int totalnum,
			List<Structs> structsList, String msgInfo,String serviceName) {
		// 构建标准Soap输出规范
		StringBuffer reXML = new StringBuffer();
		reXML.append(SoapEnvelop.XML_PI).append(SoapEnvelop.ENVELOP_START) // 信封
		.append(SoapEnvelop.HEAD_START) // soap:header
		.append(SoapEnvelop.HEAD_END).append(SoapEnvelop.BODY_START); // soap:body
		String structs = BuildStructs.getStructsData(structsList);// 结构体
		String[] structsRows = structs.split("<row");// 结构体的行数
		String[] structsColumnName = new String[structsRows.length - 1];// 结构体的字段名称
		String[] structsColumnType = new String[structsRows.length - 1];// 结构体的字段类型
		for (int i = 1; i < structsRows.length; i++) {
			structsColumnName[i - 1] = structsRows[i].split("columnname=\"")[1].split("\"")[0];// 字段名称
			structsColumnType[i - 1] = structsRows[i].split("typename=\"")[1].split("\"")[0];// 字段类型
		}
		if (colset == null || colset.size() == 0) // 找不到结果
		{
			reXML.append(SoapEnvelop.FAULT_START) // soap:fault
			.append(SoapEnvelop.FAULT_CODE_START) // faultcode
			.append("700") // 500：client端出错;600:Esb端出错;700:service端出错
			.append(SoapEnvelop.FAULT_CODE_END).append(
					SoapEnvelop.FAULT_STR_START) // faultstring
					.append(SoapEnvelop.ERROR_MSG_START) // error msg
					.append(msgInfo).append(SoapEnvelop.ERROR_MSG_END).append(
							SoapEnvelop.FAULT_STR_END).append(
									SoapEnvelop.FAULT_END);
			reXML.append(SoapEnvelop.BODY_END).append(SoapEnvelop.ENVELOP_END);
			return reXML.toString();

		} else if (colset.size() == 1) // 单条记录集
		{
			reXML.append(SoapEnvelop.BUSINESS_START); // out:business
			reXML.append(SoapEnvelop.Single_Row);
		} else if (colset.size() > 1) {
			reXML.append(SoapEnvelop.BUSINESS_START); // out:business
			reXML.append(SoapEnvelop.Multilateral_Row); // 多条记录集
		}
		reXML.append(SoapEnvelop.ServiceID_Start);
		reXML.append(serviceName);
		reXML.append(SoapEnvelop.ServiceID_End);
		if (totalnum != 0)
			reXML.append(SoapEnvelop.TotalNum_Start).append(totalnum).append(
					SoapEnvelop.TotalNum_End); // 查询条件总记录条数
		reXML.append(SoapEnvelop.Structs_START).append(structs).append(
				SoapEnvelop.Structs_END).append(SoapEnvelop.Retrieve_START);
		try {
			for (Iterator it = colset.iterator(); it.hasNext();) {
				ListOrderedMap bean = (ListOrderedMap) it.next();
				reXML.append("<row ");
				for (int i = 0; i < structsColumnName.length; i++) {
					reXML.append(structsColumnName[i]).append("=\"");
					// 判断字段类型 如果是blob类型 则需要用64位加码
					if("blob".equalsIgnoreCase(structsColumnType[i])) {
						byte[] photograph = (byte[]) bean
						.get(structsColumnName[i]);
						String base64Data = null;
						if (photograph != null)
							base64Data = getBase64Data(photograph);
						reXML.append(base64Data).append("\" ");
					} else if("clob".equalsIgnoreCase(structsColumnType[i])) {
						String photograph = (String) bean.get(structsColumnName[i]);
						reXML.append(photograph == null ? "" : photograph.toString()).append("\" ");
					} else if (bean.get(structsColumnName[i]) != null)
						  reXML.append((bean.get(structsColumnName[i]).toString())).append("\" ");
					else
						reXML.append("\" ");
					if (i == structsColumnName.length - 1)
						reXML.append("/>");
				}
			}
		} catch (Exception e) {
			return BuildSoapXMlError("700", e.getMessage()); // 返回出错信息
		}
		reXML.append(SoapEnvelop.Retrieve_END).append(SoapEnvelop.BUSINESS_END).append(SoapEnvelop.BODY_END).append(SoapEnvelop.ENVELOP_END);
		return reXML.toString();
	}


	/**
	 * @param colset
	 * @param totalnum
	 * @param structsList
	 * @param msgInfo
	 * @return
	 */
	public static String readerSoapXMlOut(List colset,String cpage,int rows,int totalnum,
			List<Structs> structsList, String msgInfo,String serviceName) {
		// 构建标准Soap输出规范
		StringBuffer reXML = new StringBuffer();
		reXML.append(SoapEnvelop.XML_PI).append(SoapEnvelop.ENVELOP_START) // 信封
		.append(SoapEnvelop.HEAD_START) // soap:header
		.append(SoapEnvelop.HEAD_END).append(SoapEnvelop.BODY_START); // soap:body
		if (colset == null || colset.size() == 0) // 找不到结果
		{
			reXML.append(SoapEnvelop.FAULT_START) // soap:fault
			.append(SoapEnvelop.FAULT_CODE_START) // faultcode
			.append("700") // 500：client端出错;600:Esb端出错;700:service端出错
			.append(SoapEnvelop.FAULT_CODE_END).append(
					SoapEnvelop.FAULT_STR_START) // faultstring
					.append(SoapEnvelop.ERROR_MSG_START) // error msg
					.append(msgInfo).append(SoapEnvelop.ERROR_MSG_END).append(
							SoapEnvelop.FAULT_STR_END).append(
									SoapEnvelop.FAULT_END);
			reXML.append(SoapEnvelop.BODY_END).append(SoapEnvelop.ENVELOP_END);
			return reXML.toString();
		} 
		if (colset.size() > 20000) // 查找到的结果过多
		{
			reXML.append(SoapEnvelop.FAULT_START) // soap:fault
			.append(SoapEnvelop.FAULT_CODE_START) // faultcode
			.append("700") // 500：client端出错;600:Esb端出错;700:service端出错
			.append(SoapEnvelop.FAULT_CODE_END).append(
					SoapEnvelop.FAULT_STR_START) // faultstring
					.append(SoapEnvelop.ERROR_MSG_START) // error msg
					.append("查询结果超过20000条").append(SoapEnvelop.ERROR_MSG_END).append(
							SoapEnvelop.FAULT_STR_END).append(
									SoapEnvelop.FAULT_END);
			reXML.append(SoapEnvelop.BODY_END).append(SoapEnvelop.ENVELOP_END);
			return reXML.toString();
		} 
		reXML.append(SoapEnvelop.BUSINESS_START); // out:business
		
		
		String[] structsColumnName =null;
		String[] structsColumnType =null;
		
		if(structsList.size()!=0){
			String structs = BuildStructs.getStructsData(structsList);// 结构体
			String[] structsRows = structs.split("<row");// 结构体的行数
			structsColumnName = new String[structsRows.length - 1];// 结构体的字段名称
			structsColumnType = new String[structsRows.length - 1];// 结构体的字段类型
			for (int i = 1; i < structsRows.length; i++) {
				structsColumnName[i - 1] = structsRows[i].split("columnname=\"")[1].split("\"")[0];// 字段名称
				structsColumnType[i - 1] = structsRows[i].split("typename=\"")[1].split("\"")[0];// 字段类型
			}
			
			reXML.append(SoapEnvelop.Structs_START).append(structs).append(SoapEnvelop.Structs_END);
		}
		
		
		if(cpage!=null){  //当前页
			reXML.append(SoapEnvelop.CPAGE_Start).append(cpage).append(SoapEnvelop.CPAGE_End); // 当前页
			int pages=totalnum%rows==0 ? totalnum/rows:totalnum/rows+1; // 总页数
			reXML.append(SoapEnvelop.PAGES_Start).append(pages).append(SoapEnvelop.PAGES_End);
			reXML.append(SoapEnvelop.ROWCOUNT_Start).append(totalnum).append(SoapEnvelop.ROWCOUNT_End); // 总条数
		}
		else if (totalnum != 0)
			reXML.append(SoapEnvelop.TotalNum_Start).append(totalnum).append(SoapEnvelop.TotalNum_End); // 查询条件总记录条数	
		reXML.append(SoapEnvelop.Retrieve_START);
		try {
		if(structsList.size()!=0){
			for (Iterator it = colset.iterator(); it.hasNext();) {
				ListOrderedMap bean = (ListOrderedMap) it.next();
				reXML.append("<row ");
				for (int i = 0; i < structsColumnName.length; i++) {
					reXML.append(structsColumnName[i]).append("=\"");
					// 判断字段类型 如果是blob类型 则需要用64位加码
					if("blob".equalsIgnoreCase(structsColumnType[i])) {
						byte[] photograph = (byte[]) bean
						.get(structsColumnName[i]);
						String base64Data = null;
						if (photograph != null)
							base64Data = getBase64Data(photograph);
						reXML.append(base64Data).append("\" ");
					} else if("clob".equalsIgnoreCase(structsColumnType[i])){
						String photograph = (String) bean.get(structsColumnName[i]);
						reXML.append(photograph == null ? "" : photograph.toString()).append("\" ");
					} else if (bean.get(structsColumnName[i]) != null){
						//"<",">"转义
						reXML.append(StringUtil.encode(bean.get(structsColumnName[i]).toString())).append("\" ");
					}
					else
						reXML.append("\" ");
					if (i == structsColumnName.length - 1)
						reXML.append("/>");
				}
			}
		}
		else{
			String[] setvalue;
			String name;
			ListOrderedMap bean;
			for (Iterator it = colset.iterator(); it.hasNext();) {
				bean = (ListOrderedMap) it.next();
				reXML.append("<row ");
				Set set =  bean.entrySet();
				for (Iterator setit = set.iterator(); setit.hasNext();) {
				     setvalue = setit.next().toString().split("=");
					 name = setvalue[0];
					//"<",">"转义
					reXML.append(name.toLowerCase()).append("=\"").append(StringUtil.encode(setvalue[1])).append("\" ");
				}
				reXML.append("/>");
	
			}
		}

		} catch (Exception e) {
			return BuildSoapXMlError("700", e.getMessage()); // 返回出错信息
		}
		reXML.append(SoapEnvelop.Retrieve_END).append(SoapEnvelop.BUSINESS_END).append(SoapEnvelop.BODY_END).append(SoapEnvelop.ENVELOP_END);
		return reXML.toString();
	}
	/**
	 * 
	 * @param colset
	 * @param totalnum
	 * @param structsList
	 * @param msgInfo
	 * @return
	 */
	public static String readerSoapXMlOut(List colset,int totalnum,List<Structs> structsList,String msgInfo){
		//构建标准Soap输出规范
		StringBuffer reXML = new StringBuffer();
		reXML.append(SoapEnvelop.XML_PI)
		.append(SoapEnvelop.ENVELOP_START) //信封
		.append(SoapEnvelop.HEAD_START)    //soap:header
		.append(SoapEnvelop.HEAD_END)      
		.append(SoapEnvelop.BODY_START);    //soap:body
		String structs=BuildStructs.getStructsData(structsList);//结构体
		String[] structsRows=structs.split("<row");//结构体的行数
		String[] structsColumnName=new String[structsRows.length-1];//结构体的字段名称
		String[] structsColumnType=new String[structsRows.length-1];//结构体的字段类型
		for(int i=1;i<structsRows.length;i++)
		{
			structsColumnName[i-1]=structsRows[i].split("columnname=\"")[1].split("\"")[0];//字段名称
			structsColumnType[i-1]=structsRows[i].split("typename=\"")[1].split("\"")[0];//字段类型
		}
		if(totalnum!=0)
			reXML.append(SoapEnvelop.TotalNum_Start).append(totalnum).append(SoapEnvelop.TotalNum_End); //查询条件总记录条数
		if(colset==null||colset.size()==0)  //找不到结果
		{   
			reXML.append(SoapEnvelop.FAULT_START)   //soap:fault
			.append(SoapEnvelop.FAULT_CODE_START) //faultcode
			.append("700")   //500：client端出错;600:Esb端出错;700:service端出错
			.append(SoapEnvelop.FAULT_CODE_END)
			.append(SoapEnvelop.FAULT_STR_START) //faultstring
			.append(SoapEnvelop.ERROR_MSG_START) //error msg
			.append(msgInfo)
			.append(SoapEnvelop.ERROR_MSG_END)
			.append(SoapEnvelop.FAULT_STR_END)
			.append(SoapEnvelop.FAULT_END);
			reXML.append(SoapEnvelop.BODY_END)
			.append(SoapEnvelop.ENVELOP_END);
			return reXML.toString();

		}
		else if(colset.size()==1)  //单条记录集
		{	 reXML.append(SoapEnvelop.BUSINESS_START); //out:business
		reXML.append(SoapEnvelop.Single_Row);
		}
		else if(colset.size()>1)
		{
			reXML.append(SoapEnvelop.BUSINESS_START); //out:business
			reXML.append(SoapEnvelop.Multilateral_Row); //多条记录集
		}
		reXML.append(SoapEnvelop.Structs_START) 
		.append(structs)
		.append(SoapEnvelop.Structs_END)
		.append(SoapEnvelop.Retrieve_START);
		try {     		
			for(Iterator it=colset.iterator();it.hasNext();)
			{
				ListOrderedMap bean=(ListOrderedMap)it.next(); 
				reXML.append("<row ");
				for(int i=0;i<structsColumnName.length;i++)
				{ 
					reXML.append(structsColumnName[i]).append("=\"");
					//判断字段类型 如果是blob类型 则需要用64位加码
					if("blob".equalsIgnoreCase(structsColumnType[i]))
					{
						byte[] photograph=(byte[])bean.get(structsColumnName[i]);
						String base64Data=null;
						if(photograph!=null)
							base64Data=getBase64Data(photograph);
						reXML.append(base64Data).append("\" ") ;
					}
					else if("clob".equalsIgnoreCase(structsColumnType[i]))
					{
						String photograph=(String)bean.get(structsColumnName[i]);
						reXML.append(photograph==null ? "":photograph.toString()).append("\" ") ;;
					}
					else if(bean.get(structsColumnName[i])!=null)
						reXML.append(bean.get(structsColumnName[i]).toString()).append("\" ") ;
					else
						reXML.append("\" ") ;  
					if(i==structsColumnName.length-1)
						reXML.append("/>") ;  
				}
			}		
		} catch (Exception e) {
			return BuildSoapXMlError("700",e.getMessage()); //返回出错信息
		}
		reXML.append(SoapEnvelop.Retrieve_END)
		.append(SoapEnvelop.BUSINESS_END)
		.append(SoapEnvelop.BODY_END)
		.append(SoapEnvelop.ENVELOP_END);
		return reXML.toString();
	}

	/**
	 * 构建导出Excel数据
	 * @param colset
	 * @param totalnum
	 * @param structsList
	 * @param msgInfo
	 * @return
	 */
	public static String readerSoapForExcel(List colset, int totalnum,
			List<Structs> structsList, String msgInfo) {
		// 构建标准Soap输出规范
		StringBuffer tString = new StringBuffer();
		String structs = BuildStructs.getStructsData(structsList);// 结构体
		String[] structsRows = structs.split("<row");// 结构体的行数
		String[] structslabelName = new String[structsRows.length - 1];
		String[] structsColumnName = new String[structsRows.length - 1];// 结构体的字段名称
		for (int k = 1; k < structsRows.length; k++) {
			structslabelName[k - 1] = structsRows[k].split("label=\"")[1].split("\"")[0];// 字段名称
			structsColumnName[k - 1] = structsRows[k].split("columnname=\"")[1].split("\"")[0];// 字段名称
		}
		int i = structsColumnName.length;
		StringBuffer stringbuffer = new StringBuffer("<table cellspacing=\"0\" cellpadding=\"5\" rules=\"all\" border=\"1\"><thead> ");
		for (int k = 0; k < i; k++) {
			stringbuffer.append("<td align=\"center\">").append(structslabelName[k]).append("</td>");
		}
		stringbuffer.append("</thead><tbody>");
		if (colset != null) 
		{
			for (Iterator iterator = colset.iterator(); iterator.hasNext();stringbuffer.append("</tr>")) {
				ListOrderedMap obj2 = (ListOrderedMap) iterator.next();
				stringbuffer.append("<tr>");
				for (int l = 0; l < i; l++) {
					Object obj1 = null;
					obj1 = obj2.get(structsColumnName[l]);
					if (obj1 == null)
						obj1 = "";
					stringbuffer.append(formatData(obj1));		
				}
			}
		}
		stringbuffer.append("</tbody></table>");
		return stringbuffer.toString();
	}

	//出错输出
	public static String BuildSoapXMlError(String  errorCode,String errorMsg) {
		//构建标准Soap输出规范
		StringBuffer reXML = new StringBuffer(SoapEnvelop.XML_PI);
		reXML.append(SoapEnvelop.ENVELOP_START) //信封
		.append(SoapEnvelop.HEAD_START)    //soap:header
		.append(SoapEnvelop.HEAD_END)      
		.append(SoapEnvelop.BODY_START);    //soap:body
		reXML.append(SoapEnvelop.FAULT_START)   //soap:fault
		.append(SoapEnvelop.FAULT_CODE_START) //faultcode
		.append(errorCode)
		.append(SoapEnvelop.FAULT_CODE_END)
		.append(SoapEnvelop.FAULT_STR_START) //faultstring
		.append(SoapEnvelop.ERROR_MSG_START) //error msg
		.append(errorMsg)
		.append(SoapEnvelop.ERROR_MSG_END)
		.append(SoapEnvelop.FAULT_STR_END)
		.append(SoapEnvelop.FAULT_END);
		reXML.append(SoapEnvelop.BODY_END)
		.append(SoapEnvelop.ENVELOP_END);
		return reXML.toString();
	} 
	
	//出错输出
	public static String BuildSoapXMlError(int errorCode,String errorMsg) {
		//构建标准Soap输出规范
		StringBuffer reXML = new StringBuffer(SoapEnvelop.XML_PI);
		reXML.append(SoapEnvelop.ENVELOP_START) //信封
		.append(SoapEnvelop.HEAD_START)    //soap:header
		.append(SoapEnvelop.HEAD_END)      
		.append(SoapEnvelop.BODY_START);    //soap:body
		reXML.append(SoapEnvelop.FAULT_START)   //soap:fault
		.append(SoapEnvelop.FAULT_CODE_START) //faultcode
		.append("700-").append(errorCode)
		.append(SoapEnvelop.FAULT_CODE_END)
		.append(SoapEnvelop.FAULT_STR_START) //faultstring
		.append(SoapEnvelop.ERROR_MSG_START) //error msg
		.append(StringUtil.encode(errorMsg))
		.append(SoapEnvelop.ERROR_MSG_END)
		.append(SoapEnvelop.FAULT_STR_END)
		.append(SoapEnvelop.FAULT_END);
		reXML.append(SoapEnvelop.BODY_END)
		.append(SoapEnvelop.ENVELOP_END);
		return reXML.toString();
	} 

	 /**
	  * @deprecated
	  * @param infoMsg
	  * @return
	  */
	//信息提示输出
	public static String BuildSoapXMlInfo(String infoMsg) {
		//构建标准Soap输出规范
		StringBuffer reXML = new StringBuffer(SoapEnvelop.XML_PI);
		reXML.append(SoapEnvelop.ENVELOP_START) //信封
		.append(SoapEnvelop.HEAD_START)    //soap:header
		.append(SoapEnvelop.HEAD_END)      
		.append(SoapEnvelop.BODY_START)    //soap:body
		.append(SoapEnvelop.BUSINESS_START); //out:business
		reXML.append(SoapEnvelop.Structs_START)
		.append(SoapEnvelop.Structs_END)
		.append(SoapEnvelop.Message_START)
		.append(infoMsg)
		.append(SoapEnvelop.Message_R)
		.append(SoapEnvelop.Message_End)
		.append(SoapEnvelop.BUSINESS_END)
		.append(SoapEnvelop.BODY_END)
		.append(SoapEnvelop.ENVELOP_END);
		return reXML.toString();
	} 
	
	/**
	 * 
	 * @param infoMsg
	 * @return
	 */
	public static String BuildSoapXMlInfo_molss(String infoMsg) {
		//构建标准Soap输出规范
		StringBuffer reXML = new StringBuffer(SoapEnvelop.XML_PI);
		reXML.append(SoapEnvelop.ENVELOP_START) //信封
		.append(SoapEnvelop.HEAD_START_MOLSS)    //soap:header
		.append(SoapEnvelop.HEAD_END)      
		.append(SoapEnvelop.BODY_START)    //soap:body
		.append(SoapEnvelop.BUSINESS_START_MOLSS); //out:business
		reXML.append(SoapEnvelop.Information_Start)
		.append(infoMsg)
		.append(SoapEnvelop.Information_End)
		.append(SoapEnvelop.BUSINESS_END)
		.append(SoapEnvelop.BODY_END)
		.append(SoapEnvelop.ENVELOP_END);
		return reXML.toString();
	} 

	/**
	 * 批量操作  执行结果 成功几条 失败几条 
	 * @param successRow
	 * @param failRow
	 * @return
	 */
	public static String BuildSoapXMl(List successRow,List failRow,String[] keySet) {  
		//构建标准Soap输出规范
		StringBuffer reXML = new StringBuffer(SoapEnvelop.XML_PI);
		reXML.append(SoapEnvelop.ENVELOP_START) //信封
		.append(SoapEnvelop.HEAD_START)    //soap:header
		.append(SoapEnvelop.HEAD_END)      
		.append(SoapEnvelop.BODY_START)    //soap:body
		.append(SoapEnvelop.BUSINESS_START) //out:business
		.append(SoapEnvelop.Retrieve_START);  
		buildRowInfo(successRow,failRow,keySet,reXML);
		reXML.append(SoapEnvelop.Retrieve_END)
		.append(SoapEnvelop.BUSINESS_END)
		.append(SoapEnvelop.BODY_END)
		.append(SoapEnvelop.ENVELOP_END);
		return reXML.toString();
	} 


	/**
	 * 构建批量  反馈结果行信息
	 * @param rowInfo
	 * @param sbXML
	 */
	private static void buildRowInfo(List successRow,List failRow,String[] keySet,StringBuffer sbXML){
		//成功row
		if(successRow!=null){
			HashMap map=null;
			for(Iterator it=successRow.iterator();it.hasNext();){
				map=(HashMap)it.next();
				sbXML.append("<row isSuccessful=\"true\" ");
				for(String key:keySet){
					if(map.containsKey("produceExeResult")){
						sbXML.append("produceExeResult=\"").append((String)map.get("produceExeResult"));
						sbXML.append("\"  ");
					}
					sbXML.append(key)
					.append("=\"")
					.append((String)map.get(key));
					sbXML.append("\"  ");
				}
				sbXML.append("/>");
			}
		}
		//失败row
		if(failRow!=null){
			HashMap map=null;
			for(Iterator it=failRow.iterator();it.hasNext();){
				map=(HashMap)it.next();
				sbXML.append("<row isSuccessful=\"false\" ");
				if(map.containsKey("failReason")){
					sbXML.append("failReason=\"");
					for(String key:keySet){
						sbXML.append(key)
						.append("=")
						.append((String)map.get(key));
					}
					sbXML.append((String)map.get("failReason"));
					sbXML.append("\"  ");
				}
				sbXML.append("/>");
			}
		}
	}

	/**
	 * 数据格式转化
	 * @param obj
	 * @return
	 */
	private static Object formatData(Object obj) {
		obj = obj != null ? obj : " ";    
		if (obj.getClass().getName().equals("java.sql.Timestamp")) {
			Timestamp timestamp = (Timestamp) obj;
			obj = new Date(timestamp.getTime());
		}
		if(isNumeric(obj))
			obj="<td style=\"vnd.ms-excel.numberformat:@\" align=\"center\">"+obj+"</td>";
		else
			obj="<td align=\"center\">"+obj+"</td>";
		return obj;
	}

	private static boolean isNumeric(Object obj)
	{
		Pattern pattern = Pattern.compile("[0-9]*"); 
		if(obj==null) //判断是不是数字
			return false;	
		Matcher isNum = pattern.matcher(obj.toString());
		if( !isNum.matches() )
		{
			return false;
		}	
		else{
			if(obj.toString().length()>6)
				return true;
			else
				return false;
		}
	}
	/**
	 * 根据照片Bolb 数据 加64码
	 * @param Blob pic
	 */
	public static String getBase64Data(byte[] buff){

		//照片数据查询
		BASE64Encoder encoder = new BASE64Encoder();
		String data=encoder.encode(buff);
		return data;
	}
}
