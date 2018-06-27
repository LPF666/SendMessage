package com.ylzinfo.fj.key.entity;

import java.util.Date;

/**
 * Ss01 entity. @author MyEclipse Persistence Tools
 */

public class Ss01 implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -190314559971858087L;
	private Long ysz037;
	private String yse069;
	private String yse073;
	private String ysc001;
	private String aac002;
	private String yse074;
	private String yse072;
	private Date yse075;

	// Constructors

	/** default constructor */
	public Ss01() {
	}

	/** minimal constructor */
	public Ss01(Long ysz037) {
		this.ysz037 = ysz037;
	}

	/** full constructor */
	public Ss01(Long ysz037, String yse069, String yse073, String yse074, String yse072, Date yse075) {
		this.ysz037 = ysz037;
		this.yse069 = yse069;
		this.yse073 = yse073;
		this.yse074 = yse074;
		this.yse072 = yse072;
		this.yse075 = yse075;
	}

	// Property accessors

	public Long getYsz037() {
		return this.ysz037;
	}

	public void setYsz037(Long ysz037) {
		this.ysz037 = ysz037;
	}

	public String getYse069() {
		return this.yse069;
	}

	public void setYse069(String yse069) {
		this.yse069 = yse069;
	}

	public String getYse073() {
		return this.yse073;
	}

	public void setYse073(String yse073) {
		this.yse073 = yse073;
	}

	public String getYse074() {
		return this.yse074;
	}

	public void setYse074(String yse074) {
		this.yse074 = yse074;
	}

	public String getYse072() {
		return this.yse072;
	}

	public void setYse072(String yse072) {
		this.yse072 = yse072;
	}

	public Date getYse075() {
		return this.yse075;
	}

	public void setYse075(Date yse075) {
		this.yse075 = yse075;
	}

	/**
	 * @return the ysc001
	 */
	public String getYsc001() {
		return ysc001;
	}

	/**
	 * @param ysc001
	 *            the ysc001 to set
	 */
	public void setYsc001(String ysc001) {
		this.ysc001 = ysc001;
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

}