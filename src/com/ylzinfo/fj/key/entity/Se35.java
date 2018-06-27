package com.ylzinfo.fj.key.entity;

/**
 * <p>
 * Title: SE35
 * </p>
 * <p>
 * Description:SE35 服务
 * </p>
 * <p>
 * Copyright:Copyright(c)2010-2011
 * </p>
 * <p>
 * Company:易联众信息技术股份有限公司
 * </p>
 * 
 * @hibernate.class table="SE35"
 * @author
 * @email
 * @version 1.0
 */
public class Se35 implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5216073397763201238L;
	private java.lang.Long ysz053; // 服务ID

	/**
	 * 服务ID
	 * 
	 * @hibernate.property column="ysz053"
	 * @return ysz053
	 */
	public java.lang.Long  getYsz053() {
		return this.ysz053;
	}

	/**
	 * 服务ID
	 * 
	 * @hibernate.property column="ysz053"
	 * @return ysz053
	 */
	public void setYsz053(java.lang.Long  ysz053) {
		this.ysz053 = ysz053;
	}

	private java.lang.String yse073; // 服务编号
	private java.lang.String yse103; // 服务名称
	private java.lang.String yse119; // 所属模块
	private java.lang.String yse120; // 服务说明
	private java.lang.String aae013; // 备注
	private java.sql.Date aae036; // 创建时间
	private java.lang.String ysf016; // 服务类别
	private java.lang.String yse133; // 服务状态
	private java.lang.String yse153; // 数据权限控制标志
	private java.lang.Long ysz064; // 数据权限规则ID
	private java.lang.Long ysz060; // 数据权限险种ID
	private java.lang.String yse163; // 是否自动路由
	private java.lang.String yse164; // 是否记录日志
	private java.lang.String yse151; // 险种类型

	/**
	 * 服务编号
	 * 
	 * @hibernate.property column="yse073"
	 * @return yse073
	 */
	public java.lang.String getYse073() {
		return this.yse073;
	}

	/**
	 * 服务编号
	 * 
	 * @hibernate.property column="yse073"
	 * @return yse073
	 */
	public void setYse073(java.lang.String yse073) {
		this.yse073 = yse073;
	}

	/**
	 * 服务名称
	 * 
	 * @hibernate.property column="yse103"
	 * @return yse103
	 */
	public java.lang.String getYse103() {
		return this.yse103;
	}

	/**
	 * 服务名称
	 * 
	 * @hibernate.property column="yse103"
	 * @return yse103
	 */
	public void setYse103(java.lang.String yse103) {
		this.yse103 = yse103;
	}

	/**
	 * 所属模块
	 * 
	 * @hibernate.property column="yse119"
	 * @return yse119
	 */
	public java.lang.String getYse119() {
		return this.yse119;
	}

	/**
	 * 所属模块
	 * 
	 * @hibernate.property column="yse119"
	 * @return yse119
	 */
	public void setYse119(java.lang.String yse119) {
		this.yse119 = yse119;
	}

	/**
	 * 服务说明
	 * 
	 * @hibernate.property column="yse120"
	 * @return yse120
	 */
	public java.lang.String getYse120() {
		return this.yse120;
	}

	/**
	 * 服务说明
	 * 
	 * @hibernate.property column="yse120"
	 * @return yse120
	 */
	public void setYse120(java.lang.String yse120) {
		this.yse120 = yse120;
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

	/**
	 * 创建时间
	 * 
	 * @hibernate.property column="aae036"
	 * @return aae036
	 */
	public java.sql.Date getAae036() {
		return this.aae036;
	}

	/**
	 * 创建时间
	 * 
	 * @hibernate.property column="aae036"
	 * @return aae036
	 */
	public void setAae036(java.sql.Date aae036) {
		this.aae036 = aae036;
	}

	/**
	 * 服务类别
	 * 
	 * @hibernate.property column="ysf016"
	 * @return ysf016
	 */
	public java.lang.String getYsf016() {
		return this.ysf016;
	}

	/**
	 * 服务类别
	 * 
	 * @hibernate.property column="ysf016"
	 * @return ysf016
	 */
	public void setYsf016(java.lang.String ysf016) {
		this.ysf016 = ysf016;
	}

	/**
	 * 服务状态
	 * 
	 * @hibernate.property column="yse133"
	 * @return yse133
	 */
	public java.lang.String getYse133() {
		return this.yse133;
	}

	/**
	 * 服务状态
	 * 
	 * @hibernate.property column="yse133"
	 * @return yse133
	 */
	public void setYse133(java.lang.String yse133) {
		this.yse133 = yse133;
	}

	/**
	 * 数据权限控制标志
	 * 
	 * @hibernate.property column="yse153"
	 * @return yse153
	 */
	public java.lang.String getYse153() {
		return this.yse153;
	}

	/**
	 * 数据权限控制标志
	 * 
	 * @hibernate.property column="yse153"
	 * @return yse153
	 */
	public void setYse153(java.lang.String yse153) {
		this.yse153 = yse153;
	}

	/**
	 * 数据权限规则ID
	 * 
	 * @hibernate.property column="ysz064"
	 * @return ysz064
	 */
	public java.lang.Long getYsz064() {
		return this.ysz064;
	}

	/**
	 * 数据权限规则ID
	 * 
	 * @hibernate.property column="ysz064"
	 * @return ysz064
	 */
	public void setYsz064(java.lang.Long ysz064) {
		this.ysz064 = ysz064;
	}

	/**
	 * 数据权限险种ID
	 * 
	 * @hibernate.property column="ysz060"
	 * @return ysz060
	 */
	public java.lang.Long getYsz060() {
		return this.ysz060;
	}

	/**
	 * 数据权限险种ID
	 * 
	 * @hibernate.property column="ysz060"
	 * @return ysz060
	 */
	public void setYsz060(java.lang.Long ysz060) {
		this.ysz060 = ysz060;
	}

	/**
	 * 是否自动路由
	 * 
	 * @hibernate.property column="yse163"
	 * @return yse163
	 */
	public java.lang.String getYse163() {
		return this.yse163;
	}

	/**
	 * 是否自动路由
	 * 
	 * @hibernate.property column="yse163"
	 * @return yse163
	 */
	public void setYse163(java.lang.String yse163) {
		this.yse163 = yse163;
	}

	/**
	 * 是否记录日志
	 * 
	 * @hibernate.property column="yse164"
	 * @return yse164
	 */
	public java.lang.String getYse164() {
		return this.yse164;
	}

	/**
	 * 是否记录日志
	 * 
	 * @hibernate.property column="yse164"
	 * @return yse164
	 */
	public void setYse164(java.lang.String yse164) {
		this.yse164 = yse164;
	}

	public java.lang.String getYse151() {
		return yse151;
	}

	public void setYse151(java.lang.String yse151) {
		this.yse151 = yse151;
	}

}
