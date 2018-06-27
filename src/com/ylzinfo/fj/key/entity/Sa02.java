package com.ylzinfo.fj.key.entity;

/**
 * <p>
 * Title: SA02
 * </p>
 * <p>
 * Description:数据权限校验规则
 * </p>
 * <p>
 * Copyright:Copyright(c)2010-2011
 * </p>
 * <p>
 * Company:易联众信息技术股份有限公司
 * </p>
 * 
 * @hibernate.class table="SA02"
 * @author linxiaoling
 * @email linxiaoling@ylzinfo.com
 * @version 1.0
 */
public class Sa02 implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2151618903717636187L;
	private java.lang.Long ysz064; // 规则ID

	/**
	 * 规则ID
	 * 
	 * @hibernate.property column="ysz064"
	 * @return ysz064
	 */
	public java.lang.Long getYsz064() {
		return this.ysz064;
	}

	/**
	 * 规则ID
	 * 
	 * @hibernate.property column="ysz064"
	 * @return ysz064
	 */
	public void setYsz064(java.lang.Long ysz064) {
		this.ysz064 = ysz064;
	}

	private java.lang.String ysa004; // 规则名称
	private java.lang.String ysa005; // 规则调用的类名
	private java.lang.String ysa006; // 参数DTO类名
	private java.lang.String ysa007; // 参数属性名
	private java.lang.String aae013; // 备注

	/**
	 * 规则名称
	 * 
	 * @hibernate.property column="ysa004"
	 * @return ysa004
	 */
	public java.lang.String getYsa004() {
		return this.ysa004;
	}

	/**
	 * 规则名称
	 * 
	 * @hibernate.property column="ysa004"
	 * @return ysa004
	 */
	public void setYsa004(java.lang.String ysa004) {
		this.ysa004 = ysa004;
	}

	/**
	 * 规则调用的类名
	 * 
	 * @hibernate.property column="ysa005"
	 * @return ysa005
	 */
	public java.lang.String getYsa005() {
		return this.ysa005;
	}

	/**
	 * 规则调用的类名
	 * 
	 * @hibernate.property column="ysa005"
	 * @return ysa005
	 */
	public void setYsa005(java.lang.String ysa005) {
		this.ysa005 = ysa005;
	}

	/**
	 * 参数DTO类名
	 * 
	 * @hibernate.property column="ysa006"
	 * @return ysa006
	 */
	public java.lang.String getYsa006() {
		return this.ysa006;
	}

	/**
	 * 参数DTO类名
	 * 
	 * @hibernate.property column="ysa006"
	 * @return ysa006
	 */
	public void setYsa006(java.lang.String ysa006) {
		this.ysa006 = ysa006;
	}

	/**
	 * 参数属性名
	 * 
	 * @hibernate.property column="ysa007"
	 * @return ysa007
	 */
	public java.lang.String getYsa007() {
		return this.ysa007;
	}

	/**
	 * 参数属性名
	 * 
	 * @hibernate.property column="ysa007"
	 * @return ysa007
	 */
	public void setYsa007(java.lang.String ysa007) {
		this.ysa007 = ysa007;
	}

	/**
	 * 备注
	 * 
	 * @hibernate.property column="aae013"
	 * @return aae013
	 */
	public java.lang.String getAae013() {
		return this.aae013;
	}

	/**
	 * 备注
	 * 
	 * @hibernate.property column="aae013"
	 * @return aae013
	 */
	public void setAae013(java.lang.String aae013) {
		this.aae013 = aae013;
	}

}
