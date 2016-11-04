/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package supervisor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.RoundRectangle2D;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;

import com.billing.Authenticate;
import com.billing.CheckBill;
import com.billing.reports.ChooseMonth;
import com.deco.AnimateDialog;
import com.deco.AnimateJFrame;
import com.deco.TranslucentJPanel;
import com.deco.TranslucentJPanel1;
import com.toedter.calendar.JDateChooser;

import cashiermodules.AddReceiptsToList;
import cashiermodules.NewPayment;
import cashiermodules.NewPaymentsModule;
import expenses.post.ListExpenses;
import expenses.post.PostExpense;
import helpers.AccessDbase;
import helpers.ComputerExplorer;
import helpers.CustomTabbedPaneUI;
import helpers.IconImage;
import helpers.ListCellRendering;
import helpers.LookAndFeelClass;
import helpers.RemoveTab;
import helpers.Ribbon1;
import helpers.SetDateCreated;
import helpers.TabMouseMotionListener;
import helpers.TextFinder;
import helpers.TextValidator;
import helpers.ToolbarPopup2;
import helpers.TreeMouseListener;
import tr.add.product.type.NewProductType;
import tr.audio.Click;
import tr.audio.HoverListener;
import tr.bank.Chooser;
import tr.bulkstock.add.AddBulkProducts;
import tr.change.search.Change;
import tr.currentstock.InStock;
import tr.dailysales.DailySales;
import tr.ledger.SearchLedger;
import tr.receipt.reprint.ListPopup;
import tr.receipt.reprint.ListPopupTrigger;
import tr.receipt.reprint.ReprintRe;
import tr.reconciliation.Reconcile;
import tr.salesreports.SalesReport;
import tr.temporarydata.delete.DeleteTMPTableData;
import tr.tooltips.ToolTips;
import tr.updateprice.UpdatePrice;
import tracer.browse.BrowseMenu;

/**
 *
 * @author Wellington
 */
@SuppressWarnings("serial")
public class MainUI extends JFrame implements ActionListener {

	public JToolBar toolbar;
	JPanel mainPanel;
	JPanel centerPanel;
	JPanel Panel;
	JPanel upperPanel;
	JLabel allLabels[], hintlabels[], error;
	JTextArea display;
	JScrollPane scroller;
	public JMenuBar mnb;
	JMenu fl, ed, wnd, hlp, con, sa;
	JMenuItem ns, of, ph, exi;
	JMenuItem und, red, cut, cop, past, del, repl, outstanding_change;
	JMenuItem nc, rec, tc, disc;
	JMenuItem hc, ks, search, report, about;
	JMenuItem ccl, aut, max, min, clw, rsw, reprint;
	public AccessDbase adbase;
	LookAndFeelClass looks;
	public JTextField cashierid, navi;
	JDialog dialog;
	JSplitPane split;
	public JTabbedPane tabs, lefttabs;
	JTree tree;
	public SetDateCreated setdate;

	JLabel tellername, numberoftrans, timelbl;
	// ToolTips tooltip;

	String labels[] = { "Transactions", "", "Debit", "Credit", "Balance" };
	String notes[] = { "Ecs:Exit, ", "PgUp:Scroll Up, ", "PgDn:Scroll Down, ", "End:Last Record, ", "F1:More Help",
			"[ENTER]:Next Page" };
	private JButton find_trans;
	private JButton re_print;
	private JButton outstand_channge;
	private JDateChooser datechooser, datechoosert;
	private String dateString, dateStringto;
	private JButton new_prod_categ;
	private JButton bulk_stock_entry;
	private JButton new_payment;
	private JList<String> paymentslist;
	public DefaultListModel<String> paymentslistmodel;
	private JButton daily_sales_report;
	private JMenuItem new_user;
	private JMenuItem all_users;
	private JMenuItem ad_mins;
	private Color color;
	private AbstractButton post_expense, expense_list;
	private JButton revenue_sus_trans, recon;
	public JLabel totallabel;
	private JMenuItem find;
	private JMenuItem daily_repor;
	private JMenuItem re_con;
	private JMenuItem exp_list;
	private JButton update_stock_price;
	private JButton cashieridbutton;
	private JButton sales_rep;
	private JButton in_stock;
	// DailySales dailysales;
	Reconcile reconcile;
	// ListExpenses listexpenses;
	// InStock instock;
	NewProductType newproducttype;
	Change change;
	AddBulkProducts addbulkproduct;
	UpdatePrice updateprice;
	NewPayment newpayment;
	SalesReport salesreport;
	PostExpense postexpense;
	ReprintRe reprintreceipt;
	public JCheckBoxMenuItem soundeffect;
	Chooser bankchooser;
	Click click;
	static String basicWindowTitle = "Cashier II -  AA: CMD";
	String changetitle = null;
	private JMenu billing;
	private JMenuItem b_settings;
	private JMenuItem b_report;

	public MainUI() {
		super(basicWindowTitle);
		adbase = new AccessDbase();
		adbase.connectionDb();
		looks = new LookAndFeelClass();
		looks.setLookAndFeels();
		setdate = new SetDateCreated();
		begin();
		classInitialization();

		tabs.addChangeListener((listen) -> {
			EventQueue.invokeLater(() -> {
				int count = tabs.getTabCount();
				for (int x = 0; x < count; x++) {
					changetitle = tabs.getTitleAt(x).trim();
					this.setTitle(basicWindowTitle + " - " + changetitle);
				}
			});
		});

		tabs.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent ev) {
				for (int i = 0; i < tabs.getTabCount(); i++) {
					changetitle = tabs.getTitleAt(i).trim();
					setTitle(basicWindowTitle + " - " + changetitle);
				}
			}
		});
	}

	public void classInitialization() {
		reconcile = new Reconcile(tabs, adbase.stm, adbase.rs, adbase.rs1, adbase.stmt, this);
		newproducttype = new NewProductType(tabs, adbase.rs, adbase.stm, this);
		change = new Change(adbase.rs, adbase.rs1, adbase.stm, adbase.stmt, tabs);
		addbulkproduct = new AddBulkProducts(tabs, adbase.rs, adbase.rs1, adbase.stm, adbase.stmt, adbase.pstmt,
				adbase.conn, this);
		updateprice = new UpdatePrice(adbase.rs, adbase.rs1, adbase.stm, adbase.stmt, tabs);
		newpayment = new NewPayment(tabs, adbase.rs, adbase.rs1, adbase.stm, adbase.stmt, adbase.pstmt, adbase.conn,
				this, paymentslistmodel, totallabel, soundeffect);

		postexpense = new PostExpense(tabs, adbase.rs, adbase.stm, this);
		reprintreceipt = new ReprintRe(adbase.rs, adbase.rs1, adbase.stm, adbase.stmt, tabs, this);
		bankchooser = new Chooser(tabs, adbase.rs, adbase.stm, this);
		click = new Click();
	}

	@SuppressWarnings({ "unchecked" })
	public final void begin() {
		toolbar = new JToolBar();
		toolbar.setFloatable(false);
		mainPanel = new JPanel();
		centerPanel = new JPanel();
		Panel = new JPanel();
		upperPanel = new JPanel();

		WindowListener listener = new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				deleteTemporaryData();
				click.closeappPlay();
				System.exit(0);
			}
		};
		addWindowListener(listener);
		mnb = new JMenuBar();
		/*
		 * mnb.setUI(new BasicMenuBarUI() { public void paint(Graphics g,
		 * JComponent comp) { g.setColor(Color.WHITE); g.fillRect(2, 2,
		 * comp.getWidth(), comp.getHeight()); } });
		 */
		setJMenuBar(mnb);

		fl = new JMenu("File");
		mnb.add(fl);
		JMenu nnew = new JMenu("New");

		new_user = new JMenuItem("New User");
		new_user.addActionListener(this);
		nnew.add(new_user);
		nnew.addSeparator();
		fl.add(nnew);
		of = new JMenuItem("Open File");
		of.setEnabled(false);
		fl.add(of);
		fl.addSeparator();
		sa = new JMenu("Cashiers...");
		fl.add(sa);

		ph = new JMenuItem("Print to HTML...");
		ph.setEnabled(false);
		fl.add(ph);
		fl.addSeparator();
		exi = new JMenuItem("Exit");
		fl.add(exi);
		exi.addActionListener(this);

		ed = new JMenu("Edit");
		mnb.add(ed);
		und = new JMenuItem("Undo");
		und.setEnabled(false);
		ed.add(und);
		red = new JMenuItem("Redo");
		red.setEnabled(false);
		ed.add(red);
		ed.addSeparator();
		cut = new JMenuItem("Cut");
		ed.add(cut);
		cop = new JMenuItem("Copy");
		ed.add(cop);
		past = new JMenuItem("Paste");
		ed.add(past);
		ed.addSeparator();
		del = new JMenuItem("Delete");
		del.setEnabled(false);
		ed.add(del);
		find = new JMenuItem("Find");
		find.addActionListener(this);
		ed.add(find);
		repl = new JMenuItem("Replace...");
		repl.setEnabled(false);
		ns = new JMenuItem("Stock Pricing");
		ns.addActionListener(this);
		ed.add(repl);
		ed.addSeparator();
		ed.add(ns);

		JMenu repr = new JMenu("Search");

		JMenu users = new JMenu("Users");

		all_users = new JMenuItem("All Usres");
		all_users.addActionListener(this);
		users.add(all_users);
		JMenu groupby = new JMenu("Group By");
		users.add(groupby);
		ad_mins = new JMenuItem("Administrators");
		ad_mins.addActionListener(this);
		groupby.add(ad_mins);

		reprint = new JMenuItem("Reprint receipt");
		reprint.addActionListener(this);
		repr.add(users);
		repr.addSeparator();
		repr.add(reprint);
		repr.addSeparator();
		outstanding_change = new JMenuItem("Outstanding Change($)");
		outstanding_change.addActionListener(this);
		repr.add(outstanding_change);
		mnb.add(repr);

		JMenu pstngs = new JMenu("Generate");
		mnb.add(pstngs);
		daily_repor = new JMenuItem("Daily Sales Report");
		daily_repor.addActionListener(this);
		re_con = new JMenuItem("Reconciliation Statement");
		re_con.addActionListener(this);
		exp_list = new JMenuItem("Expenses List");
		exp_list.addActionListener(this);

		pstngs.add(daily_repor);
		pstngs.addSeparator();
		pstngs.add(re_con);

		con = new JMenu("Connections");
		mnb.add(con);
		nc = new JMenuItem("New Connection");
		nc.setEnabled(false);
		con.add(nc);
		rec = new JMenuItem("Reconnect");
		rec.setEnabled(false);
		con.add(rec);
		tc = new JMenuItem("Test Connection");
		tc.setEnabled(false);
		con.add(tc);
		disc = new JMenuItem("Disconnect");
		disc.addActionListener(this);
		con.add(disc);

		wnd = new JMenu("Window");
		mnb.add(wnd);
		ccl = new JMenuItem("Change Background");
		ccl.addActionListener(this);
		wnd.add(ccl);
		aut = new JMenuItem("Auto Scroll");
		wnd.add(aut);
		wnd.addSeparator();
		max = new JMenuItem("Maximise Window");
		max.setEnabled(false);
		wnd.add(max);
		min = new JMenuItem("Minimise Window");
		min.setEnabled(false);
		wnd.add(min);
		clw = new JMenuItem("Close Window");
		clw.setEnabled(false);
		wnd.add(clw);
		rsw = new JMenuItem("Reset Window");
		rsw.setEnabled(false);
		wnd.add(rsw);
		soundeffect = new JCheckBoxMenuItem("Turn On Sound Effects");
		soundeffect.setState(true);
		wnd.addSeparator();
		wnd.add(soundeffect);

		mnb.add(new BrowseMenu());

		hlp = new JMenu("Help");
		mnb.add(hlp);
		hc = new JMenuItem("Help Content");
		hlp.add(hc);
		search = new JMenuItem("Search...");
		hlp.add(search);
		hlp.addSeparator();
		ks = new JMenuItem("Keys Shortcuts");
		hlp.add(ks);
		report = new JMenuItem("Report An Issue");
		hlp.add(report);
		about = new JMenuItem("About...");
		hlp.add(about);
		billing = new JMenu("Billing");
		hlp.addSeparator();
		hlp.add(billing);
		b_settings = new JMenuItem("Settings");
		b_settings.addActionListener(this);
		b_report = new JMenuItem("Report");
		b_report.addActionListener(this);
		billing.add(b_settings);
		billing.add(b_report);

		allLabels = new JLabel[6];
		hintlabels = new JLabel[notes.length];
		display = new JTextArea("");
		display.setEditable(false);
		display.setLineWrap(true);
		display.setMargin(new Insets(30, 30, 30, 30));
		display.setBackground(Color.BLACK);
		display.setForeground(Color.white);
		display.setFont(new Font("", Font.ROMAN_BASELINE, 19));
		display.getCaret().setSelectionVisible(true);
		display.getCaret().setVisible(true);
		display.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent arg0) {
				int key = arg0.getKeyCode();
				if (key == KeyEvent.VK_C || key == KeyEvent.VK_DELETE) {
					clearData();
				}
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
			}
		});

		scroller = new JScrollPane(display);
		scroller.setBorder(null);
		mainPanel = new JPanel(new BorderLayout());
		Panel = new JPanel(new FlowLayout());
		upperPanel = new JPanel(new GridLayout(1, 6, 5, 5));
		// for loops for the painting of labels here
		for (int x = 0; x < labels.length; x++) {
			allLabels[x] = new JLabel(labels[x]);
			allLabels[x].setSize(3, 3);
			allLabels[x].setForeground(Color.RED);
			allLabels[x].setFont(new Font("", Font.BOLD, 20));
			upperPanel.add(allLabels[x]);
		}

		for (int y = 0; y < notes.length; y++) {
			hintlabels[y] = new JLabel(notes[y]);
			hintlabels[y].setSize(2, 3);
			hintlabels[y].setForeground(Color.BLUE);
			hintlabels[y].setFont(new Font("", Font.ITALIC + Font.BOLD, 13));
			Panel.add(hintlabels[y]);
		}
		navi = new JTextField(20);
		navi.addActionListener(this);
		Panel.add(new JLabel(" Search:"));
		Panel.add(navi);

		mainPanel.add(upperPanel, BorderLayout.NORTH);
		mainPanel.add(scroller, BorderLayout.CENTER);
		mainPanel.add(Panel, BorderLayout.SOUTH);

		tellername = new JLabel();
		tellername.setFont(new Font("Arial", Font.PLAIN, 20));
		tellername.setForeground(Color.WHITE);
		numberoftrans = new JLabel();
		numberoftrans.setFont(new Font("Arial", Font.PLAIN, 20));
		numberoftrans.setForeground(Color.WHITE);
		timelbl = new JLabel();
		timelbl.setFont(new Font("Arial", Font.PLAIN, 20));
		timelbl.setForeground(Color.WHITE);

		tree = ComputerExplorer.createExplorer();
		tree.addMouseListener(new TreeMouseListener(tree, this));

		JPanel mailpanel = new TranslucentJPanel1(Color.BLACK);
		mailpanel.setLayout(new BorderLayout());
		JScrollPane scroll = new JScrollPane();
		scroll.setViewportView(tree);
		mailpanel.add(scroll, BorderLayout.CENTER);

		lefttabs = new JTabbedPane();
		lefttabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		lefttabs.addMouseMotionListener(new TabMouseMotionListener());
		// lefttabs.addTab("My Computer", null, mailpanel, "My Computer");
		// lefttabs.addTab("MS Office", null, new OpenPackages(), "Write
		// Reports, Documents And other Files");

		paymentslist = new JList<>();
		paymentslist.setFont(new Font("Dialog", Font.PLAIN, 19));
		paymentslist.setModel(paymentslistmodel = new DefaultListModel<>());
		paymentslistmodel.addElement("Transactions List");
		paymentslist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		paymentslist.setCellRenderer(new ListCellRendering());

		JPanel paymentsPanel = new TranslucentJPanel(Color.BLUE);
		paymentsPanel.setLayout(new BorderLayout());
		paymentsPanel.add(new JScrollPane(paymentslist), BorderLayout.CENTER);
		totallabel = new JLabel("SUM $ 0.00");
		totallabel.setFont(new Font("", Font.BOLD + Font.ITALIC, 19));
		// totallabel.setForeground(Color.WHITE);

		paymentsPanel.add(totallabel, BorderLayout.SOUTH);

		lefttabs.addTab("Payments".toUpperCase(), null, paymentsPanel, "today's payments");
		lefttabs.setFont(new Font("Dialog", Font.PLAIN, 20));

		tabs = new JTabbedPane();
		tabs.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		tabs.setUI(new CustomTabbedPaneUI());
		JPanel deco = new TranslucentJPanel1(Color.BLUE);
		deco.setLayout(new GridLayout(4, 1));
		// deco.setBackground(Color.BLACK);
		JLabel eq4 = new JLabel("<html><u>C A S H I E R II</u></html>");
		eq4.setFont(new Font("", Font.BOLD, 100));
		eq4.setForeground(Color.WHITE);
		JLabel lin = new JLabel(
				"_________________________________________________________________________________________________");
		lin.setForeground(Color.WHITE);
		JLabel md = new JLabel("<html><i><u>Inventory Management Service</u></i></html>");
		md.setFont(new Font("", Font.ITALIC, 30));
		md.setForeground(Color.WHITE);
		deco.add(eq4);
		deco.add(lin);

		deco.add(new JLabel());
		deco.add(md);

		JPanel innert = new JPanel();
		innert.setOpaque(false);
		JPanel innerb = new JPanel();
		innerb.setOpaque(false);
		JPanel innerl = new JPanel();
		innerl.setOpaque(false);
		JPanel innerr = new JPanel(new BorderLayout());
		innerr.setBackground(Color.WHITE);

		Box versionBox = Box.createHorizontalBox();
		JLabel versionLabel = new JLabel("Product version 1.0.0 - Product ID 001");
		versionLabel.setFont(new Font("", Font.ITALIC, 17));
		JLabel serviceproviderLabel = new JLabel("Copyright @2016. (Wellington Mapiku)");
		serviceproviderLabel.setFont(new Font("", Font.ITALIC, 11));
		versionBox.add(serviceproviderLabel);
		versionBox.add(Box.createHorizontalGlue());
		versionBox.add(versionLabel);
		innerr.add(versionBox);

		innerr.setOpaque(false);

		color = new Color(0.5f, 0.5f, 1f);

		JPanel tabp = new JPanel(new BorderLayout(100, 100));
		tabp.setBackground(Color.WHITE);
		// tabp.setBackground(new Color(130, 75, 100));

		tabp.add(deco, BorderLayout.CENTER);
		tabp.add(innert, BorderLayout.WEST);
		tabp.add(innerb, BorderLayout.EAST);
		tabp.add(innerl, BorderLayout.NORTH);
		tabp.add(innerr, BorderLayout.SOUTH);

		tabs.addTab("Start   ", null, tabp, "Start");
		tabs.setFont(new Font("Dialog", Font.PLAIN, 20));
		tabs.setForeground(Color.WHITE);
		// tabs.addTab("Start ", null, new Banner(), "Start");
		split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		split.setDividerSize(10);
		split.setOneTouchExpandable(true);
		split.setDividerLocation(200);
		JPanel lefttabpanel = new JPanel(new BorderLayout());
		lefttabpanel.setBackground(new Color(0.5f, 0.1f, 1f));
		JLabel navigate = new JLabel("N A V ->>>>");
		navigate.setForeground(Color.WHITE);
		navigate.setFont(new Font("", Font.BOLD + Font.ITALIC, 13));
		// lefttabpanel.add(navigate, BorderLayout.NORTH);
		lefttabpanel.add(lefttabs, BorderLayout.CENTER);
		split.add(lefttabpanel);
		Color cv = new Color(0.5f, 0.3f, 1f);
		JPanel centerpanel = new JPanel(new BorderLayout());
		centerpanel.setBackground(cv);
		centerpanel.setBorder(null);
		centerpanel.add(tabs, BorderLayout.CENTER);
		split.add(centerpanel);

		JTabbedPane toolbartabs = createToolbar();

		toolbar.add(toolbartabs);
		// toolbar.add(Box.createHorizontalGlue());
		toolbar.setComponentPopupMenu(new ToolbarPopup2(this, toolbar, mnb, toolbartabs));

		ListPopup pop = new ListPopup(adbase.rs, adbase.rs1, adbase.stm, adbase.stmt, tabs, this);
		paymentslist.addMouseListener(
				new ListPopupTrigger(pop, adbase.rs, adbase.rs1, adbase.stm, adbase.stmt, tabs, this));

		this.setLayout(new BorderLayout());
		JPanel toppanell = new JPanel();

		toppanell.setLayout(new BorderLayout());

		toppanell.setBackground(color);
		toppanell.add(toolbar, BorderLayout.CENTER);
		this.getContentPane().add(toppanell, BorderLayout.NORTH);
		this.getContentPane().add(split, BorderLayout.CENTER);
		this.setIconImage(new IconImage().createIconImage());

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(screen.width, screen.height);

		this.setUndecorated(true);
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent evvt) {
				setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 5, 5));
			}
		});

		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
				"Cancel");
		getRootPane().getActionMap().put("Cancel", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				closeAndQuit();
			}
		});

		Dimension d = this.getSize();
		int x = (screen.width - d.width) / 2, y = (screen.height - d.height) / 2;
		this.setLocation(x, y);

	}

	public void insertTab() {
		EventQueue.invokeLater(() -> {
			int numberoftabs = tabs.getTabCount();
			boolean exist = false;
			for (int a = 0; a < numberoftabs; a++) {
				if (tabs.getTitleAt(a).trim().equals("Session MGR AA_FND")) {
					exist = true;
					tabs.setSelectedIndex(a);
					break;
				}
			}
			if (!exist) {
				tabs.addTab("Session MGR AA_FND   ", null, detPanel(), "Run queries, find transactions,e.t.c");
				tabs.setSelectedIndex(numberoftabs);
				// DailyBillingThread dbt = new DailyBillingThread(adbase.stm,
				// adbase.rs);
				// dbt.analyzeData();
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == ccl) {
			EventQueue.invokeLater(() -> {
				Color cl = new Color(153, 153, 204);
				cl = JColorChooser.showDialog(this, "Choose Background Color", cl);
				display.setBackground(cl);
				display.repaint();
			});
		}
		if (e.getSource() == exi || e.getSource() == disc) {
			closeAndQuit();
		}
		if (e.getSource() == in_stock) {
			new RemoveTab(tabs).removeTab("In Stock");
			InStock instock = new InStock(adbase.rs, adbase.rs1, adbase.stm, adbase.stmt, tabs);
			instock.insertTab();
		}

		if (e.getSource() == ns || e.getSource() == new_prod_categ) {
			stockPricing();
		}
		if (e.getSource() == find || e.getSource() == find_trans) {
			EventQueue.invokeLater(() -> {
				clearData();
			});
		}
		if (e.getSource() == cashierid || e.getSource() == cashieridbutton) {
			EventQueue.invokeLater(() -> {
				display.setText("");
				tellername.setText("");
				numberoftrans.setText("");
				validateAccount();
				dialog.dispose();
			});
		}
		if (e.getSource() == reprint || e.getSource() == re_print) {
			reprintreceipt.insertTab();
		}
		if (e.getSource() == navi) {
			EventQueue.invokeLater(() -> {
				new TextFinder().find(navi.getText(), display);
			});
		}
		if (e.getSource() == outstanding_change || e.getSource() == outstand_channge) {
			new RemoveTab(tabs).removeTab("Change");
			change.showDialog();
		}
		if (e.getSource() == bulk_stock_entry) {
			new RemoveTab(tabs).removeTab("New Bulk Stock");
			addbulkproduct.insertTab();
		}
		if (e.getSource() == new_payment) {
			new RemoveTab(tabs).removeTab("New Transaction");
			deleteTemporaryData();
			newpayment.insertTab();
			NewPaymentsModule module = new NewPaymentsModule(lefttabs, adbase.rs, adbase.rs1, adbase.stm, adbase.stmt,
					adbase.pstmt, adbase.conn, this, paymentslistmodel, totallabel, soundeffect);
			new AnimateDialog().fadeIn(module, 100);
			module.close.addActionListener((ev) -> {
				new AnimateDialog().fadeOut(module, 100);
			});
			/*
			 * EventQueue.invokeLater(() -> { lefttabs.setSelectedIndex(2); });
			 */
			addReceipts();
			sendBillindData();
		}
		if (e.getSource() == daily_sales_report || e.getSource() == daily_repor) {
			DailySales dailysales = new DailySales(adbase.rs, adbase.rs1, adbase.stm, adbase.stmt, tabs);
			dailysales.insertTab();
		}
		if (e.getSource() == new_user) {

		}
		if (e.getSource() == all_users) {

		}
		if (e.getSource() == ad_mins) {

		}
		if (e.getSource() == post_expense) {
			new RemoveTab(tabs).removeTab("Expenses");
			postexpense.insertTab();
		}
		if (e.getSource() == expense_list || e.getSource() == exp_list) {
			new RemoveTab(tabs).removeTab("Expenses List");
			ListExpenses listexpenses = new ListExpenses(tabs, adbase.stm, adbase.rs, adbase.rs1, adbase.stmt, this);
			EventQueue.invokeLater(() -> {
				listexpenses.showDialog();
			});
		}
		if (e.getSource() == revenue_sus_trans) {
			bankchooser.showDialog();
		}
		if (e.getSource() == recon || e.getSource() == re_con) {
			// reconcilliation statement right here
			new RemoveTab(tabs).removeTab("Reconciliation");
			EventQueue.invokeLater(() -> {
				reconcile.showDialog();
			});
		}
		if (e.getSource() == sales_rep) {
			new RemoveTab(tabs).removeTab("Sales Report");
			salesreport = new SalesReport(tabs, adbase.stm, adbase.rs, adbase.rs1, adbase.stmt, this);
			EventQueue.invokeLater(() -> {
				salesreport.showDialog();
			});

		}

		if (e.getSource() == update_stock_price) {
			new RemoveTab(tabs).removeTab("Update Prices");
			EventQueue.invokeLater(() -> {
				updateprice.showDialog();
			});
		}
		if (e.getSource() == b_settings) {
			authenticateBilling();
		}
		if (e.getSource() == b_report) {
			processBillingreport();
		}
	}

	public void processBillingreport() {
		ChooseMonth chooseMonth = new ChooseMonth(adbase.rs, adbase.rs1, adbase.stm, adbase.stmt, tabs);
		EventQueue.invokeLater(() -> {
			chooseMonth.showDialog();
		});
	}

	public void authenticateBilling() {
		Authenticate a = new Authenticate(adbase.stm, adbase.rs);
		EventQueue.invokeLater(() -> {
			a.createDialog();
		});
	}

	public void sendBillindData() {
		CheckBill c = new CheckBill(adbase.rs, adbase.stm);
		c.check();
	}

	public void showDialog() {
		dialog = new JDialog((JFrame) null, "Search", true);
		dialog.setLayout(new BorderLayout());
		dialog.setIconImage(new IconImage().createIconImage());
		JLabel datelbl = new JLabel("From (Date):");
		JLabel datelblto = new JLabel("To (Date):");
		datechooser = new JDateChooser("yyyy/MM/dd", "####/##/##", '_');
		datechooser.setDate(new Date());
		datechoosert = new JDateChooser("yyyy/MM/dd", "####/##/##", '_');
		datechoosert.setDate(new Date());
		JPanel datepanel = new JPanel(new GridLayout(2, 2));
		datepanel.setOpaque(false);
		datepanel.add(datelbl);
		datepanel.add(datechooser);
		datepanel.add(datelblto);
		datepanel.add(datechoosert);
		dialog.getContentPane().add(datepanel, BorderLayout.NORTH);
		JLabel top = new JLabel("<html><h3>Type Account Number, <i>(eg 4100)</i><h3>");
		// top.setForeground(Color.WHITE);

		cashierid = new JTextField();
		cashierid.setFont(new Font("", Font.ROMAN_BASELINE, 35));
		cashierid.addActionListener(this);
		cashierid.addKeyListener(new TextValidator());
		JPanel midpanel = new JPanel(new GridLayout(2, 1));
		midpanel.setOpaque(false);
		midpanel.add(top);
		midpanel.add(cashierid);

		error = new JLabel();
		error.setForeground(Color.red);
		// midpanel.add(error);
		cashieridbutton = new JButton("OK");
		cashieridbutton.addActionListener(this);
		// midpanel.add(cashieridbutton);
		// dialog.getContentPane().setBackground(new Color(0.5f, 0.5f, 1f));
		dialog.getContentPane().setBackground(Color.WHITE);
		dialog.getContentPane().add(midpanel, BorderLayout.CENTER);
		Box defaultBox = Box.createHorizontalBox();
		defaultBox.add(error);
		defaultBox.add(Box.createHorizontalGlue());
		defaultBox.add(cashieridbutton);
		dialog.getRootPane().setDefaultButton(cashieridbutton);
		dialog.getContentPane().add(defaultBox, BorderLayout.SOUTH);

		dialog.setSize(300, 200);
		Dimension d = dialog.getSize(), screen = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screen.width - d.width) / 2, y = (screen.height - d.height) / 2;
		dialog.setLocation(x, y);
		dialog.setVisible(true);
		dialog.setAlwaysOnTop(true);
	}

	private JPanel detPanel() {
		JPanel p = new JPanel(new BorderLayout());
		p.setBorder(null);
		p.setBackground(Color.BLACK);
		// start the clock
		javax.swing.Timer timer = new javax.swing.Timer(1000, new Listener());
		timer.start();

		JLabel lbl = new JLabel("*** Transaction History Enquiry ***");
		lbl.setFont(new Font("Arial", Font.PLAIN, 20));
		lbl.setForeground(Color.WHITE);

		Box bc = Box.createVerticalBox();
		bc.add(lbl);
		bc.add(tellername);

		p.add(bc, BorderLayout.WEST);
		p.add(timelbl, BorderLayout.CENTER);
		p.add(numberoftrans, BorderLayout.EAST);

		JPanel mp = new TranslucentJPanel(Color.BLUE);
		mp.setLayout(new BorderLayout());
		mp.add(p, BorderLayout.NORTH);
		mainPanel.setOpaque(false);
		mp.add(mainPanel, BorderLayout.CENTER);

		return mp;
	}

	public void validateAccount() {
		if (!cashierid.getText().trim().equals("")) {
			String id = cashierid.getText();
			Date fdate = datechooser.getDate();
			Date todate = datechoosert.getDate();
			dateString = String.format("%1$tY-%1$tm-%1$td", fdate);
			dateStringto = String.format("%1$tY-%1$tm-%1$td", todate);
			if (id.equals("4131")) {
				tellername.setText("General Ledger 4131");
				SearchLedger searchledger = new SearchLedger(adbase.rs, adbase.stm, this);
				searchledger.search(dateString, dateStringto, display);
			} else {
				searchData(id);
				dialog.dispose();
			}
		} else {
			error.setText("Account DO NOT HONOR?");
		}
	}

	public void searchData(String id) {
		Date fdate = datechooser.getDate();
		Date todate = datechoosert.getDate();
		dateString = String.format("%1$tY-%1$tm-%1$td", fdate);
		dateStringto = String.format("%1$tY-%1$tm-%1$td", todate);
		try {
			int cid = Integer.parseInt(id);
			String query11 = "SELECT * FROM cashpay WHERE cashierid = '" + cid + "'AND date BETWEEN '" + dateString
					+ "' AND '" + dateStringto + "'";
			adbase.rs1 = adbase.stmt.executeQuery(query11);
			adbase.rs1.last();
			numberoftrans.setText("" + adbase.rs1.getRow() + " Transactions found");

			String query111 = "SELECT * FROM cashiers WHERE cashierid = '" + cid + "'";
			adbase.rs1 = adbase.stmt.executeQuery(query111);
			adbase.rs1.next();
			tellername.setText("Name :" + adbase.rs1.getString(3) + " " + adbase.rs1.getString(5));

			String query = "SELECT * FROM cashpay WHERE cashierid = '" + cid + "'AND date BETWEEN '" + dateString
					+ "' AND '" + dateStringto + "'";
			adbase.rs = adbase.stm.executeQuery(query);
			while (adbase.rs.next()) {
				int rid = adbase.rs.getInt(1);
				int sid = adbase.rs.getInt(2);
				String date = adbase.rs.getString(5) + " " + adbase.rs.getString(6);
				long amount = adbase.rs.getLong(4);

				String am = "" + amount;
				String trans = "ReceiptNo " + rid + "\nStudentNo " + sid + "\nDate " + date + "\t\t\t\t$" + am + "\n\n";
				display.append(trans);
			}
		} catch (SQLException ee) {
			dialog.setVisible(true);
			error.setText("Cashier ID " + cashierid.getText() + " DO NOT HONOUR!");
		}
	}

	// timer class
	class Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			Calendar now = Calendar.getInstance();
			int hr = now.get(Calendar.HOUR_OF_DAY);
			int min = now.get(Calendar.MINUTE);
			int sec = now.get(Calendar.SECOND);
			int AM_PM = now.get(Calendar.AM_PM);

			String day_night;
			if (AM_PM == 1) {
				day_night = "PM";
			} else {
				day_night = "AM";
			}
			timelbl.setText("TIME " + hr + ":" + min + ":" + sec + " " + day_night);
		}
	}

	public JTabbedPane createToolbar() {
		JTabbedPane pane = new JTabbedPane();

		pane.setUI(new Ribbon1());

		JToolBar inventory = new JToolBar();
		inventory.setFloatable(false);
		inventory.setRollover(true);
		new_prod_categ = new JButton("<html><p>Stock<br>Pricing</p></html>");
		new_prod_categ.setToolTipText(new ToolTips().stock_pr);
		new_prod_categ.addActionListener(this);
		new_prod_categ.addMouseListener(new HoverListener(soundeffect));
		bulk_stock_entry = new JButton("<html><p>Bulk Stock<br>Entry</p></html>");
		bulk_stock_entry.setToolTipText(new ToolTips().bulk_stock_ent);
		bulk_stock_entry.addActionListener(this);
		bulk_stock_entry.addMouseListener(new HoverListener(soundeffect));
		post_expense = new JButton("<html><p>Post An<br>Expense</p></html>");
		post_expense.setToolTipText(new ToolTips().post_ex);
		post_expense.addActionListener(this);
		post_expense.addMouseListener(new HoverListener(soundeffect));
		revenue_sus_trans = new JButton("<html><p>General Revenue<br>Suspense Transaction</p></html>");
		revenue_sus_trans.setToolTipText(new ToolTips().banking);
		revenue_sus_trans.addActionListener(this);
		revenue_sus_trans.addMouseListener(new HoverListener(soundeffect));
		update_stock_price = new JButton("<html><p>Update Stock<br>Price</p></html>");
		update_stock_price.setToolTipText(new ToolTips().update_price);
		update_stock_price.addActionListener(this);
		update_stock_price.addMouseListener(new HoverListener(soundeffect));

		inventory.add(new_prod_categ);
		inventory.addSeparator();
		inventory.add(bulk_stock_entry);
		inventory.addSeparator();
		inventory.addSeparator();
		inventory.add(post_expense);
		inventory.addSeparator();
		inventory.addSeparator();
		inventory.add(revenue_sus_trans);
		inventory.addSeparator();
		inventory.add(update_stock_price);
		inventory.addSeparator();

		JToolBar seach = new JToolBar();
		seach.setFloatable(false);
		seach.setRollover(true);
		new_payment = new JButton("<html><p>Process<br>Transaction</p></html> ");
		new_payment.setToolTipText(new ToolTips().pro_trans);
		new_payment.addActionListener(this);
		new_payment.addMouseListener(new HoverListener(soundeffect));
		re_print = new JButton("<html><p>Reprint<br>Receipt</p></html>");
		re_print.setToolTipText(new ToolTips().reprint);
		re_print.addActionListener(this);
		re_print.addMouseListener(new HoverListener(soundeffect));
		outstand_channge = new JButton("<html><p>Outstanding<br>Change($)</p></html>");
		outstand_channge.setToolTipText(new ToolTips().changes);
		outstand_channge.addActionListener(this);
		outstand_channge.addMouseListener(new HoverListener(soundeffect));

		find_trans = new JButton("<html><p>Session<br>Manager</p></html>");
		find_trans.setToolTipText(new ToolTips().sessions);
		find_trans.addActionListener(this);
		find_trans.addMouseListener(new HoverListener(soundeffect));

		seach.add(new_payment);
		seach.addSeparator();
		seach.add(outstand_channge);
		seach.addSeparator();
		seach.add(re_print);
		seach.addSeparator();
		seach.addSeparator();
		seach.addSeparator();
		seach.addSeparator();
		seach.add(find_trans);
		seach.addSeparator();

		JToolBar run = new JToolBar();
		run.setFloatable(false);
		run.setRollover(true);
		recon = new JButton("<html><p>Reconciliation<br>Statement</p></html>");
		recon.setToolTipText(new ToolTips().recon);
		recon.addActionListener(this);
		recon.addMouseListener(new HoverListener(soundeffect));
		daily_sales_report = new JButton("<html><p>Daily Sales<br>Report</p></html>");
		daily_sales_report.setToolTipText(new ToolTips().daily_sales_report);
		daily_sales_report.addActionListener(this);
		daily_sales_report.addMouseListener(new HoverListener(soundeffect));
		expense_list = new JButton("<html><p>Generate<br>Expenses List</p></html>");
		expense_list.setToolTipText(new ToolTips().expenses_report);
		expense_list.addActionListener(this);
		expense_list.addMouseListener(new HoverListener(soundeffect));
		sales_rep = new JButton("<html><p>Previous<br>Sales Report</p></html>");
		sales_rep.setToolTipText(new ToolTips().pre_sales);
		sales_rep.addActionListener(this);
		sales_rep.addMouseListener(new HoverListener(soundeffect));
		in_stock = new JButton("<html><p>In Stock<br> Report</p></html>");
		in_stock.setToolTipText(new ToolTips().in_stock);
		in_stock.addActionListener(this);
		in_stock.addMouseListener(new HoverListener(soundeffect));
		run.add(daily_sales_report);
		run.addSeparator();
		run.add(recon);
		run.addSeparator();
		run.add(expense_list);
		run.addSeparator();
		run.add(sales_rep);
		run.addSeparator();
		run.add(in_stock);

		pane.addTab("<html><h4 style='padding:5px;background-color:#59599C;color:#FFFFFF;'>  Inventory  </h4></html>",
				null, inventory, "Admin work");
		pane.addTab(
				"<html><h4 style='padding:5px;background-color:#628275;color:#FFFFFF;'>  Transactions  </h4></html>",
				null, seach, "Payments processing");
		pane.addTab("<html><h4 style='padding:5px;background-color:#243806;color:#FFFFFF;'>  Generate  </h4></html>",
				null, run, "Run");

		pane.addMouseMotionListener(new TabMouseMotionListener());

		return pane;
	}

	public void deleteTemporaryData() {
		DeleteTMPTableData delete = new DeleteTMPTableData(adbase.rs, adbase.stm);
		delete.deleteTemporaryData();
	}

	public void addReceipts() {
		AddReceiptsToList addreceipts = new AddReceiptsToList(adbase.rs, adbase.stm, paymentslistmodel, totallabel);
		addreceipts.returnData();
	}

	public void stockPricing() {
		new RemoveTab(tabs).removeTab("Stock Pricing");
		newproducttype.insertTab();
	}

	public void clearData() {
		display.setText("");
		insertTab();
		tellername.setText("");
		numberoftrans.setText("");
		showDialog();
	}

	public static void main(String[] args) {
		MainUI ui = new MainUI();
		EventQueue.invokeLater(() -> {
			new AnimateJFrame().fadeIn(ui, 100);
		});
	}

	public void closeAndQuit() {
		int x = JOptionPane.showConfirmDialog(this,
				"You are force quitting this session!\nYou will need to disconnect.\nAre you sure to proceed?",
				"Force Disconnect", JOptionPane.YES_NO_OPTION);
		if (x == JOptionPane.YES_OPTION) {
			new AnimateJFrame().fadeOut(this, 100);
			System.exit(0);
		} else {
			return;
		}
	}
}
