package com.kenick.util;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtils 
{
	private static String dateFormat = "yyyy-MM-dd HH:mm:ss";
	
	/**
	 *  获取 天的凌晨或最后  1 
	 */
	public static final int HEAD_TAIL_DAY = 1;
	/**
	 *  获取 周的凌晨或最后  2
	 */
	public static final int HEAD_TAIL_WEEK = 2;
	/**
	 *  获取 月的凌晨或最后 3
	 */
	public static final int HEAD_TAIL_MONTH = 3;
	/**
	 *  获取 季的凌晨或最后 4
	 */
	public static final int HEAD_TAIL_QUARTER = 4;
	/**
	 *  获取 年的凌晨或最后   5
	 */
	public static final int HEAD_TAIL_YEAR = 5;
	
	public static Date tranToDate(String str, String format)
	{
		if (StringUtils.isBlank(str))
		{
			return null;
		}
		if (StringUtils.isBlank(format))
		{
			format = dateFormat;
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date date = null;
		try {
			date = sdf.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
			//new Date();
		}
		return date;
	}
	
	public static Date tranToDate(String str)
	{
		return tranToDate(str, null);
	}
	
	public static String getNowDateStr()
	{
		return getNowDateStr(0, null);
	}
	
	public static String getNowDateStr(String format)
	{
		return getNowDateStr(0, format);
	}
	
	public static String getNowDateStr(int delay)
	{
		return getNowDateStr(delay, null);
	}

	/**
	 * <一句话功能简述> 根据当前时间，获得 差delay天 format格式的日期（delay可以为负数）
	 * <功能详细描述> 
	 * author: zhanggj
	 * 创建时间:  2017年7月9日
	 */
	public static String getNowDateStr(int delay, String format)
	{
		return getStrDate(getNowDate(delay), format);
	}

	public static Date getNowDate(int delay)
	{
		Calendar lastDate = Calendar.getInstance();
		lastDate.add(Calendar.DATE, delay);
		return lastDate.getTime();
	}

	/**
	 * <一句话功能简述> 获取某一天的0时0分0秒
	 * <功能详细描述>
	 * @param delay 正数为获取以后的时间，负数为获取以前的时间
	 * @return
	 * @see [类、类#方法、类#成员]
	 * @author  liuml
	 * 修改时间:2018年7月5日 下午8:09:42
	 */
	public static Date getZeroClockDate(int delay)
	{
	        Calendar lastDate = Calendar.getInstance();
	        lastDate.add(Calendar.DATE, delay);
	        lastDate.set(Calendar.HOUR_OF_DAY, 0);
	        lastDate.set(Calendar.MINUTE, 0);
	        lastDate.set(Calendar.SECOND, 0);
	        return lastDate.getTime();
	}
	
	/**
	 * <一句话功能简述> 获取多少个小时前/后的时间
	 * <功能详细描述>
	 * @param hours 正数为获取以后的时间，负数为获取以前的时间
	 * @return
	 * @see [类、类#方法、类#成员]
	 * @author  liuml
	 * 修改时间:2018年7月5日 下午8:09:42
	 */
	public static Date getAnyHourDate(int hours)
	{
		Calendar lastDate = Calendar.getInstance();
		lastDate.add(Calendar.HOUR, hours);
		return lastDate.getTime();
	}
	
	/**
	 * <一句话功能简述> 根据时间 获取默认格式时间字符串
	 * <功能详细描述>
	 * author: zhanggj
	 * 创建时间:  2017年7月9日
	 * @param date
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public static String getStrDate(Date date)
	{
		return getStrDate(date, null);
	}
	/**
	 * <一句话功能简述> 根据时间 以及格式，获取时间字符串格式
	 * <功能详细描述>
	 * author: zhanggj
	 * 创建时间:  2017年7月9日
	 * @param date
	 * @param format
	 * @return
	 * @see [类、类#方法、类#成员]
	 */
	public static String getStrDate(Date date, String format)
	{
		if(date == null){
			return "";
		}

		if (StringUtils.isBlank(format))
		{
			format = dateFormat;
		}
		SimpleDateFormat sdf=new SimpleDateFormat(format);
		
		return sdf.format(date);
	}
	
	public static String long2FormDate(Long millSec, String format)
	{
		Date date= new Date(millSec);
		
		return getStrDate(date, format);
	}
	
	public static String long2FormDate(Long millSec)
	{
		return long2FormDate(millSec, null);
	}
	
	//对日期类型做加减法运算
	public static Date dateCalendar(Date date, Integer year, Integer month, Integer day)
	{
		year = null == year ? 0 : year;
		month = null == month ? 0 : month;
		day = null == day ? 0 : day;

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		
		calendar.add(Calendar.YEAR, year);
		calendar.add(Calendar.MONTH, month);
		calendar.add(Calendar.DAY_OF_YEAR, day);
		Date time = calendar.getTime();
		
		return time;
	}
	
	public static Date dateCalendar(Integer year, Integer month, Integer day)
	{
		
		return dateCalendar(new Date(), year, month, day);
	}
	
	public static Date timeCalendar(Date date, Integer hour, Integer minite, Integer second)
	{
		hour = null == hour ? 0 : hour;
		minite = null == minite ? 0 : minite;
		second = null == second ? 0 : second;

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		
		calendar.add(Calendar.HOUR, hour);
		calendar.add(Calendar.MINUTE, minite);
		calendar.add(Calendar.SECOND, second);
		
		Date time = calendar.getTime();
		
		return time;
	}
	public static Date timeCalendar(Integer hour, Integer minite, Integer second)
	{
		return timeCalendar(new Date(), hour, minite, second);
	}
	
	//判断当前时间或某个时间是否在时间区间之内
	public static boolean judgeDate(Date startDate, Date endDate, Date date){
		
		if( date == null){
			date = new Date();
		}
		
		//不在时间范围返回true
		if(date.before(startDate) || date.after(endDate)){
			return true;
		}
		
		//在时间范围返回false
		return false;
	}
	
	//判断是否是闰年
	public static boolean judgeLeapYear(int year){
		
		if(year%4 == 0 && year%100 != 0 || year%400 == 0){ //能被4整除并且不能被100整除，或
			return true;
		}else{
			return false;
		}
	}
	
	//计算2个日期之间差多少天
	public static long towDateDifference(Date date1, Date date2){
		
		long days = (date1.getTime()-date2.getTime())/(1000*60*60*24);
		return days;
	}

	public static int dayDeffernce(Date upDate, Date lowDate) {
		
		long days = (upDate.getTime()-lowDate.getTime())/(1000*60*60*24);
		return (int) days;
	}

	public static int monthDeffernce(Date upDate, Date lowDate) {
		Calendar calUp = Calendar.getInstance();
		calUp.setTime(upDate);
		
		Calendar calLow = Calendar.getInstance();
		calLow.setTime(lowDate);
		if(calUp.getTimeInMillis() < calLow.getTimeInMillis()) return 0;
        int year1 = calUp.get(Calendar.YEAR);
        int year2 = calLow.get(Calendar.YEAR);
        int month1 = calUp.get(Calendar.MONTH);
        int month2 = calLow.get(Calendar.MONTH);
        int day1 = calUp.get(Calendar.DAY_OF_MONTH);
        int day2 = calLow.get(Calendar.DAY_OF_MONTH);
        // 获取年的差值 假设 d1 = 2015-8-16  d2 = 2011-9-30
        int yearInterval = year1 - year2;
        // 如果 d1的 月-日 小于 d2的 月-日 那么 yearInterval-- 这样就得到了相差的年数
        if(month1 < month2 || month1 == month2 && day1 < day2) yearInterval --;
        // 获取月数差值
        int monthInterval =  (month1 + 12) - month2  ;
        if(day1 < day2) monthInterval --;
        monthInterval %= 12;
        return yearInterval * 12 + monthInterval;
	}
    /*** 
     * 日期转换cron表达式
     * convert Date to cron ,eg.  "0 07 10 15 1 ? 2016" 
     * @param date  : 时间点 
     * @return 
     */  
    public static String getCron(Date  date){  
        String dateFormat="ss mm HH dd MM ? yyyy";  
        return getStrDate(date, dateFormat);  
    }
	
    public static Date getHeadDate(int analysSize)
    {
    	return getHeadDate(new Date(), analysSize);
    }

    /**
     * <一句话功能简述> 根据传入日期，获取当时所在的 凌晨 、周一凌晨、月首凌晨、季度凌晨、年首凌晨
     * <功能详细描述>
     * author: user
     * 创建时间:  2018年3月27日
     * @param startDate
     * @param analysSize 1、天凌晨 2、周首凌晨 3、月首凌晨 4、季首凌晨 5、年首凌晨
     * @return
     * @throws ParseException
     * @see [类、类#方法、类#成员]
     */
    public static Date getHeadDate(Date startDate, int analysSize)
	{
    	//开始时间的当天 0刻
		Calendar calDayStart = Calendar.getInstance();
		calDayStart.setTime(startDate);
		calDayStart.set(Calendar.HOUR_OF_DAY, 0);
		calDayStart.set(Calendar.MINUTE, 0);
		calDayStart.set(Calendar.SECOND, 0);
		calDayStart.set(Calendar.MILLISECOND, 0);
		
		switch (analysSize) 
		{
		case 1:
			// 天
			return calDayStart.getTime();

			// 周
		case 2:
			if(calDayStart.isSet(Calendar.SUNDAY)){
				calDayStart.add(Calendar.DAY_OF_WEEK, -1);
			}
			// 开始时间所在周 周一
			calDayStart.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			return calDayStart.getTime();
			
			
			// 月
		case 3:
		// 开始时间所在月第一天
			calDayStart.set(Calendar.DAY_OF_MONTH, 1);
			return calDayStart.getTime();

		// 季
		case 4:
			int firstMonth = calDayStart.get(Calendar.MONTH) / 3 * 3;
			// 设置季度第一月，第一天
			calDayStart.set(calDayStart.get(Calendar.YEAR), firstMonth, 1);
			return calDayStart.getTime();
		
		// 年
		case 5:
			// 开始时间所在年第一天
			calDayStart.set(Calendar.DAY_OF_YEAR, 1);
			return calDayStart.getTime();
		default : return null;
		}
	}
    

    public static Date getTailDate(int analysSize)
    {
    	return getTailDate(new Date(), analysSize);
    }

    /**
     * <一句话功能简述> 根据传入日期，获取当时所在的 天最后一毫秒 、周最后一毫秒、月最后一毫秒、季度最后一毫秒、年首最后一毫秒
     * <功能详细描述>
     * author: user
     * 创建时间:  2018年3月27日
     * @param endDate
     * @param analysSize 1、天尾  2、周尾  3、月尾  4、季尾  5、年尾
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static Date getTailDate(Date endDate, int analysSize)
	{

		//结束时间的当天 最后一毫秒
		Calendar calDayEnd = Calendar.getInstance();
		calDayEnd.setTime(endDate);
		calDayEnd.set(Calendar.HOUR_OF_DAY, 23);
		calDayEnd.set(Calendar.MINUTE, 59);
		calDayEnd.set(Calendar.SECOND, 59);
		calDayEnd.set(Calendar.MILLISECOND, 999);
		
		switch (analysSize) {
		// 天
		case 1:
			return calDayEnd.getTime();
			
			// 周
		case 2:
			if(!calDayEnd.isSet(Calendar.SUNDAY)){
				//// 开始时间所在周日（中国习惯）
				calDayEnd.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
				// 外国人习惯 周日为一周开始，计算当前时间的周日，其实是外国人的下周周日
				calDayEnd.set(Calendar.DATE, calDayEnd.get(Calendar.DATE) + 6);
			}
			return calDayEnd.getTime();

			// 月
		case 3:
			calDayEnd.set(Calendar.DAY_OF_MONTH, 1);
			// 结束时间下一月
			calDayEnd.add(Calendar.MONTH, 1);
			// 结束时间所在月最后天
			calDayEnd.add(Calendar.DATE, -1);
			return calDayEnd.getTime();

			// 季
		case 4:
			// 结束时间所在季度第最后一月
			int endMonth = calDayEnd.get(Calendar.MONTH) / 3 * 3 + 2;
			// 结束时间所在季度最后一月
			calDayEnd.set(Calendar.MONTH, endMonth);

			// 结束时间所在季度最后一月第一天
			calDayEnd.set(Calendar.DAY_OF_MONTH, 1);
			// 结束时间所在季度下一月
			calDayEnd.add(Calendar.MONTH, 1);
			// 结束时间所在季度最后一天
			calDayEnd.add(Calendar.DATE, -1);
			return calDayEnd.getTime();

			// 年
		case 5:
			// 结束时间所在年第一天
			calDayEnd.set(Calendar.DAY_OF_YEAR, 1);
			// 结束时间下一年
			calDayEnd.add(Calendar.YEAR, 1);
			// 结束时间所在年最后天
			calDayEnd.add(Calendar.DATE, -1);
			return calDayEnd.getTime();
			
			default :
				return null;
		}
    }
    
    /**
     * <一句话功能简述>  获取现在的 年月   yyyyMM
     * <功能详细描述>
     * author: user
     * 创建时间:  2019年4月17日
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static int getYearAndMonthNow()
    {
    	return getYearAndMonth(new Date());
    }
    
    public static int getYearAndMonth(Date date)
    {
    	Calendar now = Calendar.getInstance();
    	now.setTime(date);
    	int year = now.get(Calendar.YEAR);
    	int month = now.get(Calendar.MONTH) + 1;
    	String yearAndMonth = String.valueOf(year) + String.format("%02d", month);
    	return Integer.parseInt(yearAndMonth);
    }
    
    public static int getHourMiniteSecondNow()
    {
    	return getHourMiniteSecond(new Date());
    }
    
    public static int getHourMiniteSecond(Date date)
    {
    	Calendar now = Calendar.getInstance();
    	now.setTime(date);
    	int hour = now.get(Calendar.HOUR_OF_DAY);
    	int minite = now.get(Calendar.MINUTE);
    	int second = now.get(Calendar.SECOND);
    	String HMS= String.valueOf(hour) + String.format("%02d", minite) + String.format("%02d", second);
    	return Integer.parseInt(HMS);
    }
    /**
    * <一句话功能简述>  获取现在的 年月日   yyyyMMdd
    * <功能详细描述>
    * author: user
    * 创建时间:  2019年4月17日
    * @return
    * @see [类、类#方法、类#成员]
    */
   public static int getYearMonthDayNow()
   {
   	return getYearMonthDay(new Date());
   }
   
   public static int getYearMonthDay(Date date)
   {
   	Calendar now = Calendar.getInstance();
   	now.setTime(date);
   	int year = now.get(Calendar.YEAR);
   	int month = now.get(Calendar.MONTH) + 1;
   	int day = now.get(Calendar.DAY_OF_MONTH);
   	String yearMonthDay = String.valueOf(year) + String.format("%02d", month)
   	                   + String.format("%02d", day);
   	return Integer.parseInt(yearMonthDay);
   }
   
   public static int getYearMonthDayHourNow() {
	   return getYearMonthDayHour(new Date());
   }
   public static int getYearMonthDayHour(Date date)
   {
   	Calendar now = Calendar.getInstance();
   	now.setTime(date);
   	int year = now.get(Calendar.YEAR);
   	int month = now.get(Calendar.MONTH) + 1;
   	int day = now.get(Calendar.DAY_OF_MONTH);
   	int hour = now.get(Calendar.HOUR_OF_DAY);
   	String yearMonthDay = String.valueOf(year) + String.format("%02d", month)
   	                   + String.format("%02d", day) + String.format("%02d", hour);
   	return Integer.parseInt(yearMonthDay);
   }
   
   /** 
    * 获取过去第几天的日期 含详细时刻
    * 
    * @param past 
    * @return 
    */  
   public static Date getPastDate(int past) {  
       Calendar calendar = Calendar.getInstance();  
       calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
       return calendar.getTime();  
   }
   
   /** 
    * 获取过去第几天的零点时刻
    * 
    * @param past 
    * @return 
    */  
   public static Date getPastDateZero(int past) {  
       Calendar calendar = Calendar.getInstance();  
       calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
       calendar.set(Calendar.HOUR_OF_DAY, 0);
       calendar.set(Calendar.MINUTE, 0);
       calendar.set(Calendar.SECOND, 0);
       calendar.set(Calendar.MILLISECOND, 0);
       return calendar.getTime();  
   }  
   
   public static void main(String[] args) {
		System.out.println(DateUtils.getMonthNum(new Date()));
   }

	// 每隔intervalSecond秒循环一次，判断是否正是第minute倍数分钟
	public static boolean isRightTimeBySecond(Date date, Integer minute, int intervalSecond){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int minuteNow = calendar.get(Calendar.MINUTE);
		int secondNow = calendar.get(Calendar.SECOND);

		if(minute != null){ // 每隔minute分钟
			if(minuteNow % minute == 0 && secondNow < intervalSecond){
				return true;
			}
		}

		return false;
	}

	// 每隔intervalMinute分钟循环一次，判断是否正是第hour倍数小时
	public static boolean isRightTimeByMinute(Date date, Integer hour, int intervalMinute){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int hourNow = calendar.get(Calendar.HOUR_OF_DAY);
		int minuteNow = calendar.get(Calendar.MINUTE);
		System.out.println("hourNow:"+hourNow);

		if(hour != null){ // 每隔minute分钟
			if(hourNow % hour == 0 && minuteNow < intervalMinute){
				return true;
			}
		}

		return false;
	}

	public static int getHour(Date now){
   		if(now == null){
   			now = new Date();
		}

		GregorianCalendar calendar = new GregorianCalendar();
		return calendar.get(Calendar.HOUR_OF_DAY);
	}

	public static int getMinute(Date now){
		if(now == null){
			now = new Date();
		}

		GregorianCalendar calendar = new GregorianCalendar();
		return calendar.get(Calendar.MINUTE);
	}

	public static int getWeekNum(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int weekNum = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		return weekNum == 0 ? 7 : weekNum;
	}

	public static int getMonthNum(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_MONTH);
	}

    public static Date parseStockTime(String timeStr) {
   		Date ret = null;
   		try{
   			if(StringUtils.isBlank(timeStr)){
   				return null;
			}

   			// 旧的时间格式
			if(timeStr.contains(":")){
				ret = tranToDate(timeStr, "yyyy-MM-dd hh:mm:ss");
				return ret;
			}

			timeStr = timeStr.split(" ")[0];
			ret = tranToDate(timeStr, "yyyyMMddhhmmss");
		}catch (Exception e){

		}
   		return ret;
    }

}
