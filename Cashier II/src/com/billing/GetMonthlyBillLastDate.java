package com.billing;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Wellington
 */

public class GetMonthlyBillLastDate {
	ResultSet rs;
	Statement stm;

	public GetMonthlyBillLastDate(ResultSet rs, Statement stm) {
		this.rs = rs;
		this.stm = stm;
	}

	public String getDate() {
		String date = null;
		String query = "SELECT date FROM monthly_bill";
		try {
			rs = stm.executeQuery(query);
			if (rs.last()) {
				date = rs.getString(1);
			} else {
				date = "";
			}
		} catch (SQLException ee) {
			ee.printStackTrace(System.err);
		}
		return date;
	}

}
