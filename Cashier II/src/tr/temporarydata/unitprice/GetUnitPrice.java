package tr.temporarydata.unitprice;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import helpers.DoubleForm;

/**
 *
 * @author Wellington
 */
public class GetUnitPrice {
	ResultSet rs;
	Statement stm;
	DoubleForm df;

	public GetUnitPrice(ResultSet rs, Statement stm) {
		this.stm = stm;
		this.rs = rs;
		df = new DoubleForm();
	}

	public double getunitPrice(String product_name) {
		String pricequery = "SELECT unit_price FROM commonproducts WHERE product_name = '" + product_name + "'";
		double unitprice = 0;
		try {
			rs = stm.executeQuery(pricequery);
			if (rs.next()) {
				unitprice = rs.getDouble(1);
			} else {

			}
		} catch (SQLException ee) {
			ee.printStackTrace(System.err);
		}
		return df.form(unitprice);
	}
}
