package com.ylzinfo.listener;

import java.util.Date;

/**
 * @description 渠道固定key
 * @author linxiaoling
 * @date 2014-8-6 上午11:31:19
 */
public class EsbUserKeyDTO {
	private Long ysz056;// 渠道固定key的id
	private String yse100;// 渠道编号
	private String yse072;// 校验的交易密钥
	private Date yse076;// 申请日期
	private Date yse077;// 截止有效日期

	public Long getYsz056() {
		return ysz056;
	}

	public void setYsz056(Long ysz056) {
		this.ysz056 = ysz056;
	}

	public String getYse100() {
		return yse100;
	}

	public void setYse100(String yse100) {
		this.yse100 = yse100;
	}

	public String getYse072() {
		return yse072;
	}

	public void setYse072(String yse072) {
		this.yse072 = yse072;
	}

	public Date getYse076() {
		return yse076;
	}

	public void setYse076(Date yse076) {
		this.yse076 = yse076;
	}

	public Date getYse077() {
		return yse077;
	}

	public void setYse077(Date yse077) {
		this.yse077 = yse077;
	}

}
