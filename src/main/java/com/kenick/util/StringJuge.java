package com.kenick.util;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

public class StringJuge 
{
	/**
	  * 判断是否为汉字
	  * 
	  * @param str
	  * @return
	  */
	 public static boolean isGBK(String str) {
	  char[] chars = str.toCharArray();
	  boolean isGBK = false;
	  for (int i = 0; i < chars.length; i++) {
	   byte[] bytes = ("" + chars[i]).getBytes();
	   if (bytes.length == 2) {
	    int[] ints = new int[2];
	    ints[0] = bytes[0] & 0xff;
	    ints[1] = bytes[1] & 0xff;
	    if (ints[0] >= 0x81 && ints[0] <= 0xFE && ints[1] >= 0x40
	      && ints[1] <= 0xFE) {
	     isGBK = true;
	     break;
	    }
	   }
	  }
	  return isGBK;
	 }

	/**
	  * 判断是否为乱码
	  * 
	  * @param str
	  * @return
	  */
	 public static boolean isMessyCode(String str) {
	  for (int i = 0; i < str.length(); i++) {
	   char c = str.charAt(i);
	   // 当从Unicode编码向某个字符集转换时，如果在该字符集中没有对应的编码，则得到0x3f（即问号字符?）
	   //从其他字符集向Unicode编码转换时，如果这个二进制数在该字符集中没有标识任何的字符，则得到的结果是0xfffd
	   //System.out.println("--- " + (int) c);
	   if ((int) c == 0xfffd) {
	    // 存在乱码
	    //System.out.println("存在乱码 " + (int) c);
	    return true;
	   }
	  }
	  return false; 
	 }

	 public static boolean isPhone(String str)
	 {
		 if (StringUtils.isEmpty(str)) {
			   return false;
			  }
		 Pattern p = Pattern.compile("^1[3-9]\\d{9}$");
		 
		 return p.matcher(str).matches();
	 }
	 public static boolean isNotPhone(String str)
	 {
		 return !isPhone(str);
	 }
	 /**
	  * 判断字符串是否为双整型数字
	  * 
	  * @param str
	  * @return
	  */
	 public static boolean isDouble(String str) {
	  if (StringUtils.isEmpty(str)) {
	   return false;
	  }
	  Pattern p = Pattern.compile("-*\\d*.\\d*");
	  // Pattern p = Pattern.compile("-*"+"\\d*"+"."+"\\d*");
	  return p.matcher(str).matches();
	 }

	 /**
	  * 判断字符串是否为整字
	  * 
	  * @param str
	  * @return
	  */
	 public static boolean isNumber(String str) {
	  if (StringUtils.isEmpty(str)) {
	   return false;
	  }
	  Pattern p = Pattern.compile("-*\\d*");
	  return p.matcher(str).matches();
	 }
	 
	 /**
	  * 判断是否为数字
	  * 
	  * @param str
	  * @return
	  */
	 public static boolean isNumeric(String str)
	 {
		 if (StringUtils.isEmpty(str))
		 {
			 return false;
		 }
         Pattern pattern = Pattern.compile("[0-9]*");
         Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ) {
           return false;
         }
        return true;
	 }
	 
	 public static boolean isNotNumeric(String str)
	 {
		 return !isNumeric(str);
	 }
	 
	 /**
	  * 判断是否为 L位数字
	  * 
	  * @param str
	  * @return
	  */
	 public static boolean isNumeric(String str, int L)
	 {
		 if (StringUtils.isEmpty(str))
		 {
			 return false;
		 }
		 String regx = "[0-9]{" + L + "}";
         Pattern pattern = Pattern.compile(regx);
         Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ) {
           return false;
         }
        return true;
	 }
	 
	 public static boolean isNotNumeric(String str, int L)
	 {
		 return !isNumeric(str, L);
	 }
	 
	 public static boolean is16Regx(String str)
	 {
		 if (StringUtils.isEmpty(str))
		 {
			 return false;
		 }
         Pattern pattern = Pattern.compile("[\\da-fA-F]+");
         Matcher isPassWd = pattern.matcher(str);
        if( !isPassWd.matches() ) {
           return false;
         }
        return true;
	 }
	 
	 public static boolean isNot16Regx(String str)
	 {
		 return !is16Regx(str);
	 }
	 
	 public static boolean regxMatch(String regex, String input)
	 {
		 
		 return Pattern.compile(regex).matcher(input).matches();
	 }
	 
	 /**
	 * 创建指定数量的随机字符串
	 * 
	 * @param numberFlag
	 *            是否是纯数字
	 * @param length
	 * @return
	 */
	 public static String createRandom(boolean numberFlag, int length) {
			String retStr = "";
			String strTable = numberFlag ? "1234567890" : "1234567890abcdefghijkmnpqrstuvwxyz";
			int len = strTable.length();
			boolean bDone = true;
			do {
				retStr = "";
				int count = 0;
				for (int i = 0; i < length; i++) {
					double dblR = Math.random() * len;
					int intR = (int) Math.floor(dblR);
					char c = strTable.charAt(intR);
					if (('0' <= c) && (c <= '9')) {
						count++;
					}
					retStr += strTable.charAt(intR);
				}
				if (count >= 2) {
					bDone = false;
				}
			} while (bDone);
			return retStr;
		}
	 /**
	  * <一句话功能简述> 返回字符串中的数字字母
	  * <功能详细描述>
	  * author: user
	  * 创建时间:  2018年1月19日
	  * @param character
	  * @return
	  * @see [类、类#方法、类#成员]
	  */
	 public static String strFilter(String character)
	 {
		 character = character.replaceAll("[^(a-zA-Z0-9)]", "");
		 return character;
	 }
	 
	/**
	 * <一句话功能简述>获得规定范围内的随机正整数(1~N) 
	 * <功能详细描述>
	 * @param range
	 * @return
	 * @see [类、类#方法、类#成员]
	 * @author liuml 
	 * 修改时间:2018年8月9日 下午8:50:05
	 */
	public static int getRandomNumber(int small, int big) {
		Random random = new Random();
		int num = random.nextInt(big) + small;
		while (num < small || num > big) {
			num = random.nextInt(big) + small;
		}
		return num;
	}
	
	/**
	 * <一句话功能简述>去除HTML文本前的空格和换行
	 * <功能详细描述>
	 * @param content
	 * @return
	 * @see [类、类#方法、类#成员]
	 * @author  liuml
	 * 修改时间:2018年11月26日 下午9:50:50
	 */
	public static String htmlTextTrim(String content) {
		if (StringUtils.isEmpty(content)) {
			return content;
		} else {
			while (true) {
				if (content.startsWith("&ensp;")) {
					content = content.substring("&ensp;".length(), content.length()).trim();
				} else if (content.startsWith("<br>")) {
					content = content.substring("<br>".length(), content.length()).trim();
				} else if (content.startsWith("&nbsp;")) {
					content = content.substring("&nbsp;".length(), content.length()).trim();
				} else if (content.startsWith("&emsp;")) {
					content = content.substring("&emsp;".length(), content.length()).trim();
				} else {
					break;
				}
			}
		}

		return content;
	}
}
