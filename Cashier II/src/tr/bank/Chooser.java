package tr.bank;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;

import com.itextpdf.text.Font;

import helpers.IconImage;

public class Chooser {
	JTabbedPane tabs;
	ResultSet rs;
	Statement stm;
	JFrame frame;

	public Chooser(JTabbedPane tabs, ResultSet rs, Statement stm, JFrame frame) {
		this.tabs = tabs;
		this.stm = stm;
		this.rs = rs;
		this.frame = frame;
	}

	public void showDialog() {
		JDialog dialog = new JDialog((JFrame) null, "Choose", true);
		dialog.setLayout(new BorderLayout());
		dialog.setIconImage(new IconImage().createIconImage());

		JLabel top = new JLabel("<html><h2>Specify Transaction Type<i>(by selecting from below)</i><h2></html>");
		top.setForeground(Color.RED);

		JRadioButton dep = new JRadioButton(
				"<html><h3>Cash Deposit Into Cashier II<i>(INTO RS Account)</i></h3></html>");
		JRadioButton with = new JRadioButton(
				"<html><h3>Cash Withdrawal From Cashier II<i>(FROM RS Account)</i></h3></html>");
		ButtonGroup gr = new ButtonGroup();
		gr.add(dep);
		gr.add(with);

		JButton go = new JButton("Proceed \u2192");

		JLabel error = new JLabel();
		error.setForeground(Color.red);
		error.setFont(new java.awt.Font("", Font.ITALIC, 15));

		// JPanel midpanel = new TranslucentJPanel(Color.BLUE);
		JPanel midpanel = new JPanel();
		midpanel.setBackground(Color.WHITE);
		midpanel.setLayout(new GridLayout(9, 1));
		midpanel.add(top);
		midpanel.add(Box.createVerticalStrut(20));
		midpanel.add(dep);
		midpanel.add(Box.createVerticalStrut(20));
		midpanel.add(with);
		midpanel.add(Box.createVerticalStrut(20));
		midpanel.add(go);
		midpanel.add(Box.createVerticalStrut(20));
		midpanel.add(error);

		go.addActionListener((event) -> {
			error.setText("");
			if (dep.isSelected()) {
				Deposit deposit = new Deposit(tabs, rs, stm, frame);
				EventQueue.invokeLater(() -> {
					deposit.insertTab();
					dialog.dispose();
				});

			} else if (with.isSelected()) {
				Withdrawal withdraw = new Withdrawal(tabs, rs, stm, frame);
				EventQueue.invokeLater(() -> {
					withdraw.insertTab();
					dialog.dispose();
				});
			} else {
				error.setText("Null selection cannot be processed!");
			}
		});

		dialog.getContentPane().add(midpanel, BorderLayout.CENTER);

		dialog.setSize(500, 300);
		Dimension d = dialog.getSize(), screen = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screen.width - d.width) / 2, y = (screen.height - d.height) / 2;
		dialog.setLocation(x, y);
		dialog.setVisible(true);
		dialog.setAlwaysOnTop(true);

	}
}
