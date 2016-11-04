package tr.add.product.type;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import helpers.Increment;
import helpers.MonthsList;
import helpers.SetDateCreated;
import helpers.TextValidator;

/**
 *
 * @author Wellington
 */
@SuppressWarnings("serial")
public class NewProductType extends JPanel implements ActionListener {
	JTextField amounttxt, typetxt;
	JButton submit;
	JLabel imagelbl;
	JTabbedPane tabs;
	ResultSet rs;
	Statement stm;
	JFrame frame;

	public NewProductType(JTabbedPane tabs, ResultSet rs, Statement stm, JFrame frame) {
		this.tabs = tabs;
		this.stm = stm;
		this.rs = rs;
		this.frame = frame;
		this.setBackground(Color.WHITE);

		init();
	}

	public void init() {
		this.setLayout(new BorderLayout());
		JPanel topp = new JPanel();
		topp.setBackground(Color.WHITE);
		topp.setLayout(new FlowLayout());
		JLabel toplbl = new JLabel("Submit  All Stock Pricing Details");
		// toplbl.setForeground(Color.WHITE);
		toplbl.setFont(new Font("", Font.BOLD, 25));
		topp.add(toplbl, SwingConstants.CENTER);

		JPanel midpanel = new JPanel();
		midpanel.setBackground(Color.WHITE);

		GridLayout layout = new GridLayout(3, 2, 3, 5);

		midpanel.setLayout(layout);

		JLabel amountlbl = new JLabel("Unit Cost :");
		// amountlbl.setForeground(Color.WHITE);
		amountlbl.setFont(new Font("", Font.BOLD, 25));

		JLabel monthlbl = new JLabel("Product Name :");
		// monthlbl.setForeground(Color.WHITE);
		monthlbl.setFont(new Font("", Font.BOLD, 25));

		amounttxt = new JTextField();
		amounttxt.setFont(new Font("", Font.BOLD, 25));
		amounttxt.addKeyListener(new TextValidator());

		Object[] da = new String[MonthsList.getMonths().size()];
		for (int i = 0; i < MonthsList.getMonths().size(); i++) {
			da[i] = MonthsList.getMonths().get(i);
		}

		typetxt = new JTextField();
		typetxt.setFont(new Font("", Font.BOLD, 25));
		submit = new JButton("Submit \u2193");
		submit.setFont(new Font("", Font.PLAIN, 20));
		submit.addActionListener(this);

		midpanel.add(monthlbl);
		midpanel.add(typetxt);
		midpanel.add(amountlbl);
		midpanel.add(amounttxt);
		midpanel.add(new JLabel());
		midpanel.add(submit);

		this.add(new JPanel().add(new JLabel("          ")), BorderLayout.WEST);
		this.add(new JPanel().add(new JLabel("          ")), BorderLayout.EAST);
		this.add(topp, BorderLayout.NORTH);

		JPanel temp = new JPanel();
		temp.setBackground(Color.WHITE);
		temp.setLayout(new BorderLayout());
		temp.setOpaque(false);
		temp.add(midpanel, BorderLayout.NORTH);
		this.add(temp, BorderLayout.CENTER);

	}

	public void insertTab() {
		EventQueue.invokeLater(() -> {
			boolean exist = false;
			int count = tabs.getTabCount();
			for (int x = 0; x < count; x++) {
				if (tabs.getTitleAt(x).trim().equals("Stock Pricing")) {
					exist = true;
					tabs.setSelectedIndex(x);
					break;
				}
			}
			if (!exist) {
				tabs.addTab("Stock Pricing   ", null, this, "Post new Product Categories and Stock Pricing");
				tabs.setSelectedIndex(count);
			}
		});
	}

	public void postings(int product_id, String product_name, double cost, String date, String time, String year) {
		try {
			String query = "INSERT INTO commonproducts(product_id,product_name,unit_price,date,time,year)VALUES('"
					+ product_id + "','" + product_name + "','" + cost + "','" + date + "','" + time + "','" + year
					+ "')";
			stm.execute(query);
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == submit) {
			int product_id = new Increment(rs, stm).incerentcCategoryId();
			String product_name = typetxt.getText();
			String amount = amounttxt.getText();
			String date = new SetDateCreated().getDate();
			String time = new SetDateCreated().getTime();
			String year = new SetDateCreated().getYear();

			if (!(product_name.equals("") || amount.equals(""))) {
				double cost = Double.parseDouble(amount);

				int confirm = JOptionPane.showConfirmDialog(frame,
						"Product :" + product_name + "\n Unit price $" + amount + " \nConfirm?", "Confirm Unit Price",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (confirm == JOptionPane.YES_OPTION) {
					postings(product_id, product_name, cost, date, time, year);
					amounttxt.setText("");
					typetxt.setText("");
				}
			} else
				JOptionPane.showMessageDialog(frame, "Null value cannot be submitted ", "Warning",
						JOptionPane.WARNING_MESSAGE);
		}
	}
}
