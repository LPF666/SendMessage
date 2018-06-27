package com.ylzinfo.common;

import java.util.Map;
import java.util.TreeMap;

/**
 * 通用业务常量
 * 
 * @author hzf
 * 
 */
public class GlobalConst {

	/**
	 * 未提交
	 */
	public static final String LCSHZT_DBC = "990";// 数据待补充

	public static final String ZT_ZC = "Z"; // 状态正常

	public static final String ZT_SC = "D"; // 状态删除

	public static final String ZT_CG = "C"; // 状态草稿

	// 类型
	public static final String type_xw = "1"; // 新闻

	public static final String type_gg = "2"; // 公告

	public static final String type_zc = "3"; // 正常法规

	public static final String type_fj = "4"; // 附件下载
	/**
	 * 热点问题标志：是
	 */
	public static final String YSE048_S = "1";
	/**
	 * 热点问题标志：否
	 */
	public static final String YSE048_F = "0";
	/**
	 * 问题状态：待解决
	 */
	public static final String YSE051_DJJ = "002";
	/**
	 * 问题状态：已解决
	 */
	public static final String YSE051_YJJ = "001";
	/**
	 * 问题类型：一般问题
	 */
	public static final String YSE040_YBWT = "001";
	/**
	 * 问题类型：追问问题
	 */
	public static final String YSE040_ZWWT = "002";
	/**
	 * 公开级别：公开
	 */
	public static final String YSE049_GK = "001";
	/**
	 * 公开级别：仅个人可见
	 */
	public static final String YSE049_JGRKJ = "002";
	/**
	 * 公开级别：不可见
	 */
	public static final String YSE049_BKJ = "003";
	/**
	 * 业务类型编码：网上留言
	 */
	public static final String AAA121_WSLY = "S10021";
	/**
	 * 业务类型编码：追问
	 */
	public static final String AAA121_ZW = "S10022";
	/**
	 * 业务类型编码：修改问题
	 */
	public static final String AAA121_XGWT = "S10025";
	/**
	 * 业务类型编码：个人信息维护
	 */
	public static final String AAA121_GRXXWH = "S10005";
	/**
	 * 业务类型编码：个人用户交易密码设置
	 */
	public static final String AAA121_GRJYMMSZ = "S10008";
	/**
	 * 业务类型编码：短信推送单条
	 */
	public static final String AAA121_DXTSDANT = "S10027";
	/**
	 * 业务类型编码：短信推送多条
	 */
	public static final String AAA121_DXTSDUOT = "S10028";
	/**
	 * 维护业务类型：申请
	 */
	public static final String AAE395_SQ = "001";
	/**
	 * 维护业务类型：修改
	 */
	public static final String AAE395_XG = "002";
	/**
	 * 维护业务类型：注销
	 */
	public static final String AAE395_ZX = "003";
	/**
	 * 维护业务类型：恢复
	 */
	public static final String AAE395_HF = "004";

	/**
	 * 复核标志：未复核
	 */
	public static final String AAE016_WFH = "0";
	/**
	 * 复核标志：复核通过
	 */
	public static final String AAE016_FHTG = "1";
	/**
	 * 复核标志：复核不通过
	 */
	public static final String AAE016_FHBTG = "2";
	/**
	 * 问题来源：网上大厅
	 */
	public static final String YSE043_WSDT = "001";
	/**
	 * 问题来源：手机端
	 */
	public static final String YSE043_SJD = "002";
	/**
	 * 消息渠道类别：短信
	 */
	public static final String YSE106_DX = "01";
	/**
	 * 是否发送成功：是
	 */
	public static final String YSF012_S = "1";
	/**
	 * 是否发送成功：否
	 */
	public static final String YSF012_F = "0";

	/**
	 * 是否发送成功：是
	 */
	public static final String YSE136_S = "1";
	/**
	 * 是否发送成功：否
	 */
	public static final String YSE136_F = "0";
	/**
	 * 是否发送成功：等待发送
	 */
	public static final String YSE136_DDFS = "2";
	/**
	 * 是否发送成功：草稿
	 */
	public static final String YSE136_CG = "3";

	/**
	 * 消息格式：短信验证码
	 */
	public static final String YSE081_DXYZM = "DXYZM";
	/**
	 * 用户状态：正常
	 */
	public static final String YSE001_ZC = "001";
	/**
	 * 菜单定制有效标识：是
	 */
	public static final String YSE155_S = "1";
	/**
	 * 菜单定制有效标识：否
	 */
	public static final String YSE155_F = "0";
	/**
	 * 菜单是否可用：是
	 */
	public static final String YSE130_S = "1";
	/**
	 * 菜单是否可用：否
	 */
	public static final String YSE130_F = "0";
	/**
	 * 数据权限控制标志：是
	 */
	public static final String YSE153_S = "1";
	/**
	 * 数据权限控制标志：否
	 */
	public static final String YSE153_F = "0";
	/**
	 * 是否自动路由：是
	 */
	public static final String YSE163_S = "1";
	/**
	 * 是否自动路由：否
	 */
	public static final String YSE163_F = "0";
	/**
	 * 是否记录日志：是
	 */
	public static final String YSE164_S = "1";
	/**
	 * 是否记录日志：否
	 */
	public static final String YSE164_F = "0";
	/**
	 * 服务类别：服务
	 */
	public static final String YSF016_FW = "1";
	/**
	 * 短信验证：业务代码 001用户注册 002修改登录密码 003修改通讯信息 004修改手机号码 005设置交易密码 006修改交易密码
	 * 010社保卡挂失
	 */
	public static final String AAA121_YHZC = "001";
	public static final String AAA121_ZH_YHZC = "新用户注册";
	public static final String AAA121_XGDLMM = "002";
	public static final String AAA121_ZH_XGDLMM = "修改登录密码";
	public static final String AAA121_XGTXXX = "003";
	public static final String AAA121_ZH_XGTXXX = "修改通讯信息";
	public static final String AAA121_XGSJHM = "004";
	public static final String AAA121_ZH_XGSJHM = "修改手机号码";
	public static final String AAA121_SZJJMM = "005";
	public static final String AAA121_ZH_SZJJMM = "设置交易密码";
	public static final String AAA121_XGJJMM = "006";
	public static final String AAA121_ZH_XGJJMM = "修改交易密码";
	public static final String AAA121_ZHMM = "007";
	public static final String AAA121_ZH_ZHMM = "找回密码";
	public static final String AAA121_SBKGS = "010";
	public static final String AAA121_ZH_SBKGS = "社保卡挂失";

	public static String getAaa121(String code) {
		if (AAA121_YHZC.equals(code)) {
			return AAA121_ZH_YHZC;
		} else if (AAA121_XGDLMM.equals(code)) {
			return AAA121_ZH_XGDLMM;
		} else if (AAA121_XGTXXX.equals(code)) {
			return AAA121_ZH_XGTXXX;
		} else if (AAA121_XGSJHM.equals(code)) {
			return AAA121_ZH_XGSJHM;
		} else if (AAA121_SZJJMM.equals(code)) {
			return AAA121_ZH_SZJJMM;
		} else if (AAA121_ZHMM.equals(code)) {
			return AAA121_ZH_ZHMM;
		} else if (AAA121_XGJJMM.equals(code)) {
			return AAA121_ZH_XGJJMM;
		} else {
			return AAA121_ZH_SBKGS;
		}
	}

	public static final String SMS_TG = "1";
	public static final String SMS_ZH_TG = "校验通过";
	public static final String SMS_CW = "2";
	public static final String SMS_ZH_CW = "短信验证码错误";
	public static final String SMS_GQ = "3";
	public static final String SMS_ZH_GQ = "短信验证码过期";
	public static final String SMS_QTCW = "9";
	public static final String SMS_ZH_QTCW = "其他错误";

	public static String PDF_TMP_PATH = "";
	public static Map<String, String> globalvalue = new TreeMap<String, String>();
}