package com.billing;

import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Wellington
 */

public class PostMonthlyBill {
	Statement stm;

	public PostMonthlyBill(Statement stm) {
		this.stm = stm;
	}

	public void postData(String date, String time, double amount) {
		String query = "INSERT INTO monthly_bill(date,time,amount)VALUES('" + date + "','" + time + "','" + amount
				+ "')";
		try {
			stm.execute(query);
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
	}

}
