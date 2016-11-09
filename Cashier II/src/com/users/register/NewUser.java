package com.users.register;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

import com.deco.TranslucentJPanel;
import com.deco.TranslucentJPanel1;

import helpers.SetDateCreated;

@SuppressWarnings("serial")
public class NewUser extends JPanel implements ActionListener {

	JTextField amounttxt, memberids;
	JButton submit;
	JLabel imagelbl;
	JTabbedPane tabs;
	ResultSet rs;
	Statement stm;
	JFrame frame;
	private JComboBox<Object> roles;
	private JTextField fullname;

	public NewUser(JTabbedPane tabs, ResultSet rs, Statement stm, JFrame frame) {
		this.tabs = tabs;
		this.stm = stm;
		this.rs = rs;
		this.frame = frame;
		this.setBackground(Color.WHITE);
		init();
	}

	public void init() {
		this.setLayout(new BorderLayout());
		JPanel topp = new JPanel(new FlowLayout());
		topp.setOpaque(false);
		JLabel toplbl = new JLabel("Submit All User Details");
		toplbl.setFont(new Font("", Font.BOLD, 19));
		topp.add(toplbl, SwingConstants.CENTER);

		JPanel midpanel = new JPanel(new GridLayout(4, 2, 1, 10));
		midpanel.setOpaque(false);
		JLabel acclbl = new JLabel("User Name", SwingConstants.CENTER);
		acclbl.setFont(new Font("", Font.BOLD, 15));
		JLabel amountlbl = new JLabel("Password", SwingConstants.CENTER);
		amountlbl.setFont(new Font("", Font.BOLD, 15));

		amounttxt = new JTextField();

		memberids = new JTextField();
		memberids.setFont(new Font("", Font.BOLD, 20));
		amounttxt.setFont(new Font("", Font.BOLD, 20));

		fullname = new JTextField();
		fullname.setFont(new Font("", Font.BOLD, 20));
		JLabel fn = new JLabel("Full Name", SwingConstants.CENTER);
		fn.setFont(new Font("", Font.BOLD, 15));

		int itemsarraysize = Roles.getRoles().size();
		Object[] seme = new String[itemsarraysize];
		for (int i = 0; i < itemsarraysize; i++) {
			seme[i] = Roles.getRoles().get(i).toUpperCase();
		}

		JLabel itemlbl = new JLabel("Role", SwingConstants.CENTER);
		itemlbl.setFont(new Font("", Font.BOLD, 15));
		// itemlbl.setForeground(Color.WHITE);

		roles = new JComboBox<>(seme);
		roles.setMaximumRowCount(15);
		roles.setFont(new Font("", Font.PLAIN, 20));

		midpanel.add(fn);
		midpanel.add(fullname);
		midpanel.add(acclbl);
		midpanel.add(memberids);
		midpanel.add(amountlbl);
		midpanel.add(amounttxt);
		midpanel.add(itemlbl);
		midpanel.add(roles);

		JPanel bp = new JPanel(new FlowLayout());
		bp.setOpaque(false);
		submit = new JButton("Proceed");
		submit.setFont(new Font("", Font.PLAIN, 30));
		bp.add(submit, SwingConstants.CENTER);
		submit.addActionListener(this);

		this.add(new TranslucentJPanel1(Color.BLUE).add(new JLabel("          ")), BorderLayout.WEST);
		this.add(new TranslucentJPanel(Color.BLUE).add(new JLabel("          ")), BorderLayout.EAST);
		this.add(topp, BorderLayout.NORTH);

		JPanel temp = new JPanel(new BorderLayout());
		temp.setOpaque(false);
		temp.setBorder(new EmptyBorder(150, 10, 10, 10));
		temp.add(midpanel, BorderLayout.NORTH);
		temp.add(bp, BorderLayout.EAST);
		this.add(temp, BorderLayout.CENTER);

	}

	public void insertTab() {
		EventQueue.invokeLater(() -> {
			boolean exist = false;
			int count = tabs.getTabCount();
			for (int x = 0; x < count; x++) {
				if (tabs.getTitleAt(x).trim().equals("New User")) {
					exist = true;
					tabs.setSelectedIndex(x);
					break;
				}
			}
			if (!exist) {
				tabs.addTab("New User   ", null, this, "Create New User");
				tabs.setSelectedIndex(count);
			}
		});
	}

	public void debitLedger(String uname, String pwd, String group, String full_name, String date, String time) {
		String query = "SELECT password FROM users WHERE password = '" + pwd + "'";
		try {
			rs = stm.executeQuery(query);
			if (rs.next()) {
				JOptionPane.showMessageDialog(frame,
						"The password characters already exist! Try another character combination.", "Warning",
						JOptionPane.WARNING_MESSAGE);
			} else {
				String text = "INSERT INTO users(usernames,password,group,full_name,date,time)VALUES('" + uname + "','"
						+ pwd + "','" + group + "','" + full_name + "','" + date + "','" + time + "')";
				stm.execute(text);
				JOptionPane.showMessageDialog(frame, "Done.",
						"Keep these login credentials\nUsername : " + uname + "\nPassword : " + pwd + "\nThank you!",
						JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
	}

	public class Worker extends SwingWorker<Void, Void> {
		JDialog dialog;
		JProgressBar prog;
		JButton hider;
		JLabel waitlbl;
		JTable table;

		public Worker() {
			dialog = new JDialog();
			dialog.setLayout(new BorderLayout());
			prog = new JProgressBar();
			dialog.setUndecorated(true);
			hider = new JButton("Run in Background");
			hider.addActionListener((ActionEvent event) -> {
				dialog.setVisible(false);
			});
			waitlbl = new JLabel("Processing....");
			dialog.addComponentListener(new ComponentAdapter() {
				@Override
				public void componentResized(ComponentEvent evvt) {
					dialog.setShape(new RoundRectangle2D.Double(0, 0, dialog.getWidth(), dialog.getHeight(), 5, 5));
				}
			});
			Box box = Box.createVerticalBox();
			box.add(waitlbl);
			box.add(prog);
			box.add(hider);
			dialog.getContentPane().setBackground(new Color(0.5f, 0.5f, 1f));
			dialog.getContentPane().add(box, BorderLayout.CENTER);
			dialog.setSize(300, 100);
			Dimension d = dialog.getSize(), screen = Toolkit.getDefaultToolkit().getScreenSize();
			int a = (screen.width - d.width) / 2, b = (screen.height - d.height) / 2;
			dialog.setLocation(a, b);
		}

		@Override
		protected Void doInBackground() throws Exception {
			prog.setIndeterminate(true);
			dialog.setVisible(true);
			proceed();
			return null;
		}

		@Override
		public void done() {
			prog.setIndeterminate(false);
			dialog.dispose();
		}
	}

	public void proceed() {
		String uname = memberids.getText();
		String pwd = amounttxt.getText();
		if (!(pwd.equals("") && uname.equals("") && fullname.getText().equals(""))) {
			String date = new SetDateCreated().getDate();
			String time = new SetDateCreated().getTime();
			String group = roles.getSelectedItem().toString().trim().toLowerCase();
			String full_name = fullname.getText();
			debitLedger(uname, pwd, group, full_name, date, time);
			memberids.setText("");
			amounttxt.setText("");
		} else
			JOptionPane.showMessageDialog(frame,
					"Null user credentials cannot be created \nThe system asks you to act in bona fides, to maintain Records Integrity Policy",
					"Warning", JOptionPane.WARNING_MESSAGE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == submit) {
			Worker w = new Worker();
			w.execute();
		}
	}
}