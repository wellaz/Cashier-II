package soc.backup;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Wellington
 */

public class LastBackupDate {
	Statement stm;
	ResultSet rs;

	public LastBackupDate(Statement stm, ResultSet rs) {
		this.rs = rs;
		this.stm = stm;
	}

	public String getLastBackupDate() {
		String query = "SELECT date FROM last_backup";
		String query1 = "SELECT date FROM last_backup";
		String date = null;
		try {
			rs = stm.executeQuery(query);
			rs.last();
			int rows = rs.getRow();
			if (rows > 0) {
				rs = stm.executeQuery(query1);
				rs.last();
				date = rs.getString(1);
			} else {
				date = "";
			}
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
		return date;
	}

	public Date returnDate() {
		String start_dt = getLastBackupDate();
		Date date = null;
		DateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
		try {
			date = (Date) parser.parse(start_dt);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

}
