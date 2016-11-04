package com.troubleshoot;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

import com.deco.TranslucentJPanel;

import helpers.IconImage;

@SuppressWarnings("serial")
public class Window extends JDialog {
	JPanel mainp, upperp, centralp;
	public JButton log, can;
	JLabel left, lbl1, right;

	public Window() {
		super.setTitle("Troubleshoot");
		begin();
	}

	public final void begin() {
		this.setUndecorated(true);
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent evvt) {
				setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 5, 5));
			}
		});
		log = new JButton("Troubleshoot");
		log.setToolTipText("Hit to troubleshoot");
		can = new JButton("Cancel");
		can.setToolTipText("Close and quit");
		log.addActionListener((e) -> {
			log.setEnabled(false);
			can.setEnabled(false);
			Worker w = new Worker(this);
			w.execute();
		});
		can.addActionListener((e) -> {
			System.exit(0);
		});

		left = new JLabel("           ");
		right = new JLabel("           ");

		lbl1 = new JLabel(
				"<html><p>Cashier II has stopped working and <br>has detected that its Server is in an offline mode. <br>Therefore, this utility will searh for an issues and it will:<br><ul><li>Locate and ping the Server,<br></li><li>Ask the Server to restart,<br></li><li>Perform a three way handshack with the Server to ensure<br> a secure connection and then...<br></li><li>Restart the Equation for you and you will work fine.<br></li></ul> Thus we ask you to be patient in the process.<br>Hit the Troubleshoot button to proceed.</p></html>");
		lbl1.setFont(new Font("", Font.BOLD, 13));
		lbl1.setForeground(Color.WHITE);

		Color color = new Color(150, 75, 190);
		mainp = new TranslucentJPanel(color);
		mainp.setLayout(new BorderLayout(10, 10));
		centralp = new JPanel(new GridLayout(1, 2, 8, 8));
		centralp.setOpaque(false);
		upperp = new JPanel(new FlowLayout());
		upperp.setOpaque(false);

		centralp.add(log);
		centralp.add(can);
		upperp.add(lbl1, SwingConstants.CENTER);
		mainp.add(upperp, BorderLayout.CENTER);
		mainp.add(centralp, BorderLayout.SOUTH);
		mainp.add(left, BorderLayout.EAST);
		mainp.add(right, BorderLayout.WEST);

		JMenuBar bar = new JMenuBar();
		this.setJMenuBar(bar);

		bar.add(new JMenu("TroubleShooting Utility"));
		bar.add(Box.createHorizontalGlue());
		JToolBar toolbar = new JToolBar();
		toolbar.setFloatable(false);
		JButton close = new JButton(new ImageIcon(new IconImage().createCloseImage()));
		close.addActionListener((event) -> {
			System.exit(0);
		});
		toolbar.add(Box.createHorizontalGlue());
		toolbar.add(close);
		bar.add(toolbar);

		setContentPane(mainp);
		setSize(500, 300);
		setFocusable(true);
		setAlwaysOnTop(true);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension d = this.getSize();
		int x = (screen.width - d.width) / 2, y = (screen.height - d.height) / 2;
		this.setLocation(x, y);
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent evvt) {
				setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 5, 5));
			}
		});
	}
}
