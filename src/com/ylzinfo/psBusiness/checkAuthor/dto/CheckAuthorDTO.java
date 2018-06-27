/**
 * 
 */
package com.ylzinfo.psBusiness.checkAuthor.dto;

import com.ylzinfo.fj.utils.BaseDTO;
import com.ylzinfo.fj.utils.OutPara;

/**
 * @description 实名认证服务返回dto
 * @author lingxiaoling
 * @date 2015-10-12 下午01:27:54
 */
public class CheckAuthorDTO extends BaseDTO {
	@OutPara(index = 0, label = "校验成功标志")
	private String aae314;
	@OutPara(index = 1, label = "校验成功说明")
	private String aae317;
	@OutPara(index = 2, label = "社保关联号")
	private String bcc001;

	/**
	 * @return the aae314
	 */
	public String getAae314() {
		return aae314;
	}

	/**
	 * @param aae314
	 *            the aae314 to set
	 */
	public void setAae314(String aae314) {
		this.aae314 = aae314;
	}

	/**
	 * @return the aae317
	 */
	public String getAae317() {
		return aae317;
	}

	/**
	 * @param aae317
	 *            the aae317 to set
	 */
	public void setAae317(String aae317) {
		this.aae317 = aae317;
	}

	/**
	 * @return the bcc001
	 */
	public String getBcc001() {
		return bcc001;
	}

	/**
	 * @param bcc001
	 *            the bcc001 to set
	 */
	public void setBcc001(String bcc001) {
		this.bcc001 = bcc001;
	}

}
