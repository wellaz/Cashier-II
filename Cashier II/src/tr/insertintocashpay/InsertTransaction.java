package tr.insertintocashpay;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import helpers.DoubleForm;
import helpers.SetDateCreated;
import helpers.ThisMonth;
import tr.ledger.AffectLedger;
import tr.receipts.InsertAmountsToReceipts;
import tr.subtractfrominventory.SubtractFromInventory;

/**
 *
 * @author Wellington
 */
public class InsertTransaction {
	ResultSet rs, rs1;
	Statement stm, stmt;
	DoubleForm df;
	ArrayList<String> product_name_array = new ArrayList<>();
	ArrayList<Integer> quantity_array = new ArrayList<>();

	public InsertTransaction(ResultSet rs, ResultSet rs1, Statement stm, Statement stmt) {
		this.stm = stm;
		this.stmt = stmt;
		this.rs = rs;
		this.rs1 = rs1;
		df = new DoubleForm();
	}

	public void affectTransaction(double given, double change, double collected, int receiptno, double totalcost) {
		String selectQuery = "SELECT * FROM transaction_tmp";

		int cashierid = 1;
		String date = new SetDateCreated().getDate();
		String time = new SetDateCreated().getTime();
		SubtractFromInventory sb = new SubtractFromInventory(rs, stm);
		String month = ThisMonth.thisMonth();
		String year = new SetDateCreated().getYear();
		String details = "RC" + receiptno + " cash transaction CashierID" + cashierid;
		try {
			rs = stm.executeQuery(selectQuery);
			if (!rs.next()) {
				InsertAmountsToReceipts ins = new InsertAmountsToReceipts(rs, stm);
				ins.insertReceiptAmounts(receiptno, totalcost, given, change, collected, date, time);
				AffectLedger aff = new AffectLedger(rs, stm);
				aff.creditLedger(date, time, month, year, totalcost, details);
				insertIntoCashPay(receiptno, cashierid, "not found", 0, totalcost, date, time);
			} else {
				do {
					String product_name = rs.getString(1);
					int quantity = rs.getInt(2);
					double amount = rs.getDouble(3);
					product_name_array.add(product_name);
					quantity_array.add(quantity);
					insertIntoCashPay(receiptno, cashierid, product_name, quantity, amount, date, time);
				} while (rs.next());
				int listsize = product_name_array.size(), i = 0;
				while (i < listsize) {
					sb.subtract(product_name_array.get(i), quantity_array.get(i));
					i++;
				}

				InsertAmountsToReceipts ins = new InsertAmountsToReceipts(rs, stm);
				ins.insertReceiptAmounts(receiptno, totalcost, given, change, collected, date, time);
				AffectLedger aff = new AffectLedger(rs, stm);
				aff.creditLedger(date, time, month, year, totalcost, details);
				for (int x = 0; x < product_name_array.size(); x++) {
					product_name_array.remove(x);
					quantity_array.remove(x);
				}
			}
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
	}

	public void insertIntoCashPay(int receiptno, int cashierid, String product_name, int quantity, double amount,
			String date, String time) {
		String insertQuery = "INSERT INTO cashpay(receiptno,cashier_id,product_name,quantity,amount,date,time)VALUES('"
				+ receiptno + "','" + cashierid + "','" + product_name + "','" + quantity + "','" + amount + "','"
				+ date + "','" + time + "')";
		try {
			stmt.execute(insertQuery);
		} catch (SQLException ee) {
			ee.printStackTrace(System.err);
		}
	}
}
