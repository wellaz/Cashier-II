package tr.bulkstock.add;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Wellington
 */
public class AddStockData {
	ResultSet rs;
	Statement stm;

	public AddStockData(ResultSet rs, Statement stm) {
		this.stm = stm;
		this.rs = rs;
	}

	public void addStockData(String product_name, int quantity, int items_sold, int remaining, String date, String time,
			String year) {
		String update = "SELECT * FROM bulk_stock WHERE product_name = '" + product_name + "'";
		String insertQuery = "INSERT INTO bulk_stock(product_name,quantity,items_sold,remaining,date,time,year)VALUES('"
				+ product_name + "','" + quantity + "','" + items_sold + "','" + remaining + "','" + date + "','" + time
				+ "','" + year + "')";
		try {
			rs = stm.executeQuery(update);
			if (rs.next()) {
				int remain = rs.getInt(4);
				int newremaining = remain + remaining;
				String updateQuery = "UPDATE bulk_stock SET remaining = '" + newremaining + "',date = '" + date
						+ "',time = '" + time + "' WHERE product_name = '" + product_name + "'";
				stm.executeUpdate(updateQuery);
				insertintoSolidBulk(product_name, quantity, date, time, year);
			} else {
				stm.execute(insertQuery);
				insertintoSolidBulk(product_name, quantity, date, time, year);
			}

		} catch (SQLException ee) {
			ee.printStackTrace(System.err);
		}
	}

	public void insertintoSolidBulk(String product_name, int quantity, String date, String time, String year) {
		String insertQuery = "INSERT INTO solid_bulk(product_name,quantity,date,time,year)VALUES('" + product_name
				+ "','" + quantity + "','" + date + "','" + time + "','" + year + "')";
		try {
			stm.execute(insertQuery);
		} catch (SQLException ee) {
			ee.printStackTrace(System.err);
		}
	}
}
