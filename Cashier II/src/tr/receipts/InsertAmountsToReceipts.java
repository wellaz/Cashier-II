package tr.receipts;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Wellington
 */
public class InsertAmountsToReceipts {
	ResultSet rs;
	Statement stm;
	double sum = 0;

	public InsertAmountsToReceipts(ResultSet rs, Statement stm) {
		this.stm = stm;
		this.rs = rs;
	}

	public void insertReceiptAmounts(int receiptno, double amount, double given, double change, double collected,
			String date, String time) {
		String insertQuery = "INSERT INTO receipt_amount(receiptno,amount,amount_given,amount_changed,change_collected,date,time)VALUES('"
				+ receiptno + "','" + amount + "','" + given + "','" + change + "','" + collected + "','" + date + "','"
				+ time + "')";
		try {
			stm.execute(insertQuery);
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
	}

}
