package com.jscxrz.zhddkk.util;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

public class CommonUtil 
{
	/**
	 * 获取xml中某个节点中的值
	 * 
	 * @param xmlStr
	 * @param name
	 * @return
	 */
	public static String getVarFromXml(String xmlStr, String name)
	{
		String result = null;
		
		if (xmlStr.contains(name))
		{
			String leftStr = "<" + name + ">";
			String rightStr = "</" + name + ">";
			result = xmlStr.split(leftStr)[1].split(rightStr)[0].trim();
		}

		return result;
	}
	
	/**
	 * 判断一个字符串是否为空
	 * true:空  false:非空
	 * 
	 * @param str
	 * @return boolean
	 */
	public static boolean validateEmpty(String str)
	{
		if (null == str || str.equals(""))
		{
			return true;
		}
		
		return false;
	}
	
	/**
	 * 转义xml中的特殊字符
	 * 
	 * @param str
	 * @return
	 */
	public static String changeStrToXml(String str)
	{
		Map<String, String> map = new HashMap<String, String>();
		map.put("&lt;", "<");
		map.put("&gt;", ">");
		map.put("&amp;", "&");
		
		for (Entry<String, String> entry : map.entrySet())
		{
			str = str.replaceAll(entry.getKey(), entry.getValue());
		}
		
		if (str.contains("&amp;"))
		{
			str = str.replaceAll("&amp;", map.get("&amp;"));
		}
		
		return str;
	}
	
	/**
	 * 获取前几个小时时间
	 * 
	 * @param curDate 当前时间
	 * @param hour   前几个小时
	 * 
	 * @return
	 */
	public static Date queryPreHour(Date curDate, int hour)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(curDate);
		
		c.add(Calendar.HOUR, -hour);
		
		Date d = c.getTime();
		
		return d;
	}
	
	/**
	 * 获取前几分钟时间
	 * 
	 * @param curDate 当前时间
	 * @param minutes 前几分钟
	 * @return
	 */
	public static Date queryPreMinutes(Date curDate, int minutes)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(curDate);
		
		c.add(Calendar.MINUTE, -minutes);
		
		Date d = c.getTime();
		
		return d;
	}
	
	/**
	 * 获取2个时间的间隔  单位:分钟
	 * 注意:date1比date2大
	 * 
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static long getTwoDateInterval(Date date1, Date date2)
	{
		long misec = date1.getTime()-date2.getTime();
		long sec = misec/1000/60;

		return sec;
	}
	
	/**
	 * 随机获取两个整型数字之间的数
	 * 起始数字要大于结束数字
	 * 
	 * @param start 起始数字
	 * @param end  结束数字
	 * @return
	 */
	public static int randomNumber(int start,int end)
	{
		int result = 0;
		if (end > start)
		{
			int interval = end-start;
			Random rand = new Random();
		
			result = rand.nextInt(interval)+start;
		}
		
		return result;
	}
	
	/**
	 * 随机获取两个double数字之间的数
	 * 起始数字要大于结束数字
	 * 
	 * @param start 起始数字
	 * @param end  结束数字
	 * @return
	 */
	public static double randomDouble(double d1,double d2)
	{
		double result = 0d;
		if (d2 > d1)
		{
			result = Math.random()*(d2-d1) + d1;
		}
		
		return result;
	}
	
	/**
	 * 让double型保留几位小数
	 * 
	 * @param d
	 * @param scale
	 * @return
	 */
	public static double changeDouble(double d, int scale)
	{
		double result = 0d;
		BigDecimal bd = new BigDecimal(d);
		result = bd.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
		
		return result;
	}
	
	/**
	 * 比较2个string是否一样
	 * true:一样
	 * false:不一样
	 * 
	 * @param s1
	 * @param s2
	 */
	public static boolean compareToObj(String s1, String s2)
	{
		boolean res = false;
		if (s1 == null && s2 == null)
		{
			res = true;
		}
		else if (s1 != null && s2 != null)
		{
			if (s1.equals(s2))
			{
				res = true;
			}
		}
		
		return res;
	}
}
