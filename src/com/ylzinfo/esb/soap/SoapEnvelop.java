package com.ylzinfo.esb.soap;

/**
 * SOAP消息信封处理帮助类
 * Copyright ylzinfo Corporation. All rights reserved.
 * @author:    LvRongLin
 * History:  2009-12-1 Created.
 * Version: 1.0
 */
public  class SoapEnvelop {

	public static final String ENVELOP_END = "</soap:Envelope>";

	public static final String BODY_END = "</soap:Body>";

	public static final String BODY_START = "<soap:Body>";

	public static final String BUSINESS_START="<out:business xmlns:out=\"http://www.ylzinfo.com/\">";
	
	public static final String BUSINESS_START_MOLSS="<out:business xmlns:out=\"http://www.molss.gov.cn/\">";
	
	public static final String BUSINESS_END="</out:business>";
	
	public static final String HEAD_END = "</out:system></soap:Header>";

	public static final String HEAD_START = "<soap:Header><out:system xmlns:out=\"http://www.ylzinfo.com/\">";
	
	public static final String HEAD_START_MOLSS = "<soap:Header><out:system xmlns:out=\"http://www.molss.gov.cn/\">";

	public static final String ENVELOP_START = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" soap:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">";

	public static final String XML_PI = "<?xml version=\"1.0\" encoding=\"GBK\"?>";
	
	public static final String  TotalNum_Start= "<result totalnum=\"";
	public static final String  TotalNum_End=  "\" />";
	
	public static final String  CPAGE_Start= "<result cpage=\"";
	public static final String  CPAGE_End=  "\" />";
	
	public static final String  PAGES_Start= "<result pages=\"";
	public static final String  PAGES_End=  "\" />";
	
	public static final String  ROWCOUNT_Start= "<result rowCount=\"";
	public static final String  ROWCOUNT_End=  "\" />";
	
	public static final String  Single_Row= "<result showtype=\"1\"/>";
	
	public static final String  Show_Type_Result= "</result>";
	
	public static final String  ServiceID_Start= "<result serviceId=\"";
	public static final String  ServiceID_End= "\"/>";
	
	public static final String  Information_Start= "<result information=\"";
	public static final String  Information_End= "\"/>";
	
	public static final String  Multilateral_Row= "<result showtype=\"2\"/>";
	
	public static final String  Structs_START = "<resultset name=\"structs\">";
	public static final String  Structs_END = "</resultset>";
	
	public static final String  Retrieve_START = "<resultset name=\"retrieve\">";
	public static final String  Retrieve_END = "</resultset>";
	
	public static final String  Message_START = "<resultset name=\"retrieve\" information=\"";
	public static final String  Message_R= "\" >";
	public static final String  Message_End = "</resultset>";
    public static final String  Single= "1";
	
	public static final String  Multilateral= "2";
	
	public static final String  FAULT_START="<soap:Fault>";
	public static final String  FAULT_END="</soap:Fault>";
	
	public static final String  FAULT_CODE_START="<faultcode>";
	public static final String  FAULT_CODE_END="</faultcode>";
	
	public static final String  FAULT_STR_START="<faultstring>";
	public static final String  FAULT_STR_END="</faultstring>";
	
	
	public static final String ERROR_MSG_START="<error msg=\"";
	public static final String ERROR_MSG_END="\" />";

    public static final String REVERSAL_RESULT_START="<result>";
    public static final String REVERSAL_RESULT_END="</result>";
    public static final String REVERSAL_SUCCESS_START="<success>";
    public static final String REVERSAL_SUCCESS_END="</success>";
    public static final String REVERSAL_URL_START="<url>";
    public static final String REVERSAL_URL_END="</url>";
    public static final String REVERSAL_SQL_START="<sql>";
    public static final String REVERSAL_SQL_END="</sql>";
    public static final String REVERSAL_METHOD_START="<method>";
    public static final String REVERSAL_METHOD_END="</method>";
    public static final String REVERSAL_NAMESPACE_START="<namespace>";
    public static final String REVERSAL_NAMESPACE_END="</namespace>";
    public static final String REVERSAL_ERRORMSG_START="<errorMsg>";
    public static final String REVERSAL_ERRORMSG_END="</errorMsg>";
    public static final String REVERSAL_PARA_START="<para ";


	/**
	 * 构造要发送的SOAP消息
	 * 
	 * @param head
	 *            SOAP验证头
	 * @param env
	 *            消息体
	 * @return
	 */
	public static String buildSOAP(String head, String env) {
		StringBuffer soapBuf = new StringBuffer();
		{
			soapBuf.append(XML_PI);
			soapBuf.append(ENVELOP_START);

			soapBuf.append(HEAD_START);
			
			soapBuf.append(head);

			soapBuf.append(HEAD_END);

			soapBuf.append(BODY_START);
			soapBuf.append(getSoapBody(env));
			soapBuf.append(BODY_END);

			soapBuf.append(ENVELOP_END);
		}

		return soapBuf.toString();
	}

	/**
	 * 获取消息信息体
	 * 
	 * @param envelop
	 *            消息信封
	 * @return 返回消息体
	 */
	public static String getSoapBody(String envelop) {
		if (null == envelop) {
			return "";
		}
		if (envelop.indexOf(BODY_START) < 0)
			return "";

		return envelop.substring(envelop.indexOf(BODY_START)
				+ BODY_START.length(), envelop.indexOf(BODY_END));
	}
}