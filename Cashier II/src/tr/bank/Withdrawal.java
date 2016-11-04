package tr.bank;

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
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.deco.TranslucentJPanel;
import com.deco.TranslucentJPanel1;

import helpers.MonthsList;
import helpers.SetDateCreated;
import helpers.TextValidator;
import tr.ledger.AffectLedger;

@SuppressWarnings("serial")
public class Withdrawal extends JPanel implements ActionListener {

	JTextField amounttxt;
	JComboBox<Object> months;
	JButton submit;
	JLabel imagelbl;
	JTabbedPane tabs;
	ResultSet rs;
	Statement stm;
	JFrame frame;

	public Withdrawal(JTabbedPane tabs, ResultSet rs, Statement stm, JFrame frame) {
		this.tabs = tabs;
		this.stm = stm;
		this.rs = rs;
		this.frame = frame;
		init();
	}

	public void init() {
		this.setLayout(new BorderLayout());
		JPanel topp = new TranslucentJPanel(Color.BLUE);
		topp.setLayout(new FlowLayout());
		JLabel toplbl = new JLabel("Submit All Withdrawal Details");
		toplbl.setForeground(Color.WHITE);
		toplbl.setFont(new Font("", Font.BOLD, 19));
		topp.add(toplbl, SwingConstants.CENTER);

		JPanel midpanel = new TranslucentJPanel(Color.BLACK);
		midpanel.setLayout(new GridLayout(5, 2, 1, 10));
		JLabel acclbl = new JLabel("Month Of");
		acclbl.setForeground(Color.WHITE);
		acclbl.setFont(new Font("", Font.BOLD, 15));
		JLabel amountlbl = new JLabel("Amount :");
		amountlbl.setForeground(Color.WHITE);
		amountlbl.setFont(new Font("", Font.BOLD, 15));

		amounttxt = new JTextField();
		amounttxt.addKeyListener(new TextValidator());

		Object[] da = new String[MonthsList.getMonths().size()];
		for (int i = 0; i < MonthsList.getMonths().size(); i++) {
			da[i] = MonthsList.getMonths().get(i);
		}

		months = new JComboBox<>(da);
		int whichmonth = new SetDateCreated().getMonth();
		months.setSelectedIndex(whichmonth - 1);

		midpanel.add(acclbl);
		midpanel.add(months);
		midpanel.add(amountlbl);
		midpanel.add(amounttxt);
		midpanel.add(new JLabel());
		midpanel.add(new JLabel());

		JPanel bp = new TranslucentJPanel(Color.BLUE);
		bp.setLayout(new FlowLayout());
		submit = new JButton("Proceed \u2192");
		bp.add(submit, SwingConstants.CENTER);
		submit.addActionListener(this);

		this.add(new TranslucentJPanel1(Color.BLUE).add(new JLabel("          ")), BorderLayout.WEST);
		this.add(new TranslucentJPanel(Color.BLUE).add(new JLabel("          ")), BorderLayout.EAST);
		this.add(topp, BorderLayout.NORTH);

		JPanel temp = new TranslucentJPanel1(Color.BLUE);
		temp.setLayout(new BorderLayout());
		temp.setOpaque(true);
		temp.add(midpanel, BorderLayout.NORTH);
		temp.add(bp, BorderLayout.EAST);
		this.add(temp, BorderLayout.CENTER);
	}

	public void insertTab() {
		EventQueue.invokeLater(() -> {
			boolean exist = false;
			int count = tabs.getTabCount();
			for (int x = 0; x < count; x++) {
				if (tabs.getTitleAt(x).trim().equals("Revenue Withdrawal")) {
					exist = true;
					tabs.setSelectedIndex(x);
					break;
				}
			}
			if (!exist) {
				tabs.addTab("Revenue Withdrawal   ", null, this, "Withdrawal");
				tabs.setSelectedIndex(count);
			}
		});
	}

	public void processWithdrawal(String date, String time, String month, String year, double debit, String details) {
		AffectLedger a = new AffectLedger(rs, stm);
		a.debitLedger(date, time, month, year, debit, details);
		postWithdrawal(debit, date, time, month);
		amounttxt.setText("");
	}

	public void postWithdrawal(double debit, String date, String time, String month) {
		String text = "INSERT INTO bank_with(amount,date,time,month)VALUES('" + debit + "','" + date + "','" + time
				+ "','" + month + "')";
		try {
			stm.execute(text);
		} catch (SQLException ee) {
			ee.printStackTrace(System.err);
		}
	}

	public void proceed() {
		String amount = amounttxt.getText();
		double debit = Double.parseDouble(amount);
		String month = months.getSelectedItem().toString();
		String date = new SetDateCreated().getDate();
		String time = new SetDateCreated().getTime();
		String description = "Withdrawal from Equation";
		String year = new SetDateCreated().getYear();
		if (!(amount.equals(""))) {
			processWithdrawal(date, time, month, year, debit, description);
			JOptionPane.showMessageDialog(frame, "A Withdrawal of $" + amount + " is validated.\nDONE!", "Information ",
					JOptionPane.INFORMATION_MESSAGE);
		} else
			JOptionPane.showMessageDialog(frame, "Null value cannot be submitted ", "Warning",
					JOptionPane.WARNING_MESSAGE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == submit) {
			proceed();
		}

	}

}
