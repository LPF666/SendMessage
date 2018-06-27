/**
 * 
 */
package com.ylzinfo.psBusiness.checkAuthor.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.QName;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.rpc.client.RPCServiceClient;
import org.apache.log4j.Logger;

import com.ylzinfo.cbp.exception.YLZCBPException;
import com.ylzinfo.esb.bas.EsbResponse;
import com.ylzinfo.esb.client.XMLRequest;
import com.ylzinfo.esb.soap.ReaderSoapXmlOut;
import com.ylzinfo.fj.utils.XmlUtils;
import com.ylzinfo.listener.esb.ESBConfig;
import com.ylzinfo.listener.esb.ESBConfigDTO;
import com.ylzinfo.psBusiness.checkAuthor.dto.BaseinfoDTO;
import com.ylzinfo.psBusiness.checkAuthor.dto.CheckAuthorDTO;
import com.ylzinfo.psBusiness.checkAuthor.dto.QueryBcc001DTO;
import com.ylzinfo.util.SoapBody;

/**
 * @description 实名认证服务
 * @author lingxiaoling
 * @date 2015-10-12 下午01:24:00
 */
public class CheckAuthorService1 {
	private static Logger logger = Logger.getLogger(CheckAuthorService.class);

	/**
	 * 校验用户是否实名认证，如果是，返回社保关联号
	 * 
	 * @param aac002
	 *            身份证号码
	 * @param aac003
	 *            姓名
	 * @param bcc002
	 *            社保卡号
	 * @returnli
	 * @throws Exception
	 */
	public static String checkAuthor(String aac002, String aac003, String bcc002) throws Exception {
		CheckAuthorDTO dto = new CheckAuthorDTO();
		try {
			if (null == aac002 || "".equals(aac002)) {
				dto.setAae314("0");
				dto.setAae317("身份证号不能为空！");
				throw new YLZCBPException("身份证号不能为空！");
			}
			if (null == aac003 || "".equals(aac003)) {
				dto.setAae314("0");
				dto.setAae317("姓名不能为空！");
				throw new YLZCBPException("姓名不能为空！");
			}
			if (null == bcc002 || "".equals(bcc002)) {
				dto.setAae314("0");
				dto.setAae317("社保卡号不能为空！");
				throw new YLZCBPException("社保卡号不能为空！");
			}
			// 入参解密
		//	aac002 = SecurityUtil.decoder(aac002, GlobalConst.globalvalue.get("decodekey"));
		//	aac003 = SecurityUtil.decoder(aac003, GlobalConst.globalvalue.get("decodekey"));
		//	bcc002 = SecurityUtil.decoder(bcc002, GlobalConst.globalvalue.get("decodekey"));
			XMLRequest xmlRequest = new XMLRequest();
			ESBConfigDTO config = ESBConfig.configs.get("sxmsy");
			xmlRequest.setEsbUrl(config.getUrl()); // ESB服务器路径
			xmlRequest.setEsbUserPwd(new String[] { config.getUsername(), config.getPasswd() });// ESB访问用户及密码
			String[] v_param = { "aac002", "aac003" };// 访问参数
			String[] v_value = { aac002, aac003 };// 访问参数值
			xmlRequest.setSvid("common_person_getbcc001");// 服务ID
			xmlRequest.setParam(v_param);
			xmlRequest.setParamValue(v_value);
			EsbResponse esbRsp = xmlRequest.postXmlRequest();// 发送封装SOAP数据
			logger.info(esbRsp.getResponseData());
			QueryBcc001DTO bcc001dto = XmlUtils.getOutPara(esbRsp.getResponseData(), QueryBcc001DTO.class);
			if (null == bcc001dto.getBcc001() || "".equals(bcc001dto.getBcc001())) {
				dto.setAae314("0");
				dto.setAae317("身份证号码为：" + aac002 + "，姓名为：" + aac003 + "无对应的社保关联号！");
				throw new YLZCBPException("身份证号码为：" + aac002 + "，姓名为：" + aac003 + "无对应的社保关联号！");
			}
			String[] v_param1 = { "bcc001" };// 访问参数
			String[] v_value1 = { bcc001dto.getBcc001() };// 访问参数值
			xmlRequest.setSvid("common_person_baseinfoquery");// 服务ID
			xmlRequest.setParam(v_param1);
			xmlRequest.setParamValue(v_value1);
			esbRsp = xmlRequest.postXmlRequest();// 发送封装SOAP数据
			logger.info(esbRsp.getResponseData());
			BaseinfoDTO baseinfoDTO = XmlUtils.getOutPara(esbRsp.getResponseData(), BaseinfoDTO.class);
			if (null == baseinfoDTO.getAac002() || "".equals(baseinfoDTO.getAac002())) {
				dto.setAae314("0");
				dto.setAae317("查不到身份证号码为：" + aac002 + "，姓名为：" + aac003 + "的用户信息！");
				throw new YLZCBPException("查不到身份证号码为：" + aac002 + "，姓名为：" + aac003 + "的用户信息！");
			} else {
				if (bcc002.equals(baseinfoDTO.getBcc002())) {
					dto.setAae314("1");
					dto.setAae317("认证成功！");
					dto.setBcc001(bcc001dto.getBcc001());
				} else {
					dto.setAae314("0");
					dto.setAae317("认证失败：社保卡号" + bcc002 + "错误！");
					throw new YLZCBPException("认证失败：社保卡号" + bcc002 + "错误！");
				}
			}
		} catch (YLZCBPException ye) {
			logger.info(ye.getMessage());
			logger.error(ye.getMessage());
		} catch (Exception e) {
			dto.setAae314("0");
			dto.setAae317("服务调用失败，请联系管理员！");
			logger.error(e);
		}
		// 返回参数加密处理
	//	dto.setAae314(SecurityUtil.encoder(dto.getAae314(), GlobalConst.globalvalue.get("encodekey")));
	//	dto.setAae317(SecurityUtil.encoder(dto.getAae317(), GlobalConst.globalvalue.get("encodekey")));
//		if (!StringUtils.isBlank(dto.getBcc001())) {
//			dto.setBcc001(SecurityUtil.encoder(dto.getBcc001(), GlobalConst.globalvalue.get("encodekey")));
//		}
		return ReaderSoapXmlOut.readerSoapXMlOut(dto.getColset(), dto.getStructsList(), "");
	}
	/*public static void main(String[] args) {
		try {
			//testMobileStopXML();
			//checkAuthor("142621199501290535","李坤学","1");
			String ome = "<ns:getXmlResponse xmlns:ns=\"http://axis2.insigma.com/\">"
				+"<ns:return>"
				+"<Result:DATA xmlns:Result=\"http://axis2.insigma.com/\">"
				+"<result>0</result>"
				+"<description>123</description>"
				+"<list01>"
				+"<sms>"
				+"<aae005>123456</aae005>"
				+"<aae013>123456</aae013>"
				+"</sms>"
				+"<sms>"
				+"<aae005>1234567</aae005>"
				+"<aae013>1234567</aae013>"
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
			SoapBody data = parseElement(omRespBody);
			List list01 = data.getResultSet("list01");
			for (int i = 0; i < list01.size(); i++) {
				HashMap map = (HashMap) list01.get(i);
				String aae005 = (String) map.get("aae005");
				System.out.println(aae005);				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	public static final String URL_TEST = "http://172.20.4.90:8080/sms/services/HelloWebService?wsdl";
	public static final EndpointReference ENDPOINT_TEST = new EndpointReference(URL_TEST);
	public static final String NAMESPACE="http://axis2.insigma.com/";
//	public static void testGetXML2(){
//			try {
//				RPCServiceClient client = new RPCServiceClient();
//				Options options = client.getOptions();  
//				options.setTo(ENDPOINT_TEST);  
//				Object[] obj = new Object[]{"hahah"};
//				Class<?>[] classes = new Class[] { String.class};    
//				QName qname = new QName(NAMESPACE, "getXml");
//				System.out.println(client.invokeBlocking(qname, obj));
//			} catch (AxisFault e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}  
//		}

	public static void testMobileStopXML(){
		try {
			ServiceClient client = new ServiceClient();
			Options options = client.getOptions();  
			options.setTo(ENDPOINT_TEST);  
			client.setOptions(options);
			
			OMFactory fac = OMAbstractFactory.getOMFactory();  
			OMNamespace omNs = fac.createOMNamespace(NAMESPACE, "");  
			OMElement method = fac.createOMElement("mobileStopXML", omNs);  
			OMElement value = fac.createOMElement("userid",omNs);  
			value.setText("140700");
			method.addChild(value);   
			OMElement value2 = fac.createOMElement("password",omNs);  
			value2.setText("666666");
			method.addChild(value2);  
			
			OMElement list = fac.createOMElement("list01",omNs);  
			OMElement sms = fac.createOMElement("sms",omNs);
			OMElement aae005 = fac.createOMElement("aae005",omNs);  
			aae005.setText("18035441009");
			sms.addChild(aae005);  
			
			OMElement aae135 = fac.createOMElement("aae135",omNs);  
			aae135.setText("140104197204220851");
			sms.addChild(aae135);  
			list.addChild(sms);
			method.addChild(list);
			System.out.println(method.toString());
			OMElement res = client.sendReceive(method); 
			System.out.println(res);
			//SoapBody data = parseElement(res);
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	}
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
	public static void main(String[] args) {
		XMLRequest xmlRequest = new XMLRequest();
		xmlRequest.setEsbUrl("http://192.168.211.126:7001/esb/esbproxy"); //ESB服务器路径
		xmlRequest.setEsbUserPwd(new String[]{"esb","8E000FDB54B7FD93"});//ESB访问用户及密码
		//xmlRequest.set
		String[] v_param={"aac002", "aac003"};//访问参数
		String[] v_value={"142621199501290535","李坤学"};//访问参数值
		xmlRequest.setSvid("common_person_getbcc001");//服务ID
		xmlRequest.setParam(v_param);
		xmlRequest.setParamValue(v_value);
		EsbResponse esbRsp = xmlRequest.postXmlRequest();//发送封装SOAP数据
		System.out.println("返回报文"+esbRsp.getResponseData());//发送结果报文
	}


}
