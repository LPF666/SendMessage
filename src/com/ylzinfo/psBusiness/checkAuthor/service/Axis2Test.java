package com.ylzinfo.psBusiness.checkAuthor.service;

import java.io.UnsupportedEncodingException;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.rpc.client.RPCServiceClient;
import org.dom4j.DocumentException;

//import com.insigma.odin.framework.sys.axis2.entity.Request;
//import com.insigma.odin.framework.sys.axis2.entity.RqSmsDownload;

public class Axis2Test {

	public static final String URL = "http://127.0.0.1:8080/sms/services/HelloWebService?wsdl";
	public static final String URL_SMSSEND="http://192.168.211.100:38080/sms/services/SMSWebService?wsdl";
	public static final EndpointReference ENDPOINT = new EndpointReference(URL);
	public static final EndpointReference ENDPOINT_SMSSEND = new EndpointReference(URL_SMSSEND);
	public static final String NAMESPACE="http://axis2.insigma.com/";
	
	public static void main(String[] args) throws DocumentException, UnsupportedEncodingException, XMLStreamException {
//		test2();
		
		//短信发送
		testSmsSend();
		
		//短信下载（日期区间）
//		testSmsDownloadByDate();
		
		//短信下载 批次号
//		testSmsDownload();
		
		//短信下载XML请求
//		testSmsDownloadXML();
		
		//短信下载 XML请求 开始结束日期
//		testSmsDownloadXML2();
		
//		resolveXML();
		
		//短信查询 im004或im005方式
//		testSmsQuery();
		
		//短信查询 XML方式
//		testSmsQueryXML();
		
		//短信发送XML方式
		//testSmsSendXML();
		
		//上行短信查询
//		testSmsReply();
		
		//上行短信查询 XML方式
//		testSmsReplyXML();
	}
	
	public static void testSmsSendXML(){
		try {
			ServiceClient client = new ServiceClient();
			Options options = client.getOptions();  
			options.setTo(ENDPOINT_SMSSEND);  
//			options.setAction("smsDownloadXML");
			client.setOptions(options);
			
			OMFactory fac = OMAbstractFactory.getOMFactory();  
			OMNamespace omNs = fac.createOMNamespace(NAMESPACE, "");  
			OMElement method = fac.createOMElement("smsSendXML", omNs);  
			OMElement value = fac.createOMElement("userid",omNs);  
			value.setText("149900");
			method.addChild(value);   
			OMElement value2 = fac.createOMElement("password",omNs);  
			value2.setText("000000");
			method.addChild(value2);   
			OMElement list01 = fac.createOMElement("list01",omNs); 
//			OMElement sms = getSmsSendObj("您的验证码为:1235此验证码10分钟有效,请尽快使用!", "18335460095", "10035", "", "");
			OMElement sms = getSmsSendObj("您的验证码为:5432此验证码10分钟有效,请尽快使用!", "18510409981", "10039", "", "");
//			OMElement sms2 = getSmsSendObj("欢迎您开通社保业务变化及时告知服务935", "18510409981", "10030", "20170421080000", "");
			list01.addChild(sms);
//			list01.addChild(sms2);
			method.addChild(list01);  
			System.out.println("请求xml："+method);
			OMElement res = client.sendReceive(method); 
			System.out.println(res);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	}
	
	private static OMElement getSmsSendObj(String im103,String aae005,String im004,String aae036,String aae013){
		OMFactory fac = OMAbstractFactory.getOMFactory();  
		OMNamespace omNs = fac.createOMNamespace(NAMESPACE, "");  
		OMElement sms = fac.createOMElement("sms",omNs);
		OMElement im103Obj = fac.createOMElement("im103",omNs);
		im103Obj.setText(im103);
		sms.addChild(im103Obj);
		OMElement aae005Obj = fac.createOMElement("aae005",omNs);
		aae005Obj.setText(aae005);
		sms.addChild(aae005Obj);
		OMElement im004Obj = fac.createOMElement("im004",omNs);
		im004Obj.setText(im004);
		sms.addChild(im004Obj);
		OMElement aae036Obj = fac.createOMElement("aae036",omNs);
		aae036Obj.setText(aae036);
		sms.addChild(aae036Obj);
		OMElement aae013Obj = fac.createOMElement("aae013",omNs);
		aae013Obj.setText(aae013);
		sms.addChild(aae013Obj);
		
		return sms;
	}
	
	public static void testSmsQueryXML(){
		try {
			ServiceClient client = new ServiceClient();
			Options options = client.getOptions();  
			options.setTo(ENDPOINT_SMSSEND);  
//			options.setAction("smsDownloadXML");
			client.setOptions(options);
			
			OMFactory fac = OMAbstractFactory.getOMFactory();  
			OMNamespace omNs = fac.createOMNamespace(NAMESPACE, "");  
			OMElement method = fac.createOMElement("smsQueryXML", omNs);  
			OMElement value = fac.createOMElement("userid",omNs);  
			value.setText("140700");
			method.addChild(value);   
			OMElement value2 = fac.createOMElement("password",omNs);  
			value2.setText("000000");
			method.addChild(value2);   
			OMElement list01 = fac.createOMElement("list01",omNs); 
			OMElement sms = fac.createOMElement("sms",omNs);
//			OMElement im004 = fac.createOMElement("im004",omNs);
//			im004.setText("1000010");
//			sms.addChild(im004);
			OMElement im004 = fac.createOMElement("im005",omNs);
			im004.setText("10000000000000002105");
			sms.addChild(im004);
			OMElement sms2 = fac.createOMElement("sms",omNs);
			OMElement im005 = fac.createOMElement("im005",omNs);
			im005.setText("10000000000000002109");
			sms2.addChild(im005);
			list01.addChild(sms);
			list01.addChild(sms2);
			method.addChild(list01);  
//			System.out.println(method.toString());
			
			OMElement res = client.sendReceive(method); 
			System.out.println(res);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	}
	
	public static void testSmsQuery(){
		try {
			RPCServiceClient client = new RPCServiceClient();
			Options options = client.getOptions();  
			options.setTo(ENDPOINT_SMSSEND);  
			Object[] obj = new Object[]{"140700","000000","1000010"};
			QName qname = new QName(NAMESPACE, "smsQuery");
			Class<?>[] classes = new Class[] { String.class,String.class,String.class };    
			System.out.println(client.invokeBlocking(qname, obj));
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	}
	
	public static void testSmsDownloadXML(){
		try {
			ServiceClient client = new ServiceClient();
			Options options = client.getOptions();  
			options.setTo(ENDPOINT_SMSSEND);  
//			options.setAction("smsDownloadXML");
			client.setOptions(options);
			
			OMFactory fac = OMAbstractFactory.getOMFactory();  
			OMNamespace omNs = fac.createOMNamespace(NAMESPACE, "");  
			OMElement method = fac.createOMElement("smsDownloadXML", omNs);  
			OMElement value = fac.createOMElement("userid",omNs);  
			value.setText("140700");
			method.addChild(value);   
			OMElement value2 = fac.createOMElement("password",omNs);  
			value2.setText("000000");
			method.addChild(value2);   
			OMElement list01 = fac.createOMElement("list01",omNs); 
			OMElement sms = fac.createOMElement("sms",omNs);
			OMElement im004 = fac.createOMElement("im004",omNs);
			im004.setText("1000010");
			sms.addChild(im004);
			list01.addChild(sms);
			method.addChild(list01);  
//			System.out.println(method.toString());
			
			OMElement res = client.sendReceive(method); 
			System.out.println(res);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	}
	
	public static void testSmsDownloadXML2(){
		try {
			ServiceClient client = new ServiceClient();
			Options options = client.getOptions();  
			options.setTo(ENDPOINT_SMSSEND);  
//			options.setAction("smsDownloadXML");
			client.setOptions(options);
			
			OMFactory fac = OMAbstractFactory.getOMFactory();  
			OMNamespace omNs = fac.createOMNamespace(NAMESPACE, "");  
			OMElement method = fac.createOMElement("smsDownloadXML", omNs);  
			OMElement value = fac.createOMElement("userid",omNs);  
			value.setText("140700");
			method.addChild(value);   
			OMElement value2 = fac.createOMElement("password",omNs);  
			value2.setText("000000");
			method.addChild(value2);  
			
			OMElement start = fac.createOMElement("start",omNs);  
			start.setText("20170401");
			method.addChild(start);  
			
			OMElement end = fac.createOMElement("end",omNs);  
			end.setText("20170430");
			method.addChild(end);  
//			System.out.println(method.toString());
			
			OMElement res = client.sendReceive(method); 
			System.out.println(res);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	}
	
	public static void testSmsDownload(){
		try {
			RPCServiceClient client = new RPCServiceClient();
			Options options = client.getOptions();  
			options.setTo(ENDPOINT_SMSSEND);  
			Object[] obj = new Object[]{"140700","000000","1000010"};
			Class<?>[] classes = new Class[] { String.class,String.class};    
			QName qname = new QName(NAMESPACE, "smsDownload");
			System.out.println(client.invokeBlocking(qname, obj));
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	}
	
	public static void testSmsDownloadByDate(){
		try {
			RPCServiceClient client = new RPCServiceClient();
			Options options = client.getOptions();  
			options.setTo(ENDPOINT_SMSSEND);  
			Object[] obj = new Object[]{"140700","000000","20170401","20170419"};
			Class<?>[] classes = new Class[] { String.class,String.class};    
			QName qname = new QName(NAMESPACE, "smsDownloadByDate");
			System.out.println(client.invokeBlocking(qname, obj));
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	}
	public static void testSmsSend(){
		try {
			RPCServiceClient client = new RPCServiceClient();
			Options options = client.getOptions();  
			options.setTo(ENDPOINT_SMSSEND);  
			Object[] obj = new Object[]{"149900","000000","测试山西您好,再次鉴定,2017-07-26 16:52:00,山西医科大学第二附属医院,请被鉴定人携带完整病历原件(按时间排序,用后留档,不予退还)及X片,CT片等辅助检查资料.不能参加请提前请假,无故不到本次鉴定终止.收到请回复'是'.","18435117265","10000100","","备注"};
			Class<?>[] classes = new Class[] { String.class,String.class ,String.class ,String.class ,String.class ,String.class ,String.class  };    
			QName qname = new QName(NAMESPACE, "smsSend");
			System.out.println(client.invokeBlocking(qname, obj));
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	}
	
	public static void test2(){
		try {
			RPCServiceClient client = new RPCServiceClient();
			Options options = client.getOptions();  
			options.setTo(ENDPOINT);  
			Object[] obj = new Object[]{"Guoyy","Good Bye111"};
			Class<?>[] classes = new Class[] { String.class };    
			QName qname = new QName(NAMESPACE, "echo");
			String result = (String) client.invokeBlocking(qname, obj,classes)[0];
			System.out.println(result); 
			System.out.println(client.invokeBlocking(qname, obj));
			
			System.out.println("====================");
			QName qname2 = new QName(NAMESPACE, "getXml2");
			System.out.println( client.invokeBlocking(qname2, obj,classes)[0]);
			System.out.println( client.invokeBlocking(qname2, obj));
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	}
	/**
	 * 第三种方式
	 * @param args
	 */
	public static void test3() {
		 Options options = new Options();  
		 options.setAction("echo");//调用接口方法  
		 options.setTo(ENDPOINT);  
		 ServiceClient sender = null;  
		 try {
			sender = new ServiceClient();
			sender.setOptions(options);
			OMFactory fac = OMAbstractFactory.getOMFactory();  
			OMNamespace omNs = fac.createOMNamespace(NAMESPACE, "");  
			OMElement method = fac.createOMElement("echo", omNs);  
			OMElement value = fac.createOMElement("msg",omNs);  
			value.setText("Guoyy");
			method.addChild(value);   
			OMElement value2 = fac.createOMElement("param2",omNs);  
			value2.setText("Guoyy");
			method.addChild(value2);   
			OMElement res = sender.sendReceive(method); 
			System.out.println(res.getFirstElement().getText()); 
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	}
	
	public static void resolveXML() throws DocumentException, UnsupportedEncodingException, XMLStreamException{
		String xml="<smsDownloadXML xmlns=\"http://axis2.insigma.com/\"><userid>140700</userid><password>000000</password><list01><sms><im004>1000010</im004></sms></list01></smsDownloadXML>";
//		OMFactory factory = OMAbstractFactory.getOMFactory();
//		Document document = DocumentHelper.parseText(xml);
//		OMElement element = new StAXOMBuilder(new ByteArrayInputStream(xml.getBytes("UTF-8"))).getDocumentElement();
//		BeanUtil.processObject(, arg1, arg2, arg3, arg4, arg5)
		//Request rq = new Request();
		//rq.resolveXML(xml, "smsDownloadXML", RqSmsDownload.class);
		//System.out.println(rq.toString());
	}
	
	public static void testSmsReply(){
		try {
			RPCServiceClient client = new RPCServiceClient();
			Options options = client.getOptions();  
			options.setTo(ENDPOINT_SMSSEND);  
			Object[] obj = new Object[]{"149900","000000","10000000000000001361"};
			Class<?>[] classes = new Class[] { String.class,String.class};    
			QName qname = new QName(NAMESPACE, "smsReply");
			System.out.println(client.invokeBlocking(qname, obj));
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	}
	
	public static void testSmsReplyXML(){
		try {
			ServiceClient client = new ServiceClient();
			Options options = client.getOptions();  
			options.setTo(ENDPOINT_SMSSEND);  
//			options.setAction("smsDownloadXML");
			client.setOptions(options);
			
			OMFactory fac = OMAbstractFactory.getOMFactory();  
			OMNamespace omNs = fac.createOMNamespace(NAMESPACE, "");  
			OMElement method = fac.createOMElement("smsReplyXML", omNs);  
			OMElement value = fac.createOMElement("userid",omNs);  
			value.setText("149900");
			method.addChild(value);   
			OMElement value2 = fac.createOMElement("password",omNs);  
			value2.setText("000000");
			method.addChild(value2);   
			OMElement list01 = fac.createOMElement("list01",omNs); 
			OMElement sms = fac.createOMElement("sms",omNs);
			OMElement im004 = fac.createOMElement("im004",omNs);
			im004.setText("1000000000000000149999");
			sms.addChild(im004);
			list01.addChild(sms);
			method.addChild(list01);  
			System.out.println(method.toString());
			OMElement res = client.sendReceive(method); 
			System.out.println(res);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	}
}
