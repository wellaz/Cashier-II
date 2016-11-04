package helpers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Wellington
 */

public class GetProductNames {
	ResultSet rs;
	Statement stm;

	public GetProductNames(ResultSet rs, Statement stm) {
		this.stm = stm;
		this.rs = rs;

	}

	public ArrayList<String> getClassNames() {
		ArrayList<String> array = new ArrayList<>();
		String find = "SELECT product_name FROM commonproducts";
		try {
			rs = stm.executeQuery(find);
			while (rs.next()) {
				array.add(rs.getString(1));
			}
		} catch (SQLException ee) {
		}
		return array;
	}
}
