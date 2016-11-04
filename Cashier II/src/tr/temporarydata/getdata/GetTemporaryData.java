package tr.temporarydata.getdata;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JTextArea;

/**
 *
 * @author Wellington
 */
public class GetTemporaryData {
	ResultSet rs, rs1;
	Statement stm, stmt;
	public double sum = 0;

	public GetTemporaryData(ResultSet rs, ResultSet rs1, Statement stm, Statement stmt) {
		this.stm = stm;
		this.stmt = stmt;
		this.rs = rs;
		this.rs1 = rs1;
	}

	public void getTemporaryData(JTextArea display) {
		deleteZeroFields();
		String getQuery = "SELECT * FROM transaction_tmp";
		try {
			rs = stm.executeQuery(getQuery);
			if (!rs.next()) {

			} else {
				do {
					double cost = rs.getDouble(3);
					sum += cost;
					String text = rs.getString(1) + "\t\t" + rs.getInt(2) + "\t" + cost
							+ "\n-----------------------------------------------------------------------\n";
					display.append(text);
				} while (rs.next());
			}
		} catch (SQLException ee) {
			ee.printStackTrace(System.err);
		}
	}

	public void deleteZeroFields() {
		String getQuery = "SELECT * FROM transaction_tmp";
		try {
			rs1 = stmt.executeQuery(getQuery);
			if (!rs1.next()) {

			} else {
				do {
					double cost = rs1.getDouble(3);
					if (cost <= 0) {
						String deleteQuery = "DELETE FROM transaction_tmp WHERE amount = '" + cost + "'";
						stm.execute(deleteQuery);
					}
				} while (rs1.next());
			}
		} catch (SQLException ee) {
			ee.printStackTrace(System.err);
		}
	}

	public double getTotalCost() {
		return sum;
	}
}
