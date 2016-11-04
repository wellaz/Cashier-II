package cashiermodules;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;

import helpers.IconImage;

/**
 *
 * @author Wellington
 */
@SuppressWarnings("serial")
public class NewPaymentsModule extends JDialog {
	JTabbedPane tabs;
	ResultSet rs;
	ResultSet rs1;
	Statement stm;
	Statement stmt;
	PreparedStatement pstmt;
	Connection conn;
	JFrame frame;
	DefaultListModel<String> listmodel;
	JLabel totallabel;
	JCheckBoxMenuItem tuner;
	NewPayment newpayment;
	public JButton close;

	// constructor declaration here.
	public NewPaymentsModule(JTabbedPane tabs, ResultSet rs, ResultSet rs1, Statement stm, Statement stmt,
			PreparedStatement pstmt, Connection conn, JFrame frame, DefaultListModel<String> listmodel,
			JLabel totallabel, JCheckBoxMenuItem tuner) {
		this.tabs = tabs;
		this.stm = stm;
		this.stmt = stmt;
		this.rs = rs;
		this.rs1 = rs1;
		this.frame = frame;
		this.pstmt = pstmt;
		this.conn = conn;
		this.listmodel = listmodel;
		this.totallabel = totallabel;
		this.tuner = tuner;

		newpayment = new NewPayment(tabs, rs, rs1, stm, stmt, pstmt, conn, frame, listmodel, totallabel, tuner);
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

		JMenuBar bar = new JMenuBar();
		this.setJMenuBar(bar);
		JMenu menu = new JMenu("Transactions Module");
		menu.setFont(new Font("", Font.PLAIN, 30));

		bar.add(menu);
		bar.add(Box.createHorizontalGlue());
		JToolBar toolbar = new JToolBar();
		toolbar.setFloatable(false);
		close = new JButton(new ImageIcon(new IconImage().createCloseImage()));
		/*
		 * close.addActionListener((event) -> { new
		 * AnimateDialog().fadeOut(this, 100); });
		 */
		toolbar.add(Box.createHorizontalGlue());
		toolbar.add(close);
		bar.add(toolbar);

		setContentPane(newpayment);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(screen.width, screen.height);
		Dimension d = this.getSize();
		int x = (screen.width - d.width) / 2, y = (screen.height - d.height) / 2;
		this.setLocation(x, y);
	}
}
