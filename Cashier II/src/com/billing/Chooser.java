package com.billing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.border.TitledBorder;

import com.deco.AnimateDialog;
import com.deco.ContainerBorders;

import buttons.RoundButton;
import helpers.AccessDbase;
import helpers.Colors;
import helpers.IconImage;

/**
 *
 * @author Wellington
 */

public class Chooser {

	private RoundButton dailybillSett;
	private RoundButton monthlybillSett;
	private RoundButton dailybillrep;
	private RoundButton monthlybillRep;
	private JDialog dialog;
	Statement stm;
	ResultSet rs;

	public Chooser(ResultSet rs, Statement stm) {
		this.rs = rs;
		this.stm = stm;
	}

	public JPanel createPanel() {
		JPanel panel = new JPanel(new GridLayout(2, 1));

		panel.setBackground(Color.WHITE);
		JPanel panel1 = new JPanel(new BorderLayout());
		panel1.setOpaque(false);
		panel1.setBorder(new TitledBorder("Daily Biling"));
		JPanel panel2 = new JPanel(new BorderLayout());
		panel2.setOpaque(false);
		panel2.setBorder(new TitledBorder("Monthly Billing"));
		Box box1 = Box.createHorizontalBox();
		panel1.add(box1);
		Box box2 = Box.createHorizontalBox();
		panel2.add(box2);
		dailybillSett = new RoundButton("<html><h2>Daily<br>Bill<br>Settings</h2></html>");
		dailybillSett.setBackground(getGrayColor());
		dailybillSett.addActionListener((e) -> {
			DailyBillingSettings dbs = new DailyBillingSettings(rs, stm);
			EventQueue.invokeLater(() -> {
				dbs.makeDialog();
				hideDialog();
			});
		});
		dailybillrep = new RoundButton("<html><h2>Daily<br>Bill<br>Report</h2></html>");
		dailybillrep.setBackground(getGrayColor());
		dailybillrep.addActionListener((e) -> {
			EventQueue.invokeLater(() -> {
				hideDialog();
			});
		});
		monthlybillSett = new RoundButton("<html><h2>Monthly<br>Bill<br>Settings</h2></html>");
		monthlybillSett.setBackground(getGrayColor());
		monthlybillSett.addActionListener((e) -> {
			MonthlyBillingSettings mbs = new MonthlyBillingSettings(rs, stm);
			EventQueue.invokeLater(() -> {
				mbs.makeDialog();
				hideDialog();
			});
		});
		monthlybillRep = new RoundButton("<html><h2>Monthly<br>Bill<br>Report</h2></html>");
		monthlybillRep.setBackground(getGrayColor());
		monthlybillRep.addActionListener((e) -> {
			EventQueue.invokeLater(() -> {
				hideDialog();
			});
		});
		box1.add(dailybillSett);
		box1.add(dailybillrep);
		box2.add(monthlybillSett);
		box2.add(monthlybillRep);
		panel.add(panel1);
		panel.add(panel2);

		return panel;
	}

	public void createDialog() {
		dialog = new JDialog((JFrame) null, "Billing Options");
		dialog.setUndecorated(true);
		dialog.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent evvt) {
				dialog.setShape(new RoundRectangle2D.Double(0, 0, dialog.getWidth(), dialog.getHeight(), 5, 5));
			}
		});

		dialog.getContentPane().setBackground(Color.WHITE);
		dialog.setLayout(new BorderLayout());
		JPanel newpanel = createPanel();
		JPanel topp = new JPanel();
		topp.setBackground(new Colors().getColorOne());
		dialog.getContentPane().add(topp, BorderLayout.NORTH);
		dialog.getContentPane().add(newpanel, BorderLayout.CENTER);

		JMenuBar bar = new JMenuBar();
		dialog.setJMenuBar(bar);

		bar.add(new JMenu("Choose"));
		bar.add(Box.createHorizontalGlue());
		JToolBar toolbar = new JToolBar();
		toolbar.setFloatable(false);
		JButton close = new JButton(new ImageIcon(new IconImage().createCloseImage()));
		close.addActionListener((event) -> {
			EventQueue.invokeLater(() -> {
				hideDialog();
			});
		});
		toolbar.add(Box.createHorizontalGlue());
		toolbar.add(close);
		bar.add(toolbar);
		dialog.setSize(600, 500);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension d = dialog.getSize();
		int x = (screen.width - d.width) / 2, y = (screen.height - d.height) / 2;
		dialog.setLocation(x, y);
		dialog.getRootPane().setBorder(ContainerBorders.createBevelBorder());

		new AnimateDialog().fadeIn(dialog, 100);
		dialog.setAlwaysOnTop(true);
	}

	public Color getGrayColor() {
		return Color.GRAY;
	}

	public void hideDialog() {
		new AnimateDialog().fadeOut(dialog, 100);
	}

	public static void main(String[] args) {
		AccessDbase adbase = new AccessDbase();
		adbase.connectionDb();
		Chooser c = new Chooser(adbase.rs, adbase.stm);
		c.createDialog();
	}

}
