package tr.temporarydata.manage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import tr.temporarydata.insert.InsertTemporaryData;
import tr.temporarydata.update.UpdateTemporaryData;

/**
 *
 * @author Wellington
 */
public class ManageTemporaryData {
	ResultSet rs;
	Statement stm;

	public ManageTemporaryData(ResultSet rs, Statement stm) {
		this.stm = stm;
		this.rs = rs;
	}

	public void manageTemporaryData(String product_name, int quantity, double amount) {
		String selectQuery = "SELECT quantity,amount FROM transaction_tmp WHERE product_name = '" + product_name + "'";
		try {
			rs = stm.executeQuery(selectQuery);
			if (rs.next()) {
				int oldquantity = rs.getInt(1);
				double oldamount = rs.getDouble(2);
				int newquantity = quantity + oldquantity;
				double newamount = amount + oldamount;
				UpdateTemporaryData update = new UpdateTemporaryData(rs, stm);
				update.updateTemporaryData(product_name, newquantity, newamount);
			} else {
				InsertTemporaryData insert = new InsertTemporaryData(rs, stm);
				insert.insertTemporaryData(product_name, quantity, amount);
			}

		} catch (SQLException ee) {
			ee.printStackTrace(System.err);
		}
	}
}
