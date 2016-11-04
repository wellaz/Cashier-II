package com.billing;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Wellington
 */

public class GetDailyBillingFee {
	ResultSet rs;
	Statement stm;
	private double fee;
	private String date, time, narration;

	public GetDailyBillingFee(ResultSet rs, Statement stm) {
		this.rs = rs;
		this.stm = stm;
		// getData();
	}

	public void getData() {
		String query = "SELECT * FROM daily_billing_settings";
		try {
			rs = stm.executeQuery(query);
			rs.last();
			int rows = rs.getRow();
			if (rows > 0) {
				String query1 = "SELECT * FROM daily_billing_settings";
				rs = stm.executeQuery(query1);
				rs.last();
				date = rs.getString(1);
				time = rs.getString(2);
				fee = rs.getDouble(3);
				narration = rs.getString(4);
			} else {
				date = "null";
				time = "null";
				fee = 0.00;
				narration = "null";
				// Authenticate authenticate = new Authenticate(stm, rs);
				// authenticate.createDialog();
			}
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
	}

	public double getFee() {
		return fee;
	}

	public String getDate() {
		return date + " " + time;
	}

	public String getNarration() {
		return narration;
	}
}
