package com.ylzinfo.fj.key.dto;

import com.ylzinfo.fj.utils.BaseDTO;
import com.ylzinfo.fj.utils.OutPara;

public class KeyDTO extends BaseDTO {

	@OutPara(index = 0, label = "校验用户的密钥")
	private String key;

	private String sid;
	private String usr;
	private String termid;
	private String aac002;
	private String yae512;
	private String yae511;
	private String router;

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key
	 *            the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the sid
	 */
	public String getSid() {
		return sid;
	}

	/**
	 * @param sid
	 *            the sid to set
	 */
	public void setSid(String sid) {
		this.sid = sid;
	}

	/**
	 * @return the usr
	 */
	public String getUsr() {
		return usr;
	}

	/**
	 * @param usr
	 *            the usr to set
	 */
	public void setUsr(String usr) {
		this.usr = usr;
	}

	/**
	 * @return the termid
	 */
	public String getTermid() {
		return termid;
	}

	/**
	 * @param termid
	 *            the termid to set
	 */
	public void setTermid(String termid) {
		this.termid = termid;
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

	public String getYae512() {
		return yae512;
	}

	public void setYae512(String yae512) {
		this.yae512 = yae512;
	}

	public String getYae511() {
		return yae511;
	}

	public void setYae511(String yae511) {
		this.yae511 = yae511;
	}

	public String getRouter() {
		return router;
	}

	public void setRouter(String router) {
		this.router = router;
	}

}
