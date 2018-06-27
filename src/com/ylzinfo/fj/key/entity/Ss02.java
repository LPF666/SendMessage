package com.ylzinfo.fj.key.entity;

/**
 * Ss02 entity. @author MyEclipse Persistence Tools
 */

public class Ss02 implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 6454976050557779729L;
	private Long ysz038;
	private String yse073;
	private String yse078;
	private String yse079;

	// Constructors

	/** default constructor */
	public Ss02() {
	}

	/** minimal constructor */
	public Ss02(Long ysz038) {
		this.ysz038 = ysz038;
	}

	/** full constructor */
	public Ss02(Long ysz038, String yse073, String yse078, String yse079) {
		this.ysz038 = ysz038;
		this.yse073 = yse073;
		this.yse078 = yse078;
		this.yse079 = yse079;
	}

	// Property accessors

	public Long getYsz038() {
		return this.ysz038;
	}

	public void setYsz038(Long ysz038) {
		this.ysz038 = ysz038;
	}

	public String getYse073() {
		return this.yse073;
	}

	public void setYse073(String yse073) {
		this.yse073 = yse073;
	}

	public String getYse078() {
		return this.yse078;
	}

	public void setYse078(String yse078) {
		this.yse078 = yse078;
	}

	public String getYse079() {
		return this.yse079;
	}

	public void setYse079(String yse079) {
		this.yse079 = yse079;
	}

}