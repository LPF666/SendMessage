/**
 * 
 */
package com.ylzinfo.psBusiness.checkAuthor.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.rpc.client.RPCServiceClient;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.log4j.Logger;

import com.ylzinfo.cbp.exception.YLZCBPException;
import com.ylzinfo.util.OMEUtil;
import com.ylzinfo.util.ReaderSoapXmlOut;
import com.ylzinfo.util.ResponseEntity;
import com.ylzinfo.util.ResultSet;
import com.ylzinfo.util.SoapBody;
import com.ylzinfo.util.XMLUtil;

/**
 * @description 12333短信平台服务
 * @author 
 * @date 2018-06-23 
 */
public class SMSService {
	private static Logger logger = Logger.getLogger(SMSService.class);
	public static final String URL_TEST = "http://192.168.211.100:38080/sms/services/SMSWebService?wsdl";
	public static final EndpointReference ENDPOINT_TEST = new EndpointReference(URL_TEST);
	public static final String NAMESPACE="http://axis2.insigma.com/";
	/**
	 * 短信发送通过XML
	 * @return String
	 * @throws Exception
	 */
	public static String SMSsendingByXML(OMElement ome) throws Exception {
		String msg = "";
		System.out.println("初始---"+ome);
		try {
			if (null == ome || "".equals(ome)) {
				throw new YLZCBPException("参数不能为空！");
			}
			//格式化入参
			ome = OMEUtil.replace(ome);
			System.out.println("转化1----"+ome);
			//转换类型
			SoapBody data = XMLUtil.parseElement(ome);
			//将入参转化为12333短信平台所需要的格式
			OMElement method = OMEUtil.SMSsendingXML(data);
			//调用短信平台接口
			ServiceClient client = new ServiceClient();
			Options options = client.getOptions();  
			options.setTo(ENDPOINT_TEST);  
			options.setAction("smsSendXML");
			options.setManageSession(true); 
			options.setProperty(HTTPConstants.REUSE_HTTP_CLIENT,true); 
			client.setOptions(options);
			OMElement res = client.sendReceive(method); 
			client.cleanupTransport();
			System.out.println(res.toString());
			//解析出参
			res = OMEUtil.replace2(res,"smsSendXML");
			System.out.println("res-----"+res);
			SoapBody data2 = OMEUtil.parseElement2(res);
			//拼接返回报文
			// 生成返回soap
			ResponseEntity resEntity = new ResponseEntity();
			resEntity.setParaName(new String[]{"showtype"});
			resEntity.setParaValue(new String[]{"1"});
			String s = data2.getData("result");
			String description = data2.getData("description");
			// 记录集
			ResultSet resultSet = new ResultSet();

			// 设置数据集的属性
			//resultSet.setName("list01");// 数据集名称
			resultSet.setView(false);// 数据集是否有对应的数据库表
			resultSet.setTableName("");// 数据集对应的数据库表的名称
			String columnName = "返回值编码:result,返回描述:description,批次号:im004,流水号:im005,手机号:aae005,其他信息:aae013";
			resultSet.setColumns(columnName);
			//resultSet.setTotalNum(2);// 符合查询条件的记录有1条。（分页查询使用）

			List<HashMap<String, String>> result = new ArrayList<HashMap<String,String>>();
			HashMap<String, String> hm = new HashMap<String, String>();
			List list2 = data2.getResultSet("list01");
			if(list2 == null ||"".equals(list2)){
			}else{
				for (int i = 0; i < list2.size(); i++) {
					HashMap map = (HashMap) list2.get(i);
					hm.put("result",s);
					hm.put("description",description);
					hm.put("im004",(String) map.get("im004"));
					hm.put("im005",(String) map.get("im005"));
					hm.put("aae005",(String) map.get("aae005"));
					hm.put("aae013",(String) map.get("aae013"));
					result.add(hm);
				}
			}
			resultSet.setResultSet(result);
			resEntity.setResultSet(resultSet);

			msg = ReaderSoapXmlOut.readerSoapXMLOut(resEntity);
			System.out.println("msg------"+msg);
		}catch (Exception e) {
			ResponseEntity resEntity = new ResponseEntity();
			resEntity.setErrorMsg(e.toString());
			msg = ReaderSoapXmlOut.readerSoapXMLOut(resEntity);
			logger.error(e);
			e.printStackTrace();
		}
	
		return msg;
	}
	/**
	 * 短信发送通过String
	 * @return String
	 * @throws Exception
	 */
	public static String SMSsendingByString(String str) throws Exception {
		String msg = "";
		System.out.println(str);
		try {
			if (null == str || "".equals(str)) {
				throw new YLZCBPException("参数不能为空！");
			}
			//调用短信平台接口
			RPCServiceClient client = new RPCServiceClient();
			Options options = client.getOptions();  
			options.setTo(ENDPOINT_TEST);  
			options.setAction("smsSend");
			options.setManageSession(true); 
			options.setProperty(HTTPConstants.REUSE_HTTP_CLIENT,true); 
			Object[] obj = str.split(",./");
			//Object[] obj = new Object[]{"149900","000000","测试山西您好,再次鉴定,2017-07-26 16:52:00,山西医科大学第二附属医院,请被鉴定人携带完整病历原件(按时间排序,用后留档,不予退还)及X片,CT片等辅助检查资料.不能参加请提前请假,无故不到本次鉴定终止.收到请回复'是'.","18510409981","10000100","","备注"};
			QName qname = new QName(NAMESPACE, "smsSend");
			OMElement res = client.invokeBlocking(qname, obj);
			client.cleanupTransport();
			System.out.println(res);
			//解析出参
			SoapBody data2 = OMEUtil.parseElement2(res);
			//拼接返回报文
			// 生成返回soap
			ResponseEntity resEntity = new ResponseEntity();
			//resEntity.setParaName(new String[]{"result","description"});
			//resEntity.setParaValue(new String[]{data2.getData("result"),data2.getData("description")});
			resEntity.setParaName(new String[]{"showtype"});
			resEntity.setParaValue(new String[]{"1"});
			String s = data2.getData("result");
			String description = data2.getData("description");
			// 记录集
			ResultSet resultSet = new ResultSet();

			// 设置数据集的属性
			//resultSet.setName("list01");// 数据集名称
			resultSet.setView(false);// 数据集是否有对应的数据库表
			resultSet.setTableName("");// 数据集对应的数据库表的名称
			String columnName = "返回值编码:result,返回描述:description,批次号:im004,流水号:im005,手机号:aae005,其他信息:aae013";
			resultSet.setColumns(columnName);
			//resultSet.setTotalNum(2);// 符合查询条件的记录有1条。（分页查询使用）

			List<HashMap<String, String>> result = new ArrayList<HashMap<String,String>>();
			HashMap<String, String> hm = new HashMap<String, String>();
			List list2 = data2.getResultSet("list01");
			if(list2 == null ||"".equals(list2)){
			}else{
				for (int i = 0; i < list2.size(); i++) {
					HashMap map = (HashMap) list2.get(i);
					hm.put("result",s);
					hm.put("description",description);
					hm.put("im004",(String) map.get("im004"));
					hm.put("im005",(String) map.get("im005"));
					hm.put("aae005",(String) map.get("aae005"));
					hm.put("aae013",(String) map.get("aae013"));
					result.add(hm);
				}
			}
			resultSet.setResultSet(result);
			resEntity.setResultSet(resultSet);

			msg = ReaderSoapXmlOut.readerSoapXMLOut(resEntity);
			System.out.println("msg------"+msg);
		}catch (Exception e) {
			ResponseEntity resEntity = new ResponseEntity();
			resEntity.setErrorMsg(e.toString());
			msg = ReaderSoapXmlOut.readerSoapXMLOut(resEntity);
			logger.error(e);
			e.printStackTrace();
		}
	
		return msg;
	}
	/**
	 * 短信导入通过String
	 * @return String
	 * @throws Exception
	 */
	public static String SMSImportByString(String str) throws Exception {
		String msg = "";
		try {
			if (null == str || "".equals(str)) {
				throw new YLZCBPException("参数不能为空！");
			}
			//调用短信平台接口
			RPCServiceClient client = new RPCServiceClient();
			Options options = client.getOptions();  
			options.setTo(ENDPOINT_TEST);  
			//Object[] obj = str.split(",./");
			Object[] obj = new Object[]{"149900","000000","李坤学","18435117265","142621199501290535"};
			QName qname = new QName(NAMESPACE, "mobileImport");
			OMElement res = client.invokeBlocking(qname, obj);
			System.out.println(res);
			//解析出参
			SoapBody data2 = OMEUtil.parseElement2(res);
			//拼接返回报文
			// 生成返回soap
			ResponseEntity resEntity = new ResponseEntity();
			resEntity.setParaName(new String[]{"result","description"});
			resEntity.setParaValue(new String[]{data2.getData("result"),data2.getData("description")});
			// 记录集
			ResultSet resultSet = new ResultSet();

			// 设置数据集的属性
			resultSet.setName("list01");// 数据集名称
			resultSet.setView(false);// 数据集是否有对应的数据库表
			resultSet.setTableName("");// 数据集对应的数据库表的名称
			String columnName = "姓名(单位名称):aac003,性别1男2女(取身份证中信息)3单位:aac004,手机号:aae005,身份证号码(单位性质则填入单位编码):aae135,手机号码归属地:ima002," +
					"手机运营商:ima003,经办人:userid,经办时间:aae036,最后修改人,muserid,最后修改时间:aae015,有效标志:aae100,其他信息:aae013";
			resultSet.setColumns(columnName);
			resultSet.setTotalNum(2);// 符合查询条件的记录有1条。（分页查询使用）

			List<HashMap<String, String>> result = new ArrayList<HashMap<String,String>>();
			HashMap<String, String> hm = new HashMap<String, String>();
			List list2 = data2.getResultSet("list01");
			if(list2 == null ||"".equals(list2)){
			}else{
				for (int i = 0; i < list2.size(); i++) {
					HashMap map = (HashMap) list2.get(i);
					hm.put("aac003",(String) map.get("aac003"));
					hm.put("aac004",(String) map.get("aac004"));
					hm.put("aae005",(String) map.get("aae005"));
					hm.put("aae135",(String) map.get("aae135"));
					hm.put("ima002",(String) map.get("ima002"));
					hm.put("ima003",(String) map.get("ima003"));
					hm.put("userid",(String) map.get("userid"));
					hm.put("aae036",(String) map.get("aae036"));
					hm.put("muserid",(String) map.get("muserid"));
					hm.put("aae015",(String) map.get("aae015"));
					hm.put("aae100",(String) map.get("aae100"));
					hm.put("aae013",(String) map.get("aae013"));
					result.add(map);
				}
			}
			resultSet.setResultSet(result);
			resEntity.setResultSet(resultSet);

			msg = ReaderSoapXmlOut.readerSoapXMLOut(resEntity);
			System.out.println("msg------"+msg);
		}catch (Exception e) {
			ResponseEntity resEntity = new ResponseEntity();
			resEntity.setErrorMsg(e.toString());
			msg = ReaderSoapXmlOut.readerSoapXMLOut(resEntity);
			logger.error(e);
			e.printStackTrace();
		}
	
		return msg;
	}
	/*public static void main(String[] args) throws Exception {
		String ome = "<xsd:ome xmlns:xsd=\"http://www.ylzinfo.com/xsd\">\""
			+"<in:business xmlns:in=\"http://www.ylzinfo.com/\">"
			+"<para><userid>sthlw</userid></para>"
			+"<para><password>123456</password></para>"
			+"<paralist name=\"list01\">"
			+"<row im103=\"您的验证码为:5432此验证码10分钟有效,请尽快使用!\" aae005=\"18435117265\" im004=\"1003\" aae036=\"\" aae013=\"测试\" ></row>"
			//+"<row im103=\"123456\" aae005=\"18435117265\" im004=\"12345678900123456789\" aae036=\"\" aae013=\"测试\" ></row>"
			+"</paralist>"
			+"</in:business>"
			+"</xsd:ome>";
		InputStream parser = null;
		try {
			parser = new ByteArrayInputStream(ome.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		StAXOMBuilder builder = new StAXOMBuilder(parser);
		OMElement omRespBody= builder.getDocumentElement(); 
		System.out.println("初始----"+omRespBody);
		SMSsendingByXML(omRespBody);
	}*/
	public static void main(String[] args) throws Exception {
//		String str = "sthlw,./123456,./您的验证码为:5432此验证码10分钟有效,请尽快使用!,./18435165523,./13002,./,./备注";
		String str = "sthlw,./123456,./您的订单已经生成,订单号为123456,请注意查收,./18435165523,./130021,./,./备注";
		SMSsendingByString(str);
	}
}
