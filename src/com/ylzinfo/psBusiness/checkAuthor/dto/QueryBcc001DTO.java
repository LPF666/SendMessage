/**
 * 
 */
package com.ylzinfo.psBusiness.checkAuthor.dto;

import com.ylzinfo.fj.utils.BaseDTO;

/**
 * @description 查询社保关联号dto
 * @author lingxiaoling
 * @date 2015-10-12 下午05:43:58
 */
public class QueryBcc001DTO extends BaseDTO {
	private String bcc001;// 社保关联号

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
