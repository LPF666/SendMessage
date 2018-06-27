/**
 * 
 */
package com.ylzinfo.psBusiness.checkAuthor.dto;

import com.ylzinfo.fj.utils.BaseDTO;
import com.ylzinfo.fj.utils.OutPara;

/**
 * @description 用户基本信息DTO
 * @author lingxiaoling
 * @date 2015-10-13 上午10:55:15
 */
public class BaseinfoDTO extends BaseDTO {
	@OutPara(index = 0, label = "姓名")
	private String aac003;
	@OutPara(index = 1, label = "社会保障卡号码")
	private String bcc002;
	@OutPara(index = 2, label = "身份证号")
	private String aac002;
	@OutPara(index = 3, label = "性别")
	private String aac004;
	@OutPara(index = 4, label = "民族")
	private String aac005;
	@OutPara(index = 5, label = "出生日期")
	private String aac006;
	@OutPara(index = 6, label = "出生日期（档案记录）")
	private String bcc005;
	@OutPara(index = 7, label = "通讯地址")
	private String aae006;
	@OutPara(index = 8, label = "邮政编码")
	private String aae007;
	@OutPara(index = 9, label = "联系电话")
	private String aae005;

	/**
	 * @return the aac003
	 */
	public String getAac003() {
		return aac003;
	}

	/**
	 * @param aac003
	 *            the aac003 to set
	 */
	public void setAac003(String aac003) {
		this.aac003 = aac003;
	}

	/**
	 * @return the bcc002
	 */
	public String getBcc002() {
		return bcc002;
	}

	/**
	 * @param bcc002
	 *            the bcc002 to set
	 */
	public void setBcc002(String bcc002) {
		this.bcc002 = bcc002;
	}

	/**
	 * @return the aac002
	 */
	public String getAac002() {
		return aac002;
	}

	/**
	 * @param aac002
	 *            the aac002 to set
	 */
	public void setAac002(String aac002) {
		this.aac002 = aac002;
	}

	/**
	 * @return the aac004
	 */
	public String getAac004() {
		return aac004;
	}

	/**
	 * @param aac004
	 *            the aac004 to set
	 */
	public void setAac004(String aac004) {
		this.aac004 = aac004;
	}

	/**
	 * @return the aac005
	 */
	public String getAac005() {
		return aac005;
	}

	/**
	 * @param aac005
	 *            the aac005 to set
	 */
	public void setAac005(String aac005) {
		this.aac005 = aac005;
	}

	/**
	 * @return the aac006
	 */
	public String getAac006() {
		return aac006;
	}

	/**
	 * @param aac006
	 *            the aac006 to set
	 */
	public void setAac006(String aac006) {
		this.aac006 = aac006;
	}

	/**
	 * @return the bcc005
	 */
	public String getBcc005() {
		return bcc005;
	}

	/**
	 * @param bcc005
	 *            the bcc005 to set
	 */
	public void setBcc005(String bcc005) {
		this.bcc005 = bcc005;
	}

	/**
	 * @return the aae006
	 */
	public String getAae006() {
		return aae006;
	}

	/**
	 * @param aae006
	 *            the aae006 to set
	 */
	public void setAae006(String aae006) {
		this.aae006 = aae006;
	}

	/**
	 * @return the aae007
	 */
	public String getAae007() {
		return aae007;
	}

	/**
	 * @param aae007
	 *            the aae007 to set
	 */
	public void setAae007(String aae007) {
		this.aae007 = aae007;
	}

	/**
	 * @return the aae005
	 */
	public String getAae005() {
		return aae005;
	}

	/**
	 * @param aae005
	 *            the aae005 to set
	 */
	public void setAae005(String aae005) {
		this.aae005 = aae005;
	}

}
