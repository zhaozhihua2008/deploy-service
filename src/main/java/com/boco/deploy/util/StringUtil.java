package com.boco.deploy.util;


import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.zip.CRC32;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class StringUtil {
	private static Log log = LogFactory.getLog(StringUtil.class);

	private static String DATE_FORMAT="yyyy-MM-dd HH:mm:ss";
	
	public static String getCurrentTime() {
		Date date = new Date();
		SimpleDateFormat sf = new SimpleDateFormat(DATE_FORMAT);
		String str = sf.format(date);
		return str;
	}
	
	public static String getTime(long time) {
		Date date = new Date(time);
		SimpleDateFormat sf = new SimpleDateFormat(DATE_FORMAT);
		String sdate = sf.format(date);
		return sdate;
	}

	public static String getTimeStamp() {
		Date date = new Date();
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String sdate = sf.format(date);
		return sdate;
	}
	/**
	 * String convert to Date
	 */
	public static Date String2Date(String str) {
		return String2Date(str, DATE_FORMAT);
	}
	
	/**
	 * String convert to Date
	 */
	public static Date String2Date(String str,String formatStr) {
		if (StringUtils.isBlank(str)||str.equals("null")) {
			return null;
		}
		SimpleDateFormat sf = new SimpleDateFormat(formatStr);
		try {
			Date date = sf.parse(str);
			return date;
		} catch (ParseException e) {
			log.error("string[" + str + "] convert to Date,use default value null", e);
			return null;
		}
	}
	

	/**
	 * String convert to Timestamp
	 */
	public static Timestamp String2Timestamp(String str) {
		long l = String2Long(str);
		Timestamp t = new Timestamp(l);
		return t;
	}

	/**
	 * String convert to Integer
	 */
	public static Integer String2Integer(String str, Integer def) {
		if (StringUtils.isBlank(str)||str.equals("null")) {
			return def;
		}
		Integer i = 0;
		try {
			i = Integer.parseInt(str);
		} catch (NumberFormatException e) {
//			log.error("string[" + str + "] convert to Integer,use default value " + def, e);
			i = def;
		}
		return i;
	}

	public static Integer String2Integer(String str) {
		return String2Integer(str, null);
	}

	/**
	 * String convert to long
	 */
	public static Long String2Long(String str, Long def) {
		if (StringUtils.isBlank(str)||str.equals("null")) {
			return def;
		}
		Long i = null;
		try {
			if (str.contains("E")) {
				i=Double.valueOf(str).longValue();
			}else {				
				i = Long.parseLong(str);
			}
		} catch (NumberFormatException e) {
//			log.error("string[" + str + "] convert to Long,use default value" + def, e);
			i = def;
		}
		return i;
	}

	public static Long String2Long(String str) {
		return String2Long(str,null);
	}
	
	/**
	 * String convert to Double
	 */
	public static Double String2Double(String str, Double def) {
		if (StringUtils.isBlank(str)||str.equals("null")) {
			return def;
		}
		Double i = null;
		try {
			i = Double.parseDouble(str);
		} catch (NumberFormatException e) {
			log.error("string[" + str + "] convert to Double,use default value" + def, e);
			i = def;
		}
		return i;
	}

	public static Double String2Double(String str) {
		return String2Double(str,null);
	}
	
	/**
	 * String convert to Boolean
	 */
	public static Boolean String2Boolean(String str, Boolean def) {
		if (StringUtils.isBlank(str)||str.equals("null")) {
			return def;
		}
		Boolean i = null;
		try {
			i = Boolean.parseBoolean(str);
		} catch (NumberFormatException e) {
			log.error("string[" + str + "] convert to Boolean,use default value" + def, e);
			i = def;
		}
		return i;
	}

	public static Boolean String2Boolean(String str) {
		return String2Boolean(str,null);
	}
	

	public static String Date2String(Date date) {
		return Date2String(date,DATE_FORMAT);
	}

	public static String Date2String(Date date,String formatStr) {
		SimpleDateFormat sf = new SimpleDateFormat(formatStr);
		String sdate = sf.format(date);
		return sdate;
	}

	public static InputStream String2Stream(String str) {
		ByteArrayInputStream stream = new ByteArrayInputStream(str.getBytes());
		return stream;
	}

	public static String Stream2String(InputStream is) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		StringBuffer buffer = new StringBuffer();
		String line = "";
		while ((line = in.readLine()) != null) {
			buffer.append(line);
		}
		return buffer.toString();
	}
	
	public static int crc32(String data){
		CRC32 crc32 = new CRC32();
		crc32.update(data.getBytes());
		int value=(int)crc32.getValue();
		return value;
	}
	
	public static long crc64(String data){
		CRC32 crc32 = new CRC32();
		crc32.update(data.getBytes());
		long value=crc32.getValue();
		return value;
	}
	
	public static List<Long> string2Longs(String str,String regex) throws NumberFormatException {
		if (StringUtils.isBlank(str)) {
			return null;
		}
		String[] strs = str.split(regex);
		List<Long> list=new ArrayList<>();
		for(int i = 0; i < strs.length; i++) {
			Long value = StringUtil.String2Long(strs[i].trim(), null);
			if (value!=null) {	
				list.add(value);
			}
		}
		return list;
	}

	public static List<Integer> string2Integers(String str,String regex) throws NumberFormatException {
		if (StringUtils.isBlank(str)) {
			return null;
		}
		String[] strs = str.split(regex);
		List<Integer> list=new ArrayList<>();
		for(int i = 0; i < strs.length; i++) {
			Integer value = StringUtil.String2Integer(strs[i].trim(), null);
			if (value!=null) {	
				list.add(value);
			}
		}
		return list;
	}
	
	public static String getCamelCaseName(String name){
		if (StringUtils.isBlank(name)) {
			return name;
		}
		String firstLetter=name.substring(0, 1).toLowerCase();
		String newName=firstLetter.concat(name.substring(1));
		return newName;
	}

	public static String getUUID(){
		return UUID.randomUUID().toString();
	}
}
