package com.billing;

import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Wellington
 */

public class PostMonthlyBillingFee {
	Statement stm;

	public PostMonthlyBillingFee(Statement stm) {
		this.stm = stm;
	}

	public void postData(String date, String time, double amount, String narration) {
		String query = "INSERT INTO monthly_billing_settings(date,time,amount,narration)VALUES('" + date + "','" + time
				+ "','" + amount + "','" + narration + "')";
		try {
			stm.execute(query);
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
	}

}
