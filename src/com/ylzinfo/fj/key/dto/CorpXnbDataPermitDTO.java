package com.ylzinfo.fj.key.dto;


/**
 * @description 城乡居民养老保险数据权限DTO
 * @author linxiaoling
 * @date 2014-7-23 上午11:46:53
 */
public class CorpXnbDataPermitDTO extends DataPermitBaseDTO {
	private String aac002;// 身份证号码
	private String yae512;// 所属地级市行政区划代码
	private String yae511;// 所属区县行政区划代码

	public String getAac002() {
		return aac002;
	}

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

}
