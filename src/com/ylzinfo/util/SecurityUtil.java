package com.ylzinfo.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class SecurityUtil {
	private static Logger logger = Logger.getLogger(SecurityUtil.class);

	/**
	 * 加密
	 * 
	 * @param encoder
	 * @return
	 */
	public static String encoder(String encoder, String key) {
		byte[] bytes;
		String base64 = "";
		try {
			bytes = encoder.getBytes("utf-8");
			logger.info("bytes============"+bytes);
			base64 = new BASE64Encoder().encode(bytes);
			logger.info("base64========="+base64);
			base64 = "5pyN5Yqh6LCD55So5aSx6LSl77yM6K+36IGU57O7566h55CG5ZGY77yB";
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return AES.encrypttoStr(base64, key);
	}

	/**
	 * 解密
	 * 
	 * @param param
	 * @return
	 */
	public static String decoder(String param, String key) {
		if (null == param || "".equals(param)) {
			return "";
		}
		String base64 = AES.decrypttoStr(param, key);
		String ret = "";
		try {
			ret = new String(new BASE64Decoder().decodeBuffer(base64), "utf-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public static void main(String[] args) {
		// System.out.println(encoder("142201198807255984", "sxps#$inparm"));
		// System.out.println(encoder("G072535091", "sxps#$inparm"));
		// System.out.println(encoder("宿玉英", "sxps#$inparm"));

//		System.out.println(encoder("142422199110273610", "sxps#$inparm"));
//		System.out.println(encoder("J12426410", "sxps#$inparm"));
//		System.out.println(encoder("赵艺泽", "sxps#$inparm"));
//
//		System.out.println(decoder("4A8DD95BAF188CFD2A48D722DE235501", "sxps#$outparm"));
//		System.out.println(decoder("9F74FBE1CF8AEC1AFB63D5FEA881542139BBB77601E3B48D14BCDB7EC2F8860A", "sxps#$outparm"));
//		System.out.println(decoder("A8C9BDE500E82B0F5FCC5658E48D80C57027BB2ADC44FF95A0043E94827162ED", "sxps#$outparm"));
//		System.out
//				.println(decoder(
//						"FB21640169430BD65B88994A6BEFBDD378DF70E9EDA6AD677C7A12FA6F35D7789B216ED24E147576CA8537FE58C8C3DFBE58710A6B62F94CC23D2B5BAF6644AAC79BF1EAFF116328891258B4FE4E3EED",
//						"sxps#$outparm"));
		System.out
				.println(decoder(
						"74762D1202276C929BBF5F507F0F9302D25770A5BFD0CB415EDDB0C685A4B86A3600B77F5A4133248732A8176E9DEC8E93D48851A8C1FFF5229352A9EF0375DD",
						"sxps#$outparm"));
	}
}
