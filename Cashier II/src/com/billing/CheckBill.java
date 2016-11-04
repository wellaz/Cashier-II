package com.billing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.border.TitledBorder;

import com.deco.AnimateDialog;
import com.deco.BlinkingLabel;
import com.deco.ContainerBorders;

import helpers.DoubleForm;
import helpers.SetDateCreated;
import helpers.TextValidator;
import helpers.ThisMonth;

/**
 *
 * @author Wellington
 */
// this class is used for checking the Bill, if it does not exist, the billing
// interface will
public class CheckBill {
	ResultSet rs;
	Statement stm;
	GetDailyBillingFee getdailybilling;
	JButton newbilling, submit;
	JDialog dialog;
	private JPanel newpanel;

	public CheckBill(ResultSet rs, Statement stm) {
		this.rs = rs;
		this.stm = stm;
		getdailybilling = new GetDailyBillingFee(rs, stm);
	}

	// this creates a daily billing panel
	public JPanel createDailyBillingPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		TitledBorder border = new TitledBorder("Current Billing Information");
		panel.setBorder(border);
		panel.setBackground(Color.WHITE);
		String basicinfo = "----No Billing Information Was Found----\n";
		String date = "Effective From " + getdailybilling.getDate() + "\n";
		String amount = "Billing Fee $" + new DoubleForm().form(getdailybilling.getFee()) + "\n";
		String narration = "\n\n---Narration---\n\n" + getdailybilling.getNarration();
		JTextArea area = new JTextArea();
		area.setLineWrap(true);
		area.setWrapStyleWord(true);
		area.append(basicinfo + amount + date + narration);
		area.setBackground(Color.GRAY);
		area.setEditable(false);
		panel.add(new JScrollPane(area), BorderLayout.CENTER);
		newbilling = new JButton("New Billing");

		Box box = Box.createHorizontalBox();
		box.add(Box.createHorizontalGlue());
		box.add(newbilling);
		panel.add(box, BorderLayout.SOUTH);

		newbilling.addActionListener((e) -> {
			EventQueue.invokeLater(() -> {
				newpanel.setVisible(true);
				newbilling.setEnabled(false);
			});
		});
		return panel;
	}

	// creates a new Billing panel
	public JPanel newBillingPanel(JDialog dialog) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(Color.WHITE);
		TitledBorder border = new TitledBorder("New Billing Information");
		panel.setBorder(border);
		JLabel label = new BlinkingLabel("$");
		label.setFont(new Font("", Font.PLAIN, 25));
		JTextField field = new JTextField();
		field.addKeyListener(new TextValidator());
		field.setFont(new Font("", Font.PLAIN, 25));
		Box box = Box.createHorizontalBox();
		box.add(label);
		box.add(field);
		JTextArea area = new JTextArea();
		area.setLineWrap(true);
		area.setWrapStyleWord(true);
		area.setFont(new Font("", Font.PLAIN, 18));
		panel.add(box, BorderLayout.NORTH);
		Box vb = Box.createVerticalBox();
		JLabel why = new JLabel("Why is the Fee changed?");
		why.setFont(new Font("", Font.PLAIN, 20));
		vb.add(why);
		vb.add(new JScrollPane(area));

		panel.add(vb, BorderLayout.CENTER);
		// submit buttom
		submit = new JButton("Submit");
		submit.setFont(new Font("", Font.PLAIN, 20));
		Box lastBox = Box.createHorizontalBox();
		lastBox.add(Box.createHorizontalGlue());
		lastBox.add(submit);
		panel.add(lastBox, BorderLayout.SOUTH);

		submit.addActionListener((e) -> {
			if (!field.getText().equals("")) {
				double amount = Double.parseDouble(field.getText());
				String narration = area.getText();
				SetDateCreated setdate = new SetDateCreated();
				String date = setdate.getDate();
				String time = setdate.getTime();
				String month = ThisMonth.thisMonth();
				new PostDailyBillingFee(stm).postData(date, time, amount, month, narration);
				new AnimateDialog().fadeOut(dialog, 100);
				JOptionPane.showMessageDialog(null, "Done", "Daily Billing has been reconfigured",
						JOptionPane.INFORMATION_MESSAGE);
			} else {
				java.awt.Toolkit.getDefaultToolkit().beep();
			}
		});
		return panel;
	}

	public void makeDialog() {
		dialog = new JDialog((JFrame) null, "Billing Configuration");
		dialog.setUndecorated(true);
		dialog.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent evvt) {
				dialog.setShape(new RoundRectangle2D.Double(0, 0, dialog.getWidth(), dialog.getHeight(), 5, 5));
			}
		});

		dialog.getContentPane().setBackground(Color.WHITE);
		dialog.setLayout(new GridLayout(2, 1));
		newpanel = newBillingPanel(dialog);
		newpanel.setVisible(false);
		dialog.getContentPane().add(createDailyBillingPanel());
		dialog.getContentPane().add(newpanel);

		JMenuBar bar = new JMenuBar();
		dialog.setJMenuBar(bar);

		bar.add(new JMenu("BILLING"));
		bar.add(Box.createHorizontalGlue());
		JToolBar toolbar = new JToolBar();
		toolbar.setFloatable(false);

		toolbar.add(Box.createHorizontalGlue());
		bar.add(toolbar);
		dialog.setSize(500, 450);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension d = dialog.getSize();
		int x = (screen.width - d.width) / 2, y = (screen.height - d.height) / 2;
		dialog.setLocation(x, y);
		dialog.setAlwaysOnTop(true);
		dialog.getRootPane().setBorder(ContainerBorders.createBevelBorder());
		new AnimateDialog().fadeIn(dialog, 100);
	}

	public void hideDialog() {
		new AnimateDialog().fadeOut(dialog, 100);
	}

	public void check() {
		String text = "SELECT amount FROM daily_billing_settings";
		try {
			rs = stm.executeQuery(text);
			rs.last();
			int x = rs.getRow();
			if (x > 0) {
				sendBillindData();
			} else {
				EventQueue.invokeLater(() -> {
					makeDialog();
				});
			}
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
	}

	public void sendBillindData() {
		DailyBillingThread billingThread = new DailyBillingThread(stm, rs);
		billingThread.analyzeData();
	}

	public double getFee() {
		String query1 = "SELECT amount FROM daily_billing_settings";
		double fee = 0;
		try {
			rs = stm.executeQuery(query1);
			rs.last();
			fee = rs.getDouble(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fee;
	}
}
