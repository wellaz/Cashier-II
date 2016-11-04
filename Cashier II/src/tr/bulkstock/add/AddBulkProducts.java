package tr.bulkstock.add;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import helpers.GetProductNames;
import helpers.RemoveTab;
import helpers.SetDateCreated;
import tr.add.product.type.NewProductType;

/**
 *
 * @author Wellington
 */
@SuppressWarnings("serial")
public class AddBulkProducts extends JPanel implements ActionListener {
	JTabbedPane tabs;
	ResultSet rs, rs1;
	Statement stm, stmt;
	PreparedStatement pstmt;
	Connection conn;
	JFrame frame;
	SpinnerNumberModel spinnerModel;
	JSpinner spinner;
	private JComboBox<Object> itemscombo;
	private JButton okbtn;
	private JButton notbutton;

	public AddBulkProducts(JTabbedPane tabs, ResultSet rs, ResultSet rs1, Statement stm, Statement stmt,
			PreparedStatement pstmt, Connection conn, JFrame frame) {
		this.tabs = tabs;
		this.stm = stm;
		this.stmt = stmt;
		this.rs = rs;
		this.rs1 = rs1;
		this.frame = frame;
		this.pstmt = pstmt;
		this.conn = conn;
		init();
	}

	public final void init() {
		this.setLayout(new BorderLayout());
		// JPanel panel = new TranslucentJPanel(Color.BLUE);
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setLayout(new GridLayout(4, 0));
		this.add(panel, BorderLayout.CENTER);

		JLabel toplbl = new JLabel("Submit All New Bulk Stock Details", SwingConstants.CENTER);
		toplbl.setFont(new java.awt.Font("", Font.BOLD, 25));
		// toplbl.setForeground(Color.WHITE);
		panel.add(toplbl, BorderLayout.NORTH);

		int itemsarraysize = new GetProductNames(rs, stm).getClassNames().size();
		Object[] seme = new String[itemsarraysize];
		for (int i = 0; i < itemsarraysize; i++) {
			seme[i] = new GetProductNames(rs, stm).getClassNames().get(i);
		}

		JLabel itemlbl = new JLabel("Choose item Categoty", SwingConstants.CENTER);
		itemlbl.setFont(new java.awt.Font("", Font.BOLD, 16));
		// itemlbl.setForeground(Color.WHITE);

		itemscombo = new JComboBox<>(seme);
		itemscombo.setFont(new Font("", Font.PLAIN, 20));
		itemscombo.setFont(new Font("", Font.PLAIN, 18));

		Box box1 = Box.createVerticalBox();
		box1.add(itemlbl);
		box1.add(Box.createVerticalStrut(4));
		box1.add(itemscombo);

		int minValue = 1;
		int maxValue = 1000;
		int currentValue = 1;
		int steps = 1;
		spinnerModel = new SpinnerNumberModel(currentValue, minValue, maxValue, steps);
		spinner = new JSpinner(spinnerModel);
		spinner.setFont(new Font("", Font.PLAIN, 18));
		JSpinner.NumberEditor nEditor = new JSpinner.NumberEditor(spinner, "0");
		spinner.setEditor(nEditor);

		JLabel qn = new JLabel("Quantity", SwingConstants.CENTER);
		qn.setFont(new Font("", Font.BOLD, 16));
		// qn.setForeground(Color.WHITE);
		Box box2 = Box.createVerticalBox();
		box2.add(qn);
		box2.add(Box.createVerticalStrut(4));
		box2.add(spinner);

		JPanel flowpanel = new JPanel(new FlowLayout());
		flowpanel.setOpaque(false);

		JButton times = new JButton("<html><h3>X</h3></html>");
		times.setBackground(Color.GREEN);
		times.setForeground(Color.WHITE);

		flowpanel.add(box1);
		flowpanel.add(Box.createHorizontalStrut(25));
		flowpanel.add(times);
		flowpanel.add(Box.createHorizontalStrut(25));
		flowpanel.add(box2);
		flowpanel.add(Box.createHorizontalStrut(15));

		okbtn = new JButton("<html><h1>Submit</h1></html>");
		okbtn.setBackground(Color.BLUE);
		okbtn.setForeground(Color.WHITE);
		okbtn.addActionListener(this);

		flowpanel.add(okbtn);
		panel.add(flowpanel);

		JLabel nolbl = new JLabel(
				"<html><h3>If the product does not exist in the <u><i>'Choose Item Category'</i></u>  list, then click the button below and <br>add a product category and the <u>unit price</u>.</h3></html>");
		nolbl.setFont(new Font("", Font.BOLD, 15));
		nolbl.setForeground(Color.MAGENTA);

		notbutton = new JButton("<html><h2>Product Category<br>does not exist!</h2></html>");
		notbutton.setBackground(Color.RED);
		notbutton.setForeground(Color.WHITE);
		notbutton.addActionListener(this);

		Box box3 = Box.createVerticalBox();
		box3.add(nolbl);
		box3.add(Box.createVerticalStrut(25));
		box3.add(notbutton);
		panel.add(Box.createVerticalStrut(25));
		panel.add(box3);

	}

	public void insertTab() {
		EventQueue.invokeLater(() -> {
			int numberoftabs = tabs.getTabCount();
			boolean exist = false;
			for (int a = 0; a < numberoftabs; a++) {
				if (tabs.getTitleAt(a).trim().equals("New Bulk Stock")) {
					exist = true;
					tabs.setSelectedIndex(numberoftabs);
					break;
				}
			}
			if (!exist) {
				tabs.addTab("New Bulk Stock   ", null, this, "Add New Bulk Stock");
				tabs.setSelectedIndex(numberoftabs);
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == notbutton) {
			new RemoveTab(tabs).removeTab("New Bulk Stock");
			NewProductType p = new NewProductType(tabs, rs, stm, frame);
			p.insertTab();
		}
		if (e.getSource() == okbtn) {
			SetDateCreated setdate = new SetDateCreated();
			String product_name = itemscombo.getSelectedItem().toString();
			Object ob = spinnerModel.getNumber();
			int quantity = (int) ob;
			String date = setdate.getDate();
			String time = setdate.getTime();
			String year = setdate.getYear();
			int items_sold = 0;
			int remaining = quantity;
			AddStockData addst = new AddStockData(rs, stm);

			int confirm = JOptionPane.showConfirmDialog(frame,
					"Bulk Product :" + product_name + "\n Quantity :" + quantity + " items. \nConfirm?",
					"Confirm Quantity and Item Type", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (confirm == JOptionPane.YES_OPTION) {
				addst.addStockData(product_name, quantity, items_sold, remaining, date, time, year);
				EventQueue.invokeLater(() -> {
					itemscombo.setSelectedIndex(0);
				});
				EventQueue.invokeLater(() -> {
					spinnerModel.setValue(spinnerModel.getMinimum());
				});
			}
		}
	}
}
