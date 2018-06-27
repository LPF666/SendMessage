package com.ylzinfo.psBusiness.pdf.ttf;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;

public class FontType {
	private static Logger log = Logger.getLogger(FontType.class);

	/**
	 * 获取宋体
	 * @param size 字号
	 * @return
	 * @throws DocumentException
	 * @throws IOException
	 */
	public static Font getST(float size) {
		Font fontZH = null;
		BaseFont bf;
		try {
			bf = BaseFont.createFont(FontType.class.getResource("STXIHEI.TTF").toString(), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			fontZH = new Font(bf, size, Font.NORMAL);
			return fontZH;
		} catch (DocumentException e) {
			log.info(e);
			return null;
		} catch (IOException e) {
			log.info(e);
			return null;
		}
	}
	
	/**
	 * 获取宋体 加粗
	 * @param size 字号
	 * @return
	 * @throws DocumentException
	 * @throws IOException
	 */
	public static Font getSTB(float size) {
		Font fontZH = null;
		BaseFont bf;
		try {
			bf = BaseFont.createFont(FontType.class.getResource("STXIHEI.TTF").toString(), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			fontZH = new Font(bf, size, Font.BOLD);
			return fontZH;
		} catch (DocumentException e) {
			log.info(e);
			return null;
		} catch (IOException e) {
			log.info(e);
			return null;
		}
	}
	
	/**
	 * 获取黑体
	 * @param size 字号
	 * @return
	 * @throws DocumentException
	 * @throws IOException
	 */
	public static Font getHT(float size) {
		Font fontZH = null;
		BaseFont bf;
		try {
			bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.EMBEDDED);
			fontZH = new Font(bf, size, Font.NORMAL);
			return fontZH;
		} catch (DocumentException e) {
			log.info(e);
			return null;
		} catch (IOException e) {
			log.info(e);
			return null;
		}
	}
	
	/**
	 * 获取黑体 加粗
	 * @param size 字号
	 * @return
	 * @throws DocumentException
	 * @throws IOException
	 */
	public static Font getHTB(float size) {
		Font fontZH = null;
		BaseFont bf;
		try {
			bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.EMBEDDED);
			fontZH = new Font(bf, size, Font.BOLD);
			return fontZH;
		} catch (DocumentException e) {
			log.info(e);
			return null;
		} catch (IOException e) {
			log.info(e);
			return null;
		}
	}
	
	public static BaseFont getBaseFont() {
		BaseFont bf;
		try {
			bf = BaseFont.createFont(FontType.class.getResource("STXIHEI.TTF").toString(), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
			return bf;
		} catch (DocumentException e) {
			log.info(e);
			return null;
		} catch (IOException e) {
			log.info(e);
			return null;
		}
	}
}
