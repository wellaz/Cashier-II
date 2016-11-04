package tr.temporarydata.delete;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Wellington
 */
public class DeleteTMPTableData {

	ResultSet rs;
	Statement stm;

	public DeleteTMPTableData(ResultSet rs, Statement stm) {
		this.stm = stm;
		this.rs = rs;
	}

	public void deleteTemporaryData() {
		String rowsQuery = "SELECT * FROM transaction_tmp";
		String deleteQuery = "DELETE FROM transaction_tmp";
		try {
			rs = stm.executeQuery(rowsQuery);
			rs.last();
			int rows = rs.getRow();
			if (rows > 0) {
				stm.execute(deleteQuery);
			}

		} catch (SQLException ee) {
			ee.printStackTrace(System.err);
		}
	}
}
