/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tr.receipt.reprint;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.deco.TranslucentJPanel1;

import helpers.TextValidator;
import tr.printer.Printer;

/**
 *
 * @author Wellington
 */
@SuppressWarnings("serial")
public class ReprintRe extends JPanel implements ActionListener {

	JTextField sid;
	String idstring;
	JButton search;
	ResultSet rs, rs1;
	Statement stm, stmt;
	JTabbedPane tabs;
	JFrame frame;
	JTextArea area;
	private JButton reprint;

	public ReprintRe(ResultSet rs, ResultSet rs1, Statement stm, Statement stmt, JTabbedPane tabs, JFrame frame) {
		this.rs = rs;
		this.rs1 = rs1;
		this.stm = stm;
		this.stmt = stmt;
		this.tabs = tabs;
		this.frame = frame;
		this.setLayout(new BorderLayout());
		init();
	}

	public final void init() {
		JPanel p1 = new JPanel(new BorderLayout());
		p1.setOpaque(false);
		JLabel idlbl = new JLabel("Receipt Number :");
		idlbl.setFont(new Font("", Font.BOLD, 30));
		sid = new JTextField();

		sid.addKeyListener(new TextValidator());
		sid.setBackground(Color.BLACK);
		sid.setForeground(Color.WHITE);
		sid.setFont(new Font("", Font.ROMAN_BASELINE, 30));
		sid.addActionListener(this);

		p1.add(idlbl, BorderLayout.WEST);
		p1.add(sid, BorderLayout.CENTER);

		search = new JButton("Search");
		search.addActionListener(this);
		search.setForeground(Color.WHITE);
		search.setBackground(Color.BLUE);
		p1.add(search, BorderLayout.EAST);
		// JPanel p3 = new TranslucentJPanel(Color.BLUE);
		JPanel p3 = new JPanel();
		p3.setBackground(Color.WHITE);
		p3.setLayout(new BorderLayout());
		p3.add(p1, BorderLayout.NORTH);

		JPanel topp = new TranslucentJPanel1(Color.BLUE);
		topp.setLayout(new FlowLayout());
		JLabel toplbl = new JLabel("Find Any Transaction Details For Receipt Reprinting".toUpperCase());
		toplbl.setFont(new java.awt.Font("", Font.BOLD, 20));
		toplbl.setForeground(Color.WHITE);
		topp.add(toplbl, SwingConstants.CENTER);

		this.add(topp);
		this.add(p3);

		area = new JTextArea();
		area.setEditable(false);
		area.setLineWrap(true);
		area.setMargin(new Insets(30, 30, 30, 30));
		area.setBackground(Color.BLACK);
		area.setForeground(Color.white);
		area.setFont(new java.awt.Font("", Font.ROMAN_BASELINE, 19));
		area.getCaret().setSelectionVisible(true);
		area.getCaret().setVisible(true);

		JScrollPane scroller = new JScrollPane(area);
		scroller.setBorder(null);

		JPanel mainpanel = new JPanel(new GridLayout(1, 2));
		mainpanel.add(p3);
		mainpanel.add(scroller);
		reprint = new JButton("<html><h1>Re-Print</h1></html>");
		reprint.setBackground(new Color(10, 70, 90));
		reprint.setForeground(Color.WHITE);
		reprint.addActionListener(this);
		this.add(topp, BorderLayout.NORTH);
		this.add(mainpanel, BorderLayout.CENTER);
		this.add(reprint, BorderLayout.SOUTH);

	}

	public void insertTab() {
		EventQueue.invokeLater(() -> {
			int numberoftabs = tabs.getTabCount();
			boolean exist = false;
			for (int a = 0; a < numberoftabs; a++) {
				if (tabs.getTitleAt(a).trim().equals("Reprints")) {
					exist = true;
					break;
				}
			}
			if (!exist) {
				tabs.addTab("Reprints   ", null, this, "Run queries, find transactions and reprint,e.t.c");
				tabs.setSelectedIndex(numberoftabs);
				sid.requestFocusInWindow();
			}

		});
	}

	public void actualReceipt() {
		String query = "SELECT * FROM cashpay WHERE receiptno = '" + idstring + "'";
		area.append("Items\t\tQ\t$");
		area.append("\n-----------------------------------------------------------------------\n");
		try {

			rs1 = stmt.executeQuery(query);
			while (rs1.next()) {
				// int cashierid = rs1.getInt(2);
				String product_name = rs1.getString(3);
				int quantity = rs1.getInt(4);
				double amount = rs1.getDouble(5);

				String text = product_name + "\t\t" + quantity + "\t" + amount + "\n";
				area.append(text);
			}
			area.append("\n-----------------------------------------------------------------------\n");
			String query1 = "SELECT * FROM receipt_amount WHERE receiptno = '" + idstring + "'";
			rs = stm.executeQuery(query1);
			rs.next();
			String rc = "ReceiptNo :RC" + rs.getInt(1) + "\n";
			String tot = "Total Cost \t\t\t$" + rs.getDouble(2) + "\n";
			String ren = "Cash Tendered \t\t$" + rs.getDouble(3) + "\n";
			String ch = "Change \t\t\t$" + rs.getDouble(4) + "\n";
			String change_collected = "Once Off Change Collected \t\t$" + rs.getDouble(5);
			String timestamp = "Dated " + rs.getString(6) + " " + rs.getString(7) + "\n";
			area.append(timestamp);
			area.append(rc);
			area.append(tot);
			area.append(ren);
			area.append(ch);
			area.append(change_collected);
			area.append("\n\n\t------WITH THANKS------\n");
			area.append("\t----Wellington Mapiku------");

		} catch (Exception e) {
			// e.printStackTrace(System.err);
			JOptionPane.showMessageDialog(frame, "No Data for Receipt " + idstring, "Warning",
					JOptionPane.WARNING_MESSAGE);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == search || e.getSource() == sid) {
			idstring = sid.getText();
			if (!idstring.equals("")) {
				area.setText("");
				actualReceipt();
			} else {
				JOptionPane.showMessageDialog(frame, "Receipt Number is empty", "Empty Field",
						JOptionPane.ERROR_MESSAGE);
			}
		}
		if (e.getSource() == reprint) {
			// JFrame frame = new JFrame();
			// frame.getContentPane().add(area, BorderLayout.CENTER);
			// frame.pack();
			// area.setFont(new Font("Dialog", Font.PLAIN, 11));
			// area.setBackground(Color.WHITE);
			// area.setForeground(Color.BLACK);
			Printer p = new Printer(area);
			p.send();
		}
	}
}
