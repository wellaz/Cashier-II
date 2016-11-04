/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tr.receipt.reprint;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import helpers.SetDateCreated;
import helpers.TextValidator;

/**
 *
 * @author Wellington
 */
@SuppressWarnings("serial")
public class StartofdayPostings extends JPanel implements ActionListener {

	JTextField cashierid, amount;
	String amtstring, idstring;
	JButton submit;
	JLabel error;
	ResultSet rs, rs1;
	Statement stm, stmt;
	JTabbedPane tabs;
	JFrame frame;

	public StartofdayPostings(ResultSet rs, ResultSet rs1, Statement stm, Statement stmt, JTabbedPane tabs,
			JFrame frame) {
		this.rs = rs;
		this.rs1 = rs1;
		this.stm = stm;
		this.stmt = stmt;
		this.tabs = tabs;
		this.frame = frame;
		init();
	}

	public final void init() {
		cashierid = new JTextField();
		amount = new JTextField();
		cashierid = new JTextField();
		cashierid.addKeyListener(new TextValidator());
		amount = new JTextField();
		amount.addKeyListener(new TextValidator());
		amount.addActionListener(this);
		submit = new JButton("Post");
		submit.addActionListener(this);
		JLabel idlbl = new JLabel("Teller / Cashier ID:");
		idlbl.setFont(new Font("", Font.BOLD, 13));
		JLabel amlbl = new JLabel("Start Of Day Amount $:");
		amlbl.setFont(new Font("", Font.BOLD, 13));
		error = new JLabel();
		error.setForeground(Color.RED);

		JPanel p = new JPanel(new GridLayout(3, 2));
		p.add(idlbl);
		p.add(cashierid);
		p.add(amlbl);
		p.add(amount);
		p.add(error);
		p.add(submit);
		this.setLayout(new GridBagLayout());
		this.add(p);
	}

	public void insertTab() {
		EventQueue.invokeLater(() -> {
			int numberoftabs = tabs.getTabCount();
			boolean exist = false;
			for (int a = 0; a < numberoftabs; a++) {
				if (tabs.getTitleAt(a).trim().equals("Start Of Day Postings")) {
					exist = true;
					break;
				}
			}
			if (!exist) {
				tabs.addTab("Start Of Day Postings   ", null, this, "Posting Start Of Day");
				tabs.setSelectedIndex(numberoftabs);
			}
		});
	}

	public void submit() {
		String query = "SELECT * FROM startofday WHERE cashierid  = '" + idstring + "' AND date = '"
				+ new SetDateCreated().getDate() + "' AND amount = '" + amtstring + "'";
		try {
			rs = stm.executeQuery(query);
			rs.next();
			double amo = rs.getDouble(4);

			String query1 = "SELECT * FROM ledger";
			rs1 = stmt.executeQuery(query1);
			rs1.last();
			double bal = rs1.getDouble(5);
			double sum = bal - amo;
			int dep = 0;
			String details = "Cashier id " + idstring + " Start of day cash Withdrawal.Approved By supervisor";
			String query11 = "INSERT INTO ledger(date,time,deposit,withdrawal,balance,details)VALUES('"
					+ new SetDateCreated().getDate() + "','" + new SetDateCreated().getTime() + "','" + dep + "','"
					+ amo + "','" + sum + "','" + details + "')";
			stm.execute(query11);
		} catch (Exception ee) {
			JOptionPane.showMessageDialog(frame, "Amount rejected. \nIt does not match cashier id Start Of Day amount",
					"Amount Do Not Honor", JOptionPane.ERROR_MESSAGE);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		amtstring = amount.getText();
		idstring = cashierid.getText();
		if (e.getSource() == submit) {
			submit();
		}
	}

}
