package cashiermodules;

import java.awt.EventQueue;
import java.util.ArrayList;

import javax.swing.DefaultListModel;

import helpers.DoubleForm;

/**
 *
 * @author Wellington
 */
public class PaymentsList {
	ArrayList<String> list;
	ArrayList<Double> sum;
	DoubleForm df;

	public PaymentsList() {
		list = new ArrayList<>();
		sum = new ArrayList<>();
		df = new DoubleForm();
	}

	public void addtoArray(String id, String amount) {
		String data = "ReceiptNo " + id + ", Amount $" + amount;
		list.add(data);
		sum.add(Double.parseDouble(amount));
	}

	public void addToListModel(DefaultListModel<String> listmodel) {
		EventQueue.invokeLater(() -> {
			int size = listmodel.getSize();
			boolean exist = false;
			int listsize = list.size();
			if (listsize > 0) {
				for (String c : list) {
					for (int a = 0; a < size; a++) {
						if (listmodel.getElementAt(a).equals(c)) {
							exist = true;
							break;
						}
					}
					if (!exist) {
						listmodel.addElement(c);
					}
				}
			}
		});
	}

	public String getTotal() {
		double summ = 0;
		int size = sum.size();
		if (size > 0) {
			for (double c : sum)
				summ += c;
		}
		return "SUM $ " + df.form(summ);
	}
}
