package tr.change.search;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import helpers.DoubleForm;
import helpers.IconImage;
import helpers.RemoveTab;
import helpers.TextValidator;

public class Change {

	ResultSet rs, rs1;
	Statement stm, stmt;
	DoubleForm df;
	private JButton batch;
	JTabbedPane tabs;
	private JDialog dialog;
	private JTextField amount;
	private JButton okButton;
	private JButton ocancelButton;
	private JLabel error;

	public Change(ResultSet rs, ResultSet rs1, Statement stm, Statement stmt, JTabbedPane tabs) {
		this.tabs = tabs;
		this.stm = stm;
		this.stmt = stmt;
		this.rs = rs;
		this.rs1 = rs1;
		df = new DoubleForm();
	}

	public void showDialog() {
		dialog = new JDialog((JFrame) null, "Search", true);
		dialog.setLayout(new BorderLayout());
		dialog.setIconImage(new IconImage().createIconImage());
		// dialog.setTitle("Change Suggestion");

		JLabel yeslbl = new JLabel("<html><h1>Enter Receipt Number?<h1>", SwingConstants.CENTER);
		//yeslbl.setForeground(Color.WHITE);

		Box b = Box.createVerticalBox();

		amount = new JTextField();
		amount.setFont(new Font("", Font.BOLD, 15));
		amount.addKeyListener(new TextValidator());
		b.add(yeslbl);
		b.add(amount);

		Box b3 = Box.createHorizontalBox();
		b3.add(b);
		JPanel midpanel = new JPanel(new GridLayout(3, 1));
		midpanel.setOpaque(false);
		midpanel.add(b3);

		Box buttonbox = Box.createHorizontalBox();

		midpanel.add(buttonbox);
		// dialog.getContentPane().setBackground(new Color(0.5f, 0.5f, 1f));
		dialog.getContentPane().setBackground(Color.WHITE);
		dialog.getContentPane().add(midpanel, BorderLayout.CENTER);
		dialog.getContentPane().add(yeslbl, BorderLayout.NORTH);

		okButton = new JButton("OK");
		error = new JLabel();
		okButton.addActionListener((event) -> {
			String numbr = amount.getText();
			if (!numbr.equals("")) {
				int receiptno = Integer.parseInt(numbr);
				dialog.dispose();
				ChangeTable t = new ChangeTable(rs, rs1, stm, stmt, tabs, receiptno);
				t.insertTab();
			} else {
				error.setText("Enter a receipt number!");
			}
		});
		ocancelButton = new JButton("Cancel");
		ocancelButton.setBackground(Color.MAGENTA);
		ocancelButton.setForeground(Color.WHITE);
		ocancelButton.addActionListener((event) -> {
			dialog.dispose();
		});
		batch = new JButton("Uncollected Change List");
		batch.setBackground(Color.BLUE);
		batch.setForeground(Color.WHITE);

		batch.addActionListener((event) -> {
			dialog.dispose();
			new RemoveTab(tabs).removeTab("Outstanding Change List");
			AllChangeList t = new AllChangeList(rs, rs1, stm, stmt, tabs);
			t.insertTab();
		});
		buttonbox.add(Box.createHorizontalGlue());
		buttonbox.add(okButton);
		buttonbox.add(Box.createHorizontalStrut(30));
		buttonbox.add(ocancelButton);
		buttonbox.add(Box.createHorizontalStrut(30));
		buttonbox.add(batch);
		dialog.getRootPane().setDefaultButton(okButton);

		error.setFont(new Font("", Font.BOLD, 15));
		error.setForeground(Color.RED);
		midpanel.add(error);

		dialog.setSize(400, 200);
		dialog.setResizable(false);
		Dimension d = dialog.getSize(), screen = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screen.width - d.width) / 2, y = (screen.height - d.height) / 2;
		dialog.setLocation(x, y);
		dialog.setVisible(true);
		dialog.setAlwaysOnTop(true);
	}

}
