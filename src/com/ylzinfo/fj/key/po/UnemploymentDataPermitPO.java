package com.ylzinfo.fj.key.po;

import com.ylzinfo.cbp.IPersistence;
import com.ylzinfo.cbp.Platform;
import com.ylzinfo.fj.key.dto.DataPermitBaseDTO;
import com.ylzinfo.fj.key.dto.UnemploymentDataPermitDTO;

/**
 * @description 失业保险数据权限控制
 * @author linxiaoling
 * @date 2014-7-28 上午09:21:42
 */
public class UnemploymentDataPermitPO extends DataPermitBasePO {
	/**
	 * 渠道地区服务权限校验
	 */
	@Override
	public String checkDataPermit(DataPermitBaseDTO dto) {
		IPersistence ip = Platform.getPersistence();
		int count =  ip.getSQLCount("select 1 from se45 a, se32 b, se43 c where a.ysz050 = b.ysz050" + " and a.ysz059 = c.ysz059 and b.yse100 = '" + dto.getYse100() + "' and c.yse147 in (select yae512 from ywsj.jy51 where aac002='" + ((UnemploymentDataPermitDTO) dto).getAac002() + "')");
		if (count == 0) {
			return "渠道" + dto.getYse100() + "没有数据权限，身份证号码：" + ((UnemploymentDataPermitDTO) dto).getAac002();
		}
		return "OK";
	}
}
