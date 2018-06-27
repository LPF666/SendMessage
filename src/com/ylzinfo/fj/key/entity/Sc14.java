package com.ylzinfo.fj.key.entity;

import java.util.Date;

/**
 * Sc14 entity. @author MyEclipse Persistence Tools
 */

public class Sc14 implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1697713874505260424L;
	private Long ysz036;
	private String ysc001;
	private String aac002;
	private String yse069;
	private String yse070;
	private String yse071;
	private String yse072;
	private Date yse076;
	private Date yse077;

	// Constructors

	/** default constructor */
	public Sc14() {
	}

	/** minimal constructor */
	public Sc14(Long ysz036) {
		this.ysz036 = ysz036;
	}

	/** full constructor */
	public Sc14(Long ysz036, String ysc001, String aac002, String yse069, String yse070, String yse071, String yse072, Date yse076, Date yse077) {
		this.ysz036 = ysz036;
		this.ysc001 = ysc001;
		this.aac002 = aac002;
		this.yse069 = yse069;
		this.yse070 = yse070;
		this.yse071 = yse071;
		this.yse072 = yse072;
		this.yse076 = yse076;
		this.yse077 = yse077;
	}

	// Property accessors

	public Long getYsz036() {
		return this.ysz036;
	}

	public void setYsz036(Long ysz036) {
		this.ysz036 = ysz036;
	}

	public String getYsc001() {
		return this.ysc001;
	}

	public void setYsc001(String ysc001) {
		this.ysc001 = ysc001;
	}

	public String getAac002() {
		return this.aac002;
	}

	public void setAac002(String aac002) {
		this.aac002 = aac002;
	}

	public String getYse069() {
		return this.yse069;
	}

	public void setYse069(String yse069) {
		this.yse069 = yse069;
	}

	public String getYse070() {
		return this.yse070;
	}

	public void setYse070(String yse070) {
		this.yse070 = yse070;
	}

	public String getYse071() {
		return this.yse071;
	}

	public void setYse071(String yse071) {
		this.yse071 = yse071;
	}

	public String getYse072() {
		return this.yse072;
	}

	public void setYse072(String yse072) {
		this.yse072 = yse072;
	}

	public Date getYse076() {
		return this.yse076;
	}

	public void setYse076(Date yse076) {
		this.yse076 = yse076;
	}

	public Date getYse077() {
		return this.yse077;
	}

	public void setYse077(Date yse077) {
		this.yse077 = yse077;
	}

}