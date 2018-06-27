package com.ylzinfo.esb.util;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;



/**
 * 字符串处理帮助类
 * <p>
 * Description: 字符串处理帮助类
 * </p>
 * <p>
 * Copyright:Copyright(c)2007
 * </p>
 * <p>
 * Company:JEAF CN
 * </p>
 * 
 * @author <a href="mailto:jeafcn@gmail.com" >weich</a>
 * @version 1.0
 */
public class StringUtil {
	

	private static final char[] AMP_ENCODE;
	  private static final int DUMP_HEX_CHAR_COUNT = 75;
	  private static final char[] GT_ENCODE;
	  private static final char[] LT_ENCODE;
	  private static final char[] QUOTE_ENCODE = "&quot;".toCharArray();
	  private static final char[] base64Chars;
	  private static int[] base64Codes;
	  private static MessageDigest digest;
	  private static final int fillchar = 61;
	  private static int[] hexCharCodes;
	  private static final char[] lowerHexChar;
	  private static SimpleDateFormat m_ISO8601Local;
	  private static char[] numbersAndLetters;
	  private static Random randGen;
	  private static final char[] upcaseHexChar;
	  private static final char[] zeroArray;

	  static
	  {
	    AMP_ENCODE = "&amp;".toCharArray();
	    LT_ENCODE = "&lt;".toCharArray();
	    GT_ENCODE = "&gt;".toCharArray();

	    digest = null;

	    randGen = new Random();
	    numbersAndLetters = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
	    zeroArray = "0000000000000000".toCharArray();
	    base64Chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();
	    upcaseHexChar = "0123456789ABCDEF".toCharArray();
	    lowerHexChar = "0123456789abcdef".toCharArray();

	    hexCharCodes = new int[256];

	    base64Codes = new int[256];
	    m_ISO8601Local = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

	    for (int i = 0; i < 256; ++i)
	    {
	      byte tmp128_127 = -1; base64Codes[i] = tmp128_127; hexCharCodes[i] = tmp128_127;
	    }
	    for (int i = 0; i < base64Chars.length; ++i) {
	      base64Codes[base64Chars[i]] = (byte)i;
	    }
	    for (int i = 0; i < upcaseHexChar.length; ++i) {
	      hexCharCodes[upcaseHexChar[i]] = (byte)i;
	    }
	    for (int i = 0; i < lowerHexChar.length; ++i)
	      hexCharCodes[lowerHexChar[i]] = (byte)i;
	  }

	  public static int compareString(String s, String s2, String encoding)
	  {
	    if (s2 == null) {
	      if (s != null) {
	        return 1;
	      }

	      return 0;
	    }

	    if (s == null) {
	      return -1;
	    }
	    try
	    {
	      byte[] v1 = s.getBytes(encoding);
	      byte[] v2 = s2.getBytes(encoding);
	      int i = v1.length;
	      int j = v2.length;
	      int n = Math.min(i, j);
	      int k = 0;
	      int lim = n;
	      while (k < lim) {
	        int c1 = v1[k] & 0xFF;
	        int c2 = v2[k] & 0xFF;
	        if (c1 != c2) {
	          return c1 - c2;
	        }
	        ++k;
	      }

	      return i - j;
	    } catch (Exception e) {
	    }
	    return 0;
	  }

	  public static byte[] decodeBase64(String data, int offset)
	  {
	    int len = data.length();
	    byte[] result = new byte[len * 3 / 4];
	    int pos = 0;
	    if (offset >= len) {
	      return null;
	    }
	    for (int i = offset; i < len; ++i)
	    {
	      int c = base64Codes[data.charAt(i)];
	      if (c == -1) {
	        continue;
	      }
	      ++i;
	      int c1 = base64Codes[data.charAt(i)];
	      c = c << 2 | c1 >> 4 & 0x3;
	      result[(pos++)] = (byte)c;
	      if (++i < len) {
	        c = data.charAt(i);
	        if (61 == c) {
	          break;
	        }
	        c = base64Codes[data.charAt(i)];
	        c1 = c1 << 4 & 0xF0 | c >> 2 & 0xF;
	        result[(pos++)] = (byte)c1;
	      }
	      if (++i >= len) {
	        continue;
	      }
	      c1 = data.charAt(i);
	      if (61 == c1) {
	        break;
	      }
	      c1 = base64Codes[data.charAt(i)];
	      c = c << 6 & 0xC0 | c1;
	      result[(pos++)] = (byte)c;
	    }
	    if (result.length != pos) {
	      byte[] result2 = new byte[pos];
	      System.arraycopy(result, 0, result2, 0, pos);
	      result = result2;
	    }
	    return result;
	  }

	  public static byte[] decodeBase64(String data)
	  {
	    return decodeBase64(data, 0);
	  }

	  public static final byte[] decodeHex(String hex)
	  {
	    byte[] bytes = new byte[hex.length() / 2];
	    int byteCount = 0;
	    int length = hex.length();
	    for (int i = 0; i < length; i += 2) {
	      byte newByte = 0;
	      newByte = (byte)(newByte | hexCharCodes[hex.charAt(i)]);
	      newByte = (byte)(newByte << 4);
	      newByte = (byte)(newByte | hexCharCodes[hex.charAt(i + 1)]);
	      bytes[byteCount] = newByte;
	      ++byteCount;
	    }

	    return bytes;
	  }

	  public static String decodeUrlString(String str)
	  {
	    String strret = null;
	    if (str == null)
	      return str;
	    try
	    {
	      strret = URLDecoder.decode(str, "GBK");
	    }
	    catch (Exception e) {
	      e.printStackTrace(System.err);
	      return null;
	    }
	    return strret;
	  }
	  

	  public static String encode(String text) {
	        char c;
	        StringBuffer n = new StringBuffer();
	        for (int i = 0; i < text.length(); i++) {
	            c = text.charAt(i);
	            switch (c) {
	                case '&':
	                    {
	                        n.append("&amp;");
	                        break;
	                    }
	                case '<':
	                    {
	                        n.append("&lt;");
	                        break;
	                    }
	                case '>':
	                    {
	                        n.append("&gt;");
	                        break;
	                    }
	                case '"':
	                    {
	                        n.append("&quot;");
	                        break;
	                    }
	                case '\'':
	                    {
	                        n.append("&apos;");
	                        break;
	                    }
	                default :
	                    {
	                        n.append(c);
	                        break;
	                    }
	            }
	        }
	        return new String(n);
	    }

	  public static final String dumpHex(byte[] bytes)
	  {
	    int linecount = (bytes.length + 15) / 16;
	    char[] buf = new char[linecount * 75];
	    byte[] bs = new byte[16];

	    int bytepos = 0;
	    for (int i = 0; i < linecount; ++i) {
	      int addr = i * 16;
	      int bufpos = i * 75;
	      buf[(bufpos++)] = upcaseHexChar[(addr >> 12 & 0xF)];
	      buf[(bufpos++)] = upcaseHexChar[(addr >> 8 & 0xF)];
	      buf[(bufpos++)] = upcaseHexChar[(addr >> 4 & 0xF)];
	      buf[(bufpos++)] = upcaseHexChar[(addr & 0xF)];
	      buf[(bufpos++)] = ' ';
	      buf[(bufpos++)] = ' ';
	      for (int j = 0; j < 16; ++bytepos) {
	        if (bytepos < bytes.length) {
	          int code = bytes[bytepos] & 0xFF;
	          bs[j] = bytes[bytepos];
	          buf[(bufpos++)] = upcaseHexChar[(code >> 4)];
	          buf[(bufpos++)] = upcaseHexChar[(code & 0xF)];
	          if (j == 7) {
	            buf[(bufpos++)] = '-';
	          }
	          else
	            buf[(bufpos++)] = ' ';
	        }
	        else
	        {
	          buf[(bufpos++)] = ' ';
	          buf[(bufpos++)] = ' ';
	          buf[(bufpos++)] = ' ';
	          bs[j] = 32;
	        }
	        ++j;
	      }

	      buf[(bufpos++)] = ' ';
	      buf[(bufpos++)] = ' ';
	      char[] chs = new String(bs).toCharArray();
	      for (int k = 0; k < chs.length; ++k) {
	        char ch = chs[k];
	        if ((ch > 0) && (ch < ' ')) {
	          buf[(bufpos++)] = '.';
	        }
	        else {
	          buf[(bufpos++)] = ch;
	        }
	      }

	      int endLine = (i + 1) * 75 - 2;
	      for (; bufpos < endLine; ++bufpos) {
	        buf[bufpos] = ' ';
	      }
	      buf[(bufpos++)] = '\r';
	      buf[(bufpos++)] = '\n';
	    }
	    return new String(buf);
	  }

	  public static String encodeBase64(byte[] data)
	  {
	    return encodeBase64(data, false);
	  }

	  public static String encodeBase64(byte[] data, boolean lineBreak)
	  {
	    int len = data.length;
	    char[] buf = new char[(len / 3 + 1) * 4 + len / 57 + 1];
	    int pos = 0;
	    for (int i = 0; i < len; ++i) {
	      int c = data[i] >> 2 & 0x3F;
	      buf[(pos++)] = base64Chars[c];
	      c = data[i] << 4 & 0x3F;
	      if (++i < len) {
	        c |= data[i] >> 4 & 0xF;
	      }
	      buf[(pos++)] = base64Chars[c];
	      if (i < len) {
	        c = data[i] << 2 & 0x3F;
	        if (++i < len) {
	          c |= data[i] >> 6 & 0x3;
	        }
	        buf[(pos++)] = base64Chars[c];
	      }
	      else {
	        ++i;
	        buf[(pos++)] = '=';
	      }
	      if (i < len) {
	        c = data[i] & 0x3F;
	        buf[(pos++)] = base64Chars[c];
	      }
	      else {
	        buf[(pos++)] = '=';
	      }

	      if ((lineBreak) && (i % 57 == 56)) {
	        buf[(pos++)] = '\n';
	      }
	    }

	    return new String(buf, 0, pos);
	  }

	  public static String encodeBase64(String data)
	  {
	    return encodeBase64(data.getBytes());
	  }

	  public static final String encodeHex(byte[] bytes, boolean isUpper)
	  {
	    char[] hexChar;
	    if (isUpper) {
	      hexChar = upcaseHexChar;
	    }
	    else {
	      hexChar = lowerHexChar;
	    }

	    char[] buf = new char[bytes.length * 2];
	    for (int i = 0; i < bytes.length; ++i) {
	      int code = bytes[i] & 0xFF;
	      buf[(2 * i)] = hexChar[(code >> 4)];
	      buf[(2 * i + 1)] = hexChar[(code & 0xF)];
	    }
	    return new String(buf);
	  }

	  public static final String encodeHex(byte[] bytes)
	  {
	    return encodeHex(bytes, true);
	  }

	  public static String encodeUrlString(String str)
	  {
	    String strret = null;
	    if (str == null)
	      return str;
	    try
	    {
	      strret = URLEncoder.encode(str, "GBK");
	    }
	    catch (Exception e) {
	      e.printStackTrace(System.err);
	      return null;
	    }
	    return strret;
	  }

	  public static synchronized String formatChineseString(String text)
	  {
	    String ret = text;
	    if (ret == null) {
	      return ret;
	    }

	    ret = replace(ret, "０", "0");
	    ret = replace(ret, "１", "1");
	    ret = replace(ret, "２", "2");
	    ret = replace(ret, "３", "3");
	    ret = replace(ret, "４", "4");
	    ret = replace(ret, "５", "5");
	    ret = replace(ret, "６", "6");
	    ret = replace(ret, "７", "7");
	    ret = replace(ret, "８", "8");
	    ret = replace(ret, "９", "9");
	    ret = replace(ret, "＃", "#");
	    return ret;
	  }

	  public static String genEmptyString(int length)
	  {
	    char[] cs = new char[length];
	    for (int i = 0; i < length; ++i) {
	      cs[i] = ' ';
	    }
	    return new String(cs);
	  }

	  public static String getCurrTime()
	  {
	    Date now = new Date();
	    SimpleDateFormat outFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	    String s = outFormat.format(now);
	    return s;
	  }

	  public static String getCurrTimeISO8601(Date date)
	  {
	    if (date == null) {
	      date = new Date();
	    }

	    String dateStr = m_ISO8601Local.format(date);

	    return dateStr.substring(0, 22) + ":" + dateStr.substring(22);
	  }

	  public static int getStrIndex(String s, String[] args)
	  {
	    int length = args.length;
	    for (int i = 0; i < length; ++i) {
	      if (args[i].equals(s)) {
	        return i;
	      }
	    }
	    return -1;
	  }

	  public static String join(Iterator iterator, String separator)
	  {
	    StringBuffer buf = new StringBuffer();
	    while (iterator.hasNext()) {
	      buf.append(iterator.next());
	      if (iterator.hasNext()) {
	        buf.append(separator);
	      }
	    }

	    return buf.toString();
	  }

	  public static String join(Object[] list, String separator)
	  {
	    return join(Arrays.asList(list).iterator(), separator);
	  }

	  public static boolean nullOrBlank(String param)
	  {
	    return (param == null) || (param.trim().equals(""));
	  }

	  public static boolean parseBoolean(String param, boolean value)
	  {
	    if (nullOrBlank(param)) {
	      return value;
	    }
	    switch (param.charAt(0))
	    {
	    case '1':
	    case 'T':
	    case 'Y':
	    case 't':
	    case 'y':
	      return true;
	    case '0':
	    case 'F':
	    case 'N':
	    case 'f':
	    case 'n':
	      return false;
	    }
	    return value;
	  }

	  public static boolean parseBoolean(String param)
	  {
	    return parseBoolean(param, false);
	  }

	  public static double parseDouble(String param, double defValue)
	  {
	    double d = defValue;
	    try {
	      d = Double.parseDouble(param);
	    }
	    catch (Exception e) {
	    }
	    return d;
	  }

	  public static float parseFloat(String param, float defValue)
	  {
	    float f = defValue;
	    try {
	      f = Float.parseFloat(param);
	    }
	    catch (Exception e) {
	    }
	    return f;
	  }

	  public static int parseInt(String param, int defValue)
	  {
	    int i = defValue;
	    try {
	      i = Integer.parseInt(param);
	    }
	    catch (Exception e) {
	    }
	    return i;
	  }

	  public static long parseLong(String param, long defValue)
	  {
	    long l = defValue;
	    try {
	      l = Long.parseLong(param);
	    }
	    catch (Exception e) {
	    }
	    return l;
	  }

	  public static final String randomString(int length)
	  {
	    if (length < 1) {
	      return null;
	    }
	    char[] randBuffer = new char[length];
	    for (int i = 0; i < randBuffer.length; ++i) {
	      randBuffer[i] = numbersAndLetters[randGen.nextInt(numbersAndLetters.length)];
	    }

	    return new String(randBuffer);
	  }

	  public static final String replace(String line, String oldString, String newString, boolean ignoreCase)
	  {
	    if ((line == null) || (oldString == null) || (newString == null)) {
	      return null;
	    }
	    String lcLine = line;
	    String lcOldString = oldString;
	    if (ignoreCase) {
	      lcLine = line.toLowerCase();
	      lcOldString = oldString.toLowerCase();
	    }

	    int i = 0;
	    if ((i = lcLine.indexOf(lcOldString, i)) >= 0) {
	      char[] line2 = line.toCharArray();
	      char[] newString2 = newString.toCharArray();
	      int oLength = oldString.length();
	      StringBuffer buf = new StringBuffer(line2.length);
	      buf.append(line2, 0, i).append(newString2);
	      i += oLength;
	      int j = i;
	      for (; (i = lcLine.indexOf(lcOldString, i)) > 0; j = i) {
	        buf.append(line2, j, i - j).append(newString2);
	        i += oLength;
	      }

	      buf.append(line2, j, line2.length - j);
	      return buf.toString();
	    }

	    return line;
	  }

	  public static final String replace(String line, String oldString, String newString)
	  {
	    return replace(line, oldString, newString, false);
	  }

	  public static final String replaceIgnoreCase(String line, String oldString, String newString)
	  {
	    return replace(line, oldString, newString, true);
	  }

	  static boolean strEquals(String s1, String s2)
	  {
	    if ((s1 != null) && (s2 != null)) {
	      return s1.equals(s2);
	    }
	    return s1 == s2;
	  }

	  public static String toChinese(String strvalue)
	  {
	    return toEncoding(strvalue, "ISO-8859-1", "GBK");
	  }

	  public static String toEncoding(String strvalue, String fromEncoding, String toEncoding)
	  {
	    try
	    {
	      if (strvalue == null) {
	        return null;
	      }

	      strvalue = new String(strvalue.getBytes(fromEncoding), toEncoding);
	      return strvalue;
	    }
	    catch (Exception e) {
	    }
	    return null;
	  }

	  public static String toLatin(String strvalue)
	  {
	    return toEncoding(strvalue, "GBK", "ISO-8859-1");
	  }

	  public static boolean toBoolean(String strvalue){
		  if("1".equals(strvalue))
			  return true;
		  else 
			  return false;
	  }
	  
	  private static String toUnicodeEscapeString(String str)
	  {
	    StringBuffer buf = new StringBuffer();
	    int len = str.length();

	    for (int i = 0; i < len; ++i) {
	      char ch = str.charAt(i);
	      switch (ch)
	      {
	      case '\\':
	        buf.append("\\\\"); break;
	      case '\t':
	        buf.append("\\t"); break;
	      case '\n':
	        buf.append("\\n"); break;
	      case '\r':
	        buf.append("\\r"); break;
	      default:
	        if ((ch >= ' ') && (ch <= '')) {
	          buf.append(ch);
	        }
	        else {
	          buf.append('\\');
	          buf.append('u');
	          buf.append(upcaseHexChar[(ch >> '\f' & 0xF)]);
	          buf.append(upcaseHexChar[(ch >> '\b' & 0xF)]);
	          buf.append(upcaseHexChar[(ch >> '\004' & 0xF)]);
	          buf.append(upcaseHexChar[(ch >> '\000' & 0xF)]);
	        }
	      }
	    }
	    return buf.toString();
	  }

	
	/**
	 * 字符串替换
	 * @param source 要替换的源
	 * @param from 要替换的串
	 * @param to 用来替换的串
	 * @return
	 */
	public static String StringReplace(
		String source,
		String from,
		String to) {
		int i = source.indexOf(from);
		while (i > 0) {
			source =source.substring(0, i).concat(to).concat(
					source.substring(i + from.length()));
			i = source.indexOf(from);
		}
		return (source);

	}
	
	/**
	 * 处理特殊字符
	 * @param val
	 * @return
	 */
	public static String deal(String val) {
		val = StringReplace(val, "\"", "’");
		val = StringReplace(val, ">", "’");
		val = StringReplace(val, "<", "’");
		return val;
	}
	/**
	 * 查询格式转换，“null”转换成" "
	 * @param obj
	 * @return
	 */
	public static String transform(String obj){
		if(obj==null||obj.equals("null")){
			obj=" ";
		}
		return obj;	
	}
	/**
	 * 查询服务参数添加'符号，'转换成\'
	 * @param parm
	 * @return
	 */
	public static String transFormOut(String parm){
		if(parm.contains("'")){
			parm = parm.replaceAll("'", "\\\\'");
		}
		return parm;
	}
	
}
