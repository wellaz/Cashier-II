package com.billing;

import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Wellington
 */

public class PostDailyBill {
	Statement stm;

	public PostDailyBill(Statement stm) {
		this.stm = stm;
	}

	public void postData(String date, String time, double amount, String month) {
		String query = "INSERT INTO daily_bill(date,time,amount,month)VALUES('" + date + "','" + time + "','" + amount
				+ "','" + month + "')";
		try {
			stm.execute(query);
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
	}

}
