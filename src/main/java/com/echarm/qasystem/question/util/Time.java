package com.echarm.qasystem.question.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Time {

	public Time() {
		// TODO Auto-generated constructor stub
	}
	
	public static String getCurrentTimeStr() {
		TimeZone tz = TimeZone.getTimeZone(DatabaseParameters.DB_TIMEZONE);
	    DateFormat df = new SimpleDateFormat(DatabaseParameters.DB_TIME_FORMAT);
	    df.setTimeZone(tz);
	    String nowAsISO = df.format(new Date());
	    
	    return nowAsISO;
	}
	
	// used for generate question_id
	public static String getCurrentTimeMillisStr() {
		return Long.toString(System.currentTimeMillis());
	}

}
