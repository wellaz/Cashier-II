package tr.updateprice;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import helpers.DoubleForm;

public class Update {

	ResultSet rs, rs1;
	Statement stm, stmt;
	DoubleForm df;

	public Update(ResultSet rs, ResultSet rs1, Statement stm, Statement stmt) {
		this.stm = stm;
		this.stmt = stmt;
		this.rs = rs;
		this.rs1 = rs1;
		df = new DoubleForm();
	}

	public void update(String product_name, double price, String date, String time) {
		String updateString = "UPDATE commonproducts SET unit_price = '" + price + "',date = '" + date + "',time = '"
				+ time + "' WHERE product_name = '" + product_name + "'";
		try {
			stm.executeUpdate(updateString);
		} catch (SQLException ee) {
			ee.printStackTrace(System.err);
		}
	}
}
