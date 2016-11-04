package expenses.post;

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

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import helpers.MonthsList;
import helpers.SetDateCreated;
import helpers.TextValidator;
import helpers.ThisMonth;
import tr.ledger.AffectLedger;

/**
 * @author Wellington
 *
 */
@SuppressWarnings("serial")
public class PostExpense extends JPanel implements ActionListener {
	JTextField amounttxt;
	JTextArea area;
	JButton submit;
	JLabel imagelbl;
	JTabbedPane tabs;
	ResultSet rs;
	Statement stm;
	JFrame frame;
	private JComboBox<Object> months;

	public PostExpense(JTabbedPane tabs, ResultSet rs, Statement stm, JFrame frame) {
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
		JLabel toplbl = new JLabel("Submit  Details of An Expense ");
		// toplbl.setForeground(Color.WHITE);
		toplbl.setFont(new Font("", Font.BOLD, 25));
		topp.add(toplbl, SwingConstants.CENTER);

		JPanel midpanel = new JPanel();
		midpanel.setBackground(Color.WHITE);
		midpanel.setLayout(new GridLayout(3, 2, 1, 10));

		JLabel amountlbl = new JLabel("Amount :");
		// amountlbl.setForeground(Color.WHITE);
		amountlbl.setFont(new Font("", Font.BOLD, 25));

		JLabel monthlbl = new JLabel("Month Of (month) :");
		// monthlbl.setForeground(Color.WHITE);
		monthlbl.setFont(new Font("", Font.BOLD, 25));

		amounttxt = new JTextField();
		amounttxt.setFont(new Font("", Font.PLAIN, 25));
		amounttxt.addKeyListener(new TextValidator());

		Object[] da = new String[MonthsList.getMonths().size()];
		for (int i = 0; i < MonthsList.getMonths().size(); i++) {
			da[i] = MonthsList.getMonths().get(i);
		}

		months = new JComboBox<>(da);
		months.setFont(new Font("", Font.PLAIN, 25));
		months.setSelectedItem(ThisMonth.thisMonth());

		Box box = Box.createVerticalBox();
		JLabel natlbl = new JLabel("Description :(nature of the expense)");
		// natlbl.setForeground(Color.WHITE);
		natlbl.setFont(new Font("", Font.BOLD + Font.ITALIC, 20));

		area = new JTextArea();
		area.setLineWrap(true);
		area.setWrapStyleWord(true);
		area.setFont(new Font("", Font.PLAIN, 18));

		midpanel.add(amountlbl);
		midpanel.add(amounttxt);
		midpanel.add(monthlbl);
		midpanel.add(months);

		midpanel.add(new JLabel(""));
		box.add(natlbl);
		box.add(new JScrollPane(area));
		midpanel.add(box);

		JPanel bp = new JPanel();
		bp.setBackground(Color.WHITE);
		bp.setLayout(new FlowLayout());
		submit = new JButton("Submit \u2193");
		submit.setFont(new Font("", Font.PLAIN, 20));
		bp.add(submit, SwingConstants.CENTER);
		submit.addActionListener(this);

		this.add(new JPanel().add(new JLabel("          ")), BorderLayout.WEST);
		this.add(new JPanel().add(new JLabel("          ")), BorderLayout.EAST);
		this.add(topp, BorderLayout.NORTH);

		JPanel temp = new JPanel();
		temp.setLayout(new BorderLayout());
		temp.setBackground(Color.WHITE);
		temp.add(midpanel, BorderLayout.NORTH);
		temp.add(bp, BorderLayout.EAST);
		this.add(temp, BorderLayout.CENTER);

	}

	public void insertTab() {
		EventQueue.invokeLater(() -> {
			boolean exist = false;
			int count = tabs.getTabCount();
			for (int x = 0; x < count; x++) {
				if (tabs.getTitleAt(x).trim().equals("Expenses")) {
					exist = true;
					tabs.setSelectedIndex(x);
					break;
				}
			}
			if (!exist) {
				tabs.addTab("Expenses   ", null, this, "Post Expenses");
				tabs.setSelectedIndex(count);
			}
		});
	}

	public void postings(double amount, String date, String time, String year, String month, String description) {
		try {
			String query = "INSERT INTO expenses(amount,date,time,year,month,details)VALUES('" + amount + "','" + date
					+ "','" + time + "','" + year + "','" + month + "','" + description + "')";
			stm.execute(query);

			AffectLedger aff = new AffectLedger(rs, stm);
			aff.debitLedger(date, time, month, year, amount, description);

		} catch (SQLException ee) {
			ee.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == submit) {
			String amount = amounttxt.getText();
			String date = new SetDateCreated().getDate();
			String time = new SetDateCreated().getTime();
			String year = new SetDateCreated().getYear();
			String month = months.getSelectedItem().toString();
			String description = area.getText();

			if (!amount.equals("")) {
				postings(Double.parseDouble(amount), date, time, year, month, description);
				JOptionPane.showMessageDialog(frame, "$" + amount + " expense successfully posted!\nDONE!",
						"Information", JOptionPane.INFORMATION_MESSAGE);
				amounttxt.setText("");
				area.setText("");
			} else
				JOptionPane.showMessageDialog(frame, "Null value cannot be submitted ", "Warning",
						JOptionPane.WARNING_MESSAGE);
		}

	}

}
