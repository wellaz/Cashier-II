package org.license;

import java.awt.EventQueue;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JOptionPane;

import com.deco.AnimateJFrame;

import helpers.AccessDbase;
import validation.FormValidation;

/**
 * @author Wellington
 *
 */
public class CheckKeys {

	KeyDialog kd = new KeyDialog();
	ArrayList<Integer> attempts = new ArrayList<>();
	AccessDbase adbase;

	public CheckKeys() {
		adbase = new AccessDbase();
		adbase.connectionDb();
	}

	public void check(String month, int year) {
		if (isValidMonth(month)) {
			progress(Integer.parseInt(month), year);
			String select = "SELECT attempts FROM memockeck WHERE month = '" + month + "' AND year = '" + year + "'";
			try {
				adbase.rs = adbase.stm.executeQuery(select);
				if (adbase.rs.next()) {
					int att = adbase.rs.getInt(1) + 1;
					String checkmemo = "SELECT month FROM memo WHERE year =  '" + year + "'";
					adbase.rs = adbase.stm.executeQuery(checkmemo);
					if (!adbase.rs.next()) {
						String update = "UPDATE memockeck SET attempts = '" + att + "'WHERE month = '" + month
								+ "' AND year = '" + year + "'";
						adbase.stm.executeUpdate(update);
					} else {

					}
				} else {

				}
			} catch (SQLException dd) {
				dd.printStackTrace(System.err);
			}
		} else {
			launcher();
		}
	}

	private boolean isValidMonth(String acc) {
		List<String> mnTypes = null;
		int size = months().size();
		String[] dat = new String[size];
		for (int i = 0; i < size; i++)
			dat[i] = Integer.toString(months().get(i));
		mnTypes = Arrays.asList(dat);
		return mnTypes.stream().anyMatch(t -> acc.equals(t));
	}

	private ArrayList<Integer> months() {
		ArrayList<Integer> m = new ArrayList<>();
		m.add(1);
		m.add(2);
		m.add(3);
		m.add(4);
		m.add(5);
		m.add(6);
		m.add(7);
		m.add(8);
		m.add(9);
		m.add(10);
		m.add(11);
		m.add(12);
		return m;
	}

	public void launcher() {
		EventQueue.invokeLater(() -> {
			FormValidation formValidation = new FormValidation();
			new AnimateJFrame().fadeIn(formValidation, 100);
			// new FormValidation().setVisible(true);
			closeConn();
		});
	}

	public void progress(int month, int year) {
		String query0 = "SELECT k_ey FROM memo WHERE month = '" + month + "' AND year = '" + year + "'";
		try {
			adbase.rs = adbase.stm.executeQuery(query0);
			adbase.rs.last();
			int rows = adbase.rs.getRow();
			if (rows == 0) {
				kd.setVisible(true);
				String checkk = "SELECT attempts FROM memockeck WHERE month = '" + month + "' AND year = '" + year
						+ "'";
				adbase.rs = adbase.stm.executeQuery(checkk);
				if (adbase.rs.next()) {
					int value = adbase.rs.getInt(1);
					if (value > 3) {
						kd.pro.setVisible(false);
					}
				} else {
					String insertcheck = "INSERT INTO memockeck(month,year,attempts)VALUES('" + month + "','" + year
							+ "','" + 1 + "') ";
					adbase.stm.execute(insertcheck);
				}

				kd.log.addActionListener((event) -> {
					String key = kd.key.getText();
					String query1 = "SELECT k_ey FROM memo WHERE k_ey = '" + key + "'";
					try {
						adbase.rs = adbase.stm.executeQuery(query1);
						if (adbase.rs.next()) {
							String foundkey = adbase.rs.getString(1);
							String query11 = "SELECT month,year FROM memo WHERE k_ey = '" + foundkey + "'";
							adbase.rs = adbase.stm.executeQuery(query11);
							adbase.rs.next();
							int mn = adbase.rs.getInt(1);
							int yr = adbase.rs.getInt(2);
							if (mn == 0 || yr == 0) {
								// proceed
								String updatest = "UPDATE memo SET month = '" + month + "',year = '" + year
										+ "' WHERE k_ey = '" + foundkey + "'";
								adbase.stm.executeUpdate(updatest);
								kd.dispose();
								JOptionPane.showMessageDialog(kd,
										"We are inspired by your commitment in the license renewal process.\nData BootStrappers is dedicated to solving your business issues\nand will continue to do so!\nThank you!!!\n\nData BootStrappers, Inc ('your e-business trailblazer!')",
										"Consignment Note", JOptionPane.INFORMATION_MESSAGE);
								launcher();
							} else {
								JOptionPane.showMessageDialog(kd,
										"The entered key has already expired!\nKindly purchase a new key from the manufacturer!",
										"Warning", JOptionPane.WARNING_MESSAGE);
								kd.key.setText("");
							}
						} else {
							JOptionPane.showMessageDialog(kd, "The entered key is invalid", "Error",
									JOptionPane.ERROR_MESSAGE);
							kd.key.setText("");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
			} else {
				launcher();
				// closeConnection();
			}
			kd.can.addActionListener(e -> System.exit(0));
			kd.pro.addActionListener((event) -> {
				kd.dispose();
				launcher();
			});
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
	}

	public void closeConn() {
		try {
			if (adbase.rs != null) {
				adbase.rs.close();
			}
			if (adbase.rs1 != null) {
				adbase.rs1.close();
			}
			if (adbase.stm != null) {
				adbase.stm.close();
			}
			if (adbase.stmt != null) {
				adbase.stmt.close();
			}
			if (adbase.stmt1 != null) {
				adbase.stmt1.close();
			}
			if (adbase.conn != null) {
				adbase.conn.close();
			}
			if (adbase.conn1 != null) {
				adbase.conn1.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
