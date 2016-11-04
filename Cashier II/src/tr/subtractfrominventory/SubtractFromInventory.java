package tr.subtractfrominventory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Wellington
 */
public class SubtractFromInventory {
	ResultSet rs;
	Statement stm;

	public SubtractFromInventory(ResultSet rs, Statement stm) {
		this.stm = stm;
		this.rs = rs;
	}

	public void subtract(String product_name, int quantity) {
		String selectQuery = "SELECT items_sold,remaining FROM bulk_stock WHERE product_name = '" + product_name + "'";
		try {
			rs = stm.executeQuery(selectQuery);
			if (rs.next()) {
				int sold = rs.getInt(1);
				int remaining = rs.getInt(2);
				int newremaining = remaining - quantity;
				int newsold = sold + quantity;
				String updateQuery = "UPDATE bulk_stock SET items_sold = '" + newsold + "',remaining = '" + newremaining
						+ "'WHERE product_name = '" + product_name + "'";
				stm.executeUpdate(updateQuery);
			} else {

			}

		} catch (SQLException ee) {
			ee.printStackTrace(System.err);
		}
	}
}
