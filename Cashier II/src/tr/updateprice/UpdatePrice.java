package tr.updateprice;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import helpers.DoubleForm;
import helpers.GetProductNames;
import helpers.IconImage;
import helpers.SetDateCreated;
import helpers.TextValidator;
import tr.temporarydata.unitprice.GetUnitPrice;

/**
 *
 * @author Wellington
 */
public class UpdatePrice {
	private JDialog dialog;
	private JButton okButton;
	private JButton ocancelButton;
	private JTextField amount;
	private JComboBox<Object> itemscombo;

	ResultSet rs, rs1;
	Statement stm, stmt;
	DoubleForm df;
	private JButton batch;
	JTabbedPane tabs;

	public UpdatePrice(ResultSet rs, ResultSet rs1, Statement stm, Statement stmt, JTabbedPane tabs) {
		this.tabs = tabs;
		this.stm = stm;
		this.stmt = stmt;
		this.rs = rs;
		this.rs1 = rs1;
		df = new DoubleForm();
	}

	public void showDialog() {
		dialog = new JDialog((JFrame) null, "Price Change", true);
		dialog.setLayout(new BorderLayout());
		dialog.setIconImage(new IconImage().createIconImage());
		// dialog.setTitle("Change Suggestion");

		JLabel top = new JLabel("<html><h1>Price Changing?<h1>", SwingConstants.CENTER);
		// top.setForeground(Color.WHITE);

		Box b = Box.createVerticalBox();
		JLabel yeslbl = new JLabel("$", SwingConstants.CENTER);
		yeslbl.setFont(new Font("", Font.BOLD, 19));
		amount = new JTextField();
		amount.setFont(new Font("", Font.PLAIN, 20));
		amount.addKeyListener(new TextValidator());
		b.add(yeslbl);
		b.add(amount);

		Box b1 = Box.createVerticalBox();
		JLabel nolbl = new JLabel("Select Item", SwingConstants.CENTER);
		nolbl.setFont(new Font("", Font.BOLD, 19));

		int itemsarraysize = new GetProductNames(rs, stm).getClassNames().size();
		Object[] seme = new String[itemsarraysize];
		for (int i = 0; i < itemsarraysize; i++) {
			seme[i] = new GetProductNames(rs, stm).getClassNames().get(i);
		}

		itemscombo = new JComboBox<>(seme);
		itemscombo.setFont(new Font("", Font.PLAIN, 20));
		itemscombo.addItemListener((e) -> {
			Object item = e.getItem();
			if (e.getStateChange() == ItemEvent.SELECTED) {
				String selecteditem = item.toString();
				double unitprice = new GetUnitPrice(rs, stm).getunitPrice(selecteditem);
				String labeltext = "" + unitprice;
				EventQueue.invokeLater(() -> {
					amount.setText(labeltext);
				});

			} else if (e.getStateChange() == ItemEvent.DESELECTED) {
				// Item has been deselected
				// System.out.println(item + " has been deselected");
			}
		});

		// no.setSelected(true);
		b1.add(nolbl);
		b1.add(itemscombo);

		Box b3 = Box.createHorizontalBox();
		b3.add(Box.createHorizontalStrut(30));
		b3.add(b1);
		b3.add(Box.createHorizontalStrut(80));
		b3.add(b);
		JPanel midpanel = new JPanel(new GridLayout(3, 1));
		midpanel.setOpaque(false);
		midpanel.add(top);
		midpanel.add(b3);

		Box buttonbox = Box.createHorizontalBox();

		midpanel.add(buttonbox);
		// dialog.getContentPane().setBackground(new Colors().getColorOne());
		dialog.getContentPane().setBackground(Color.WHITE);
		dialog.getContentPane().add(midpanel, BorderLayout.CENTER);

		okButton = new JButton("OK");
		okButton.requestFocusInWindow();
		okButton.addActionListener((event) -> {
			String product_name = itemscombo.getSelectedItem().toString();

			SetDateCreated setdate = new SetDateCreated();
			String date = setdate.getDate();
			String time = setdate.getTime();
			Update update = new Update(rs, rs1, stm, stmt);
			if (!amount.getText().equals("")) {
				double price = Double.parseDouble(amount.getText());
				int confirm = JOptionPane.showConfirmDialog(dialog, "Change " + product_name + "'s price to $" + price,
						"Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (confirm == JOptionPane.YES_OPTION) {
					update.update(product_name, df.form(price), date, time);
				} else {
				}
			} else {
				JOptionPane.showMessageDialog(dialog, "Null price rejected!", "Rejected", JOptionPane.ERROR_MESSAGE);
			}
		});
		ocancelButton = new JButton("Cancel");
		ocancelButton.setBackground(Color.MAGENTA);
		ocancelButton.setForeground(Color.WHITE);
		ocancelButton.addActionListener((event) -> {
			dialog.dispose();
		});
		batch = new JButton("Batch Update");
		batch.setBackground(Color.BLUE);
		batch.setForeground(Color.WHITE);
		batch.addActionListener((event) -> {
			dialog.dispose();
			Table t = new Table(rs, rs1, stm, stmt, tabs);
			t.insertTab();
		});
		buttonbox.add(Box.createHorizontalGlue());
		buttonbox.add(okButton);
		buttonbox.add(Box.createHorizontalStrut(30));
		buttonbox.add(ocancelButton);
		buttonbox.add(Box.createHorizontalStrut(30));
		buttonbox.add(batch);
		dialog.getRootPane().setDefaultButton(okButton);

		dialog.setSize(600, 300);
		// dialog.setResizable(false);
		Dimension d = dialog.getSize(), screen = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screen.width - d.width) / 2, y = (screen.height - d.height) / 2;
		dialog.setLocation(x, y);
		dialog.setVisible(true);
		dialog.setAlwaysOnTop(true);
	}
}
