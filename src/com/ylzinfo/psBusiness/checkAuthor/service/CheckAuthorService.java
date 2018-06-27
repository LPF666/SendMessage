/**
 * 
 */
package com.ylzinfo.psBusiness.checkAuthor.service;

import org.apache.axiom.om.OMElement;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.ylzinfo.cbp.exception.YLZCBPException;
import com.ylzinfo.common.GlobalConst;
import com.ylzinfo.esb.bas.EsbResponse;
import com.ylzinfo.esb.client.XMLRequest;
import com.ylzinfo.esb.soap.ReaderSoapXmlOut;
import com.ylzinfo.fj.utils.XmlUtils;
import com.ylzinfo.listener.esb.ESBConfig;
import com.ylzinfo.listener.esb.ESBConfigDTO;
import com.ylzinfo.psBusiness.checkAuthor.dto.BaseinfoDTO;
import com.ylzinfo.psBusiness.checkAuthor.dto.CheckAuthorDTO;
import com.ylzinfo.psBusiness.checkAuthor.dto.QueryBcc001DTO;
import com.ylzinfo.util.SecurityUtil;

/**
 * @description 实名认证服务
 * @author lingxiaoling
 * @date 2015-10-12 下午01:24:00
 */
public class CheckAuthorService {
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
	public String checkAuthor(OMElement ome) throws Exception {
		CheckAuthorDTO dto = new CheckAuthorDTO();
		System.out.println(ome);
//		try {
//			if (null == aac002 || "".equals(aac002)) {
//				dto.setAae314("0");
//				dto.setAae317("身份证号不能为空！");
//				throw new YLZCBPException("身份证号不能为空！");
//			}
//			if (null == aac003 || "".equals(aac003)) {
//				dto.setAae314("0");
//				dto.setAae317("姓名不能为空！");
//				throw new YLZCBPException("姓名不能为空！");
//			}
//			if (null == bcc002 || "".equals(bcc002)) {
//				dto.setAae314("0");
//				dto.setAae317("社保卡号不能为空！");
//				throw new YLZCBPException("社保卡号不能为空！");
//			}
//			// 入参解密
//		//	aac002 = SecurityUtil.decoder(aac002, GlobalConst.globalvalue.get("decodekey"));
//		//	aac003 = SecurityUtil.decoder(aac003, GlobalConst.globalvalue.get("decodekey"));
//		//	bcc002 = SecurityUtil.decoder(bcc002, GlobalConst.globalvalue.get("decodekey"));
//			XMLRequest xmlRequest = new XMLRequest();
//			ESBConfigDTO config = ESBConfig.configs.get("sxmsy");
//			xmlRequest.setEsbUrl(config.getUrl()); // ESB服务器路径
//			xmlRequest.setEsbUserPwd(new String[] { config.getUsername(), config.getPasswd() });// ESB访问用户及密码
//			String[] v_param = { "aac002", "aac003" };// 访问参数
//			String[] v_value = { aac002, aac003 };// 访问参数值
//			xmlRequest.setSvid("common_person_getbcc001");// 服务ID
//			xmlRequest.setParam(v_param);
//			xmlRequest.setParamValue(v_value);
//			EsbResponse esbRsp = xmlRequest.postXmlRequest();// 发送封装SOAP数据
//			logger.info(esbRsp.getResponseData());
//			QueryBcc001DTO bcc001dto = XmlUtils.getOutPara(esbRsp.getResponseData(), QueryBcc001DTO.class);
//			if (null == bcc001dto.getBcc001() || "".equals(bcc001dto.getBcc001())) {
//				dto.setAae314("0");
//				dto.setAae317("身份证号码为：" + aac002 + "，姓名为：" + aac003 + "无对应的社保关联号！");
//				throw new YLZCBPException("身份证号码为：" + aac002 + "，姓名为：" + aac003 + "无对应的社保关联号！");
//			}
//			String[] v_param1 = { "bcc001" };// 访问参数
//			String[] v_value1 = { bcc001dto.getBcc001() };// 访问参数值
//			xmlRequest.setSvid("common_person_baseinfoquery");// 服务ID
//			xmlRequest.setParam(v_param1);
//			xmlRequest.setParamValue(v_value1);
//			esbRsp = xmlRequest.postXmlRequest();// 发送封装SOAP数据
//			logger.info(esbRsp.getResponseData());
//			BaseinfoDTO baseinfoDTO = XmlUtils.getOutPara(esbRsp.getResponseData(), BaseinfoDTO.class);
//			if (null == baseinfoDTO.getAac002() || "".equals(baseinfoDTO.getAac002())) {
//				dto.setAae314("0");
//				dto.setAae317("查不到身份证号码为：" + aac002 + "，姓名为：" + aac003 + "的用户信息！");
//				throw new YLZCBPException("查不到身份证号码为：" + aac002 + "，姓名为：" + aac003 + "的用户信息！");
//			} else {
//				if (bcc002.equals(baseinfoDTO.getBcc002())) {
//					dto.setAae314("1");
//					dto.setAae317("认证成功！");
//					dto.setBcc001(bcc001dto.getBcc001());
//				} else {
//					dto.setAae314("0");
//					dto.setAae317("认证失败：社保卡号" + bcc002 + "错误！");
//					throw new YLZCBPException("认证失败：社保卡号" + bcc002 + "错误！");
//				}
//			}
//		} catch (YLZCBPException ye) {
//			logger.info(ye.getMessage());
//			logger.error(ye.getMessage());
//		} catch (Exception e) {
//			dto.setAae314("0");
//			dto.setAae317("服务调用失败，请联系管理员！");
//			logger.error(e);
//		}
//		// 返回参数加密处理
//	//	dto.setAae314(SecurityUtil.encoder(dto.getAae314(), GlobalConst.globalvalue.get("encodekey")));
//	//	dto.setAae317(SecurityUtil.encoder(dto.getAae317(), GlobalConst.globalvalue.get("encodekey")));
////		if (!StringUtils.isBlank(dto.getBcc001())) {
////			dto.setBcc001(SecurityUtil.encoder(dto.getBcc001(), GlobalConst.globalvalue.get("encodekey")));
////		}
//		return ReaderSoapXmlOut.readerSoapXMlOut(dto.getColset(), dto.getStructsList(), "");
		return ""	;
	}
}
