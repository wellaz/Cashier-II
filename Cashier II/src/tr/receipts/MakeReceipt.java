package tr.receipts;

import java.awt.BorderLayout;
import java.awt.Font;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import helpers.DoubleForm;
import helpers.SetDateCreated;
import tr.printer.Printer;
import tr.temporarydata.delete.DeleteTMPTableData;

/**
 *
 * @author Wellington
 */
public class MakeReceipt {
	ResultSet rs, rs1;
	Statement stm, stmt;
	double sum = 0;
	JTextArea area;

	// constructor that constructs a all neccessary components with their
	// respective layout.
	public MakeReceipt(ResultSet rs, ResultSet rs1, Statement stm, Statement stmt) {
		this.stm = stm;
		this.stmt = stmt;
		this.rs = rs;
		this.rs1 = rs1;
		area = new JTextArea();
		area.setFont(new Font("Dialog", Font.PLAIN, 11));
	}

	// method that is used to make receipt, and that is used to format a
	// receipt.
	public void makeReceipt(int receiptno, double totalcost, double rendered, double change) {
		String getQuery = "SELECT * FROM transaction_tmp";
		String timestamp = "Dated " + new SetDateCreated().timeStamp() + "\n";
		String rc = "ReceiptNo :" + receiptno;
		String tot = "Total Cost $";
		String ren = "Cash Tendered $";
		String ch = "Change $";
		area.append(timestamp);
		area.append(rc);
		area.append("\n\n\nItems\t\tQnty\t$");
		area.append("\n-----------------------------------------------------------------------\n");
		try {
			rs = stm.executeQuery(getQuery);
			if (!rs.next()) {
			} else {
				do {
					double cost = returnTwoSF(rs.getDouble(3));
					sum += cost;
					String text = rs.getString(1) + "\t\t" + rs.getInt(2) + "\t" + cost + "\n";
					area.append(text);
				} while (rs.next());
			}
		} catch (SQLException ee) {
			ee.printStackTrace(System.err);
		}

		area.append("\n-----------------------------------------------------------------------\n");
		area.append(tot + "\t\t\t" + returnTwoSF(totalcost) + "\n");
		area.append(ren + "\t\t" + returnTwoSF(rendered) + "\n");
		area.append(ch + "\t\t\t" + returnTwoSF(change) + "\n");
		area.append("\n\n\t------WITH THANKS------\n");
		area.append("\t----Wellington Mapiku------");
		JFrame frame = new JFrame();
		frame.getContentPane().add(area, BorderLayout.CENTER);
		frame.pack();
		// frame.setVisible(true);
		Printer p = new Printer(area);

		p.send();
		new DeleteTMPTableData(rs, stm).deleteTemporaryData();
	}

	// return cash to two significant figures
	public double returnTwoSF(double number) {
		return new DoubleForm().form(number);
	}
}
