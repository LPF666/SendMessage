package com.ylzinfo.fj.utils;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.xml.sax.InputSource;

import org.jdom2.Attribute;

import com.ylzinfo.cbp.exception.YLZCBPException;

/**
 * 
 * @author zero
 *
 */
public class XmlUtils {

	public static <T extends BaseDTO> T getOutPara(final String xml, final Class type) throws Exception {
		//创建一个新的字符串
        StringReader read = new StringReader(xml);
        //创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
        InputSource source = new InputSource(read);
        //创建一个新的SAXBuilder
        SAXBuilder sb = new SAXBuilder();
        T dto = null;
        try {
			dto = (T)type.newInstance();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		}
        try {
        	//通过输入源构造一个Document
            Document doc = sb.build(source);
          //取的根元素
            Element root = doc.getRootElement();
            for(Element et : root.getChildren()) {
            	if("soap:Body".equals(et.getName())||"Body".equals(et.getName())) {//判断是否是主信息
            		List<Element> children = et.getChildren();
            		if(children != null && children.size() > 0) {
	            		Element in = et.getChild("out:business");
	            		if(in == null){
	            			in = children.get(0);
	            		}
	            		for(Element info : in.getChildren()) {
	            			if(info.getName().equals("resultset") && "retrieve".equals(info.getAttributes().get(0).getValue())){
	            				Element row = info.getChildren().get(0);
		            			for(Attribute para : row.getAttributes()){
		            				try{
		            					String paraname = para.getName().toLowerCase();
			            				if(PropertyUtils.getPropertyType(dto, paraname) == Long.class)
			            					PropertyUtils.setProperty(dto, paraname, Long.valueOf(para.getValue().trim()));
			            				else if(PropertyUtils.getPropertyType(dto, para.getName()) == Double.class)
			            					PropertyUtils.setProperty(dto, paraname, Double.valueOf(para.getValue().trim()));
			            				else
			            					PropertyUtils.setProperty(dto, paraname, encodeString(para.getValue().trim()));
		            				}
		            				catch (NoSuchMethodException e) {
//		            					throw new YLZCBPException(e);
									}
		            			}
	            			}
	            			else if(info.getName().equals("faultstring")) {
	            				Element row = info.getChildren().get(0);
	            				Attribute attribute = row.getAttribute("msg");
	            				try{
		            				PropertyUtils.setProperty(dto, attribute.getName(), encodeString(attribute.getValue().trim()));
	            				}
	            				catch (NoSuchMethodException e) {
									throw new YLZCBPException(e);
								}
	            			}
	            		}
            		}
            	}
            }
		} catch (JDOMException e) {
			throw new Exception("传入的xml格式不正确：" + e.getMessage());
		} catch (IOException e) {
			throw new Exception("传入的xml格式不正确：" + e.getMessage());
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return dto;
	}
	
	public static <T extends BaseDTO> List<T> getOutParaList(final String xml, final Class type) throws Exception {
		//创建一个新的字符串
        StringReader read = new StringReader(xml);
        //创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
        InputSource source = new InputSource(read);
        //创建一个新的SAXBuilder
        SAXBuilder sb = new SAXBuilder();
        List<T> list = new ArrayList<T>();
        try {
        	//通过输入源构造一个Document
            Document doc = sb.build(source);
            //取的根元素
            Element root = doc.getRootElement();
            for(Element et : root.getChildren()) {
            	if("soap:Body".equals(et.getName())||"Body".equals(et.getName())) {//判断是否是主信息
            		List<Element> children = et.getChildren();
            		if(children != null && children.size() > 0) {
	            		Element in = et.getChild("out:business");
	            		if(in == null){
	            			in = children.get(0);
	            		}
	            		for(Element info : in.getChildren()) {
	            			if(info.getName().equals("resultset") && "retrieve".equals(info.getAttributes().get(0).getValue())){
	            				
	            				for (Element row : info.getChildren()) {

	            			        T dto = null;
	            			        try {
	            						dto = (T)type.newInstance();
	            					} catch (InstantiationException e1) {
	            						throw e1;
	            					} catch (IllegalAccessException e1) {
	            						throw e1;
	            					}
			            			for(Attribute para : row.getAttributes()){
			            				try{
			            					String paraname = para.getName().toLowerCase();
				            				if(PropertyUtils.getPropertyType(dto, paraname) == Long.class)
				            					PropertyUtils.setProperty(dto, paraname, Long.valueOf(para.getValue().trim()));
				            				else if(PropertyUtils.getPropertyType(dto, para.getName()) == Double.class)
				            					PropertyUtils.setProperty(dto, paraname, Double.valueOf(para.getValue().trim()));
				            				else
				            					PropertyUtils.setProperty(dto, paraname, encodeString(para.getValue().trim()));
			            				}
			            				catch (NoSuchMethodException e) {
//											throw e;
										}
			            			}
			            			list.add(dto);
	            				}
	            			}
	            			
	            		}
            		}
            	}
            }
		} catch (JDOMException e) {
			throw new Exception("传入的xml格式不正确：" + e.getMessage());
		} catch (IOException e) {
			throw new Exception("传入的xml格式不正确：" + e.getMessage());
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/** 
	  * 替换字符串中特殊字符 
	  */  
	private static String encodeString(String strData)  
	 {  
	     if (strData == null)  
	     {  
	         return "";  
	     }  
	     strData = strData.replaceAll( "&", "&amp;");  
	     strData = strData.replaceAll( "<", "&lt;");  
	     strData = strData.replaceAll( ">", "&gt;");  
	     strData = strData.replaceAll( "&apos;", "&apos;");  
	     strData = strData.replaceAll( "\"", "&quot;");  
	     return strData;  
	 } 
}
