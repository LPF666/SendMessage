package com.ylzinfo.fj.cache.dto;

import com.ylzinfo.fj.utils.BaseDTO;
import com.ylzinfo.fj.utils.OutPara;

/**
 * @description
 * @author linxiaoling
 * @date 2014-8-12 上午11:37:25
 */
public class CacheReturnDTO extends BaseDTO {
	@OutPara(index = 0, label = "校验成功标志")
	String aae314;

	@OutPara(index = 1, label = "校验成功说明")
	String aae317;

	public String getAae314() {
		return aae314;
	}

	public void setAae314(String aae314) {
		this.aae314 = aae314;
	}

	public String getAae317() {
		return aae317;
	}

	public void setAae317(String aae317) {
		this.aae317 = aae317;
	}

}
