package com.ylzinfo.util;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.log4j.Logger;


public class OMEUtil {
	private static Logger logger = Logger.getLogger(OMEUtil.class);
	public static final String NAMESPACE="http://axis2.insigma.com/";
	//格式化入参
	public static OMElement replace(OMElement ome){
		InputStream parser = null;;
		String omeStr = ome.toString().replaceAll("&lt;", "<");
		omeStr = omeStr.toString().replaceAll("&gt;", ">");
		omeStr = omeStr.toString().replaceAll("<arg0>", "");
		omeStr = omeStr.toString().replaceAll("</arg0>", "");
		omeStr = omeStr.toString().replaceAll("<ome xmlns=\"http://www.ylzinfo.com/xsd\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"xs:string\">", "");
		omeStr = omeStr.toString().replaceAll("</ome>", "");
		try {
			parser = new ByteArrayInputStream(omeStr.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		StAXOMBuilder builder = null;
		try {
			builder = new StAXOMBuilder(parser);
		} catch (XMLStreamException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//get the root element
		OMElement omRespBody= builder.getDocumentElement(); 
		return omRespBody;
	}
	//格式化出参
	public static OMElement replace2(OMElement ome,String Qname){
		InputStream parser = null;;
		String omeStr = ome.toString();
		omeStr = "<ns:"+Qname+" xmlns:ns=\"http://axis2.insigma.com/\"><ns:return>"+omeStr+"</ns:return></ns:"+Qname+">";
		try {
			parser = new ByteArrayInputStream(omeStr.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		StAXOMBuilder builder = null;
		try {
			builder = new StAXOMBuilder(parser);
		} catch (XMLStreamException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//get the root element
		OMElement omRespBody= builder.getDocumentElement(); 
		return omRespBody;
	}
	//将入参转化为12333短信平台所需要的格式--短信发送
	public static OMElement SMSsendingXML(SoapBody soapbody){
		OMElement res = null;
		try {
			//拼接入参
			OMFactory fac = OMAbstractFactory.getOMFactory();  
			OMNamespace omNs = fac.createOMNamespace(NAMESPACE, "");
			//userid
			OMElement method = fac.createOMElement("smsSendXML", omNs);  
			OMElement value = fac.createOMElement("userid",omNs);  
			value.setText((String) soapbody.getData("userid"));
			method.addChild(value);  
			//password
			OMElement value2 = fac.createOMElement("password",omNs);  
			value2.setText((String) soapbody.getData("password"));
			method.addChild(value2);  
			//list01
			OMElement list = fac.createOMElement("list01",omNs);
			//判断入参有无list01
			List list01 = soapbody.getResultSet("list01");
			if(list01 == null || "".equals(list01)){
				OMElement sms = fac.createOMElement("sms",omNs);
				OMElement im103 = fac.createOMElement("im103",omNs);  
				sms.addChild(im103);  
				OMElement aae005 = fac.createOMElement("aae005",omNs);  
				sms.addChild(aae005);
				OMElement im004 = fac.createOMElement("im004",omNs);  
				sms.addChild(im004); 
				OMElement aae036 = fac.createOMElement("aae036",omNs);  
				sms.addChild(aae036); 
				OMElement aae013 = fac.createOMElement("aae013",omNs);  
				sms.addChild(aae013); 
				list.addChild(sms);
				method.addChild(list);
			}else{
				for (int i = 0; i < list01.size(); i++) {
					HashMap map = (HashMap) list01.get(i);
					OMElement sms = fac.createOMElement("sms",omNs);
					OMElement im103 = fac.createOMElement("im103",omNs);  
					im103.setText((String) map.get("im103"));
					sms.addChild(im103);  
					OMElement aae005 = fac.createOMElement("aae005",omNs); 
					aae005.setText((String) map.get("aae005"));
					sms.addChild(aae005);
					OMElement im004 = fac.createOMElement("im004",omNs);
					im004.setText((String) map.get("im004"));
					sms.addChild(im004); 
					OMElement aae036 = fac.createOMElement("aae036",omNs); 
					aae036.setText((String) map.get("aae036"));
					sms.addChild(aae036); 
					OMElement aae013 = fac.createOMElement("aae013",omNs);  
					aae013.setText((String) map.get("aae013"));
					sms.addChild(aae013); 
					list.addChild(sms);
					method.addChild(list);
				}
			}
			System.out.println("转化1---"+method.toString());
			res = method; 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;  
	}
	 /**
     * 解析12333短信服务返回的OMElement对象。--短信发送
     * @param element
     * @return SoapBody对象。
     * @author 
     * @date 
     */
    public static SoapBody parseElement2(OMElement element) {
        SoapBody body = new SoapBody();
        HashMap paraMap = null;
        List l = new ArrayList();
        OMElement root = element.getFirstElement(); //根元素
        if (root != null) {
            Iterator<?> iterator = root.getChildElements(); //子元素
            while (iterator != null && iterator.hasNext()) {
                OMElement om = (OMElement) iterator.next();
                    Iterator<?> rowData = om.getChildElements();
                    while (rowData != null && rowData.hasNext()) {
                        OMElement data = (OMElement) rowData.next();
                        if("list01".equals(data.getLocalName())){
                        	 Iterator<?> list = data.getChildElements();
                        	 while (list != null && list.hasNext()) {
                        		 paraMap = new HashMap();
                        		 OMElement s = (OMElement) list.next();
                        		 Iterator<?> sms = s.getChildElements();
                        		 while (sms != null && sms.hasNext()) {
                        			 OMElement s1 = (OMElement) sms.next();
                        			 paraMap.put(s1.getLocalName(), s1.getText().trim());
                        		 }
                        		 l.add(paraMap);
                        	 }
                        	 body.setResultSet("list01", l);
                        }else{
                        	body.setData(data.getLocalName(), data.getText().trim());
                        }
                    }
            }
        }
        return body;
    }
    public static void main(String[] args) throws XMLStreamException {
    	String ome = "<ns:getXmlResponse xmlns:ns=\"http://axis2.insigma.com/\">"
			+"<ns:return>"
			+"<Result:DATA xmlns:Result=\"http://axis2.insigma.com/\">"
			+"<result>0</result>"
			+"<description />"
			+"<list01>"
			+"<sms>"
			+"<aac003 xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"true\" />"
			+"<aae005 xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"true\" />"
			+"<aae013>手机号码需为数字！身份证号码不得为空;</aae013>"
			+"<aae135 xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"true\" />"
			+"</sms>"
			+"<sms>"
			+"<aac003 xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"true\" />"
			+"<aae005 xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"true\" />"
			+"<aae013>手机号码需为数字！身份证号码不得为空;</aae013>"
			+"<aae135 xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"true\" />"
			+"</sms>"
			+"</list01>"
			+"</Result:DATA>"
			+"</ns:return>"
			+"</ns:getXmlResponse>";
		InputStream parser = null;;
		try {
			parser = new ByteArrayInputStream(ome.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		StAXOMBuilder builder = new StAXOMBuilder(parser);
		//get the root element
		OMElement omRespBody= builder.getDocumentElement(); 
		//String sBody = omRespBody.toString();
		System.out.println(omRespBody);
		SoapBody data = parseElement2(omRespBody);
	}
}
