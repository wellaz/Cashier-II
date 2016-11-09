package soc.backup;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JFrame;

import helpers.SetDateCreated;

/**
 *
 * @author Wellington
 */

public class RunBackUpProcess {
	Statement stm;
	ResultSet rs;
	JFrame frame;

	public RunBackUpProcess(Statement stm, ResultSet rs, JFrame frame) {
		this.rs = rs;
		this.stm = stm;
		this.frame = frame;
	}

	public void runBackUp() {
		String check = new LastBackupDate(stm, rs).getLastBackupDate();
		String today = new SetDateCreated().getDate().trim();
		if (check.equals("")) {
			backUp();
		} else {
			if (!isValidDate(today)) {
				backUp();
			}
		}
		/*
		 * if (!check.equals("")) { // Date lastdate = new LastBackupDate(stm,
		 * rs).returnDate(); // String to = //
		 * ((DateFormat.getDateInstance(DateFormat.SHORT)).format(new //
		 * Date())); DateTimeFormatter formatter =
		 * DateTimeFormatter.ofPattern("yyyy-MM-dd"); LocalDate previous =
		 * (LocalDate) formatter.parse(check); LocalDate now = LocalDate.now();
		 * System.out.println("Whats in there"); if (now.isBefore(previous) ==
		 * true) { System.out.println("check1");
		 * JOptionPane.showMessageDialog(frame,
		 * "The System has detected that today's date is somehow incorrect.\nKindly correct the system date and Sign in again"
		 * , "Warning", JOptionPane.WARNING_MESSAGE); System.exit(0); } if
		 * (now.isAfter(previous) == true) { System.out.println("check2");
		 * Period difference = Period.between(previous, now); if
		 * (difference.getDays() >= 7) { System.out.println("Time for backup");
		 * backUp(); } } if (now.isEqual(previous) == true) {
		 * System.out.println("check3"); System.out.println(
		 * "Days are just equal"); } } else { System.out.println(
		 * "First Backup time"); backUp(); }
		 */
	}

	private void backUp() {
		BackUp b = new BackUp(frame);
		BackUp.Worker w = b.new Worker();
		w.execute();
		postBackUpMetaData();
	}

	public void postBackUpMetaData() {
		SetDateCreated setdate = new SetDateCreated();
		String date = setdate.getDate();
		String time = setdate.getTime();
		String text = "INSERT INTO last_backup(date,time)VALUES('" + date + "','" + time + "')";
		try {
			stm.execute(text);
		} catch (SQLException ee) {
			ee.printStackTrace(System.err);
		}
	}

	public boolean isValidDate(String date) {
		List<String> imgTypes = null;
		int arrsize = allDates().size();
		String[] dat = new String[arrsize];
		for (int i = 0; i < arrsize; i++)
			dat[i] = allDates().get(i);
		imgTypes = Arrays.asList(dat);
		return imgTypes.stream().anyMatch(t -> date.equals(t));
	}

	public ArrayList<String> allDates() {
		ArrayList<String> data = new ArrayList<>();
		String date = null;
		String query = "SELECT date FROM last_backup";
		try {
			rs = stm.executeQuery(query);
			if (rs.next()) {
				do {
					date = rs.getString(1);
					data.add(date);
				} while (rs.next());
			}

		} catch (SQLException ee) {
			ee.printStackTrace(System.err);
		}
		return data;
	}
}
