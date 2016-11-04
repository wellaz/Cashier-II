package tr.ledger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AffectLedger {
	ResultSet rs;
	Statement stm;
	int count = 0;

	public AffectLedger(ResultSet rs, Statement stm) {
		this.stm = stm;
		this.rs = rs;
	}

	public void creditLedger(String date, String time, String month, String year, double credit, String details) {
		try {
			String text = "INSERT INTO ledger(date,time,month,year,debit,credit,details)VALUES('" + date + "','" + time
					+ "','" + month + "','" + year + "','" + 0 + "','" + credit + "','" + details + "')";
			stm.execute(text);
		} catch (SQLException ee) {
			ee.printStackTrace(System.err);
		}
	}

	public void debitLedger(String date, String time, String month, String year, double debit, String details) {
		try {
			String text = "INSERT INTO ledger(date,time,month,year,debit,credit,details)VALUES('" + date + "','" + time
					+ "','" + month + "','" + year + "','" + debit + "','" + 0 + "','" + details + "')";
			stm.execute(text);
		} catch (SQLException ee) {
			ee.printStackTrace(System.err);
		}
	}
}
