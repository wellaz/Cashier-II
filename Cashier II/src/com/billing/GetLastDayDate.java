package com.billing;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Wellington
 */

public class GetLastDayDate {
	ResultSet rs;
	Statement stm;

	public GetLastDayDate(ResultSet rs, Statement stm) {
		this.rs = rs;
		this.stm = stm;
	}

	public String getLastDateValue() {
		String date = null;
		String query = "SELECT date FROM cashpay";
		try {
			rs = stm.executeQuery(query);
			if (rs.last()) {
				date = rs.getString(1);
			}
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
		return date;
	}
}
