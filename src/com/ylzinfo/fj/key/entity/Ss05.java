package com.ylzinfo.fj.key.entity;

/**
 * <p>
 * Title: SS05
 * </p>
 * <p>
 * Description:路由编码
 * </p>
 * <p>
 * Copyright:Copyright(c)2010-2011
 * </p>
 * <p>
 * Company:易联众信息技术股份有限公司
 * </p>
 * 
 * @hibernate.class table="SS05"
 * @author
 * @email
 * @version 1.0
 */
public class Ss05 implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3372024446448949284L;
	private java.lang.Long ysz066; // 路由编码ID

	/**
	 * 路由编码ID
	 * 
	 * @hibernate.property column="ysz066"
	 * @return ysz066
	 */
	public java.lang.Long getYsz066() {
		return this.ysz066;
	}

	/**
	 * 路由编码ID
	 * 
	 * @hibernate.property column="ysz066"
	 * @return ysz066
	 */
	public void setYsz066(java.lang.Long ysz066) {
		this.ysz066 = ysz066;
	}

	private java.lang.String aac002; // 身份证号码
	private java.lang.String aae140; // 险种类型
	private java.lang.String yae512; // 所属地级市行政区划代码

	private java.lang.String aac001; // 原业务系统人员编号

	/**
	 * 身份证号码
	 * 
	 * @hibernate.property column="aac002"
	 * @return aac002
	 */
	public java.lang.String getAac002() {
		return this.aac002;
	}

	/**
	 * 身份证号码
	 * 
	 * @hibernate.property column="aac002"
	 * @return aac002
	 */
	public void setAac002(java.lang.String aac002) {
		this.aac002 = aac002;
	}

	/**
	 * 险种类型
	 * 
	 * @hibernate.property column="aae140"
	 * @return aae140
	 */
	public java.lang.String getAae140() {
		return this.aae140;
	}

	/**
	 * 险种类型
	 * 
	 * @hibernate.property column="aae140"
	 * @return aae140
	 */
	public void setAae140(java.lang.String aae140) {
		this.aae140 = aae140;
	}

	/**
	 * 所属地级市行政区划代码
	 * 
	 * @hibernate.property column="yae512"
	 * @return yae512
	 */
	public java.lang.String getYae512() {
		return this.yae512;
	}

	/**
	 * 所属地级市行政区划代码
	 * 
	 * @hibernate.property column="yae512"
	 * @return yae512
	 */
	public void setYae512(java.lang.String yae512) {
		this.yae512 = yae512;
	}

	/**
	 * 原业务系统人员编号
	 * 
	 * @hibernate.property column="aac001"
	 * @return aac001
	 */
	public java.lang.String getAac001() {
		return this.aac001;
	}

	/**
	 * 原业务系统人员编号
	 * 
	 * @hibernate.property column="aac001"
	 * @return aac001
	 */
	public void setAac001(java.lang.String aac001) {
		this.aac001 = aac001;
	}

}
