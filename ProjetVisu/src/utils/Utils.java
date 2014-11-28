package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import exception.MyOutOfBoundException;

public class Utils {
	public static String parseIntWithSize(int value, int size) {
		String result = "" + value;
		if(result.length() > size)
			result = result.substring(result.length() - size);
		for(int i = result.length(); i < size; ++i)
			result = "0" + result;
		return result;
	}
	
	public static Date getDate(int month, int day, int year, int hour, int minute, int second) throws MyOutOfBoundException {
		exception.MyOutOfBoundException.test("day", day, 1, 31);
		exception.MyOutOfBoundException.test("month", month, 1, 12);
		exception.MyOutOfBoundException.test("hour", hour, 0, 23);
		exception.MyOutOfBoundException.test("minute", minute, 0, 59);
		exception.MyOutOfBoundException.test("second", second, 0, 59);
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yy-hh:mm:ss");
		String s = parseIntWithSize(month, 2) + "/" + parseIntWithSize(day, 2) + "/" + parseIntWithSize(year, 4) + "-" + parseIntWithSize(hour, 2) + ":" + parseIntWithSize(minute, 2) + ":" + parseIntWithSize(second, 2);
		Date result = null;
		try {
			result = df.parse(s);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static String getString(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("MM/dd/yy-HH:mm:ss");
		return df.format(date);
	}
	
	public static String getKMLString(Date date) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss;");
		return df.format(date).replace(',', 'T').replace(';', 'Z');
	}
}
