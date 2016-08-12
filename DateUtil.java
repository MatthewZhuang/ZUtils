package zhuang.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 
 * @author guomianzhuang
 * 
 */
public class DateUtil {

	/**
	 * @param d
	 *            in format 'yyyy-MM-dd'
	 * @return 星期数
	 * 
	 */
	public static String getWeekOfDate(String d) {
		SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = df2.parse(d);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] weekDaysName = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		return weekDaysName[intWeek];
	}

	public static String exchangeDate(String time) {
		String result = time.split("\\s+")[0];
		int index = -1;
		if ((index = result.indexOf(':')) != -1) {
			result = result.substring(0, index - 2);
		}
		if ((index = result.indexOf('：')) != -1) {
			result = result.substring(0, index - 2);
		}

		if (result.contains("年")) {
			result = result.replaceAll("年", "-");
			result = result.replaceAll("月", "-");
			result = result.replaceAll("日", "");
		} else if (result.contains("/")) {
			result = result.replaceAll("/", "-");
		} else if (result.contains(".")) {
			result = result.replaceAll("\\.", "-");
		}
		return result;
	}

	/**
	 * @return return string of the date with the given format
	 */
	public static String dateToString(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	/**
	 * @return return Date of the dateString with the given format
	 */
	public static Date stringToDate(String dateString, String format) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.parse(dateString);
	}

	/**
	 * @return return Timestamp of the dateString
	 */
	public static Timestamp stringToTimstamp(String dateString) {
		return Timestamp.valueOf(dateString);
	}

	/**
	 * @return return Timestamp of currentTime
	 */
	public static Timestamp getCurrentTimeStamp() {
		return new Timestamp(System.currentTimeMillis());
	}

	/**
	 * @return return dateString of the timestamp with the given format
	 */
	public static String timestampToString(Timestamp timestamp, String formate) {
		SimpleDateFormat df = new SimpleDateFormat(formate);
		return df.format(timestamp);
	}

	/**
	 * @return return string of the date with the given format
	 */
	public static String getFormatDate(String format, Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	/**
	 * @return return dateString of the date with the given format
	 */
	public static String getDateAfterNow(int days, String format) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, days);
		return getFormatDate(format, calendar.getTime());
	}

	/**
	 * @return return the interval of the given date with current time
	 */
	public static int calculateInterval(Date createTime) {
		Calendar calNow = Calendar.getInstance();
		Calendar calCreate = Calendar.getInstance();
		calNow.setTime(new Date());
		calCreate.setTime(createTime);
		long l = calNow.getTimeInMillis() - calCreate.getTimeInMillis();
		int days = new Long(l / (1000 * 60 * 60 * 24)).intValue();
		return days;
	}

	/**
	 * @param number
	 *            : long time
	 * @return return timestamp with the given format
	 */
	public static Timestamp numberStringToTimestamp(String number, String format) {
		if (number == null || number.length() == 0)
			return null;
		SimpleDateFormat df = new SimpleDateFormat(format);
		Date date = new Date(Long.parseLong(number));
		String time = df.format(date);
		Timestamp ts = Timestamp.valueOf(time);
		return ts;
	}

	/**
	 * @param number
	 *            : long time
	 * @return return formated datestring with the given format
	 */
	public static String numberStringToDateString(String number, String format) {
		if (number == null || number.length() == 0)
			return null;
		SimpleDateFormat df = new SimpleDateFormat(format);
		Date date = new Date(Long.parseLong(number));
		return df.format(date);
	}

	/**
	 * @param time
	 *            to transform
	 */
	public static String formatTrans(String time, String fromformat, String toformat) {
		SimpleDateFormat df = new SimpleDateFormat(fromformat);
		Date date;
		try {
			date = df.parse(time);
			SimpleDateFormat sdf = new SimpleDateFormat(toformat);
			return sdf.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @return return time add days
	 */
	public static String dateAfter(int days, String time, String format) {
		SimpleDateFormat df2 = new SimpleDateFormat(format);
		Date date = null;
		try {
			date = df2.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, 1 * days);
		Date newd = calendar.getTime();
		return df2.format(newd);
	}

	/**
	 * @return return time minus days
	 */
	public static String dateBefore(int days, String time, String format) {
		SimpleDateFormat df2 = new SimpleDateFormat(format);
		Date date = null;
		try {
			date = df2.parse(time);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, -1 * days);
		Date newd = calendar.getTime();
		return df2.format(newd);
	}

	/**
	 * @return whether the same day
	 */
	public static boolean isTheSameDate(Timestamp time1, Timestamp time2) {
		if (time1 != null && time2 != null) {
			Calendar c1 = Calendar.getInstance();
			c1.setTime(time1);
			int y1 = c1.get(Calendar.YEAR);
			int m1 = c1.get(Calendar.MONTH);
			int d1 = c1.get(Calendar.DATE);
			Calendar c2 = Calendar.getInstance();
			c2.setTime(time2);
			int y2 = c2.get(Calendar.YEAR);
			int m2 = c2.get(Calendar.MONTH);
			int d2 = c2.get(Calendar.DATE);
			if (y1 == y2 && m1 == m2 && d1 == d2) {
				return true;
			}
		} else {
			if (time1 == null && time2 == null) {
				return true;
			}
		}
		return false;
	}
}
