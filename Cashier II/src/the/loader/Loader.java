/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package the.loader;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.geom.RoundRectangle2D;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.Timer;

import com.deco.AnimateDialog;
import com.deco.AnimateJFrame;
import com.deco.TranslucentJPanel;
import com.sign.in.Monitor;

import cashiermodules.NewPaymentsModule;
import helpers.IconImage;
import helpers.LookAndFeelClass;
import supervisor.MainUI;

/**
 *
 * @author Wellington
 */
public class Loader extends SwingWorker<Void, Void> {
	JPanel toppanel, progresspanel, rpanel;
	JProgressBar bar;
	Timer t;
	int i;
	LookAndFeelClass looks;
	IconImage icon;
	static int interval = 500;
	JDialog dialog;

	public Loader() {
		init();
	}

	@SuppressWarnings("serial")
	public final void init() {
		looks = new LookAndFeelClass();
		looks.setLookAndFeels();
		icon = new IconImage();
		toppanel = createTopPanel();
		progresspanel = reateProgressPanel();
		rpanel = createRPanel();

		JPanel mainp = new TranslucentJPanel(Color.BLUE);
		mainp.setLayout(new BorderLayout());
		mainp.add(toppanel, BorderLayout.CENTER);

		mainp.add(progresspanel, BorderLayout.SOUTH);
		// mainp.add(rpanel, BorderLayout.SOUTH);

		dialog = new JDialog();
		dialog.setUndecorated(true);
		dialog.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent evvt) {
				dialog.setShape(new RoundRectangle2D.Double(0, 0, dialog.getWidth(), dialog.getHeight(), 5, 5));
			}
		});
		dialog.setContentPane(mainp);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		dialog.setSize(screen.width, screen.height);

		dialog.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent evvt) {
				dialog.setShape(new RoundRectangle2D.Double(0, 0, dialog.getWidth(), dialog.getHeight(), 5, 5));
			}
		});

		dialog.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Cancel");
		dialog.getRootPane().getActionMap().put("Cancel", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				closeAndQuit(dialog);
			}
		});

		Dimension d = dialog.getSize();
		int x = (screen.width - d.width) / 2, y = (screen.height - d.height) / 2;
		dialog.setLocation(x, y);
	}

	private JPanel createTopPanel() {
		JPanel top = new JPanel(new GridBagLayout());
		String sysuser = System.getProperty("user.name");
		JLabel eq4 = new JLabel("C A S H I E R II ");
		eq4.setFont(new Font("", Font.BOLD + Font.ITALIC, 35));
		eq4.setForeground(Color.WHITE);
		JLabel on = new JLabel("on");
		on.setFont(new Font("", Font.BOLD + Font.ITALIC, 13));
		on.setForeground(Color.WHITE);
		JLabel pc = new JLabel(sysuser + "-PC");
		pc.setFont(new Font("", Font.BOLD + Font.ITALIC, 13));
		pc.setForeground(Color.WHITE);
		Box b = Box.createVerticalBox();
		b.add(eq4);
		b.add(on);
		b.add(pc);

		top.setOpaque(false);

		JPanel imgpanel = new JPanel(new FlowLayout());
		imgpanel.add(new JLabel(new ImageIcon(new IconImage().createSigmaImage())), SwingConstants.CENTER);
		imgpanel.setOpaque(false);
		top.add(imgpanel);
		top.add(b);
		return top;
	}

	private JPanel reateProgressPanel() {
		bar = new JProgressBar(0, 30);
		JPanel prg = new JPanel(new GridLayout(2, 1));
		prg.setOpaque(false);
		JLabel loading = new JLabel("Loading modules...");
		loading.setFont(new Font("", Font.BOLD + Font.ITALIC, 11));
		loading.setForeground(Color.WHITE);
		prg.add(loading);
		prg.add(bar);
		return prg;
	}

	private JPanel createRPanel() {
		JPanel rights = new JPanel(new BorderLayout());
		rights.setOpaque(false);
		JLabel lbl = new JLabel("The Bearer of this Software is a Trusted Client of DBS Inc");
		lbl.setFont(new Font("", Font.ITALIC, 13));
		lbl.setForeground(Color.RED);
		rights.add(lbl, BorderLayout.CENTER);
		return rights;
	}

	@Override
	protected Void doInBackground() throws Exception {
		bar.setIndeterminate(true);
		new AnimateDialog().fadeIn(dialog, 100);
		load();
		return null;
	}

	@Override
	public void done() {
		bar.setIndeterminate(false);
		new AnimateDialog().fadeOut(dialog, 100);
	}

	@SuppressWarnings("serial")
	public void load() {
		MainUI mainUI = new MainUI();
		NewPaymentsModule module = new NewPaymentsModule(mainUI.lefttabs, mainUI.adbase.rs, mainUI.adbase.rs1,
				mainUI.adbase.stm, mainUI.adbase.stmt, mainUI.adbase.pstmt, mainUI.adbase.conn, mainUI,
				mainUI.paymentslistmodel, mainUI.totallabel, mainUI.soundeffect);
		String group = Monitor.grouper.get(Monitor.grouper.size() - 1);
		String user = Monitor.users.get(Monitor.users.size() - 1);
		if (group.trim().equals("admin")) {
			new AnimateJFrame().fadeIn(mainUI, 100);

		} else {
			new AnimateDialog().fadeIn(module, 100);
			module.close.addActionListener((e) -> {
				closeAndQuit(module);
			});

			module.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
					.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Cancel");
			module.getRootPane().getActionMap().put("Cancel", new AbstractAction() {
				public void actionPerformed(ActionEvent e) {
					closeAndQuit(module);
				}
			});
		}
	}

	public void closeAndQuit(JDialog doalog) {
		int x = JOptionPane.showConfirmDialog(null,
				"You are force quitting this session!\nYou will need to disconnect.\nAre you sure to proceed?",
				"Force Disconnect", JOptionPane.YES_NO_OPTION);
		if (x == JOptionPane.YES_OPTION) {
			new AnimateDialog().fadeOut(dialog, 100);
			System.exit(0);
		} else {
			return;
		}
	}

	/*
	 * public static void main(String[] args) { Loader l = new Loader();
	 * l.execute(); }
	 */
}
