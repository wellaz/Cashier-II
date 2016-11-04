package com.billing;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Wellington
 */

public class GetDailyBillLastDate {
	ResultSet rs;
	Statement stm;

	public GetDailyBillLastDate(ResultSet rs, Statement stm) {
		this.rs = rs;
		this.stm = stm;
	}

	public String getDate() {
		String date = null;
		String query = "SELECT date FROM daily_bill";
		try {
			rs = stm.executeQuery(query);
			if (rs.last()) {
				date = rs.getString(1);
			} else {
				date = "null";
			}
		} catch (SQLException ee) {
			ee.printStackTrace(System.err);
		}
		return date;
	}

	public Date returnDate() {
		String start_dt = getDate();
		Date date = null;
		DateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
		try {
			date = (Date) parser.parse(start_dt);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public boolean isValidDate(String date) {
		List<String> imgTypes = null;
		int arrsize = allDates().size();
		String[] dat = new String[arrsize];
		for (int i = 0; i < arrsize; i++)
			dat[i] = allDates().get(i);
		imgTypes = Arrays.asList(dat);
		return imgTypes.stream().anyMatch(t -> date.equals(t));
	}

	public ArrayList<String> allDates() {
		ArrayList<String> data = new ArrayList<>();
		String date = null;
		String query = "SELECT date FROM daily_bill";
		try {
			rs = stm.executeQuery(query);
			if (rs.next()) {
				do {
					date = rs.getString(1);
					data.add(date);
				} while (rs.next());
			}

		} catch (SQLException ee) {
			ee.printStackTrace(System.err);
		}
		return data;
	}
}