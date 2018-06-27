package com.ylzinfo.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.map.ListOrderedMap;
import org.springframework.jdbc.core.JdbcTemplate;

import sun.misc.BASE64Encoder;

import com.ylzinfo.esb.webservice.gen.Structs;

/***
 * 解析soap输出规范 Copyright ylzinfo Corporation. All rights reserved.
 * 
 * @author: LvRongLin History: 2009-11-17 Created. Version: 1.0
 */
public class ReaderSoapXmlOut {

    public static final String EQUAL_BEGIN = "=\"";

    public static final String EQUAL_END = "\" ";

    /**
     * 生成数据集的机构描述
     * 
     * @param columnName
     * @return
     * @author 秦磊
     * @date 2011-4-19
     */
    private static String getStructsData(String columnName) {
        StringBuffer StrStructs = new StringBuffer("");
        if (null != columnName && !"".equals(columnName)) {
            String[] arrayColumn = columnName.split(",");
            for (int i = 0; i < arrayColumn.length; i++) {
                String[] temp = arrayColumn[i].split(":");
                StrStructs.append("<row ");
                StrStructs.append(" label=\"").append(temp[0].toUpperCase())
                        .append("\"");
                /*StrStructs.append(" columnname=\"").append(temp[1].toLowerCase()).append(
                        "\" typename=\"Varchar2\" displaysize=\"20\"").append(
                        "/>");*/
                StrStructs.append(" columnname=\"").append(temp[1].toLowerCase()).append(
                "\" typename=\"Varchar2\"").append(
                "/>");

            }
        }
        return StrStructs.toString();
    }

    /**
     * 生成数据集的数据部分
     * 
     * @param resultSet
     * @param columnNames
     *            TODO
     * @return
     * @author 秦磊
     * @date 2011-4-19
     */
    private static String getRetrieveData(List resultSet, String[] columnNames) {
        StringBuffer strRetrieve = new StringBuffer("");
        int s = resultSet.size();
        String failReason = null;
        for (int i = 0; i < s; i++) {
            Map bean = (Map) resultSet.get(i);
            strRetrieve.append("<row ");
            /*failReason = (String) bean.get(SoapEnvelop.RESULTSET_FAILREASON);
            if (null != failReason) {
                strRetrieve.append(SoapEnvelop.RESULTSET_ISSUC).append(
                        EQUAL_BEGIN).append(false).append(EQUAL_END).append(
                        SoapEnvelop.RESULTSET_FAILREASON).append(EQUAL_BEGIN)
                        .append(failReason).append(EQUAL_END);
            } else {
                strRetrieve.append(SoapEnvelop.RESULTSET_ISSUC).append(
                        EQUAL_BEGIN).append(true).append(EQUAL_END);
            }*/
            if (null == columnNames || 0 == columnNames.length) {

                Set set = bean.keySet();
                Object[] o = set.toArray();
                columnNames = new String[o.length];
                for (int m = 0; m < o.length ; m++)
                {
                    columnNames[m] = o[m].toString();
                }
            }
            for (int j = 0; null != columnNames && j < columnNames.length; j++) {
                String key = columnNames[j];
                strRetrieve.append(key.toLowerCase()).append(EQUAL_BEGIN);
                //判断字段类型 如果是blob类型 则需要用64位加码
                /*
                 * if ("blob".equals(ks[j])) { byte[] photograph = (byte[])
                 * bean.get(ks[j]); String base64Data = null; if (photograph !=
                 * null) base64Data = getBase64Data(photograph);
                 * strRetrieve.append(base64Data).append("\" "); } else
                 */
                Object vO = bean.get(key.toLowerCase());
                if (null == vO) {
                    vO = bean.get(key.toUpperCase());
                }
                if (vO != null) {
                    strRetrieve.append(XMLUtil.formatBeforeTrans(vO.toString())).append(
                            EQUAL_END);
                } else {
                    strRetrieve.append(EQUAL_END);
                }
            }
            strRetrieve.append("/>");
        }
        return strRetrieve.toString();
    }

    /**
     * 生成数据集结构描述的标签头
     * 
     * @param resultSetName
     * @return
     * @author 秦磊
     * @date 2011-4-19
     */
    private static String getStructStart1(String resultSetName) {
        return SoapEnvelop.RESULTSET_STRUCT_START + SoapEnvelop.RESULTSET_NAME
                + resultSetName + "\"" + SoapEnvelop.COMMON_END;

    }
    private static String getStructStart(String resultSetName) {
    	if(resultSetName == null || resultSetName.equals("")){
    		return SoapEnvelop.RESULTSET_STRUCT_START + SoapEnvelop.COMMON_END;

    	}else{
    		return SoapEnvelop.RESULTSET_STRUCT_START + SoapEnvelop.RESULTSET_NAME
            + resultSetName + "\"" + SoapEnvelop.COMMON_END;

    	}
        
    }
    /**
     * 生成数据集字段结构的标签头
     * 
     * @param resultSetName
     * @param totalNum
     *            TODO
     * @return
     * @author 秦磊
     * @date 2011-4-19
     */
    private static String getRetrieveStart(String resultSetName, int totalNum) {
        String numS = "";
        if (0 < totalNum) {
            numS = SoapEnvelop.RESULTSET_TOTALNUM + totalNum + "\"";
        }
        if(resultSetName == null || resultSetName.equals("")){
        	return SoapEnvelop.RESULTSET_RETRIEVE_START
            + numS
            + SoapEnvelop.COMMON_END;

    	}else{
    		return SoapEnvelop.RESULTSET_RETRIEVE_START
            + SoapEnvelop.RESULTSET_NAME + resultSetName + "\"" + numS
            + SoapEnvelop.COMMON_END;

    	}
    }

    /**
     * 生成服务响应的SOAP字符串
     * 
     * @param entity
     * @return
     * @author 秦磊
     * @date 2011-4-2
     */
    public static String readerSoapXMLOut(ResponseEntity entity) {
        StringBuffer reXML = new StringBuffer(SoapEnvelop.XML_PI);
        //soap头部信息
        reXML.append(SoapEnvelop.ENVELOP_START)
                //信封
                .append(SoapEnvelop.HEAD_START)
                //soap:header
                .append(SoapEnvelop.HEAD_END).append(SoapEnvelop.BODY_START)
                .append(SoapEnvelop.BUSINESS_START); //soap:body

        //soap体
        if (null != entity && !entity.isOk()) {
            return BuildSoapXMlInfo(entity.getErrorMsg());
        } else if (null != entity) {
            String[] pN = entity.getParaName();
            String[] pV = entity.getParaValue();
            if (null == pV) {
                pV = new String[0];
            }
            int pNum = null == pN ? 0 : pN.length;
            //单条记录处理
            for (int i = 0; i < pNum; i++) {
                String value = null;
                if (i > pV.length || null == pV[i]) {
                    value = "";
                } else {
                    value = pV[i];
                }
                reXML.append(SoapEnvelop.RESULT_START).append(
                        pN[i].toLowerCase()).append("=").append(
                        "\"" + value + "\"").append(SoapEnvelop.RESULT_END);
            }

            //数据集处理
            ArrayList<ResultSet> rS = entity.getResultSetList();
            int rNum = null == rS ? 0 : rS.size();
            ResultSet r = null;
            int rSize = 0;
            String rName = null;
            for (int i = 0; i < rNum; i++) {
                r = (ResultSet) rS.get(i);
                rSize = null == r.getResultSet()? 0 : r.getResultSet().size();
                rName = r.getName();
                if (rSize > 0) {
                    //结构
                    reXML.append(getStructStart(rName));
                    String structs = getStructsData(r.getColumns());
                    reXML.append(structs).append(SoapEnvelop.RESULTSET_END);
                    //数据
                    reXML.append(getRetrieveStart(rName, r.getTotalNum()));
                    String retrieve = getRetrieveData(r.getResultSet(), r
                            .getColumnNames());
                    reXML.append(retrieve).append(SoapEnvelop.RESULTSET_END);
                }
            }

        }
        reXML.append(SoapEnvelop.BUSINESS_END).append(SoapEnvelop.BODY_END)
                .append(SoapEnvelop.ENVELOP_END);

        return reXML.toString();
    }

    public static String readerSoapXMlOut(JdbcTemplate jdbctemplate,
            List colset, Boolean isView, String tableName, String columnName,
            String msgInfo) {
        return readerSoapXMlOut(jdbctemplate, colset, isView, 0, tableName,
                columnName, msgInfo);
    }

    public static String readerSoapXMlOut(JdbcTemplate jdbctemplate,
            List colset, Boolean isView, int totalNum, String tableName,
            String columnName, String msgInfo) {
        //构建标准Soap输出规范
        StringBuffer reXML = new StringBuffer(SoapEnvelop.XML_PI);
        reXML.append(SoapEnvelop.ENVELOP_START) //信封
                .append(SoapEnvelop.HEAD_START) //soap:header
                .append(SoapEnvelop.HEAD_END).append(SoapEnvelop.BODY_START); //soap:body
        String structs = BuildStructs.getStructsData(jdbctemplate, isView,
                tableName, columnName);//结构体
        String[] structsRows = structs.split("<row");//结构体的行数
        String[] structsColumnName = new String[structsRows.length - 1];//结构体的字段名称
        String[] structsColumnType = new String[structsRows.length - 1];//结构体的字段类型
        for (int i = 1; i < structsRows.length; i++) {
            structsColumnName[i - 1] = structsRows[i].split("columnname=\"")[1]
                    .split("\"")[0];//字段名称
            structsColumnType[i - 1] = structsRows[i].split("typename=\"")[1]
                    .split("\"")[0];//字段类型
        }
        if (colset == null || colset.size() == 0) //找不到结果
        {
            msgInfo = msgInfo.replaceAll("\\\"", "'");
            reXML.append(SoapEnvelop.FAULT_START) //soap:fault
                    .append(SoapEnvelop.FAULT_CODE_START) //faultcode
                    .append("700") //500：client端出错;600:Esb端出错;700:service端出错
                    .append(SoapEnvelop.FAULT_CODE_END).append(
                            SoapEnvelop.FAULT_STR_START) //faultstring
                    .append(SoapEnvelop.ERROR_MSG_START) //error msg
                    .append(msgInfo).append(SoapEnvelop.ERROR_MSG_END).append(
                            SoapEnvelop.FAULT_STR_END).append(
                            SoapEnvelop.FAULT_END);
            reXML.append(SoapEnvelop.BODY_END).append(SoapEnvelop.ENVELOP_END);
            return reXML.toString();

        }
        reXML.append(SoapEnvelop.BUSINESS_START); // out:business
        if (totalNum != 0)
            reXML.append(SoapEnvelop.TotalNum_Start).append(totalNum).append(
                    SoapEnvelop.TotalNum_End); // 查询条件总记录条数
        if (colset.size() == 1) // 单条记录集
        {
            reXML.append(SoapEnvelop.Single_Row);
        }
        if (colset.size() > 1) {
            reXML.append(SoapEnvelop.Multilateral_Row); // 多条记录集
        }
        reXML.append(SoapEnvelop.Structs_START).append(structs).append(
                SoapEnvelop.Structs_END).append(SoapEnvelop.Retrieve_START);
        try {
            for (Iterator it = colset.iterator(); it.hasNext();) {
                ListOrderedMap bean = (ListOrderedMap) it.next();
                reXML.append("<row ");
                for (int i = 0; i < structsColumnName.length; i++) {
                    reXML.append(structsColumnName[i]).append("=\"");
                    //判断字段类型 如果是blob类型 则需要用64位加码
                    if ("blob".equals(structsColumnType[i])) {
                        byte[] photograph = (byte[]) bean
                                .get(structsColumnName[i]);
                        String base64Data = null;
                        if (photograph != null)
                            base64Data = getBase64Data(photograph);
                        reXML.append(base64Data).append("\" ");
                    } else if (bean.get(structsColumnName[i]) != null)
                        reXML.append(
                                XMLUtil.formatBeforeTrans(bean.get(
                                        structsColumnName[i]).toString()))
                                .append("\" ");
                    else
                        reXML.append("\" ");
                    if (i == structsColumnName.length - 1)
                        reXML.append("/>");
                }
            }
        } catch (Exception e) {
            return BuildSoapXMlError("700", e.getMessage()); //返回出错信息
        }
        reXML.append(SoapEnvelop.Retrieve_END).append(SoapEnvelop.BUSINESS_END)
                .append(SoapEnvelop.BODY_END).append(SoapEnvelop.ENVELOP_END);
        return reXML.toString();
    }

    //	  
    //	  public static String readerSoapXMlOut(List colset,List<Structs> structsList,String msgInfo){
    //	    	//构建标准Soap输出规范
    //		    String reXML = SoapEnvelop.XML_PI;
    //		    reXML+=SoapEnvelop.ENVELOP_START+SoapEnvelop.HEAD_START+SoapEnvelop.HEAD_END+SoapEnvelop.BODY_START+SoapEnvelop.BUSINESS_START;
    //	    	String structs=BuildStructs.getStructsData(structsList);//结构体
    //		    String[] structsRows=structs.split("<row");//结构体的行数
    //		    String[] structsColumnName=new String[structsRows.length-1];//结构体的字段名称
    //		    String[] structsColumnType=new String[structsRows.length-1];//结构体的字段类型
    //		    for(int i=1;i<structsRows.length;i++)
    //		      {
    //		    	structsColumnName[i-1]=structsRows[i].split("columnname=\"")[1].split("\"")[0];//字段名称
    //		    	structsColumnType[i-1]=structsRows[i].split("typename=\"")[1].split("\"")[0];//字段类型
    //		      }
    //		     if(colset==null||colset.size()==0)  //找不到结果
    //		     {
    //		    	 reXML+=SoapEnvelop.Structs_START+SoapEnvelop.Structs_END+SoapEnvelop.Message_START+msgInfo+SoapEnvelop.Message_R+SoapEnvelop.Message_End
    //		    	      +SoapEnvelop.BODY_END+SoapEnvelop.ENVELOP_END;
    //		    	     
    //			    	return reXML;
    //		     }
    //		     else if(colset.size()==1)  //单条记录集
    //		         reXML+=SoapEnvelop.Single_Row;
    //		     else if(colset.size()>1)
    //		    	 reXML+=SoapEnvelop.Multilateral_Row; //多条记录集
    //	    reXML+=SoapEnvelop.Structs_START+structs+SoapEnvelop.Structs_END+SoapEnvelop.Retrieve_START;
    //	       	try {     		
    //	       		  for(Iterator it=colset.iterator();it.hasNext();)
    //	       		  {
    //	       			ListOrderedMap bean=(ListOrderedMap)it.next(); 
    //	       	    	reXML+="<row ";
    //	       	        for(int i=0;i<structsColumnName.length;i++)
    //	       	        { 
    //	       	        	reXML+=structsColumnName[i]+"=\"";
    //	       	         //判断字段类型 如果是blob类型 则需要用64位加码
    //	       		         if(structsColumnType[i].toUpperCase().compareTo("BLOB") == 0)
    //	       		         {
    //	       		        	 byte[] photograph=(byte[])bean.get(structsColumnName[i]);
    //	       		        	 String base64Data=null;
    //	       		        	 if(photograph!=null)
    //	       		        		base64Data=getBase64Data(photograph);
    //	       		        	reXML+=base64Data+"\" " ;
    //	       		         }
    //	       		         else if(structsColumnType[i].toUpperCase().compareTo("CLOB") == 0)
    //	       		         {
    //	       		        	   String photograph=(String)bean.get(structsColumnName[i]);
    //	    						reXML+=(photograph==null ? "":photograph.toString())+"\" " ;
    //	       		         }
    //	       		         else if(bean.get(structsColumnName[i])!=null)
    //	       		        	       reXML+=(XMLUtil.formatBeforeTrans(bean.get(structsColumnName[i]).toString()))+"\" " ;
    //	       		        	  else
    //	       		        		  reXML+="\" " ;  
    //	       		         if(i==structsColumnName.length-1)
    //	       		        	reXML+="/>" ;  
    //	       		   }
    //	       		}		
    //	   		} catch (Exception e) {
    //	   			return BuildSoapXMlError("700",e.getMessage()); //返回出错信息
    //	   		}
    //	     	reXML+=SoapEnvelop.Retrieve_END+
    //	     	     (SoapEnvelop.BUSINESS_END)
    //	     	     +(SoapEnvelop.BODY_END)
    //	     	     +(SoapEnvelop.ENVELOP_END);
    //	    	return reXML;
    //	    }

    public static String readerSoapXMlOut(List colset,
            List<Structs> structsList, String msgInfo) {
        //构建标准Soap输出规范
        StringBuffer reXML = new StringBuffer();
        reXML.append(SoapEnvelop.XML_PI).append(SoapEnvelop.ENVELOP_START) //信封
                .append(SoapEnvelop.HEAD_START) //soap:header
                .append(SoapEnvelop.HEAD_END).append(SoapEnvelop.BODY_START); //soap:body
        String structs = BuildStructs.getStructsData(structsList);//结构体
        String[] structsRows = structs.split("<row");//结构体的行数
        String[] structsColumnName = new String[structsRows.length - 1];//结构体的字段名称
        String[] structsColumnType = new String[structsRows.length - 1];//结构体的字段类型
        for (int i = 1; i < structsRows.length; i++) {
            structsColumnName[i - 1] = structsRows[i].split("columnname=\"")[1]
                    .split("\"")[0];//字段名称
            structsColumnType[i - 1] = structsRows[i].split("typename=\"")[1]
                    .split("\"")[0];//字段类型
        }
        if (colset == null || colset.size() == 0) //找不到结果
        {
            reXML.append(SoapEnvelop.FAULT_START) //soap:fault
                    .append(SoapEnvelop.FAULT_CODE_START) //faultcode
                    .append("700") //500：client端出错;600:Esb端出错;700:service端出错
                    .append(SoapEnvelop.FAULT_CODE_END).append(
                            SoapEnvelop.FAULT_STR_START) //faultstring
                    .append(SoapEnvelop.ERROR_MSG_START) //error msg
                    .append(msgInfo).append(SoapEnvelop.ERROR_MSG_END).append(
                            SoapEnvelop.FAULT_STR_END).append(
                            SoapEnvelop.FAULT_END);
            reXML.append(SoapEnvelop.BODY_END).append(SoapEnvelop.ENVELOP_END);
            return reXML.toString();

        } else if (colset.size() == 1) //单条记录集
        {
            reXML.append(SoapEnvelop.BUSINESS_START); //out:business
            reXML.append(SoapEnvelop.Single_Row);
        } else if (colset.size() > 1) {
            reXML.append(SoapEnvelop.BUSINESS_START); //out:business
            reXML.append(SoapEnvelop.Multilateral_Row); //多条记录集
        }
        reXML.append(SoapEnvelop.Structs_START).append(structs).append(
                SoapEnvelop.Structs_END).append(SoapEnvelop.Retrieve_START);
        try {
            for (Iterator it = colset.iterator(); it.hasNext();) {
                ListOrderedMap bean = (ListOrderedMap) it.next();
                reXML.append("<row ");
                for (int i = 0; i < structsColumnName.length; i++) {
                    reXML.append(structsColumnName[i]).append("=\"");
                    //判断字段类型 如果是blob类型 则需要用64位加码
                    if (structsColumnType[i].toUpperCase().compareTo("BLOB") == 0) {
                        byte[] photograph = (byte[]) bean
                                .get(structsColumnName[i]);
                        String base64Data = null;
                        if (photograph != null)
                            base64Data = getBase64Data(photograph);
                        reXML.append(base64Data).append("\" ");
                    } else if (structsColumnType[i].toUpperCase().compareTo(
                            "CLOB") == 0) {
                        String photograph = (String) bean
                                .get(structsColumnName[i]);
                        reXML
                                .append(
                                        photograph == null ? "" : photograph
                                                .toString()).append("\" ");
                        ;
                    } else if (bean.get(structsColumnName[i]) != null)
                        reXML.append(
                                XMLUtil.formatBeforeTrans(bean.get(
                                        structsColumnName[i]).toString()))
                                .append("\" ");
                    else
                        reXML.append("\" ");
                    if (i == structsColumnName.length - 1)
                        reXML.append("/>");
                }
            }
        } catch (Exception e) {
            return BuildSoapXMlError("700", e.getMessage()); //返回出错信息
        }
        reXML.append(SoapEnvelop.Retrieve_END).append(SoapEnvelop.BUSINESS_END)
                .append(SoapEnvelop.BODY_END).append(SoapEnvelop.ENVELOP_END);
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
            List<Structs> structsList, String msgInfo) {
        //构建标准Soap输出规范
        StringBuffer reXML = new StringBuffer();
        reXML.append(SoapEnvelop.XML_PI).append(SoapEnvelop.ENVELOP_START) //信封
                .append(SoapEnvelop.HEAD_START) //soap:header
                .append(SoapEnvelop.HEAD_END).append(SoapEnvelop.BODY_START); //soap:body

        String structs = BuildStructs.getStructsData(structsList);//结构体
        String[] structsRows = structs.split("<row");//结构体的行数
        String[] structsColumnName = new String[structsRows.length - 1];//结构体的字段名称
        String[] structsColumnType = new String[structsRows.length - 1];//结构体的字段类型
        for (int i = 1; i < structsRows.length; i++) {
            structsColumnName[i - 1] = structsRows[i].split("columnname=\"")[1]
                    .split("\"")[0];//字段名称
            structsColumnType[i - 1] = structsRows[i].split("typename=\"")[1]
                    .split("\"")[0];//字段类型
        }
        if (colset == null || colset.size() == 0) //找不到结果
        {
            reXML.append(SoapEnvelop.FAULT_START) //soap:fault
                    .append(SoapEnvelop.FAULT_CODE_START) //faultcode
                    .append("700") //500：client端出错;600:Esb端出错;700:service端出错
                    .append(SoapEnvelop.FAULT_CODE_END).append(
                            SoapEnvelop.FAULT_STR_START) //faultstring
                    .append(SoapEnvelop.ERROR_MSG_START) //error msg
                    .append(msgInfo).append(SoapEnvelop.ERROR_MSG_END).append(
                            SoapEnvelop.FAULT_STR_END).append(
                            SoapEnvelop.FAULT_END);
            reXML.append(SoapEnvelop.BODY_END).append(SoapEnvelop.ENVELOP_END);
            return reXML.toString();

        }
        reXML.append(SoapEnvelop.BUSINESS_START); // out:business
        if (totalnum != 0)
            reXML.append(SoapEnvelop.TotalNum_Start).append(totalnum).append(
                    SoapEnvelop.TotalNum_End); // 查询条件总记录条数
        if (colset.size() == 1) // 单条记录集
        {
            reXML.append(SoapEnvelop.Single_Row);
        }
        if (colset.size() > 1) {
            reXML.append(SoapEnvelop.Multilateral_Row); //多条记录集
        }
        reXML.append(SoapEnvelop.Structs_START).append(structs).append(
                SoapEnvelop.Structs_END).append(SoapEnvelop.Retrieve_START);
        try {
            for (Iterator it = colset.iterator(); it.hasNext();) {
                ListOrderedMap bean = (ListOrderedMap) it.next();
                reXML.append("<row ");
                for (int i = 0; i < structsColumnName.length; i++) {
                    reXML.append(structsColumnName[i]).append("=\"");
                    //判断字段类型 如果是blob类型 则需要用64位加码
                    if (structsColumnType[i].toUpperCase().compareTo("BLOB") == 0) {
                        byte[] photograph = (byte[]) bean
                                .get(structsColumnName[i]);
                        String base64Data = null;
                        if (photograph != null)
                            base64Data = getBase64Data(photograph);
                        reXML.append(base64Data).append("\" ");
                    } else if (structsColumnType[i].toUpperCase().compareTo(
                            "CLOB") == 0) {
                        String photograph = (String) bean
                                .get(structsColumnName[i]);
                        reXML
                                .append(
                                        photograph == null ? "" : photograph
                                                .toString()).append("\" ");
                        ;
                    } else if (bean.get(structsColumnName[i]) != null)
                        reXML.append(
                                XMLUtil.formatBeforeTrans(bean.get(
                                        structsColumnName[i]).toString()))
                                .append("\" ");
                    else
                        reXML.append("\" ");
                    if (i == structsColumnName.length - 1)
                        reXML.append("/>");
                }
            }
        } catch (Exception e) {
            return BuildSoapXMlError("700", e.getMessage()); //返回出错信息
        }
        reXML.append(SoapEnvelop.Retrieve_END).append(SoapEnvelop.BUSINESS_END)
                .append(SoapEnvelop.BODY_END).append(SoapEnvelop.ENVELOP_END);
        return reXML.toString();
    }

    //出错输出
    public static String BuildSoapXMlError(String errorCode, String errorMsg) {
        //构建标准Soap输出规范
        StringBuffer reXML = new StringBuffer(SoapEnvelop.XML_PI);
        reXML.append(SoapEnvelop.ENVELOP_START) //信封
                .append(SoapEnvelop.HEAD_START) //soap:header
                .append(SoapEnvelop.HEAD_END).append(SoapEnvelop.BODY_START); //soap:body
        reXML.append(SoapEnvelop.FAULT_START)
                //soap:fault
                .append(SoapEnvelop.FAULT_CODE_START)
                //faultcode
                .append(errorCode).append(SoapEnvelop.FAULT_CODE_END).append(
                        SoapEnvelop.FAULT_STR_START)
                //faultstring
                .append(SoapEnvelop.ERROR_MSG_START)
                //error msg
                .append(errorMsg).append(SoapEnvelop.ERROR_MSG_END).append(
                        SoapEnvelop.FAULT_STR_END)
                .append(SoapEnvelop.FAULT_END);
        reXML.append(SoapEnvelop.BODY_END).append(SoapEnvelop.ENVELOP_END);
        return reXML.toString();
    }

    //信息提示输出
    public static String BuildSoapXMlInfo(String infoMsg) {
        //构建标准Soap输出规范
        StringBuffer reXML = new StringBuffer(SoapEnvelop.XML_PI);
        reXML.append(SoapEnvelop.ENVELOP_START) //信封
                .append(SoapEnvelop.HEAD_START) //soap:header
                .append(SoapEnvelop.HEAD_END).append(SoapEnvelop.BODY_START) //soap:body
                .append(SoapEnvelop.BUSINESS_START); //out:business
        reXML.append(SoapEnvelop.Structs_START).append(SoapEnvelop.Structs_END)
                .append(SoapEnvelop.Message_START).append(change(infoMsg)).append(
                        SoapEnvelop.Message_R).append(SoapEnvelop.Message_End)
                .append(SoapEnvelop.BUSINESS_END).append(SoapEnvelop.BODY_END)
                .append(SoapEnvelop.ENVELOP_END);
        return reXML.toString();
    }
    
    private static String change(String msg) {
        String r = null;
        if (null != msg && !"".equals(msg)) {
            r = msg.replace("\"", "'");
            r = r.replace("<", "{");
            r = r.replace(">", "}");
        }
        return r;
    }

    /**
     * 批量操作 执行结果 成功几条 失败几条
     * 
     * @param successRow
     * @param failRow
     * @return
     */
    public static String BuildSoapXMl(List successRow, List failRow,
            String[] keySet) {
        //构建标准Soap输出规范
        StringBuffer reXML = new StringBuffer(SoapEnvelop.XML_PI);
        reXML.append(SoapEnvelop.ENVELOP_START) //信封
                .append(SoapEnvelop.HEAD_START) //soap:header
                .append(SoapEnvelop.HEAD_END).append(SoapEnvelop.BODY_START) //soap:body
                .append(SoapEnvelop.BUSINESS_START) //out:business
                .append(SoapEnvelop.Retrieve_START);
        buildRowInfo(successRow, failRow, keySet, reXML);
        reXML.append(SoapEnvelop.Retrieve_END).append(SoapEnvelop.BUSINESS_END)
                .append(SoapEnvelop.BODY_END).append(SoapEnvelop.ENVELOP_END);
        return reXML.toString();
    }

    /**
     * 构建批量 反馈结果行信息
     * 
     * @param rowInfo
     * @param sbXML
     */
    private static void buildRowInfo(List successRow, List failRow,
            String[] keySet, StringBuffer sbXML) {
        //成功row
        if (successRow != null) {
            HashMap map = null;
            for (Iterator it = successRow.iterator(); it.hasNext();) {
                map = (HashMap) it.next();
                sbXML.append("<row isSuccessful=\"true\" ");
                for (String key : keySet) {
                    if (map.containsKey("produceExeResult")) {
                        sbXML.append("produceExeResult=\"").append(
                                (String) map.get("produceExeResult"));
                        sbXML.append("\"  ");
                    }
                    sbXML.append(key).append("=\"").append(
                            (String) map.get(key));
                    sbXML.append("\"  ");
                }
                sbXML.append("/>");
            }
        }
        //失败row
        if (failRow != null) {
            HashMap map = null;
            for (Iterator it = failRow.iterator(); it.hasNext();) {
                map = (HashMap) it.next();
                sbXML.append("<row isSuccessful=\"false\" ");
                if (map.containsKey("failReason")) {
                    sbXML.append("failReason=\"").append(
                            (String) map.get("failReason"));
                    sbXML.append("\"  ");
                }
                for (String key : keySet) {
                    sbXML.append(key).append("=\"").append(
                            (String) map.get(key));
                    sbXML.append("\"  ");
                }
                sbXML.append("/>");
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(BuildSoapXMlError("1500", "找不到出错信息!"));
    }

    /**
     * 根据照片Bolb 数据 加64码
     * 
     * @param Blob
     *            pic
     */
    public static String getBase64Data(byte[] buff) {

        //照片数据查询
        BASE64Encoder encoder = new BASE64Encoder();
        String data = encoder.encode(buff);
        return data;
    }

}
