package net.andrewcpu.payroll.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	public static DateFormat getDateFormat() {
		return new SimpleDateFormat("MM-dd-yyyy");
	}
	public static DateFormat getInDocumentDateFormat() {
		return new SimpleDateFormat("MM/dd/yyyy");
	}

	public static boolean isTodayMonday() {
		return Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY;
	}

	public static Date getLastMonday() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		c.add(Calendar.DATE, -7);
		return (c.getTime());
	}

	public static Date getCurrentMonday() {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return (c.getTime());
	}
}
