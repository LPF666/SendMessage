/**
 * 
 */
package com.ylzinfo.fj.key.po;

import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Transaction;

import com.ylz.ggyw.cache.CacheFactory;
import com.ylzinfo.cbp.IPersistence;
import com.ylzinfo.cbp.Platform;
import com.ylzinfo.common.GlobalConst;
import com.ylzinfo.fj.key.dto.DataPermitBaseDTO;
import com.ylzinfo.fj.key.dto.KeyDTO;
import com.ylzinfo.fj.key.entity.Sa02;
import com.ylzinfo.fj.key.entity.Ss01;
import com.ylzinfo.fj.key.entity.Ss02;
import com.ylzinfo.fj.key.entity.Ss05;
import com.ylzinfo.fj.utils.LoginKeyUtil;

/**
 * 用户访问服务的校验密钥
 * 
 * @author zero
 * 
 */
public class PublicKeyPO {
	Logger logger = Logger.getLogger(PublicKeyPO.class);
	IPersistence ip = Platform.getPersistence();

	public String checkKey(KeyDTO dto) {
		logger.info("PublicKeyPO:>>usr:" + dto.getUsr());
		if (null == dto.getSid() || "".equals(dto.getSid())) {
			return "#3,传入SID为空";
		}
		if (null == dto.getUsr() || "".equals(dto.getUsr())) {
			return "#3,传入ESB用户名为空";
		}
		if (null == dto.getKey() || "".equals(dto.getKey())) {
			return "#3,传入校验密钥为空";
		}
		if (null == dto.getTermid() || "".equals(dto.getTermid())) {
			return "#3,传入终端编号为空";
		}
		if (null == dto.getAac002() || "".equals(dto.getAac002())) {
			dto.setAac002("public");// 身份证号码为空，则为公共查询
		}
		Transaction tm = ip.getCurSession().beginTransaction();
		// 路由编号
		String yae512 = "";
		try {
			tm.begin();
			String key = dto.getKey();

			// 判断渠道是否校验固定key
//			Object object = CacheFactory.getCacheClient("forevercache").get("ss04_" + dto.getUsr());
//			if (object != null) {
//				String result = LoginKeyUtil.checkKey(dto.getUsr(), "", key);
//				if (!"OK".equals(result)) {// 校验不成功
//					return result;
//				}
//			}

			// 判断是否是不校验的服务
			String ifcheck = (String) CacheFactory.getCacheClient("forevercache").get("ss02_" + dto.getSid());

			if ("0".equals(ifcheck) || StringUtils.isEmpty(ifcheck)) { // 要校验
				String result = LoginKeyUtil.checkKey(dto.getUsr(), dto.getAac002(), key);
				if (!"OK".equals(result)) {// 校验不成功
					return result;
				}
			}
//			else if ("1".equals(ifcheck)) {// 校验公共查询的密钥或用户的校验密钥
//				String result = LoginKeyUtil.checkKey(dto.getUsr(),"public_"+dto.getAac002(), key);
//				if (!"OK".equals(result)) {// 校验不成功
//					return result;
//				}
//			}
			else if ("2".equals(ifcheck)) {// 校验公共查询的密钥,并生成用户的校验密钥
//				String result = LoginKeyUtil.checkKey(dto.getUsr(), "public",key);
//				if (!"OK".equals(result)) {// 校验不成功
//					return result;
//				}
				LoginKeyUtil.getkey(dto.getUsr(), dto.getAac002());
			}

			Map se35Map = (Map) CacheFactory.getCacheClient("forevercache").get("se35_" + dto.getSid());
			// 校验服务是否授权给渠道
			String sql = "select 1 from se33 a,se32 b where a.ysz050=b.ysz050 and b.yse100='" + dto.getUsr() + "' and a.yse073 ='" + dto.getSid() + "'";
			int count_0 = ip.getSQLCount(sql);
			if (count_0 == 0) {
				if (se35Map == null) {
					return "服务：" + dto.getSid() + "，不存在";
				}
				return "渠道" + dto.getUsr() + "没有权限访问服务" + dto.getSid();
			}

			// 校验终端是否有限制服务访问
			String sql_1 = "select 1 from se34  where yse074='" + dto.getTermid() + "' and yse073 ='" + dto.getSid() + "'";
			count_0 = ip.getSQLCount(sql_1);
			if (count_0 > 0) {
				return "终端" + dto.getTermid() + "没有权限访问服务" + dto.getSid();
			}

//			// 渠道险种服务权限校验
//			count_0 = ip.getSQLCount("select 1 from se46 a,se32 b where a.ysz050 = b.ysz050 and b.yse100='" + dto.getUsr() + "'" + "and a.ysz060=" + Long.valueOf((String) se35Map.get("ysz060")));
//			if (count_0 == 0) {
//				return "渠道" + dto.getUsr() + "没有数据权限，服务编号：" + dto.getSid();
//			}
//			// 渠道地区服务权限校验
//			String checkResult = dataPermit(ip, dto);
//			if (!checkResult.equals("OK")) {
//				return checkResult;
//			}

			// 路由编码为空、服务为自动路由
			if (StringUtils.isEmpty(dto.getRouter()) && (se35Map.get("yse163") == null || ((String) se35Map.get("yse163")).equals(GlobalConst.YSE163_S)) && se35Map.get("yse151") != null) {
				List<Ss05> ss05list = ip.executeSQLQuery("select yae512 from ss05 where aac002='" + dto.getAac002() + "' and aae140='" + (String) se35Map.get("yse151") + "' order by aae030 desc",
						Ss05.class);
				if (ss05list != null && ss05list.size() > 0) {
					yae512 = ss05list.get(0).getYae512();
				}else{
					yae512 = "350000";
				}
			}
			
			// 判断服务是否记录日志
			if (GlobalConst.YSE164_S.equals(se35Map.get("yse164"))) {
				Ss01 ss01 = new Ss01();
				ss01.setYsz037(ip.getSequenceL("SQ_YSZ037"));
				ss01.setAac002(dto.getAac002());
				ss01.setYse069(dto.getUsr());
				ss01.setYse073(dto.getSid());
				ss01.setYse074(dto.getTermid());
				ss01.setYse072(dto.getKey());
				ss01.setYse075(ip.getDBTimestamp());
				ip.save(ss01);
			}
			tm.commit();
		} catch (Exception e) {
			tm.rollback();
			logger.info(e.getMessage());
			return "#9," + e.getMessage();
		}
		return "OK," + yae512;
	}

	/**
	 * 判断是否是不校验key的服务
	 * 
	 * @param sid
	 *            服务ID
	 * @return 0:要校验 其他：不要校验
	 */
	public String ifNotCheckService(String sid) {
		List<Ss02> list = ip.retrieveObjs("from Ss02 where yse073=?", new Object[] { sid });
		if (null == list || list.size() == 0) {
			return "0";// 要校验
		} else {
			Ss02 ss02 = list.get(0);
			return ss02.getYse079();// 返回不校验类型
		}
	}

	/**
	 * 数据权限校验
	 * 
	 * @param ip
	 * @param dto
	 * @return
	 */
	public String dataPermit(IPersistence ip, KeyDTO dto) {
		int count = ip.getSQLCount("select 1 from se35 where yse073='" + dto.getSid() + "' and yse153='" + GlobalConst.YSE153_S + "'");
		// 服务的数据权限校验标志为1
		if (count > 0) {
			List<Sa02> sa02List = ip.executeSQLQuery("select a.* from sa02 a,se35 b where a.ysz064=b.ysz064 and b.yse073='" + dto.getSid() + "'", Sa02.class);
			if (sa02List != null && sa02List.size() > 0) {
				Sa02 sa02 = sa02List.get(0);
				Class DataPermitBasePO_clazz;
				Class DataPermitBaseDTO_clazz;
				try {
					DataPermitBasePO_clazz = Class.forName(sa02.getYsa005());
					// 实例化DataPermitBasePO
					DataPermitBasePO po = (DataPermitBasePO) DataPermitBasePO_clazz.newInstance();
					DataPermitBaseDTO_clazz = Class.forName(sa02.getYsa006());
					// 实例化DataPermitBaseDTO
					DataPermitBaseDTO dataPermitDto = (DataPermitBaseDTO) DataPermitBaseDTO_clazz.newInstance();
					// 给dto属性设值
					String[] attrs = sa02.getYsa007().split(",");
					for (int i = 0; i < attrs.length; i++) {
						if (attrs[i].equals("aac002")) {
							PropertyUtils.setProperty(dataPermitDto, "aac002", dto.getAac002());
						}
						if (attrs[i].equals("yae512")) {
							PropertyUtils.setProperty(dataPermitDto, "yae512", dto.getYae512());
						}
						if (attrs[i].equals("yae511")) {
							PropertyUtils.setProperty(dataPermitDto, "yae511", dto.getYae511());
						}
					}
					dataPermitDto.setYse073(dto.getSid());
					dataPermitDto.setYse100(dto.getUsr());
					// 数据权限校验
					return po.checkDataPermit(dataPermitDto);
				} catch (Exception e) {
					logger.error(e);
				}
			} else {
				// 服务的数据权限校验标志为0
				return "OK";
			}
		}
		return "OK";
	}

}
