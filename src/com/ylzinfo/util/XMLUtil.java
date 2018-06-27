// Decompiled by DJ v3.9.9.91 Copyright 2005 Atanas Neshkov  Date: 2009-2-23 16:55:53
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   XMLUtil.java

package com.ylzinfo.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;



public class XMLUtil
{
    
    public static final String SOAP_ATTR_NAME = "comment";
    
    public static final String SOAP_RS_ATTR_TYPE = "name";
    
    public static final String SOAP_RS_ATTR_TYPE_STRUCT = "structs";
    
    public static final String SOAP_ATTR_TYPE_RETRIEVE = "retrieve";
    
    public static final String SOAP_RESULT = "msg";
    
    public static final String SOAP_ITEMNAME_RESULTSET = "resultset";
    
    public static final String SOAP_ITEMNAME_FAULT = "faultstring";
    
    public static final String SOAP_ITEMNAME_PARAM = "result";

    private static final String SOAP_RS_ATTR_NAME = "comment";

    private static final String SOAP_RS_ATTR_TOTALNUM = "totalnum";

    private static final String SOAP_RS_ATTR_INFO = "information";

    public XMLUtil()
    {
    }
    
    /**
     * 解析WebService服务传入的OMElement对象。
     * @param element
     * @return SoapBody对象。
     * @author 秦磊
     * @date 2011-4-2
     */
    public static SoapBody parseElement(OMElement element) {
        SoapBody body = new SoapBody();
        HashMap paraMap = new HashMap();
        OMElement root = element.getFirstElement(); //根元素
        if (root != null) {
            Iterator<?> iterator = root.getChildElements(); //子元素
            while (iterator != null && iterator.hasNext()) {
                OMElement om = (OMElement) iterator.next();
                OMAttribute omattribute = om.getAttribute(new QName("name"));
                if (omattribute == null) { //单数据集       para  
                    Iterator<?> rowData = om.getChildElements();
                    while (rowData != null && rowData.hasNext()) {
                        OMElement data = (OMElement) rowData.next();
                        body.setData(data.getLocalName(), data.getText().trim());
                    }
                } else { //参数数据集  paralist
                    String tableName = omattribute.getAttributeValue();
                    List l = new ArrayList();
                    HashMap vs = null;
                    Iterator<?> rowData = om.getChildElements();
                    while (rowData != null && rowData.hasNext()) {
                        OMElement data = (OMElement) rowData.next();
                        Iterator<?> ats = data.getAllAttributes();
                        vs = new HashMap();
                        while (ats != null && ats.hasNext()) {
                            OMAttribute d = (OMAttribute) ats.next();
                            vs.put(d.getLocalName(), d.getAttributeValue()
                                    .trim());
                        }
                        l.add(vs);
                    }
                    body.setResultSet(tableName, l);
                }

            }
        }

        return body;
    }
   
    /**
     * @param e
     * @return
     * @author 秦磊
     * @date 2011-4-8
     */
    @SuppressWarnings("unchecked")
    private static List parseResultSet(Element e) {
        List<HashMap<String, String>> s = new ArrayList<HashMap<String, String>>();
        Iterator<?> struct = e.elementIterator();
        Element se = null;
        HashMap<String, String> eim = null;//存放元素
        
        while (struct.hasNext()) {
            se = (Element) struct.next();
            List<Attribute> ei = se.attributes();
            int ai = ei.size();
            eim = new HashMap<String, String>();
            for (int i = 0; i < ai; i++) {
                eim.put(ei.get(i).getName().toLowerCase(), ei.get(i).getValue());
            }
            s.add(eim);
        }
        return s;
    }
    
    /**
     * 解析WebService服务返回的soap的XML字符串。
     * @param xmlStr
     * @return  SoapBody对象
     * @author 秦磊
     * @date 2011-4-2
     */
    @SuppressWarnings("unchecked")
    public static SoapBody parseXmlStr(String xmlStr) {
        SoapBody body = new SoapBody();
        body.setResult(SoapBody.MSG_OK);// 赋初始值
        xmlStr = formatAfterTrans(xmlStr);//将转义字符恢复
        // 解析xml文档
        try {
            Document doc = DocumentHelper.parseText(xmlStr);
            Element root = doc.getRootElement();
            List<Element> rows = root.elements();
            List<Element> bodyData = rows.get(1).elements();
            for (Element bodyChild : bodyData) {
                String totalNum = null;
                List<Element> business = bodyChild.elements();
                for (Element e : business) {

                    //数据集元素
                    if (SOAP_ITEMNAME_RESULTSET.equals(e.getName())) {
                        String en = e.attributeValue(SOAP_RS_ATTR_NAME);
                        
                        //兼容一卡通SOAP格式，没有comment属性
                        if (null == en || "".equals(en)) {
                            en = SOAP_RS_ATTR_TYPE_STRUCT;
                        }
                        List s = parseResultSet(e);
                        //结构数据集
                        if (SOAP_RS_ATTR_TYPE_STRUCT.equals(e
                                .attributeValue(SOAP_RS_ATTR_TYPE))) {
                            body.setStructs(en, s);
                        } else if (SOAP_ATTR_TYPE_RETRIEVE.equals(e
                                .attributeValue(SOAP_RS_ATTR_TYPE))) {
                            Attribute info = e.attribute(SOAP_RS_ATTR_INFO);
                            // 提示信息
                            if (null != info) {
                                body.setResult(info.getValue());
                            } else {
                                //设置记录总数
                                Attribute tn = e
                                        .attribute(SOAP_RS_ATTR_TOTALNUM);
                                if (null == tn) {
                                    totalNum = "" + s.size();
                                } else {
                                    totalNum = tn.getValue();
                                }
                                body.setTotalNum(en, totalNum);
                                body.setResultSet(en, s);
                            }
                        }
                    } else if (SOAP_ITEMNAME_PARAM.equals(e.getName())) {
                        body.setData(e.attribute(0).getName().toLowerCase(), e.attribute(0).getValue());
                    } else if (SOAP_ITEMNAME_FAULT.equals(e.getName())) {
                        List<Element> errorList = e.elements();
                        for (Element errorElement : errorList) {
                            body.setResult(errorElement.attribute(SOAP_RESULT).getValue());
                        }
                    }
                }
            }
        } catch (Exception e) {
            Writer w = new StringWriter();
            e.printStackTrace(new PrintWriter(w));
            body.setResult(w.toString());
        }
        return body;
    }
    
    
	public static List parseData(OMElement element) {
		// 解析xml文档
		try { 
		    List<HashMap<Object,Object>>  allData=new ArrayList<HashMap<Object,Object>>();
		    Iterator<?> iterator = element.getChildElements();	    
 	        while (iterator != null && iterator.hasNext()) 
 	        {
    		  OMElement om = (OMElement) iterator.next();
    		  Iterator<?> rowData = om.getChildElements();
    		  HashMap<Object,Object> rowMap=new HashMap<Object,Object>();
    		  while (rowData != null && rowData.hasNext()) 
	 	        {
	    		  OMElement data = (OMElement) rowData.next();
	    		  rowMap.put(data.getLocalName(), data.getText().trim());
	 	        }
    		  allData.add(rowMap);		 
 	        }
			return allData;
		 }
		    catch (Exception e) {
			 List lt=new ArrayList();
			 lt.add(e.getMessage());
             return lt;
 		}
	}
    
    
    
    /**
	 * 解析服务返回soap信息  
	 * @param xmlStr
	 * @param request
	 * @return
	 */
	public static List parseData(String xmlStr) {
		// 解析xml文档
		try { 
			Document doc = DocumentHelper.parseText(xmlStr);
			Element root = doc.getRootElement();
			List<Element> rows = root.elements();	
			List<Element> bodyData = rows.get(1).elements();
		    List<HashMap<Object,Object>>  allData=new ArrayList<HashMap<Object,Object>>();
			for(Element bodyChild:bodyData)
			{
				List<Element> business=	 bodyChild.elements();
				for(Element e:business){
					HashMap<Object,Object> rowMap=new HashMap<Object,Object>();
					for(Iterator<?> retrieveRow=e.attributeIterator();retrieveRow.hasNext();) {
						Attribute roweValueElement =(Attribute)retrieveRow.next();
						rowMap.put(roweValueElement.getName(), roweValueElement.getValue());
					}
					allData.add(rowMap);
				}
			}
			return allData;
		 }
		    catch (Exception e) {
			 List lt=new ArrayList();
			 lt.add(e.getMessage());
             return lt;
 		}
	}
    

    public static String formatBeforeTrans(String data)
    {
        if((data == null) | "".equals(data))
        {
            return "";
        } else
        {
            data = data.replaceAll("\\&", "&amp;");
            data = data.replaceAll("\\<", "&lt;");
            data = data.replaceAll("\\>", "&gt;");
            data = data.replaceAll("\\\"", "&quot;");
            data = data.replaceAll("\\'", "&apos;");
            data = data.replaceAll("\\\n", "STEAF_NEWLINE");
            return data;
        }
    }

    public static String formatAfterTrans(String data)
    {
        if((data == null) | "".equals(data))
        {
            return "";
        } else
        {
            data = data.replaceAll("&amp;", "&");
            data = data.replaceAll("&lt;", "<");
            data = data.replaceAll("&gt;", ">");
            data = data.replaceAll("&quot;", "\"");
            data = data.replaceAll("&apos;", "'");
            data = data.replaceAll("STEAF_NEWLINE", "\n");
            return data;
        }
    }
    
    public static String formatBeforeTransfor(String data)
    {
        if((data == null) | "".equals(data))
        {
            return "";
        } else
        {
            data = data.replaceAll("\\&", "___am_p;");
            data = data.replaceAll("\\<", "___l_t;");
            data = data.replaceAll("\\>", "___g_t;");
            data = data.replaceAll("\\\"", "___quo_t;");
            data = data.replaceAll("\\'", "___apo_s;");
            data = data.replaceAll("\\\n", "STEAF_NEWLIN_E");
            return data;
        }
    }

    public static String formatAfterTransfor(String data)
    {
        if((data == null) | "".equals(data))
        {
            return "";
        } else
        {
            data = data.replaceAll("___am_p;", "&");
            data = data.replaceAll("___l_t;", "<");
            data = data.replaceAll("___g_t;", ">");
            data = data.replaceAll("___quo_t;", "\"");
            data = data.replaceAll("___apo_s;", "'");
            data = data.replaceAll("STEAF_NEWLIN_E", "\n");
            return data;
        }
    }

    public static void main(String args[])
 {
        String testData = "ab&*\n\"'def<>";
        System.out.println(testData + "\n");
        System.out.println(formatBeforeTransfor(testData));
        String testData2 = formatBeforeTransfor(testData);
        System.out.println(formatAfterTransfor(testData2));

        String xml = "<?xml version=\"1.0\" encoding=\"GBK\"?><soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" soap:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\"><soap:Header><out:system xmlns:out=\"http://www.molss.gov.cn/\"></out:system></soap:Header><soap:Body><out:business xmlns:out=\"http://www.molss.gov.cn/\"><resultset name=\"structs\"></resultset><resultset name=\"retrieve\" information=\"PreparedStatementCallback; invalid ResultSet access for SQL [select BCC002,BCC001,AAC003,AAC002,AAC004,XAC004,AKC023,AAB034,AAB031,AKB020,AKB021,AAB001,AAB004,AAB019,YAB019,AKC141,AKA150,YYC001,YYC002,YYC003,AKC087,AKC021,YKC021 from vw_grjbxx where bcc001 = '140202198207305559']; nested exception is java.sql.SQLException: 无效的列索引\" ></resultset></out:business></soap:Body></soap:Envelope>";
        SoapBody b = parseXmlStr(xml);
        System.out.println(b.getMsg());
    }
}