/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cashiermodules;

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
import java.awt.event.ItemEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import com.deco.TranslucentJPanel;
import com.toedter.calendar.JDateChooser;

import buttons.RoundButton;
import helpers.DoubleForm;
import helpers.GetProductNames;
import helpers.IconImage;
import helpers.Increment;
import helpers.MathKeysValidator;
import tr.audio.ButtonListener;
import tr.audio.CharButtonListener;
import tr.audio.ClearCartListener;
import tr.audio.Click;
import tr.audio.MathOppListener;
import tr.audio.OkMouseListener;
import tr.insertintocashpay.InsertTransaction;
import tr.receipts.MakeReceipt;
import tr.script.Script;
import tr.temporarydata.delete.DeleteTMPTableData;
import tr.temporarydata.getdata.GetTemporaryData;
import tr.temporarydata.manage.ManageTemporaryData;
import tr.temporarydata.unitprice.GetUnitPrice;

/**
 *
 * @author Wellington
 */
@SuppressWarnings("serial")
public class NewPayment extends JPanel implements ActionListener {

	JTextField amounttxt;
	JComboBox<Object> memberids;
	JComboBox<Object> months;
	JDateChooser birth;
	JButton submit, cancel, uploadimage;
	JLabel imagelbl;
	JTabbedPane tabs;
	ResultSet rs, rs1;
	Statement stm, stmt;
	PreparedStatement pstmt;
	Connection conn;
	JFrame frame;
	private JComboBox<Object> itemscombo;
	DoubleForm df;
	String pay;
	DefaultListModel<String> listmodel;
	PaymentsList list = new PaymentsList();
	JLabel totallabel;
	String cashierid;
	SpinnerNumberModel spinnerModel;
	JSpinner spinner;
	String[] buttonText = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "*", "+", ".", "-", "/", "(", ")" };
	JButton[] calculatorButtons;
	JButton plus, minus, receipt;
	JTextArea display;
	private JButton enterbutton;
	JLabel[] allLabels;
	private JLabel totallbl, changelbl;
	private JLabel topmostlbl;
	private JTextField rendered;
	private JDialog dialog;
	private JRadioButton yes;
	private JRadioButton no;
	private JButton okButton;
	private JButton ocancelButton;
	private double changefee;
	private JToggleButton usedialpad;
	private JRadioButton normalmode;
	Click soundclick;
	JCheckBoxMenuItem tuner;
	private JDialog dialog1;
	private JLabel changelbl1;
	private JRadioButton yes1;
	private JRadioButton no1;
	private JButton okButton1;
	private JButton ocancelButton1;
	private JButton reset;

	public NewPayment(JTabbedPane tabs, ResultSet rs, ResultSet rs1, Statement stm, Statement stmt,
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
		df = new DoubleForm();
		cashierid = "1";// LogData.details.get(LogData.details.size() - 1);
		soundclick = new Click();
		init();

	}

	public void init() {
		JPanel topp = new TranslucentJPanel(Color.BLUE);
		topp.setLayout(new FlowLayout());
		topmostlbl = new JLabel("", SwingConstants.CENTER);
		topmostlbl.setForeground(Color.WHITE);
		topmostlbl.setFont(new Font("", Font.BOLD, 19));
		topp.add(topmostlbl, SwingConstants.CENTER);

		JPanel leftpanel = new JPanel(new BorderLayout());
		JPanel topleft = new JPanel(new FlowLayout());

		int itemsarraysize = new GetProductNames(rs, stm).getClassNames().size();
		Object[] seme = new String[itemsarraysize];
		for (int i = 0; i < itemsarraysize; i++) {
			seme[i] = new GetProductNames(rs, stm).getClassNames().get(i);
		}

		JLabel itemlbl = new JLabel("Choose item", SwingConstants.CENTER);
		itemlbl.setFont(new Font("", Font.BOLD, 16));
		// itemlbl.setForeground(Color.WHITE);

		itemscombo = new JComboBox<>(seme);
		itemscombo.setMaximumRowCount(15);
		itemscombo.setFont(new Font("", Font.PLAIN, 18));
		itemscombo.setPreferredSize(new Dimension(250, 40));
		itemscombo.addItemListener((e) -> {
			Object item = e.getItem();
			if (e.getStateChange() == ItemEvent.SELECTED) {
				String selecteditem = item.toString();
				double unitprice = new GetUnitPrice(rs, stm).getunitPrice(selecteditem);
				String labeltext = "Unit Price for " + selecteditem.toUpperCase() + " = $" + unitprice;
				EventQueue.invokeLater(() -> {
					topmostlbl.setText(labeltext);
				});

			} else if (e.getStateChange() == ItemEvent.DESELECTED) {
			}
		});
		itemscombo.addKeyListener(new KeyAdapter() {

			@Override
			public void keyTyped(KeyEvent e) {
				char character = e.getKeyChar();
				if (character == KeyEvent.VK_PLUS || character == KeyEvent.VK_INSERT || character == KeyEvent.VK_ADD
						|| character == KeyEvent.VK_SPACE || character == KeyEvent.VK_SHIFT
						|| character == KeyEvent.VK_ENTER) {
					addProducatsToCart();
				} else if (character == KeyEvent.VK_MINUS || character == KeyEvent.VK_DELETE
						|| character == KeyEvent.VK_BACK_SPACE) {
					removeProductFromCart();
				} else if (character == KeyEvent.VK_EQUALS) {
					focusTenderred();
				} else {

				}
			}
		});
		Box box1 = Box.createVerticalBox();
		box1.add(itemlbl);
		box1.add(Box.createVerticalStrut(4));
		box1.add(itemscombo);

		int minValue = 1;
		int maxValue = 100;
		int currentValue = 1;
		int steps = 1;
		spinnerModel = new SpinnerNumberModel(currentValue, minValue, maxValue, steps);
		spinner = new JSpinner(spinnerModel);
		spinner.setFont(new Font("", Font.PLAIN, 18));
		JSpinner.NumberEditor nEditor = new JSpinner.NumberEditor(spinner, "0");
		spinner.setEditor(nEditor);

		// adding a keylistener to the spinner to the spinner that is used to
		// select the quantity ofa given item
		spinner.addKeyListener(new KeyAdapter() {

			@Override
			public void keyTyped(KeyEvent e) {
				char character = e.getKeyChar();
				if (character == KeyEvent.VK_PLUS || character == KeyEvent.VK_INSERT || character == KeyEvent.VK_ADD
						|| character == KeyEvent.VK_SPACE || character == KeyEvent.VK_SHIFT
						|| character == KeyEvent.VK_ENTER) {
					addProducatsToCart();
				} else if (character == KeyEvent.VK_MINUS || character == KeyEvent.VK_DELETE
						|| character == KeyEvent.VK_BACK_SPACE) {
					removeProductFromCart();
				} else {

				}
			}
		});
		spinner.addChangeListener((e) -> {

		});

		JLabel qn = new JLabel("Quantity", SwingConstants.CENTER);
		qn.setFont(new Font("", Font.BOLD, 16));
		// qn.setForeground(Color.WHITE);
		Box box2 = Box.createVerticalBox();
		box2.add(qn);
		box2.add(Box.createVerticalStrut(4));
		box2.add(spinner);

		topleft.add(box1);
		topleft.add(Box.createHorizontalStrut(30));
		topleft.add(box2);

		JPanel centerleft = new JPanel(new BorderLayout());
		centerleft.setBackground(Color.WHITE);
		JPanel calculatorpanel = new JPanel(new GridLayout(6, 3, 5, 5));
		calculatorpanel.setBorder(new TitledBorder(""));
		calculatorpanel.setBackground(Color.WHITE);

		calculatorButtons = new JButton[buttonText.length];
		for (int i = 0; i < buttonText.length; i++) {
			calculatorButtons[i] = new RoundButton(buttonText[i]);
			calculatorButtons[i].setBackground(new Color(10, 75, 90));
			calculatorButtons[i].setForeground(Color.WHITE);
			calculatorButtons[i].setFont(new Font("", Font.BOLD, 25));
			calculatorButtons[i].setToolTipText(buttonText[i]);
			calculatorButtons[i].setEnabled(false);
			calculatorpanel.add(calculatorButtons[i]);
		}
		setOnNermalMode();

		calculatorButtons[10].setBackground(new Color(100, 80, 90));
		calculatorButtons[11].setBackground(new Color(100, 80, 90));
		calculatorButtons[12].setBackground(new Color(100, 45, 101));
		calculatorButtons[13].setBackground(new Color(130, 75, 100));

		calculatorButtons[0].addActionListener((ev) -> {
			String newtext = "1";
			String oldtext = getRenderedText();
			rendered.setText(oldtext + newtext);
			rendered.requestFocusInWindow();
		});
		calculatorButtons[0].addMouseListener(new ButtonListener(tuner));
		calculatorButtons[1].addActionListener((ev) -> {
			String newtext = "2";
			String oldtext = getRenderedText();
			rendered.setText(oldtext + newtext);
			rendered.requestFocusInWindow();
		});
		calculatorButtons[1].addMouseListener(new ButtonListener(tuner));
		calculatorButtons[2].addActionListener((ev) -> {
			String newtext = "3";
			String oldtext = getRenderedText();
			rendered.setText(oldtext + newtext);
			rendered.requestFocusInWindow();
		});
		calculatorButtons[2].addMouseListener(new ButtonListener(tuner));
		calculatorButtons[3].addActionListener((ev) -> {
			String newtext = "4";
			String oldtext = getRenderedText();
			rendered.setText(oldtext + newtext);
			rendered.requestFocusInWindow();
		});
		calculatorButtons[3].addMouseListener(new ButtonListener(tuner));
		calculatorButtons[4].addActionListener((ev) -> {
			String newtext = "5";
			String oldtext = getRenderedText();
			rendered.setText(oldtext + newtext);
			rendered.requestFocusInWindow();
		});
		calculatorButtons[4].addMouseListener(new ButtonListener(tuner));
		calculatorButtons[5].addActionListener((ev) -> {
			String newtext = "6";
			String oldtext = getRenderedText();
			rendered.setText(oldtext + newtext);
			rendered.requestFocusInWindow();
		});
		calculatorButtons[5].addMouseListener(new ButtonListener(tuner));
		calculatorButtons[6].addActionListener((ev) -> {
			String newtext = "7";
			String oldtext = getRenderedText();
			rendered.setText(oldtext + newtext);
			rendered.requestFocusInWindow();
		});
		calculatorButtons[6].addMouseListener(new ButtonListener(tuner));
		calculatorButtons[7].addActionListener((ev) -> {
			String newtext = "8";
			String oldtext = getRenderedText();
			rendered.setText(oldtext + newtext);
			rendered.requestFocusInWindow();
		});
		calculatorButtons[7].addMouseListener(new ButtonListener(tuner));
		calculatorButtons[8].addActionListener((ev) -> {
			String newtext = "9";
			String oldtext = getRenderedText();
			rendered.setText(oldtext + newtext);
			rendered.requestFocusInWindow();
		});
		calculatorButtons[8].addMouseListener(new ButtonListener(tuner));
		calculatorButtons[9].addActionListener((ev) -> {
			String newtext = "0";
			String oldtext = getRenderedText();
			rendered.setText(oldtext + newtext);
			rendered.requestFocusInWindow();
		});
		calculatorButtons[9].addMouseListener(new ButtonListener(tuner));
		calculatorButtons[10].addActionListener((ev) -> {
			String newtext = "*";
			String oldtext = getRenderedText();
			rendered.setText(oldtext + newtext);
			rendered.requestFocusInWindow();
		});
		calculatorButtons[10].addMouseListener(new MathOppListener(tuner));
		calculatorButtons[11].addActionListener((ev) -> {
			String newtext = "+";
			String oldtext = getRenderedText();
			rendered.setText(oldtext + newtext);
			rendered.requestFocusInWindow();
		});
		calculatorButtons[11].addMouseListener(new MathOppListener(tuner));
		calculatorButtons[12].addActionListener((ev) -> {
			String newtext = ".";
			String oldtext = getRenderedText();
			rendered.setText(oldtext + newtext);
			rendered.requestFocusInWindow();
		});
		calculatorButtons[12].addMouseListener(new CharButtonListener(tuner));
		calculatorButtons[13].addActionListener((ev) -> {
			String newtext = "-";
			String oldtext = getRenderedText();
			rendered.setText(oldtext + newtext);
			rendered.requestFocusInWindow();
		});
		calculatorButtons[13].addMouseListener(new MathOppListener(tuner));
		calculatorButtons[14].addActionListener((ev) -> {
			String newtext = "/";
			String oldtext = getRenderedText();
			rendered.setText(oldtext + newtext);
			rendered.requestFocusInWindow();
		});
		calculatorButtons[14].addMouseListener(new MathOppListener(tuner));
		calculatorButtons[15].addActionListener((ev) -> {
			String newtext = "(";
			String oldtext = getRenderedText();
			rendered.setText(oldtext + newtext);
			rendered.requestFocusInWindow();
		});
		calculatorButtons[15].addMouseListener(new CharButtonListener(tuner));
		calculatorButtons[16].addActionListener((ev) -> {
			String newtext = ")";
			String oldtext = getRenderedText();
			rendered.setText(oldtext + newtext);
			rendered.requestFocusInWindow();
		});
		calculatorButtons[16].addMouseListener(new CharButtonListener(tuner));

		enterbutton = new JButton("<html><h1>ENTER</h1></html>");
		enterbutton.addActionListener(this);

		calculatorpanel.add(enterbutton);
		leftpanel.add(topleft, BorderLayout.NORTH);
		plus = new JButton("<html><h1>+<h1></html>");
		plus.addActionListener(this);
		plus.addMouseListener(new MathOppListener(tuner));
		plus.setBackground(Color.BLUE);
		plus.setForeground(Color.WHITE);

		minus = new JButton("<html><h1>-<h1></html>");
		minus.addActionListener(this);
		minus.addMouseListener(new CharButtonListener(tuner));
		minus.setBackground(Color.RED);
		minus.setForeground(Color.WHITE);

		receipt = new JButton("<html><h1>OK<h1></html>");
		receipt.setBackground(new Color(10, 70, 90));
		receipt.setForeground(Color.WHITE);

		reset = new JButton("<html><h3>Reset \u2192<h3></html>");
		reset.setBackground(new Color(10, 40, 100));
		reset.setForeground(Color.WHITE);

		Box buttonBox = Box.createVerticalBox();
		buttonBox.add(plus);
		buttonBox.add(Box.createVerticalStrut(5));
		buttonBox.add(minus);
		buttonBox.add(Box.createVerticalStrut(15));
		buttonBox.add(receipt);
		buttonBox.add(Box.createVerticalStrut(20));
		buttonBox.add(reset);
		centerleft.add(calculatorpanel, BorderLayout.CENTER);
		centerleft.add(buttonBox, BorderLayout.EAST);

		leftpanel.add(centerleft, BorderLayout.CENTER);

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
					deleteTemporaryData();
				}
			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// no iplementation for the key released action
			}

			@Override
			public void keyTyped(KeyEvent arg0) {
				// yet to implement key typed key event
			}
		});

		JScrollPane scroller = new JScrollPane(display);
		scroller.setBorder(null);

		JPanel rightpanel = new JPanel(new BorderLayout());
		rightpanel.setBackground(Color.BLACK);
		JLabel cartlbl = new JLabel("<html><i><u>Shopping Cart \u2193</u></i></html>", SwingConstants.CENTER);
		cartlbl.setForeground(Color.WHITE);
		cartlbl.setFont(new Font("", Font.BOLD, 18));

		JLabel label1 = new JLabel("TOTAL $");
		label1.setForeground(Color.WHITE);
		label1.setFont(new Font("", Font.BOLD, 30));

		totallbl = new JLabel("0.00");
		totallbl.setForeground(Color.WHITE);
		totallbl.setFont(new Font("", Font.BOLD, 30));

		Box topprightbox = Box.createHorizontalBox();
		topprightbox.add(label1);
		topprightbox.add(totallbl);
		topprightbox.add(Box.createHorizontalStrut(15));
		topprightbox.add(cartlbl);

		String[] labels = { "Item Category", "", "Quantity", "Total Cost($)" };
		allLabels = new JLabel[labels.length];
		JPanel rightupperPanel = new JPanel(new GridLayout(1, 4, 5, 5));
		// for loops for the painting of labels here
		for (int x = 0; x < labels.length; x++) {
			allLabels[x] = new JLabel(labels[x]);
			allLabels[x].setSize(3, 3);
			allLabels[x].setForeground(Color.RED);
			allLabels[x].setFont(new Font("", Font.BOLD, 20));
			rightupperPanel.add(allLabels[x]);
		}
		Box rightbox = Box.createVerticalBox();
		rightbox.add(topprightbox);
		rightbox.add(Box.createVerticalStrut(20));
		rightbox.add(rightupperPanel);

		rightpanel.add(rightbox, BorderLayout.NORTH);
		rightpanel.add(scroller, BorderLayout.CENTER);

		this.setLayout(new BorderLayout());
		JPanel mainpanbel = new JPanel(new GridLayout(1, 2, 15, 0));
		rendered = new JTextField();
		rendered.addKeyListener(new MathKeysValidator());
		rendered.setBackground(Color.BLACK);
		rendered.setForeground(Color.WHITE);
		rendered.addActionListener(this);
		rendered.setFont(new Font("", Font.ROMAN_BASELINE, 30));

		// rightpanel.add(rendered, BorderLayout.SOUTH);
		rendered.setEditable(false);

		mainpanbel.add(leftpanel);
		mainpanbel.add(rightpanel);
		JPanel topmostpanel = new JPanel(new BorderLayout());
		// topmostpanel.setLayout(new GridLayout(1, 3, 3, 0));
		usedialpad = new JRadioButton("Use Dial Pad", false);
		usedialpad.setForeground(Color.WHITE);
		normalmode = new JRadioButton("Use Normal Mode", true);
		normalmode.setForeground(Color.WHITE);
		ButtonGroup swichmodegroup = new ButtonGroup();
		swichmodegroup.add(normalmode);
		swichmodegroup.add(usedialpad);
		usedialpad.addActionListener(this);
		normalmode.addActionListener(this);
		Box checkboxes = Box.createVerticalBox();
		checkboxes.setBorder(new TitledBorder(""));
		checkboxes.add(normalmode);
		checkboxes.add(Box.createVerticalStrut(8));
		checkboxes.add(usedialpad);

		JPanel anotherpanel = new JPanel(new GridLayout(1, 2));
		anotherpanel.setBackground(new Color(10, 90, 90));
		anotherpanel.add(topmostlbl);
		anotherpanel.add(rendered);

		topmostpanel.add(checkboxes, BorderLayout.WEST);
		topmostpanel.setBackground(new Color(10, 90, 90));
		topmostpanel.add(anotherpanel, BorderLayout.CENTER);

		this.add(topmostpanel, BorderLayout.NORTH);
		this.add(mainpanbel, BorderLayout.CENTER);
		// this.add(checkboxes, BorderLayout.WEST);

		receipt.addActionListener(this);
		receipt.addMouseListener(new OkMouseListener(tuner));
		reset.addActionListener(this);
		reset.addMouseListener(new ClearCartListener(tuner));

		changelbl = new JLabel("", JLabel.CENTER);
		// changelbl.setForeground(Color.WHITE);
		changelbl.setFont(new Font("", Font.BOLD, 25));
		// showDialog();
		// showDialog1();
		changelbl1 = new JLabel("", JLabel.CENTER);
		// changelbl1.setForeground(Color.WHITE);
		changelbl1.setFont(new Font("", Font.BOLD, 25));
	}

	public void insertTab() {
		EventQueue.invokeLater(() -> {
			int numberoftabs = tabs.getTabCount();
			boolean exist = false;
			for (int a = 0; a < numberoftabs; a++) {
				if (tabs.getTitleAt(a).trim().equals("New Transaction")) {
					exist = true;
					tabs.setSelectedIndex(numberoftabs);
					break;
				}
			}
			if (!exist) {
				tabs.addTab("New Transaction   ", null, this, "Make New Transaction");
				tabs.setSelectedIndex(numberoftabs);
			}
		});
	}

	public void processTemporaryTransaction(String product_name, int quantity, double amount) {
		ManageTemporaryData manage = new ManageTemporaryData(rs, stm);
		manage.manageTemporaryData(product_name, quantity, amount);

		GetTemporaryData getdata = new GetTemporaryData(rs, rs1, stm, stmt);

		EventQueue.invokeLater(() -> {
			getdata.getTemporaryData(display);
		});
		EventQueue.invokeLater(() -> {
			totallbl.setText("" + getdata.getTotalCost());
		});
		EventQueue.invokeLater(() -> {
			// itemscombo.setSelectedIndex(0);
		});
		EventQueue.invokeLater(() -> {
			spinnerModel.setValue(spinnerModel.getMinimum());
		});
	}

	public void clearCart() {
		EventQueue.invokeLater(() -> {
			display.setText("");
		});
	}

	public void showDialog() {
		dialog = new JDialog((JFrame) null, "Change Suggestion", true);
		dialog.getContentPane().setLayout(new BorderLayout());
		dialog.setIconImage(new IconImage().createIconImage());

		dialog.getContentPane().add(changelbl, BorderLayout.NORTH);

		JLabel top = new JLabel("<html><h1>Keep Change?<h1>", SwingConstants.CENTER);
		// top.setForeground(Color.WHITE);

		Box b = Box.createVerticalBox();
		JLabel yeslbl = new JLabel("YES", SwingConstants.CENTER);
		yeslbl.setFont(new Font("", Font.BOLD, 19));
		yes = new JRadioButton();
		b.add(yeslbl);
		b.add(yes);

		Box b1 = Box.createVerticalBox();
		JLabel nolbl = new JLabel("NO", SwingConstants.CENTER);
		nolbl.setFont(new Font("", Font.BOLD, 19));
		no = new JRadioButton("", true);
		no.setBackground(Color.GREEN);
		// no.setSelected(true);
		b1.add(nolbl);
		b1.add(no);

		ButtonGroup bgr = new ButtonGroup();
		bgr.add(yes);
		bgr.add(no);

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
		// dialog.getContentPane().setBackground(new Color(0.5f, 0.5f, 1f));
		dialog.getContentPane().setBackground(Color.WHITE);
		dialog.getContentPane().add(midpanel, BorderLayout.CENTER);

		okButton = new JButton("OK");
		okButton.addActionListener(this);
		ocancelButton = new JButton("Cancel");
		ocancelButton.setBackground(Color.MAGENTA);
		ocancelButton.setForeground(Color.WHITE);
		ocancelButton.addActionListener(this);
		buttonbox.add(Box.createHorizontalGlue());
		buttonbox.add(okButton);
		buttonbox.add(Box.createHorizontalStrut(30));
		buttonbox.add(ocancelButton);
		dialog.getRootPane().setDefaultButton(okButton);

		dialog.setSize(300, 185);
		dialog.setResizable(false);
		Dimension d = dialog.getSize(), screen = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screen.width - d.width) / 2, y = (screen.height - d.height) / 2;
		dialog.setLocation(x, y);
		dialog.setVisible(true);

		dialog.setAlwaysOnTop(true);
		okButton.addMouseListener(new OkMouseListener(tuner));
	}

	public void showDialog1() {
		dialog1 = new JDialog((JFrame) null, "Change Suggestion", true);
		dialog1.setLayout(new BorderLayout());
		dialog1.setIconImage(new IconImage().createIconImage());

		dialog1.getContentPane().add(changelbl1, BorderLayout.NORTH);

		JLabel top = new JLabel("<html><h1>Keep Change?<h1>", SwingConstants.CENTER);
		top.setForeground(Color.WHITE);

		Box b = Box.createVerticalBox();
		JLabel yeslbl = new JLabel("YES", SwingConstants.CENTER);
		yeslbl.setFont(new Font("", Font.BOLD, 19));
		yes1 = new JRadioButton();
		b.add(yeslbl);
		b.add(yes1);

		Box b1 = Box.createVerticalBox();
		JLabel nolbl = new JLabel("NO", SwingConstants.CENTER);
		nolbl.setFont(new Font("", Font.BOLD, 19));
		no1 = new JRadioButton("", true);
		no1.setBackground(Color.GREEN);
		// no.setSelected(true);
		b1.add(nolbl);
		b1.add(no1);

		ButtonGroup bgr = new ButtonGroup();
		bgr.add(yes1);
		bgr.add(no1);

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
		dialog1.getContentPane().setBackground(new Color(0.5f, 0.5f, 1f));
		dialog1.getContentPane().add(midpanel, BorderLayout.CENTER);

		okButton1 = new JButton("OK");
		okButton1.addActionListener(this);
		ocancelButton1 = new JButton("Cancel");
		ocancelButton1.setBackground(Color.MAGENTA);
		ocancelButton1.setForeground(Color.WHITE);
		ocancelButton1.addActionListener(this);
		buttonbox.add(Box.createHorizontalGlue());
		buttonbox.add(okButton1);
		buttonbox.add(Box.createHorizontalStrut(30));
		buttonbox.add(ocancelButton1);
		dialog1.getRootPane().setDefaultButton(okButton1);

		dialog1.setSize(300, 185);
		dialog1.setResizable(false);
		Dimension d = dialog1.getSize(), screen = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screen.width - d.width) / 2, y = (screen.height - d.height) / 2;
		dialog1.setLocation(x, y);
		dialog1.setVisible(true);
		dialog1.setAlwaysOnTop(true);
		okButton1.addMouseListener(new OkMouseListener(tuner));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		DoubleForm df = new DoubleForm();
		if (e.getSource() == plus) {
			addProducatsToCart();
		}
		if (e.getSource() == minus) {
			removeProductFromCart();
		}
		if (e.getSource() == receipt) {

			focusTenderred();

		}
		if (e.getSource() == rendered || e.getSource() == enterbutton) {
			String the_totallbl = totallbl.getText().trim();
			if (!the_totallbl.equals("0.00")) {
				double totalcost = Double.parseDouble(the_totallbl);
				if (!rendered.getText().equals("")) {
					double renderedamount = getRenderedAmount();
					if (renderedamount < totalcost) {
						JOptionPane.showMessageDialog(frame,
								"Tendered $" + renderedamount + " is less than  $" + totalcost + " total cost!",
								"Invalid Payment", JOptionPane.ERROR_MESSAGE);
					} else {
						if (normalmode.isSelected()) {
							changefee = df.form(renderedamount - totalcost);
							changelbl.setText("CHANGE $" + changefee);
							soundclick.dialogPlayer();
							showDialog();
							// rendered.setEditable(false);
							for (int i = 0; i < buttonText.length; i++) {
								calculatorButtons[i].setEnabled(false);
							}
						} else {
							changefee = df.form(renderedamount - totalcost);
							String text = getRenderedText();
							String script = new Script().evaluateString(text);
							totallbl.setText(script);
							soundclick.dialogPlayer();
							changelbl1.setText("CHANGE $" + changefee);
							showDialog();
						}
					}
				} else {
					JOptionPane.showMessageDialog(frame, "No Cash Tendered!", "Invalid Payment",
							JOptionPane.ERROR_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(frame, "No goods are in the shopping cart!", "No items",
						JOptionPane.ERROR_MESSAGE);
				rendered.setText("");
			}
		}

		if (e.getSource() == okButton) {
			double given = getRenderedAmount();
			double cost = Double.parseDouble(totallbl.getText());// getTotalCostOfGoods();
			double change = df.form(given - cost);
			double collected = 0;
			int receiptno = new Increment(rs1, stmt).incrementReceiptno();
			InsertTransaction insert = new InsertTransaction(rs, rs1, stm, stmt);
			if (no.isSelected()) {
				dialog.dispose();
				collected = change;
				insert.affectTransaction(given, change, collected, receiptno, cost);
				new MakeReceipt(rs, rs1, stm, stmt).makeReceipt(receiptno, cost, given, change);
				list.addtoArray(Integer.toString(receiptno), Double.toString(cost));
				list.addToListModel(listmodel);
				EventQueue.invokeLater(() -> {
					totallabel.setText(list.getTotal());
				});
				clearData();
			} else {
				dialog.dispose();
				collected = 0;
				insert.affectTransaction(given, change, collected, receiptno, cost);
				new MakeReceipt(rs, rs1, stm, stmt).makeReceipt(receiptno, cost, given, change);
				list.addtoArray(Integer.toString(receiptno), Double.toString(cost));
				list.addToListModel(listmodel);
				EventQueue.invokeLater(() -> {
					totallabel.setText(list.getTotal());
				});
				clearData();
			}
		}
		if (e.getSource() == usedialpad) {
			display.setText("");
			totallbl.setText("0.00");
			rendered.setEditable(true);
			rendered.requestFocusInWindow();
			// plus.setEnabled(false);
			// minus.setEnabled(false);
			// receipt.setEnabled(false);
			for (int i = 0; i < buttonText.length; i++) {
				// calculatorButtons[i].setEnabled(true);
				calculatorButtons[i].setVisible(true);
			}
		}
		if (e.getSource() == normalmode) {
			for (int i = 0; i < buttonText.length; i++) {
				// calculatorButtons[i].setEnabled(false);
				calculatorButtons[i].setVisible(false);
			}
			rendered.setText("");
			rendered.setEditable(false);
			plus.setEnabled(true);
			minus.setEnabled(true);
			receipt.setEnabled(true);
		}
		if (e.getSource() == ocancelButton) {
			dialog.dispose();
			rendered.setEditable(true);
			rendered.requestFocusInWindow();
			/*
			 * for (int i = 0; i < buttonText.length; i++) {
			 * calculatorButtons[i].setEnabled(true); }
			 */
		}
		if (e.getSource() == okButton1) {
			double given = getRenderedAmount();
			double cost = Double.parseDouble(totallbl.getText());// getTotalCostOfGoods();
			double change = df.form(given - cost);
			double collected = 0;
			int receiptno = new Increment(rs1, stmt).incrementReceiptno();
			InsertTransaction insert = new InsertTransaction(rs, rs1, stm, stmt);
			if (no1.isSelected()) {
				dialog1.dispose();
				collected = change;
				insert.affectTransaction(given, change, collected, receiptno, cost);
				new MakeReceipt(rs, rs1, stm, stmt).makeReceipt(receiptno, cost, given, change);
				list.addtoArray(Integer.toString(receiptno), Double.toString(cost));
				list.addToListModel(listmodel);
				totallabel.setText(list.getTotal());
				clearData();
			} else {
				dialog1.dispose();
				collected = 0;
				insert.affectTransaction(given, change, collected, receiptno, cost);
				new MakeReceipt(rs, rs1, stm, stmt).makeReceipt(receiptno, cost, given, change);
				list.addtoArray(Integer.toString(receiptno), Double.toString(cost));
				list.addToListModel(listmodel);
				totallabel.setText(list.getTotal());
				clearData();
			}
		}
		if (e.getSource() == ocancelButton1) {
			dialog1.dispose();
			rendered.setEditable(true);
			rendered.requestFocusInWindow();
			for (int i = 0; i < buttonText.length; i++) {
				calculatorButtons[i].setEnabled(true);
			}
		}
		if (e.getSource() == reset) {
			clearData();
			deleteTemporaryData();
		}
	}

	private void focusTenderred() {
		String the_totallbl = totallbl.getText().trim();
		if (!the_totallbl.equals("0.00")) {
			rendered.setEditable(true);
			rendered.getCaret().setVisible(true);
			rendered.requestFocusInWindow();
		} else {
			JOptionPane.showMessageDialog(this, "There's nothing in the cart", "No goods", JOptionPane.ERROR_MESSAGE);
		}

		/*
		 * for (int i = 0; i < buttonText.length; i++) {
		 * calculatorButtons[i].setEnabled(true); }
		 */
	}

	// this method removes any selected item from the cart.
	private void removeProductFromCart() {
		clearCart();
		String product_name = itemscombo.getSelectedItem().toString();
		if (!product_name.equals("")) {
			Object qn = spinnerModel.getNumber();
			int quantity = (int) qn;
			double unitprice = new GetUnitPrice(rs, stm).getunitPrice(product_name);
			double cost_price = df.form(unitprice * quantity);
			processTemporaryTransaction(product_name, (-1) * quantity, (-1) * cost_price);
		} else {

		}

	}

	// this method adds any selected items to the cart.
	private void addProducatsToCart() {
		clearCart();
		String product_name = itemscombo.getSelectedItem().toString();
		if (!product_name.equals("")) {
			Object qn = spinnerModel.getNumber();
			int quantity = (int) qn;
			double unitprice = new GetUnitPrice(rs, stm).getunitPrice(product_name);
			double cost_price = df.form(unitprice * quantity);
			processTemporaryTransaction(product_name, quantity, cost_price);
		} else {

		}
	}

	public double getTotalCostOfGoods() {
		return new GetTemporaryData(rs, rs1, stm, pstmt).getTotalCost();
	}

	public void clearData() {
		EventQueue.invokeLater(() -> {
			rendered.setText("");
		});
		EventQueue.invokeLater(() -> {
			display.setText("");
		});
		EventQueue.invokeLater(() -> {
			totallbl.setText("0.00");
		});
	}

	public String getRenderedText() {
		return rendered.getText();
	}

	public double getRenderedAmount() {
		String script = new Script().evaluateString(rendered.getText());
		return Double.parseDouble(script);
	}

	public void closeDialog(JDialog dialog) {
		ScheduledExecutorService s = Executors.newSingleThreadScheduledExecutor();
		s.schedule(() -> {
			dialog.dispose();
			clearData();
			synchronized (s) {
				deleteTemporaryData();
			}
			JOptionPane.showMessageDialog(frame, "Transaction session has expired", "Session Expired",
					JOptionPane.ERROR_MESSAGE);
		}, 20, TimeUnit.SECONDS);
	}

	// method to delete temporary data, this should be able to manage
	// transactions that InnoDB offers as a functionality.
	public void deleteTemporaryData() {
		DeleteTMPTableData delete = new DeleteTMPTableData(rs, stm);
		delete.deleteTemporaryData();
	}

	public void setOnNermalMode() {
		for (int i = 0; i < buttonText.length; i++) {
			// calculatorButtons[i].setEnabled(false);
			calculatorButtons[i].setVisible(false);
		}
	}
}
