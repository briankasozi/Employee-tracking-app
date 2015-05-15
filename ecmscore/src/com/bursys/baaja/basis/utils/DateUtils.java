package com.bursys.baaja.basis.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.bursys.baaja.basis.constants.MiscConstants;

/**
 * @author JosephBrian
 */
public class DateUtils {
	private final static BaajaConfig config = BaajaConfig.getInstance();
	public final static SimpleDateFormat timestampFormat = new SimpleDateFormat(config.getAppProperty(MiscConstants.TIMESTAMP_FORMAT));
	public final static String dateFormatStr = "yyyy-MM-dd HH:mm:ss";
	public final static SimpleDateFormat dateFormat = new SimpleDateFormat(config.getAppProperty(MiscConstants.DATE_FORMAT));
	public final static SimpleDateFormat dateFormatMySql = new SimpleDateFormat("yyyy-MM-dd");
	public final static SimpleDateFormat dateFormatMailDat = new SimpleDateFormat("yyyyMMdd");
	public final static SimpleDateFormat dateFormatMailDatHHMM = new SimpleDateFormat("HH:MM");
	public final static SimpleDateFormat dateFormatMaxiSortFileNameHHMM = new SimpleDateFormat("HH:MM");	
	public final static SimpleDateFormat timestampDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
	public final static SimpleDateFormat dateFormatSt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


	/**
	 * formats a timestamp to string using this format MM/dd/yyyy HH:mm:ss
	 * 
	 * @param timestamp
	 * @return
	 */
	public static String formatTimestamp(Date timestamp) {
		String s = "";
		if (timestamp != null) {
			s = timestampFormat.format(timestamp);
		}
		return s;
	}

	/**
	 * this will parse the timestamp back to a Date, this expects the format
	 * MM/dd/yyyy HH:mm:ss and if the format is not correct it will return a
	 * null
	 * 
	 * @param s
	 * @return
	 */
	public static Date parseTimestamp(String s) {
		Date d = null;
		if ((s != null) && (!s.equals(""))) {
			try {
				d = timestampFormat.parse(s);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return d;
	}

	/**
	 * this will format the Date to a string using teh format MM/dd/yyyy
	 * 
	 * @param timestamp
	 * @return
	 */
	public static String formatDate(Date timestamp) {
		String s = "";
		if (timestamp != null) {
			s = dateFormat.format(timestamp);
		}
		return s;
	}

	/**
	 * this will parse a string and return a date back. This expects the format
	 * of a string to be MM/dd/yyyy. If the format is incorrect then it will
	 * return a null.
	 * 
	 * @param s
	 * @return
	 */
	public static Date parseDate(String s) {
		Date d = null;
		if ((s != null) && (!s.equals(""))) {
			try {
				d = dateFormat.parse(s);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return d;
	}
	

	public static String formatDateMySql(String s) {
		Date d = parseDate(s);
		String retStr;
		retStr = dateFormatMySql.format(d);
		return retStr;
	}
	
	/**
	 * this will parse a string and return a date back. This expects the format
	 * of a string to be yyyy-MM-dd hh:mm:ss.SSS. If the format is incorrect then it will
	 * return a null.
	 * 
	 * @param s
	 * @return
	 */
	public static Date timestampParseDate(String s) {
		Date d = null;
		if ((s != null) && (!s.equals(""))) {
			try {
				d = timestampDateFormat.parse(s);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return d;
	}
	
	public static String timestampFormatDate(String s) {
		Date d = timestampParseDate(s);
		String retStr;
		retStr = dateFormatMySql.format(d);
		return retStr;
	}

	public static String formatDateMailDat(Date timestamp) {
		String s = "";
		if (timestamp != null) {
			s = dateFormatMailDat.format(timestamp);
		}
		return s;
	}

	public static String formatDateMailDatHHMM(Date timestamp) {
		String s = "";
		if (timestamp != null) {
			s = dateFormatMailDatHHMM.format(timestamp);
		}
		return s;
	}

	public static String getPreviousDate() {
		int MILLIS_IN_DAY = 7000 * 60 * 60 * 24;
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatStr);
		String prevDate = dateFormat.format(date.getTime() - MILLIS_IN_DAY);
		return prevDate;
	}

	public static String getDateString(String format, Date date) {
		String dateOut;
		java.text.DateFormat dateFormatter;
		dateFormatter = new java.text.SimpleDateFormat(format);
		dateOut = dateFormatter.format(date);
		return dateOut;
	}

	public static String dateFormatMaxiSortFileNameHHMM(Date timestamp) {
		String s = "";
		if (timestamp != null) {
			s = dateFormatMaxiSortFileNameHHMM.format(timestamp);
		}
		String s1 = s.replaceAll(":", "");
		return s1;
	}

	/**
	 * this will parse a string and return a date back. This expects the format
	 * of a string to be MM/dd/yyyy. If the format is incorrect then it will
	 * return a null.
	 * 
	 * @param date
	 * @return
	 */
	public static Date parseDate(String date, String format) {
		Date d = null;
		java.text.DateFormat dateFormatter;
		dateFormatter = new java.text.SimpleDateFormat(format);
		if ((date != null) && (!date.equals(""))) {
			try {
				d = dateFormatter.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return d;
	}

	/**
	 * this function returns a string in the MYSQL date format. You can specify
	 * a from format to make date of a string
	 * 
	 * @param s
	 * @param fromFormat
	 * @return
	 */
	public static String formatDateMySql(String s, String fromFormat) {
		Date d = parseDate(s, fromFormat);
		String retStr;
		retStr = dateFormatMySql.format(d);
		return retStr;
	}

	/**
	 * this function returns a string in the 'to date format' from the 'from
	 * date format'. You can specify a from format to make date of a string
	 * 
	 * @param s
	 * @param fromFormat
	 * @return
	 */
	public static String convertDateFormat(String s, String fromFormat, String toFormat) {
		Date d = parseDate(s, fromFormat);
		String retStr;
		SimpleDateFormat dateFormatMySql = new SimpleDateFormat(toFormat);
		retStr = dateFormatMySql.format(d);
		return retStr;
	}
	
	/**
	 * this function returns the to string of the SQL Date
	 * @param date
	 * @param toFormat
	 * @return
	 */
	public static String convertSQLDatetoFormat(java.sql.Date date){
		Date d = new Date(date.getTime());
		SimpleDateFormat dateFormatSql = new SimpleDateFormat(dateFormatStr);
		return dateFormatSql.format(d);
	}

	public static String convertSQLTimeStamptoFormat(Timestamp timestamp) {
		Date d = new Date(timestamp.getTime());
		SimpleDateFormat dateFormatSql = new SimpleDateFormat(dateFormatStr);
		return dateFormatSql.format(d);
	}
	
	/**
	 * this will parse the timestamp back to a Date, this expects the format
	 * MM/dd/yyyy HH:mm:ss and if the format is not correct it will return a
	 * null
	 * 
	 * @param s
	 * @return
	 */
	public static Date parseStrToTimeSt(String s) {
		Date d = null;
		if ((s != null) && (!s.equals(""))) {
			try {
				d = dateFormatSt.parse(s);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return d;
	}
	
	public static String convertStrDateToDate(String date) {
		try {
			SimpleDateFormat sdfSource = new SimpleDateFormat("MM/dd/yyyy");
			Date date1 = sdfSource.parse(date);
			SimpleDateFormat sdfDestination = new SimpleDateFormat("yyyyMMdd");
			date = sdfDestination.format(date1);
		} catch (ParseException pe) {
			System.out.println("Parse Exception : " + pe);
		}
		return date;
	}
	
	public static String getPreviousDate(int day) {
		int MILLIS_IN_DAY = day * 1000 * 60 * 60 * 24;
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatStr);
		String prevDate = dateFormat.format(date.getTime() - MILLIS_IN_DAY);
		return prevDate;
	}
	
	public static String formatDateSql(Date timestamp) {
		String s = "";
		if (timestamp != null) {
			s = dateFormatMySql.format(timestamp);
		}
		return s;
	}
}
