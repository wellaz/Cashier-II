/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import com.toedter.calendar.JDateChooser;

import javafx.util.StringConverter;

/**
 *
 * @author Wellington
 */
public class SetDateCreated {

	// this method returns a sort of a timestamp
	public String setDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date dates = new Date();
		String text = dateFormat.format(dates);
		return text;
	}

	public StringConverter<LocalDate> dateForm() {
		StringConverter<LocalDate> dt = new StringConverter<LocalDate>() {
			private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

			@Override
			public String toString(LocalDate t) {
				if (t == null)
					return "";
				return dateTimeFormatter.format(t);
			}

			@Override
			public LocalDate fromString(String string) {
				if (string == null || string.trim().isEmpty()) {
					return null;
				}
				return LocalDate.parse(string, dateTimeFormatter);
			}
		};
		return dt;
	}

	// this method returns the time in hours, seconds and minutes
	public String getTime() {
		Calendar cal = Calendar.getInstance();
		Date date = cal.getTime();
		DateFormat dateFormatter = new SimpleDateFormat("HH:mm:ss");
		return dateFormatter.format(date);
	}

	// returns a date in a specified format
	public String getDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Date date1 = new Date();
		return dateFormat.format(date1);
	}

	public String getDateStrock() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date1 = new Date();
		return dateFormat.format(date1);
	}

	// returns a yesterday's date
	public String getYesterdayDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		return dateFormat.format(cal.getTime());
	}

	// returnas a timestamp
	public String timeStamp() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy HH-mm-ss");
		Date dates = new Date();
		String text = dateFormat.format(dates);
		return text;
	}

	// returns a year
	public String getYear() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, 0);
		return dateFormat.format(cal.getTime());
	}

	// this method returns the thirtieth date from today
	public String getThirtythDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 30);
		return dateFormat.format(cal.getTime());
	}

	// this method returns the current month value as an integer
	public int getMonth() {
		Date date = new Date();
		LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		return localDate.getMonthValue();
	}

	// this month returns a date in a long format
	public String getExactDate(JDateChooser whendate) {
		Date d = whendate.getDate();
		String date = String.format("%1$tY-%1$tm-%1$td", d);
		return date;
	}

	// this method returns a third month from today
	public String getThirdMonth() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 90);
		return dateFormat.format(cal.getTime());
	}

	// this method returns today's date in short as a string.
	public String today() {
		Date date = new Date();
		DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
		return df.format(date);
	}
}
