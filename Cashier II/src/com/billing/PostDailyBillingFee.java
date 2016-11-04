package com.billing;

import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Wellington
 */

public class PostDailyBillingFee {
	Statement stm;

	public PostDailyBillingFee(Statement stm) {
		this.stm = stm;
	}

	public void postData(String date, String time, double amount, String month, String narration) {
		String query = "INSERT INTO daily_billing_settings(date,time,amount,month,narration)VALUES('" + date + "','"
				+ time + "','" + amount + "','" + month + "','" + narration + "')";
		try {
			stm.execute(query);
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
	}
}
