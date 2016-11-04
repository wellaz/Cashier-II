package tr.temporarydata.insert;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Wellington
 */
public class InsertTemporaryData {
	ResultSet rs;
	Statement stm;

	public InsertTemporaryData(ResultSet rs, Statement stm) {
		this.stm = stm;
		this.rs = rs;
	}

	public void insertTemporaryData(String product_name, int quantity, double amount) {
		String insertQuery = "INSERT INTO transaction_tmp(product_name,quantity,amount)VALUES('" + product_name + "','"
				+ quantity + "','" + amount + "')";
		try {
			stm.execute(insertQuery);
		} catch (SQLException ee) {
			ee.printStackTrace(System.err);
		}
	}
}
