package cashiermodules;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;

import helpers.DoubleForm;

/**
 *
 * @author Wellington
 */
public class AddReceiptsToList {
	ResultSet rs;
	Statement stm;
	double sum = 0;
	DefaultListModel<String> listmodel;
	JLabel totallabel;

	public AddReceiptsToList(ResultSet rs, Statement stm, DefaultListModel<String> listmodel, JLabel totallabel) {
		this.stm = stm;
		this.rs = rs;
		this.listmodel = listmodel;
		this.totallabel = totallabel;
	}

	public ArrayList<String> listData() {
		String query = "SELECT receiptno,amount,SUM(amount) FROM receipt_amount WHERE date = CURDATE()";
		ArrayList<String> list = new ArrayList<>();
		try {
			rs = stm.executeQuery(query);
			String data = null;
			if (!rs.next()) {

			} else {
				sum = rs.getDouble(3);
				do {
					data = "ReceiptNo " + rs.getInt(1) + ", Amount $" + rs.getDouble(2);
					list.add(data);
				} while (rs.next());
			}
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
		return list;
	}

	public String getSum() {
		return "SUM $ " + new DoubleForm().form(sum);
	}

	public void returnData() {
		int size = listmodel.getSize();
		boolean exist = false;
		int listsize = listData().size();
		for (String c : listData()) {
			if (listsize > 0) {
				for (int a = 0; a < size; a++) {
					if (listmodel.getElementAt(a).equals(c)) {
						exist = true;
						break;
					}
				}
				if (!exist) {
					listmodel.addElement(c);
					totallabel.setText(getSum());
				}
			}
		}
	}
}
