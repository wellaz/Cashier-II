package tr.temporarydata.update;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Wellington
 */
public class UpdateTemporaryData {
	ResultSet rs;
	Statement stm;

	public UpdateTemporaryData(ResultSet rs, Statement stm) {
		this.stm = stm;
		this.rs = rs;
	}

	public void updateTemporaryData(String product_name, int quantity, double amount) {
		
		String updateQuery = "UPDATE transaction_tmp SET quantity = '" + quantity + "',amount = '" + amount
				+ "' WHERE product_name = '" + product_name + "'";
		try {
			stm.executeUpdate(updateQuery);
		} catch (SQLException ee) {
			ee.printStackTrace(System.err);
		}
	}
}
