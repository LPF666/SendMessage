package com.ylzinfo.fj.key.po;

import com.ylzinfo.fj.key.dto.DataPermitBaseDTO;

/**    
 * @description 数据权限控制基类
 * @author linxiaoling
 * @date 2014-7-23 下午02:00:21    
 */
public abstract class DataPermitBasePO {
	
	/**
	 * 校验数据权限
	 */
	public abstract String checkDataPermit(DataPermitBaseDTO dto);
}
