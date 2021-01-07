/**
 * 2009-10-12 21:47:02
 */
package com.cntinker.util;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sun.misc.BASE64Decoder;

/**
 * @author bin_liu
 */
public class StringHelper {

	private static final char SEPARATOR = '_';
	private static final String CHARSET_NAME = "UTF-8";

	private static String phoneMatcher;

	static {
		StringBuffer query = new StringBuffer();
		query.append("147\\d{9}");
		query.append("|1[3,8]\\d{9}");
		query.append("|15\\[0,1,2,3,5,6,7,8,9]");
		query.append("|17[3,6,7,8]\\d{8}");
		query.append("|170[0,1,2,5,7,8,9]\\d{7}");

		phoneMatcher = query.toString();
	}

	public static String getHideEmail(String str, int type, double hidePer) {
		String temp = str;
		String account = temp.substring(0, temp.indexOf("@"));
		String website = temp.substring(temp.indexOf("@"));

		return getHideStr(account, type, hidePer) + website;
	}

	public static String getHideStr(String str, int type, double hidePer) {
		String temp = str;
		int hideCount = 0;
		Double hc = str.length() * (hidePer * 0.01);
		Double hc2 = new Double(String.format("%.2f", hc));
		DecimalFormat df = new DecimalFormat("######0");
		hideCount = new Integer(df.format(hc2));

		if (type == 0) {
			// 从头开始
			temp = temp.substring(hideCount);
			for (int i = 0; i < hideCount; i++) {
				temp = "*" + temp;
			}
		} else if (type == 1) {
			//中间位置
			int d = str.length() / 2;
			int subCount = hideCount / 2;
			int subCountBehind = hideCount - subCount;
			//从中间切前后
			String subFornt = temp.substring(0, d);
			String subBehind = temp.substring(d);

			subFornt = subFornt.substring(0, subFornt.length() - subCount);
			for (int i = 0; i < subCount; i++) {
				subFornt = subFornt + "*";
			}
			subBehind = subBehind.substring(subCountBehind);
			for (int i = 0; i < subCountBehind; i++) {
				subBehind = "*" + subBehind;
			}
			return subFornt + subBehind;
		} else if (type == 2) {
			//最后
			temp = temp.substring(0, temp.length() - hideCount);
			for (int i = 0; i < hideCount; i++) {
				temp = temp + "*";
			}
		}
		return temp;
	}

	/**
	 * 按指定长度切割字符串，方法调用传入co为null即可
	 *
	 * @param str
	 * @param pos
	 * @param co
	 * @return List<String>
	 */
	public static List<String> split(String str, int pos, List<String> co) {
		if (str.length() <= 0) {
			return co;
		}
		if (str.length() <= pos) {
			co.add(str);
			return co;
		}
		if (co == null || co.size() <= 0) {
			co = new ArrayList<String>();
		}
		String temp = str.substring(0, pos);
		co.add(temp);
		return split(str.substring(pos), pos, co);
	}

	private StringHelper() {

	}

	public static Long toUnixTimestamp(Long timestamp) {
		Long r = timestamp / 1000;
		return r;
	}

	public static Long unixTimestampToJAVA(Long timestamp) {
		Long r = timestamp * 1000;
		return r;
	}

	public static int compareCharterEqualsCount(String input, String compare) {
		int flag = 0;

		for (int i = 0; i < input.length(); i++) {
			if (i >= compare.length()) {
				break;
			}
			if (input.charAt(i) == compare.charAt(i)) {
				flag++;
			}
		}
		return flag;
	}

	/**
	 * 判断一个字符串是否是由英文，数字，下划线组成
	 *
	 * @param str
	 * @return boolean
	 */
	public static boolean isAlphanumeric(String str) {
		int flag = 0;
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			int n = (int) c;

			if (n >= 0 && n <= 9) {
				// num
				continue;
			} else if (n >= 65 && n <= 90) {
				// en b
				continue;
			} else if (n == 95) {
				// _
				continue;
			} else if (n >= 97 && n <= 122) {
				// en s
				continue;
			}
			flag++;
		}
		if (flag == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 得到系统当前时间
	 *
	 * @return String
	 */
	public static String getSystime() {
		DateFormat dft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dft.format(new Date());
	}

	/**
	 * 按格式获取系统当前时间
	 *
	 * @param format
	 * @return String
	 */
	public static String getSystime(String format) {
		DateFormat dft = new SimpleDateFormat(format);
		return dft.format(new Date());
	}

	/**
	 * 按格式获取系统当前时间
	 *
	 * @param format
	 * @return String
	 */
	public static List<String> getTimeDistance(String format, int distance) {
		List<String> dateList = new ArrayList<String>();

		for (int i = 0; i < distance; i++) {
			Calendar curCal = Calendar.getInstance();
			SimpleDateFormat datef = new SimpleDateFormat(format);
			curCal.add(Calendar.MONTH, -i);
			Date beginTime = curCal.getTime();
			String time = datef.format(beginTime);
			dateList.add(time);
		}

		return dateList;
	}

	/**
	 * 按格式获取系统当前时间
	 *
	 * @param format
	 * @return String
	 */
	public static String getSystime(String format, long timestamp) {
		DateFormat dft = new SimpleDateFormat(format);
		return dft.format(new Date(timestamp));
	}

	/**
	 * 得到上月的这一天0点，返回格式：yyyy-MM-dd HH:mm:ss
	 *
	 * @return String
	 */
	public static String getPreMonth() {
		Calendar curCal = Calendar.getInstance();
		SimpleDateFormat datef = new SimpleDateFormat("yyyy-MM-dd");

		curCal.add(Calendar.MONTH, -1);
		Date beginTime = curCal.getTime();
		String time = datef.format(beginTime) + " 00:00:00";
		return time;
	}

	/**
	 * 得到下月的这一天0点，返回格式：yyyy-MM-dd HH:mm:ss
	 *
	 * @return String
	 */
	public static String getNextMonth() {
		Calendar curCal = Calendar.getInstance();
		SimpleDateFormat datef = new SimpleDateFormat("yyyy-MM-dd");

		curCal.add(Calendar.MONTH, 1);
		Date beginTime = curCal.getTime();
		String time = datef.format(beginTime) + " 00:00:00";
		return time;
	}

	/**
	 * 指定时间的月份第一天
	 *
	 * @param time
	 * @return String
	 */
	public static String getFirstDay(Long time) {
		Calendar curCal = Calendar.getInstance();
		SimpleDateFormat datef = new SimpleDateFormat("yyyy-MM-dd");
		curCal.setTime(new Date(time));
		curCal.set(Calendar.DAY_OF_MONTH, 1);
		Date beginTime = curCal.getTime();
		String sTime = datef.format(beginTime) + " 00:00:00";

		return sTime;
	}

	/**
	 * 指定时间的月份第一天
	 *
	 * @param time
	 * @return String
	 */
	public static Long getFirstDayForTime(Long time) {
		Calendar curCal = Calendar.getInstance();
		SimpleDateFormat datef = new SimpleDateFormat("yyyy-MM-dd");
		curCal.setTime(new Date(time));
		curCal.set(Calendar.DAY_OF_MONTH, 1);
		Date beginTime = curCal.getTime();
		String sTime = datef.format(beginTime) + " 00:00:00";
		Long res = null;
		try {
			res = datef.parse(sTime).getTime();
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * 指定时间的月份第一天
	 *
	 * @param time
	 * @return String
	 */
	public static Timestamp getFirstDayForTimestamp(Long time) {
		Calendar curCal = Calendar.getInstance();
		SimpleDateFormat datef = new SimpleDateFormat("yyyy-MM-dd");
		curCal.setTime(new Date(time));
		curCal.set(Calendar.DAY_OF_MONTH, 1);
		Date beginTime = curCal.getTime();
		String sTime = datef.format(beginTime) + " 00:00:00";
		Long res = null;
		try {
			res = datef.parse(sTime).getTime();
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		if(res != null) {
			return new Timestamp(res);
		}else {
			return null;
		}
	}

	/**
	 * 指定时间的月份最后一天
	 *
	 * @param time
	 * @return String
	 */
	public static String getEndDay(Long time) {
		Calendar curCal = Calendar.getInstance();
		SimpleDateFormat datef = new SimpleDateFormat("yyyy-MM-dd");
		curCal.setTime(new Date(time));
		curCal.set(Calendar.DATE, 1);
		curCal.roll(Calendar.DATE, -1);
		Date endTime = curCal.getTime();
		String eTime = datef.format(endTime) + " 23:59:59";

		return eTime;
	}

	/**
	 * 指定时间的月份最后一天
	 *
	 * @param time
	 * @return Long
	 */
	public static Long getEndDayForTime(Long time) {
		Calendar curCal = Calendar.getInstance();
		SimpleDateFormat datef = new SimpleDateFormat("yyyy-MM-dd");
		curCal.setTime(new Date(time));
		curCal.set(Calendar.DATE, 1);
		curCal.roll(Calendar.DATE, -1);
		Date endTime = curCal.getTime();
		String eTime = datef.format(endTime) + " 23:59:59";
		Long res = null;
		try {
			res = datef.parse(eTime).getTime();
		} catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return res;
	}

	/**
	 * 指定时间的月份最后一天
	 *
	 * @param time
	 * @return Long
	 */
	public static Timestamp getEndDayForTimestamp(Long time) {
		Calendar curCal = Calendar.getInstance();
		SimpleDateFormat datef = new SimpleDateFormat("yyyy-MM-dd");
		curCal.setTime(new Date(time));
		curCal.set(Calendar.DATE, 1);
		curCal.roll(Calendar.DATE, -1);
		Date endTime = curCal.getTime();
		String eTime = datef.format(endTime) + " 23:59:59";
		Long res = null;
		try {
			res = datef.parse(eTime).getTime();
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		if(res != null) {
			return new Timestamp(res);
		}else {
			return null;
		}
	}

	/**
	 * 得到默认日期格式的时间:yyyy-MM-dd HH:mm:ss
	 *
	 * @param time
	 * @return Long
	 * @throws java.text.ParseException
	 */
	public static Long getTime(String time) throws java.text.ParseException {
		return getTime(time, "yyyy-MM-dd HH:mm:ss");
	}

	public static Timestamp getTimestamp(String time, String formart)
			throws java.text.ParseException {
		return new Timestamp(getTime(time, formart));
	}

	public static Timestamp getTimestamp(String time)
			throws java.text.ParseException {
		return new Timestamp(getTime(time));
	}

	/**
	 * 得到指定日期格式的时间
	 *
	 * @param time
	 * @param formart
	 * @return Long
	 * @throws java.text.ParseException
	 */
	public static Long getTime(String time, String formart) {
		SimpleDateFormat datef = new SimpleDateFormat(formart);
		try {
			return datef.parse(time).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return -1L;
	}

	/**
	 * 当月第一天
	 *
	 * @return String
	 */
	public static String getFirstDay() {
		Calendar curCal = Calendar.getInstance();
		SimpleDateFormat datef = new SimpleDateFormat("yyyy-MM-dd");

		curCal.set(Calendar.DAY_OF_MONTH, 1);
		Date beginTime = curCal.getTime();
		String sTime = datef.format(beginTime) + " 00:00:00";

		return sTime;
	}

	/**
	 * 当月最后一天
	 *
	 * @return String
	 */
	public static String getEndDay() {
		Calendar curCal = Calendar.getInstance();
		SimpleDateFormat datef = new SimpleDateFormat("yyyy-MM-dd");

		curCal.set(Calendar.DATE, 1);
		curCal.roll(Calendar.DATE, -1);
		Date endTime = curCal.getTime();
		String eTime = datef.format(endTime) + " 23:59:59";

		return eTime;
	}

	/**
	 * 获得星期几
	 *
	 * @return String
	 */
	public static String getWeek(Date date) {
		String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int week = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (week < 0) {
			week = 0;
		}
		return weekDays[week];
	}

	/**
	 * 得到指定类型时间间隔的之前日期
	 *
	 * @param timeType       1-天，2-月<br>
	 * @param intervalNumber 间隔几天/几个月<br>
	 * @param currentTime    指定时间<br>
	 * @return Long
	 * @throws java.text.ParseException
	 */
	public static Long getBeforeTimeLong(int timeType, int intervalNumber,
										 long currentTime) throws java.text.ParseException {
		String time = getBeforeTimeStr(timeType, intervalNumber, currentTime);
		return getTime(time);
	}

	/**
	 * 得到指定类型时间间隔的之后日期
	 *
	 * @param timeType       0-小时,1-天，2-月,3-年<br>
	 * @param intervalNumber 间隔几天/几个月<br>
	 * @param currentTime    指定时间<br>
	 * @return Long
	 * @throws java.text.ParseException
	 */
	public static Long getAfterTimeLong(int timeType, int intervalNumber,
										long currentTime) throws java.text.ParseException {
		String time = getAfterTimeStr(timeType, intervalNumber, currentTime);
		return getTime(time);
	}

	/**
	 * 得到指定类型时间间隔的之后日期
	 *
	 * @param timeType       0-小时,1-天，2-月,3-年<br>
	 * @param intervalNumber 间隔几天/几个月<br>
	 * @param currentTime    指定时间<br>
	 * @return String
	 */
	public static String getAfterTimeStr(int timeType, int intervalNumber,
										 long currentTime) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(currentTime));

		if (timeType == 0) {
			calendar.add(Calendar.HOUR, +intervalNumber); // 得到前一小时
		} else if (timeType == 1) {
			calendar.add(Calendar.DATE, +intervalNumber); // 得到前一天
		} else if (timeType == 2) {
			calendar.add(Calendar.MONTH, +intervalNumber); // 得到前一个月
		} else if (timeType == 3) {
			calendar.add(Calendar.YEAR, +intervalNumber); // 得到前一年
		}

		StringBuffer sb = new StringBuffer();
		sb.append(calendar.get(Calendar.YEAR)).append("-");
		sb.append(calendar.get(Calendar.MONTH) + 1).append("-");
		sb.append(calendar.get(Calendar.DATE)).append(" ");
		sb.append(calendar.get(Calendar.HOUR_OF_DAY)).append(":");
		sb.append(calendar.get(Calendar.MINUTE)).append(":");
		sb.append(calendar.get(Calendar.SECOND));

		return sb.toString();
	}

	/**
	 * 得到指定类型时间间隔的之前日期
	 *
	 * @param timeType       1-天，2-月<br>
	 * @param intervalNumber 间隔几天/几个月<br>
	 * @param currentTime    指定时间<br>
	 * @return String
	 */
	public static String getBeforeTimeStr(int timeType, int intervalNumber,
										  long currentTime) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(currentTime));

		if (timeType == 1) {
			calendar.add(Calendar.DATE, -intervalNumber); // 得到前一天
		} else if (timeType == 2) {
			calendar.add(Calendar.MONTH, -intervalNumber); // 得到前一个月
		}

		StringBuffer sb = new StringBuffer();
		sb.append(calendar.get(Calendar.YEAR)).append("-");
		sb.append(calendar.get(Calendar.MONTH) + 1).append("-");
		sb.append(calendar.get(Calendar.DATE)).append(" ");
		sb.append(calendar.get(Calendar.HOUR_OF_DAY)).append(":");
		sb.append(calendar.get(Calendar.MINUTE)).append(":");
		sb.append(calendar.get(Calendar.SECOND));

		return sb.toString();
	}

	public static String byte2hex(byte[] b) {
		StringBuffer hs = new StringBuffer(b.length);
		String stmp = "";
		int len = b.length;
		for (int n = 0; n < len; n++) {
			stmp = Integer.toHexString(b[n] & 0xFF);
			if (stmp.length() == 1) {
				hs = hs.append("0").append(stmp);
			} else {
				hs = hs.append(stmp);
			}
		}
		return String.valueOf(hs);
	}

	/**
	 * 得到UNICODE码
	 *
	 * @param str
	 * @return String
	 */
	public static String getUnicode(String str) {

		if (str == null) {
			return "";
		}
		String hs = "";

		try {
			byte b[] = str.getBytes("UTF-16");
			for (int n = 0; n < b.length; n++) {
				str = (java.lang.Integer.toHexString(b[n] & 0XFF));
				if (str.length() == 1) {
					hs = hs + "0" + str;
				} else {
					hs = hs + str;
				}
				if (n < b.length - 1) {
					hs = hs + "";
				}
			}
			str = hs.toUpperCase().substring(4);
			char[] chs = str.toCharArray();
			str = "";
			for (int i = 0; i < chs.length; i = i + 4) {
				str += "\\u" + chs[i] + chs[i + 1] + chs[i + 2] + chs[i + 3];
			}
			return str;
		} catch (Exception e) {
			System.out.print(e.getMessage());
		}
		return str;
	}

	public int compare(Object o1, Object o2) {
		String a = (String) o1;
		String b = (String) o2;

		if (!isDigit(a) || !isDigit(b)) {
			throw new IllegalArgumentException("the object must a digit");
		}

		long aa = Long.valueOf(a).longValue();
		long bb = Long.valueOf(b).longValue();

		if (aa > bb) {
			return 1;
		} else if (aa < bb) {
			return -1;
		}

		return 0;
	}

	/**
	 * 去空格并将其替换成指定字符
	 *
	 * @param content
	 * @return String
	 */
	public static String alterSpace(String content, String character) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < content.length(); i++) {
			String c = new String(new char[]{content.charAt(i)});
			if (c.trim().length() == 0) {
				sb.append(character);
				continue;
			}
			sb.append(c);
		}
		return sb.toString();
	}

	/**
	 * @param line
	 * @return boolean
	 */
	public static boolean isEmail(String line, int length) {
		return line.matches("\\w+[\\w.]*@[\\w.]+\\.\\w+$")
				&& line.length() <= length;
	}

	public static boolean isEmail(String line) {
		return line.matches("\\w+[\\w.]*@[\\w.]+\\.\\w+$");
	}

	/**
	 * 判断输入是否全是中文
	 *
	 * @param value
	 * @param length
	 * @return boolean
	 */
	public static boolean isChineseName(String value, int length) {
		return value.matches("^[\u4e00-\u9fa5]+$") && value.length() <= length;
	}

	/**
	 * 判断字符串是否含有HTML标签
	 *
	 * @param value
	 * @return boolean
	 */

	public static boolean isHaveHtmlTag(String value) {
		return value.matches("<(\\S*?)[^>]*>.*?</\\1>|<.*? />");
	}

	/**
	 * 检查URL是否合法
	 *
	 * @param value
	 * @return boolean
	 */
	public static boolean isURL(String value) {
		return value.matches("[a-zA-z]+://[^\\s]*");
	}

	/**
	 * 检查IP是否合法
	 *
	 * @param value
	 * @return boolean
	 */
	public static boolean iskIP(String value) {
		return value.matches("\\d{1,3}+\\.\\d{1,3}+\\.\\d{1,3}+\\.\\d{1,3}");
	}

	/**
	 * 检查QQ是否合法，必须是数字，且首位不能字幕
	 *
	 * @param value
	 * @return boolean
	 */

	public static boolean isQQ(String value) {
		return value.matches("[1-9][0-9]{4,13}");
	}

	/**
	 * 检查邮编是否合法
	 *
	 * @param value
	 * @return boolean
	 */
	public static boolean isPostCode(String value) {
		return value.matches("[1-9]\\d{5}(?!\\d)");
	}

	/**
	 * 检查输入的字符串是否为手机号
	 *
	 * @param line
	 * @return List
	 */
	public static boolean isPhone(String line) {

		Pattern p = null; // 正则表达??
		Matcher m = null; // 操作的字符串
		p = Pattern.compile(phoneMatcher);// 匹配移动手机号码
		m = p.matcher(line);
		if (m.matches()) {
			return true;
		}
		return false;
	}

	/**
	 * 去掉手机号码前面的86和+86符号
	 *
	 * @param phoneno
	 * @return String
	 */
	public static String fixPhoneno(String phoneno) {
		if (phoneno.length() > 11 && phoneno.startsWith("86")) {
			return phoneno.substring(2);
		} else if (phoneno.length() > 11 && phoneno.startsWith("+86")) {
			return phoneno.substring(3);
		}
		return phoneno;
	}

	/**
	 * 是否包含手机号
	 *
	 * @param line
	 * @return boolean
	 */
	public static boolean hasPhone(String line) {
		Pattern p = null; // 正则表达??
		Matcher m = null; // 操作的字符串
		p = Pattern.compile(phoneMatcher);// 匹配移动手机号码
		m = p.matcher(line);
		if (m.find()) {
			return true;
		}
		return false;
	}

	/**
	 * 从一行字符串中提取号码由左至右,11为数字符合手机号规则;
	 *
	 * @param line
	 * @return String
	 */
	public static String getPhone(String line) {
		Pattern p = null; // 正则表达??
		Matcher m = null; // 操作的字符串
		p = Pattern.compile(phoneMatcher);// 匹配移动手机号码

		for (int i = 0; i < line.length(); i++) {

			m = p.matcher(line);
			if (m.find()) {
				String str = line.substring(m.start(), m.end());
				return str;
			}
		}

		return "";
	}

	/**
	 * 按规定条件得到一个文本里的字符
	 *
	 * @param text
	 * @param compile
	 * @return Set
	 */
	public static Set getTextBlock(String text, String compile) {

		Set set = new HashSet();
		Pattern p = null; // 正则表达式
		Matcher m = null; // 操作的字符串
		p = Pattern.compile(compile);// 匹配条件
		m = p.matcher(text);
		while (m.find()) {
			// System.out.println(strMail.substring(m.start(),m.end()));
			String str = text.substring(m.start(), m.end());
			set.add(str);
		}
		return set;
	}

	private static String PHONE_PATTERN = "1[3,5][4,5,6,7,8,9]\\d{8}|15[8,9]\\d{8}";

	/**
	 * 返回所有手机号码
	 *
	 * @param strMail
	 * @return Set
	 */
	public static Set getCode(String strMail) {
		Set set = new HashSet();
		Pattern p = Pattern.compile(PHONE_PATTERN);// 匹配移动手机号码
		Matcher m = p.matcher(strMail);
		while (m.find()) {
			// System.out.println(strMail.substring(m.start(),m.end()));
			String str = strMail.substring(m.start(), m.end());
			set.add(str);
		}
		return set;
	}

	private static String CMCC_PHONE = "(?i)(?<=\\b)[a-z0-9][-a-z0-9_.]+[a-z0-9]@([a-z0-9][-a-z0-9]+\\.)+[a-z]{2,4}(?=\\b)";

	/**
	 * get email
	 *
	 * @param content
	 * @return Set
	 */
	public static Set getMail(String content) {
		Set set = new HashSet();
		Pattern p = null; // 正则表达??
		Matcher m = null; // 操作的字符串
		p = Pattern.compile(CMCC_PHONE);// 匹配移动手机号码
		m = p.matcher(content);
		while (m.find()) {
			// System.out.println(strMail.substring(m.start(),m.end()));
			String str = content.substring(m.start(), m.end());
			set.add(str);
		}
		return set;
	}

	/**
	 * 生成指定位之间的随机数
	 *
	 * @param min
	 * @param max
	 * @return int
	 */
	public static int getRandom(int min, int max) {
		return (int) ((double) min + (int) (max - min) * Math.random());
	}

	/**
	 * 生成length位数字
	 *
	 * @param length
	 * @return int
	 */
	public static int getRandom(int length) {
		return Integer.valueOf(getRand(length)).intValue();
	}// end...

	/**
	 * 生成length位数字
	 *
	 * @param length
	 * @return long
	 */
	public static long getRandomL(int length) {
		return Long.valueOf(getRand(length)).longValue();
	}// end...

	/**
	 * 生成length位数字
	 *
	 * @param length
	 * @return String
	 */
	public static String getRandomStr(int length) {
		return Long.toString(getRandomL(length));
	}// end...

	/**
	 * 生成随机数字的字符串
	 *
	 * @param length
	 * @return String
	 */
	private static String getRand(int length) {
		StringBuffer t = new StringBuffer();
		for (int j = 0; j < length; j++) {
			double d = Math.random() * 10;
			int c = (int) d;
			t.append(c);
		}
		String result = t.toString();
		if (result.substring(0, 1).equalsIgnoreCase("0")) {
			result = result.replaceAll("0", "1");
		}
		if (result.length() > length) {
			result = result.substring(0, length);
		} else if (result.length() < length) {
			result = result + StringHelper.getRand(length - result.length());
		}
		return result;
	}// end...

	public static String getRandomChar(int length) {
		String defaultContent = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
		return getRandomCharForInput(defaultContent, length);
	}

	public static String getRandomCharForInput(String inputContent, int length) {
		String chars = inputContent;
		String res = "";
		for (int i = 0; i <= length; i++) {
			double d = Math.random() * inputContent.length();
			int pos = (int)d;
			res += new Character(inputContent.charAt(pos));
		}
		return res;
	}

	/**
	 * 判断字符串是否为空
	 *
	 * @param str
	 * @return boolean
	 */
	public static boolean isNull(String[] str) {
		for (int i = 0; i < str.length; i++) {
			if (isNull(str[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 字符串是否为数字
	 *
	 * @param str
	 * @return boolean
	 */
	public static boolean isNull(String str) {
		return (str == null || str.trim().length() == 0);
	}// end....

	/**
	 * 判断字符串中的每个字符是否都是数字
	 *
	 * @param str
	 * @return boolean
	 */
	public static boolean isDigit(String[] str) {
		for (int i = 0; i < str.length; i++) {
			if (!isDigit(str[i])) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断字符串是否都是数字
	 *
	 * @param str
	 * @return boolean
	 */
	public static boolean isDigit(String str) {
		if (isNull(str)) {
			throw new NullPointerException();
		}
		for (int i = 0, size = str.length(); i < size; i++) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}// end....

	/**
	 * 得到STACK中信息
	 *
	 * @param e
	 * @return String
	 */
	public static String getStackInfo(Throwable e) {
		StringBuffer info = new StringBuffer("Found Exception: ");

		info.append("\n");
		info.append(e.getClass().getName());
		info.append(" : ").append(e.getMessage() == null ? "" : e.getMessage());
		StackTraceElement[] st = e.getStackTrace();
		for (int i = 0; i < st.length; i++) {
			info.append("\t\n").append("at ");
			info.append(st[i].toString());
		}
		return info.toString();
	}// end..

	/**
	 * 将输入的字符按指定的正则式转换
	 *
	 * @param str
	 * @param regEx
	 * @param code
	 * @return String
	 */
	private static String insteadCode(String str, String regEx, String code) {
		if (isNull(str)) {
			return "";
		}
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		String s = m.replaceAll(code);
		return s;
	}// end insteadCode method

	/**
	 * 将HTML的关键字替换输出
	 *
	 * @param sourceStr
	 * @return String
	 */
	public static String toHtml(String sourceStr) {
		if (isNull(sourceStr)) {
			return "";
		}
		String targetStr;
		targetStr = insteadCode(sourceStr, ">", "&gt;");
		targetStr = insteadCode(targetStr, "<", "&lt;");
		targetStr = insteadCode(targetStr, "\n", "<br>");
		targetStr = insteadCode(targetStr, " ", "&nbsp;");
		return targetStr.trim();
	}// end toHTML method

	/**
	 * 转义传参中的特殊字符问题 <li>+ URL ??号表示空??%2B<br/> <li>空格 URL中的空格可以??号或者编??%20<br/>
	 * <li>/ 分隔目录和子目录 %2F <br/> <li>? 分隔实际??URL 和参??%3F<br/> <li>% 指定特殊字符 %25
	 * <br/> <li># 表示书签 %23 <br/> <li>& URL 中指定的参数间的分隔??%26 <br/> <li>=URL
	 * 中指定参数的??%3D <br/>
	 *
	 * @param parameter
	 * @return String
	 */
	public static String sendGetParameter(String parameter) {
		parameter = insteadCode(parameter, "&", "%26");
		parameter = insteadCode(parameter, " ", "%20");
		parameter = insteadCode(parameter, "%", "%25");
		parameter = insteadCode(parameter, "#", "%23");

		return parameter.trim();
	}

	/**
	 * 按指定的起始和终止字符，切割字符??
	 *
	 * @param content
	 * @param start
	 * @param end
	 * @return String
	 */
	public static String spiltStr(String content, String start, String end) {
		if (!(content.indexOf(start) > -1) || !(content.indexOf(end) > -1)) {
			throw new IndexOutOfBoundsException(
					"[start Character or end Character,isn't exist in the specified content]");
		}

		int s = content.indexOf(start);

		int e = start.equals(end) ? content.substring(s + 1).indexOf(end)
				: content.indexOf(end);

		if (s >= e) {
			throw new IndexOutOfBoundsException(
					"[the Character end is smallness Character start]");
		} else {
			content = new String(content.substring(s + 1, e));
		}

		return content.trim();
	}// end...

	/**
	 * 得到????中按指定分割符切好的????元素
	 *
	 * @param content
	 * @param split
	 * @return String[]
	 */
	public static String[] splitStr(String content, String split) {

		if (content.indexOf(split) < 0) {
			return new String[]{content};
		}

		int s = 0;
		int e = content.indexOf(split);

		List list = new ArrayList();

		while (e <= content.length()) {
			if (content.indexOf(split) == -1 && list.size() != 0) {
				list.add(content);
				break;
			}
			list.add(content.substring(s, e));
			content = content.substring(e + 1, content.length());
			e = s + content.indexOf(split);
		}
		return (String[]) list.toArray(new String[0]);
	}// end...

	/**
	 * 按指定的字符位切割字符串(用于页面显示),剩余位字符用变量end中的字符表示 此方法带过滤转意字符功能
	 *
	 * @param str
	 * @param num
	 * @param end
	 * @return String
	 */
	public static String splitStr(String str, int num, String end) {
		StringBuffer sb = new StringBuffer();
		if (str == null || end == null) {
			throw new NullPointerException();
		}
		if (str.length() > num) {
			str = sb.append(str.substring(0, num)).append(end).toString();
		}
		return toHtml(str);
	}// end of splitStr()

	/**
	 * 按左补零右对齐的规则格式化内??
	 *
	 * @param content
	 * @param count
	 * @return String
	 */
	public static String completeText(String content, int count) {
		StringBuffer sb = new StringBuffer();
		if (count > content.length()) {
			for (int i = count - content.length(); content.length() < count
					&& i != 0; i--) {
				sb.append("0");
			}
		}
		sb.append(content);
		return sb.toString();
	}// end..

	/**
	 * 按左补零右对齐的规则格式化内??
	 *
	 * @param content
	 * @param count
	 * @return String
	 */
	public static String completeText(int content, int count) {
		String c = Integer.toString(content);
		StringBuffer sb = new StringBuffer();
		if (count > c.length()) {
			for (int i = count - c.length(); c.length() < count && i != 0; i--) {
				sb.append("0");
			}
		}
		sb.append(content);
		return sb.toString();
	}// end..

	/**
	 * 按右补空格左对齐的规则格式化内容
	 *
	 * @param content
	 * @param count
	 * @return String
	 */
	public static String completeTextSpace(String content, int count) {
		StringBuffer sb = new StringBuffer();

		sb.append(content);
		if (count > content.length()) {
			for (int i = 0; i < count - content.length(); i++) {
				sb.append(" ");
			}
		}

		return sb.toString();
	}// end..

	/**
	 * 按右补空格左对齐的规则格式化内容
	 *
	 * @param content
	 * @param count
	 * @return String
	 */
	public static String completeTextSpace(int content, int count) {
		StringBuffer sb = new StringBuffer();
		String c = Integer.toString(content);
		sb.append(content);
		if (count > c.length()) {
			for (int i = 0; i < count - c.length(); i++) {
				sb.append(" ");
			}
		}
		return sb.toString();
	}// end..

	/**
	 * 特殊处理号码
	 *
	 * @param phone
	 * @return String
	 */
	public static String processPhone(String phone) {
		StringBuffer sb = new StringBuffer();
		sb.append(phone.substring(0, 3)); // 3
		sb.append(phone.substring(5, 6)); // 6
		sb.append(phone.substring(4, 5)); // 5
		sb.append(phone.substring(3, 4)); // 4
		sb.append(phone.substring(6, 7)); // 7
		sb.append(phone.substring(9, 10)); // 10
		sb.append(phone.substring(7, 8)); // 8
		sb.append(phone.substring(8, 9)); // 9
		sb.append(phone.substring(10, 11)); // 11

		return sb.toString();
	}

	public static String toHex(String phoneno) {
		if (isNull(phoneno)) {
			return "";
		}
		long i = new Long(phoneno).longValue();
		String i_16 = Long.toHexString(i);
		return i_16;
	}

	/**
	 * 如果字符串都是中文才返回true
	 * @param c
	 * @return
	 */
	public static boolean isChinese(String c){
		if(isNull(c)){
			return false;
		}
		char[] lst = c.toCharArray();
		for(int i=0;i<lst.length;i++){
			char e = lst[i];
			if(!isChinese(e)){
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断字符是否是中文
	 *
	 * @param c
	 * @return boolean
	 */
	public static boolean isChinese(char c) {

		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);

		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS

				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS

				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A

				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION

				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION

				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {

			return true;

		}

		return false;

	}

	/**
	 * @param b
	 * @return String
	 */
	public static String getBASE64(byte[] b) {
		String s = null;
		if (b != null) {
			s = Base64.getEncoder().encodeToString(b);
		}
		return s;
	}

	/**
	 * @param s
	 * @return byte[]
	 */
	public static byte[] getFromBASE64(String s) {
		byte[] b = null;
		if (s != null) {
			try {
				b = Base64.getDecoder().decode(s);
				return b;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return b;
	}

	/**
	 * 把链接的中文转成可识别链
	 *
	 * @param content
	 * @return String
	 * @throws UnsupportedEncodingException
	 */
	public static String getUrlEncode(String content)
			throws UnsupportedEncodingException {
		char[] a = content.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < a.length; i++) {
			if (isChinese(a[i])) {
				sb.append(URLEncoder.encode(a[i] + "", "GBK"));
			} else {
				sb.append(a[i]);
			}
		}

		return sb.toString();
	}

	public static String getUrlEncode(String content, String encode)
			throws UnsupportedEncodingException {
		char[] a = content.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < a.length; i++) {
			if (isChinese(a[i])) {
				sb.append(URLEncoder.encode(a[i] + "", encode));
			} else {
				sb.append(a[i]);
			}
		}

		return sb.toString();
	}

	public static String getUrlDecode(String content)
			throws UnsupportedEncodingException {
		StringBuffer sb = new StringBuffer();
		sb.append(URLDecoder.decode(content, "GBK"));
		return sb.toString();
	}

	public static String getUrlDecode(String content, String encode)
			throws UnsupportedEncodingException {
		StringBuffer sb = new StringBuffer();
		sb.append(URLDecoder.decode(content, encode));
		return sb.toString();
	}

	/**
	 * 转换十六进制编码为字符串
	 *
	 * @param bytes
	 * @return String
	 */
	public static String toStringHex(String bytes) {

		String hexString = "0123456789ABCDEF ";
		ByteArrayOutputStream baos = new ByteArrayOutputStream(
				bytes.length() / 2);
		// 将每2位16进制整数组装成一个字节
		for (int i = 0; i < bytes.length(); i += 2) {
			baos.write((hexString.indexOf(bytes.charAt(i)) << 4 | hexString
					.indexOf(bytes.charAt(i + 1))));
		}
		return new String(baos.toByteArray());
	}

	public static String fromUnicode(String str) {
		if (str.indexOf("\\u") < 0) {
			return str;
		}
		return fromUnicode(str.toCharArray(), 0, str.length(), new char[1024]);

	}

	public static String fromUnicode(char[] in, int off, int len,
									 char[] convtBuf) {

		if (convtBuf.length < len) {

			int newLen = len * 2;

			if (newLen < 0) {

				newLen = Integer.MAX_VALUE;

			}

			convtBuf = new char[newLen];

		}

		char aChar;

		char[] out = convtBuf;

		int outLen = 0;

		int end = off + len;

		while (off < end) {

			aChar = in[off++];

			if (aChar == '\\') {

				aChar = in[off++];

				if (aChar == 'u') {

					// Read the xxxx

					int value = 0;

					for (int i = 0; i < 4; i++) {

						aChar = in[off++];

						switch (aChar) {

							case '0':

							case '1':

							case '2':

							case '3':

							case '4':

							case '5':

							case '6':

							case '7':

							case '8':

							case '9':

								value = (value << 4) + aChar - '0';

								break;

							case 'a':

							case 'b':

							case 'c':

							case 'd':

							case 'e':

							case 'f':

								value = (value << 4) + 10 + aChar - 'a';

								break;

							case 'A':

							case 'B':

							case 'C':

							case 'D':

							case 'E':

							case 'F':

								value = (value << 4) + 10 + aChar - 'A';

								break;

							default:

								throw new IllegalArgumentException(

										"Malformed \\uxxxx encoding.");

						}

					}

					out[outLen++] = (char) value;

				} else {

					if (aChar == 't') {

						aChar = '\t';

					} else if (aChar == 'r') {

						aChar = '\r';

					} else if (aChar == 'n') {

						aChar = '\n';

					} else if (aChar == 'f') {

						aChar = '\f';

					}

					out[outLen++] = aChar;

				}

			} else {

				out[outLen++] = (char) aChar;

			}

		}

		return new String(out, 0, outLen);

	}

	public static String removeSpace(String content) {
		String unicode = toUnicode(content);
		unicode = unicode.replaceAll("\\\\u0020", "");
		unicode = unicode.replaceAll("\\\\u3000", "");
		unicode = unicode.replaceAll("\\\\u00a0", "");
		return fromUnicode(unicode);
	}

	/**
	 * 将字符串转成unicode
	 *
	 * @param str 待转字符串
	 * @return unicode字符串
	 */
	public static String toUnicode(String str) {
		str = (str == null ? "" : str);
		String tmp;
		StringBuffer sb = new StringBuffer(1000);
		char c;
		int i, j;
		sb.setLength(0);
		for (i = 0; i < str.length(); i++) {
			c = str.charAt(i);
			sb.append("\\u");
			j = (c >>> 8); // 取出高8位
			tmp = Integer.toHexString(j);
			if (tmp.length() == 1) {
				sb.append("0");
			}
			sb.append(tmp);
			j = (c & 0xFF); // 取出低8位
			tmp = Integer.toHexString(j);
			if (tmp.length() == 1) {
				sb.append("0");
			}
			sb.append(tmp);

		}
		return (new String(sb));
	}

	/**
	 * 转换为16进制字符串
	 *
	 * @param str
	 * @return String
	 */
	public static String toHexString(String str) {
		if (str.length() <= 0) {
			return "";
		}

		String hexString = "0123456789ABCDEF ";
		// 根据默认编码获取字节数组
		byte[] bytes = str.getBytes();
		StringBuilder sb = new StringBuilder(bytes.length * 2);
		// 将字节数组中每个字节拆解成2位16进制整数
		for (int i = 0; i < bytes.length; i++) {
			sb.append(hexString.charAt((bytes[i] & 0xf0) >> 4));
			sb.append(hexString.charAt((bytes[i] & 0x0f) >> 0));
		}
		return sb.toString();

	}

	/**
	 * 取一个整数的指定百分比的整数
	 *
	 * @param max
	 * @param perc
	 * @return Integer
	 */
	public static Integer findPerc(Integer max, Integer perc) {
		Double a = max * (perc * 0.01);
		long mathRes = Math.round(a);
		int res = (int)mathRes;
		return res;
	}

	public static Double formartDecimalToDouble(Double d) {
		return new Double(formartDecimalToStr(d));
	}

	public static String formartDecimalToStr(Double d) {
		return formartDecimalToStr(d, "0.00");
	}

	public static String formartDecimalToStr(Float d) {
		return formartDecimalToStr(d.doubleValue(), "0.00");
	}

	public static Double formartDecimalToDouble(Double d, String formart) {
		return new Double(formartDecimalToStr(d, formart));
	}

	public static String formartDecimalToStr(Double d, String formart) {
		DecimalFormat decimalFormat = new DecimalFormat(formart);
		String resultStr = decimalFormat.format(d);
		return resultStr;
	}

	public static Integer[] strToInt(String[] str) {
		List<Integer> l = new ArrayList<Integer>();
		for (String e : str) {
			l.add(new Integer(e));
		}
		return (Integer[]) l.toArray(new Integer[0]);
	}

	public static String[] intToStr(String[] in) {
		List<String> l = new ArrayList<String>();
		for (String e : in) {
			l.add(new Integer(e).toString());
		}
		return (String[]) l.toArray(new String[0]);
	}

	public static int getDiffMinute(Date td1, Date td2) throws Exception {
		long lBeginTime = td1.getTime();
		long lEndTime = td2.getTime();

		int iminte = (int) ((lEndTime - lBeginTime) / (60 * 1000));
		return iminte;
	}

	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(
			Map<K, V> map) {
		List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(
				map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
			@Override
			public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});

		Map<K, V> result = new LinkedHashMap<K, V>();
		for (Map.Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

	private static String truncateUrlPage(String strURL) {
		String strAllParam = null;
		String[] arrSplit = null;

		strURL = strURL.trim().toLowerCase();

		arrSplit = strURL.split("[?]");
		if (strURL.length() > 1) {
			if (arrSplit.length > 1) {
				if (arrSplit[1] != null) {
					strAllParam = arrSplit[1];
				}
			}
		}

		return strAllParam;
	}

	/**
	 * 获取一个URL后的所有参数及值，k,v格式
	 *
	 * @param url
	 * @return Map<String, String>
	 */
	public static Map<String, String> getRequestParameters(String url) {
		Map<String, String> mapRequest = new HashMap<String, String>();

		String[] arrSplit = null;

		String strUrlParam = truncateUrlPage(url);
		if (strUrlParam == null) {
			return mapRequest;
		}
		arrSplit = strUrlParam.split("[&]");
		for (String strSplit : arrSplit) {
			String[] arrSplitEqual = null;
			arrSplitEqual = strSplit.split("[=]");
			if (arrSplitEqual.length > 1) {
				mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);
			} else {
				if (arrSplitEqual[0]!=null && !"".equals(arrSplitEqual[0])) {
					// 只有参数没有值，不加入
					mapRequest.put(arrSplitEqual[0], "");
				}
			}
		}
		return mapRequest;
	}

	/**
	 * 获取一个URL后的指定参数的值
	 *
	 * @param url
	 * @param key
	 * @return String
	 */
	public static String getRequestValue(String url, String key) {
		Map<String, String> m = getRequestParameters(url);
		if (m == null || !m.containsKey(key)) {
			return "";
		}
		return m.get(key).toString();
	}

	private static String IDCOARD_PATTERN = "(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])";

	/**
	 * 判定是否为有效的身份证
	 *
	 * @param idcard
	 * @return boolean
	 */
	public static boolean isIdcard(String idcard) {
		Pattern p = null; // 正则表达??
		Matcher m = null; // 操作的字符串
		p = Pattern.compile(IDCOARD_PATTERN);// 匹配移动手机号码
		m = p.matcher(idcard);
		if (m.matches()) {
			return true;
		}
		return false;
	}

	/**
	 * 时间段合法性检测,规则：新的时间段不能和原来的时间段有交集
	 *
	 * @param oldStartTime
	 * @param oldEndTime
	 * @param newStartTime
	 * @param newEndTime
	 * @return boolean
	 */
	public static boolean checkTimeArea(Long oldStartTime, Long oldEndTime,
										Long newStartTime, Long newEndTime) {
		if (newStartTime >= oldStartTime && newStartTime <= oldEndTime) {
			return false;
		}
		if (newEndTime >= oldStartTime && newEndTime <= oldEndTime) {
			return false;
		}
		return true;
	}

	/**
	 * 转成加引号的字符串
	 *
	 * @param array
	 * @return String
	 */
	public static String arrayToStringVarchar(Object[] array) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < array.length; i++) {
			sb.append("'").append(array[i]).append("'");
			if (i < array.length - 1) {
				sb.append(",");
			}
		}
		return sb.toString();
	}

	/**
	 * 转成不加引号的字符串
	 *
	 * @param array
	 * @return String
	 */
	public static String arrayToStringNumber(Object[] array) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < array.length; i++) {
			sb.append(array[i]);
			if (i < array.length - 1) {
				sb.append(",");
			}
		}
		return sb.toString();
	}

	private static String[] getLst(String content) {
		List<String> res = new ArrayList<>();
		int s = 0;
		int e = 1;
		for (int i = 0; i < content.length(); i++) {
			String temp = content.substring(s, e);
			res.add(temp);
			s++;
			e++;
		}
		return res.toArray(new String[0]);
	}

	/**
	 * 获得主机IP
	 *
	 * @return String
	 */
	public static boolean isWindowsOS() {
		boolean isWindowsOS = false;
		String osName = System.getProperty("os.name");
		if (osName.toLowerCase().indexOf("windows") > -1) {
			isWindowsOS = true;
		}
		return isWindowsOS;
	}

	/**
	 * 获取本机ip地址，并自动区分Windows还是linux操作系统，能过滤指定IP段，多网卡情况，例如传入：192.168这样的
	 *
	 * @param filter 空的话就是取第一个
	 * @return String
	 */
	public static String getLocalIP(Set<String> filter) {
		String sIP = "";
		InetAddress ip = null;
		try {
			// 如果是Windows操作系统
			if (isWindowsOS()) {
				ip = InetAddress.getLocalHost();
			}
			// 如果是Linux操作系统
			else {
				boolean bFindIP = false;
				Enumeration<NetworkInterface> netInterfaces = (Enumeration<NetworkInterface>) NetworkInterface
						.getNetworkInterfaces();
				while (netInterfaces.hasMoreElements()) {
					if (bFindIP) {
						break;
					}
					NetworkInterface ni = (NetworkInterface) netInterfaces
							.nextElement();
					// ----------特定情况，可以考虑用ni.getName判断
					// 遍历所有ip
					Enumeration<InetAddress> ips = ni.getInetAddresses();
					while (ips.hasMoreElements()) {
						ip = (InetAddress) ips.nextElement();

						if (StringHelper.iskIP(ip.getHostAddress())) {
							// 检查过滤
							boolean isfilter = false;
							if (filter != null) {
								Iterator<String> it = filter.iterator();
								while (it.hasNext()) {
									String ft2 = it.next();
									if (ip.getHostAddress().startsWith(ft2)) {
										if (ip.getHostAddress().startsWith(ft2)) {
											isfilter = true;
											continue;
										}
									} else {
										bFindIP = false;
										break;
									}
								}
							}
							if (!isfilter) {
								bFindIP = true;
								break;
							}
						}

					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (null != ip) {
			sIP = ip.getHostAddress();
		}
		return sIP;
	}

	/**
	 * 获取本机ip地址，并自动区分Windows还是linux操作系统
	 *
	 * @return String
	 */
	public static String getLocalIP() {
		return getLocalIP(null);
	}

	/**
	 * 驼峰命名法工具
	 *
	 * @return toCamelCase(" hello_world ") == "helloWorld"
	 * toCapitalizeCamelCase("hello_world") == "HelloWorld"
	 * toUnderScoreCase("helloWorld") = "hello_world"
	 */
	public static String toCamelCase(String s) {
		if (s == null) {
			return null;
		}

		s = s.toLowerCase();

		StringBuilder sb = new StringBuilder(s.length());
		boolean upperCase = false;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);

			if (c == SEPARATOR) {
				upperCase = true;
			} else if (upperCase) {
				sb.append(Character.toUpperCase(c));
				upperCase = false;
			} else {
				sb.append(c);
			}
		}

		return sb.toString();
	}

	/**
	 * 驼峰命名法工具
	 *
	 * @return toCamelCase(" hello_world ") == "helloWorld"
	 * toCapitalizeCamelCase("hello_world") == "HelloWorld"
	 * toUnderScoreCase("helloWorld") = "hello_world"
	 */
	public static String toUnderScoreCase(String s) {
		if (s == null) {
			return null;
		}

		StringBuilder sb = new StringBuilder();
		boolean upperCase = false;
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);

			boolean nextUpperCase = true;

			if (i < (s.length() - 1)) {
				nextUpperCase = Character.isUpperCase(s.charAt(i + 1));
			}

			if ((i > 0) && Character.isUpperCase(c)) {
				if (!upperCase || !nextUpperCase) {
					sb.append(SEPARATOR);
				}
				upperCase = true;
			} else {
				upperCase = false;
			}

			sb.append(Character.toLowerCase(c));
		}

		return sb.toString();
	}

	public static Date stringTodate(String date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.parse(date);
	}

	/**
	 * 处理模板占位符替换
	 *
	 * @param content
	 * @param target
	 * @param parameter
	 * @return String
	 */
	public static String processTemp(String content, String target,
									 List<String> parameter) {
		String temp = content;
		int s = 0;
		int e = 0;
		int l = target.length();
		List<String> tl = parameter;
		if (!content.contains(target) || parameter == null
				|| parameter.size() <= 0) {
			return content;
		}
		s = content.indexOf(target);
		e = s + l;
		if (s == 0) {
			temp = tl.get(0) + temp.substring(e);
			tl.remove(0);
		} else {
			temp = temp.substring(0, s) + tl.get(0) + temp.substring(e);
			tl.remove(0);
		}
		if (parameter.size() > 0) {
			return processTemp(temp, target, tl);
		}
		return temp;
	}

	/**
	 * 获取某年第一天日期
	 * @return Date
	 */
	public static String getYearFirst(String year){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar currCal=Calendar.getInstance();
		int currentYear = currCal.get(Calendar.YEAR);
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, Integer.parseInt(year));
		Date currYearFirst = calendar.getTime();
		return sdf.format(currYearFirst);
	}

	/**
	 * 获取某年最后一天日期
	 * @return Date
	 */
	public static String getYearLast(String year){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR,  Integer.parseInt(year));
		calendar.roll(Calendar.DAY_OF_YEAR, -1);
		Date currYearLast = calendar.getTime();
		return sdf.format(currYearLast);
	}
	/**
	 * 获取当月开始时间戳
	 * @return
	 */
	public static String getMinMonthDate(String year, String month) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		YearMonth yearMonth = YearMonth.of(Integer.parseInt(year),Integer.parseInt( month));
		LocalDate localDate = yearMonth.atDay(1);
		LocalDateTime startOfDay = localDate.atStartOfDay();
		ZonedDateTime zonedDateTime = startOfDay.atZone(ZoneId.of("Asia/Shanghai"));
		return sdf.format(Date.from(zonedDateTime.toInstant()));
	}
	public static String getMaxMonthDate(String year, String month) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		YearMonth yearMonth = YearMonth.of(Integer.parseInt(year), Integer.parseInt(month));
		LocalDate endOfMonth = yearMonth.atEndOfMonth();
		LocalDateTime localDateTime = endOfMonth.atTime(23, 59, 59, 999);
		ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of("Asia/Shanghai"));
		return sdf.format(Date.from(zonedDateTime.toInstant()));
	}
}
