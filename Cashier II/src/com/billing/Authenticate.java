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
import java.sql.Statement;
import java.util.Arrays;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import com.deco.AnimateDialog;
import com.deco.BlinkingLabel;
import com.deco.ContainerBorders;

import helpers.IconImage;

/**
 *
 * @author Wellington
 */

public class Authenticate {

	private JPasswordField passwordField;
	private JDialog dialog;
	private JButton go;
	private String password;
	Statement stm;
	ResultSet rs;

	public Authenticate(Statement stm, ResultSet rs) {
		this.rs = rs;
		this.stm = stm;
	}

	public JPanel createMidPanel() {
		JPanel panel = new JPanel(new GridLayout(3, 1));
		panel.setBackground(Color.WHITE);
		panel.setBorder(new TitledBorder(""));
		passwordField = new JPasswordField();
		passwordField.setFont(new Font("", Font.PLAIN, 20));
		passwordField.setEchoChar('*');
		passwordField.setToolTipText("Enter the password here.");
		passwordField.setForeground(Color.RED);
		passwordField.addActionListener((e) -> {
			createListener();
		});
		JLabel code = new BlinkingLabel("Password");
		code.setFont(new Font("", Font.PLAIN, 18));
		code.setForeground(Color.RED);
		panel.add(code, SwingConstants.CENTER);
		panel.add(passwordField);
		go = new JButton("OK");
		go.addActionListener((e) -> {
			createListener();
		});
		Box box = Box.createHorizontalBox();
		box.add(Box.createHorizontalGlue());
		box.add(go);
		panel.add(box);

		return panel;
	}

	public void createDialog() {
		dialog = new JDialog((JFrame) null, "Billing Authorization");
		dialog.setUndecorated(true);

		dialog.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent evvt) {
				dialog.setShape(new RoundRectangle2D.Double(0, 0, dialog.getWidth(), dialog.getHeight(), 5, 5));
			}
		});

		dialog.getContentPane().setBackground(Color.WHITE);
		dialog.setLayout(new BorderLayout());
		JPanel newpanel = createMidPanel();
		dialog.getContentPane().add(newpanel);
		dialog.getRootPane().setDefaultButton(go);

		JMenuBar bar = new JMenuBar();
		dialog.setJMenuBar(bar);

		bar.add(new JMenu("BILLING"));
		bar.add(Box.createHorizontalGlue());
		JToolBar toolbar = new JToolBar();
		toolbar.setFloatable(false);
		JButton close = new JButton(new ImageIcon(new IconImage().createCloseImage()));
		close.addActionListener((event) -> {
			new AnimateDialog().fadeOut(dialog, 100);
		});
		toolbar.add(Box.createHorizontalGlue());
		toolbar.add(close);
		bar.add(toolbar);
		dialog.setSize(300, 155);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension d = dialog.getSize();
		int x = (screen.width - d.width) / 2, y = (screen.height - d.height) / 2;
		dialog.setLocation(x, y);
		dialog.getRootPane().setBorder(ContainerBorders.createBevelBorder());

		new AnimateDialog().fadeIn(dialog, 100);
		passwordField.requestFocusInWindow();
		dialog.setAlwaysOnTop(true);
	}

	public void createListener() {
		setPassword("awesome");
		char[] p = passwordField.getPassword();
		char[] pass = getThePassword().toCharArray();

		if (passwordField.getPassword().equals("")) {
			java.awt.Toolkit.getDefaultToolkit().beep();
		} else {
			if (Arrays.equals(p, pass)) {
				Chooser chooser = new Chooser(rs, stm);
				EventQueue.invokeLater(() -> {
					chooser.createDialog();
					hideDialog();
				});
			} else {
				java.awt.Toolkit.getDefaultToolkit().beep();
				passwordField.setText("");
			}
		}
	}

	private void setPassword(String password) {
		this.password = password;
	}

	public String getThePassword() {
		return password;
	}

	public void hideDialog() {
		new AnimateDialog().fadeOut(dialog, 100);
	}
}
