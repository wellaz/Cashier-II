package tr.reconciliation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import com.deco.TranslucentJPanel;
import com.toedter.calendar.JDateChooser;

import helpers.DoubleForm;
import helpers.IconImage;
import helpers.PopupTrigger;
import helpers.TableColumnResizer;
import helpers.TableRenderer;
import helpers.TableRowResizer;

@SuppressWarnings("serial")
public class Reconcile extends JPanel {

	Statement stm;
	Statement stmt;
	ResultSet rs;
	JFrame comp;
	ResultSet rs1;
	private JProgressBar prog;
	private JLabel waitlbl;
	JPanel panel;
	JPanel midpanel, mainpanel;
	// private JTable table;
	JLabel timelbl;
	JTabbedPane tabs;
	private JDateChooser datechooser;
	private JDialog dialog;
	private JDateChooser datechoosert;
	private JButton find;
	private JLabel error;
	String datefrom, dateto, prevday;
	JTable table;
	DoubleForm df;

	public Reconcile(JTabbedPane tabs, Statement stm, ResultSet rs, ResultSet rs1, Statement stmt, JFrame comp) {
		this.rs = rs;
		this.stm = stm;
		this.stmt = stmt;
		this.rs1 = rs1;
		this.comp = comp;
		this.tabs = tabs;
		this.setLayout(new BorderLayout());
		panel = new JPanel(new BorderLayout());
		panel.add(progresspanel(), BorderLayout.CENTER);
		mainpanel = new JPanel(new GridBagLayout());
		mainpanel.add(new JLabel("WAIT"));

		this.add(mainpanel, BorderLayout.CENTER);
		this.add(panel, BorderLayout.SOUTH);
		df = new DoubleForm();
	}

	public void insertTab() {
		EventQueue.invokeLater(() -> {
			int numberoftabs = tabs.getTabCount();
			boolean exist = false;
			for (int a = 0; a < numberoftabs; a++) {
				if (tabs.getTitleAt(a).trim().equals("Reconciliation")) {
					exist = true;
					tabs.setSelectedIndex(a);
					break;
				}
			}
			if (!exist) {
				tabs.addTab("Reconciliation   ", null, this, "Reconciliation Statement");
				tabs.setSelectedIndex(numberoftabs);
				Worker w = new Worker();
				w.execute();
			}
		});
	}

	public Object[][] tableData(double ledgerbal) {
		GetTotalSales ts = new GetTotalSales(rs, stm, comp);
		double totalsub = ts.getTotalSales(datefrom, dateto);

		GetTotalExp exp = new GetTotalExp(rs, stm, comp);
		double totalexp = exp.getTotalExp(datefrom, dateto);

		LambdaDep cdep = new LambdaDep(rs, stm, comp);
		double cdp = cdep.getTotalDep(datefrom, dateto);

		LambdaWith cwith = new LambdaWith(rs, stm, comp);
		double cwt = cwith.getTotalWith(datefrom, dateto);

		Object[][] data = new Object[13][Header.header.length];

		// int tablesize = size1 + size3 + size4 + 15;

		data[0][0] = "Revenue Suspense ";
		data[0][1] = "";
		data[0][2] = "";
		data[0][3] = ledgerbal;

		data[1][0] = "Add Total Sales";
		data[1][1] = "";
		data[1][2] = "";
		data[1][3] = "";

		data[2][0] = "TOTAL";
		data[2][1] = "";
		data[2][2] = totalsub;
		data[2][3] = "";

		data[3][0] = "POSITION A";
		data[3][1] = "";
		data[3][2] = "";
		double posa = ledgerbal + totalsub;
		data[3][3] = df.form(posa);

		data[4][0] = "Less Expenses";
		data[4][1] = "";
		data[4][2] = "";
		data[4][3] = "";

		data[5][0] = "TOTAL";
		data[5][1] = totalexp;
		data[5][2] = "";
		data[5][3] = "";

		data[6][0] = "POSITION B";
		data[6][1] = "";
		data[6][2] = "";
		double posb = posa - totalexp;
		data[6][3] = df.form(posb);

		data[7][0] = "Less Withdrawals From The System";
		data[7][1] = "";
		data[7][2] = "";
		data[7][3] = "";

		data[8][0] = "TOTAL";
		data[8][1] = cwt;
		data[8][2] = "";
		data[8][3] = "";

		data[9][0] = "POSITION C";
		data[9][1] = "";
		data[9][2] = "";
		double posc = posb - cwt;
		data[9][3] = df.form(posc);

		data[10][0] = "Add Deposits Into The System";
		data[10][1] = "";
		data[10][2] = "";
		data[10][3] = "";

		data[11][0] = "TOTAL";
		data[11][1] = "";
		data[11][2] = cdp;
		data[11][3] = "";

		data[12][0] = "POSITION D";
		data[12][1] = "";
		data[12][2] = "";
		double posd = posc + cdp;
		data[12][3] = df.form(posd);

		return data;
	}

	public JPanel midPanel() {
		JPanel panel = new TranslucentJPanel(Color.BLUE);
		panel.setLayout(new BorderLayout());
		double ledgerbal = new GetLedgerBalance(rs, stm).getLedgerBalance(datefrom);
		if (ledgerbal >= 0) {
			Object[][] data = tableData(ledgerbal);
			DefaultTableModel model = new DefaultTableModel(data, Header.header);
			table = new JTable(model) {
				@Override
				public Component prepareRenderer(TableCellRenderer renderer, int Index_row, int Index_col) {
					Component comp = super.prepareRenderer(renderer, Index_row, Index_col);
					// even index, selected or not selected
					if (Index_row % 2 == 0 && !isCellSelected(Index_row, Index_col)) {
						comp.setBackground(new Color(235, 235, 235));
					} else {
						comp.setBackground(new Color(204, 204, 204));
					}
					if (isCellSelected(Index_row, Index_col)) {
						comp.setBackground(new Color(0.5f, 0.5f, 1f));
					}
					if (Index_row == 0 && Index_col == 0) {
						comp.setFont(new java.awt.Font("", Font.BOLD, 18));
						comp.setForeground(new Color(0.3f, 0.2f, 1f));
					}
					if (Index_row == 1 && Index_col == 0) {
						comp.setFont(new java.awt.Font("", Font.BOLD, 18));
						comp.setForeground(new Color(0.1f, 0.1f, 1f));
					}
					if (Index_col == 1) {
						comp.setBackground(Color.RED);
						comp.setFont(new java.awt.Font("", Font.BOLD, 18));
					}
					if (Index_col == 2) {
						comp.setBackground(Color.GREEN);
						comp.setFont(new java.awt.Font("", Font.BOLD, 18));
					}
					if (Index_col == 3) {
						comp.setBackground(Color.GRAY);
						comp.setFont(new java.awt.Font("", Font.BOLD, 18));
					}
					if (Index_row > 1 && Index_col == 0)
						comp.setFont(new java.awt.Font("", Font.BOLD, 15));

					return comp;
				}

				public String getToolTipText(MouseEvent e) {
					String tip = null;
					java.awt.Point p = e.getPoint();
					int rowIndex = rowAtPoint(p);
					int colIndex = columnAtPoint(p);

					try {
						tip = getValueAt(rowIndex, colIndex).toString();
					} catch (RuntimeException e1) {
						// catch null pointer exception if mouse is over an
						// empty line
					}

					return tip;
				}

				@Override
				public boolean isCellEditable(int rowIndex, int colIndex) {
					return false;
				}
			};
			TableRenderer.setJTableColumnsWidth(table, 480, 40, 20, 20, 20);
			table.setRowHeight(30);
			table.addMouseListener(new PopupTrigger(new RecoPopup(datefrom, dateto, rs, stm, comp)));
			new TableColumnResizer(table);
			new TableRowResizer(table);
			table.setShowGrid(true);
			JScrollPane scroll = new JScrollPane();
			scroll.setViewportView(table);
			panel.add(scroll, BorderLayout.CENTER);
			JPanel lowerpanel = new TranslucentJPanel(Color.BLUE);
			lowerpanel.setLayout(new FlowLayout());
			lowerpanel.add(new JLabel());
			JButton generate = new JButton("<html><p>Download<br>PDF File</p></html>");
			lowerpanel.add(generate);
			generate.addActionListener((ActionEvent event) -> {
				GenerateRecoPDF gnpdf = new GenerateRecoPDF(datefrom, dateto, comp, data, table);
				GenerateRecoPDF.Worker wk = gnpdf.new Worker();
				wk.execute();
			});

			panel.add(lowerpanel, BorderLayout.SOUTH);

			JLabel rc = new JLabel(
					"Reconciliation Statement For Period " + datefrom + " - " + dateto + " On 4131 (Revenue Suspense)");
			rc.setForeground(Color.WHITE);
			rc.setFont(new Font("", Font.BOLD, 16));
			JPanel toppanel = new JPanel(new FlowLayout());
			toppanel.setOpaque(false);
			toppanel.add(rc, SwingConstants.CENTER);
			panel.add(toppanel, BorderLayout.NORTH);
		} else

		{
			JOptionPane.showMessageDialog(comp, "NO DATA", "Information", JOptionPane.INFORMATION_MESSAGE);
		}

		return panel;

	}

	public class Worker extends SwingWorker<Void, Void> {

		public Worker() {

		}

		@Override
		protected Void doInBackground() throws Exception {
			prog.setIndeterminate(true);
			midpanel = midPanel();
			mainpanel.removeAll();
			mainpanel.setLayout(new BorderLayout());
			mainpanel.add(midpanel, BorderLayout.CENTER);
			mainpanel.revalidate();
			mainpanel.repaint();
			return null;
		}

		@Override
		public void done() {
			prog.setIndeterminate(false);
			panel.removeAll();
			panel.setLayout(new BorderLayout());
			panel.add(createLowerPanel(), BorderLayout.CENTER);
			panel.revalidate();
			panel.repaint();
			Timer timer = new Timer(1000, new Listener());
			timer.start();
		}

	}

	public JPanel progresspanel() {
		JPanel panel = new JPanel(new FlowLayout());
		prog = new JProgressBar();

		waitlbl = new JLabel("Processing....");

		Box box = Box.createHorizontalBox();
		box.add(waitlbl);
		box.add(prog);
		panel.add(box);
		return panel;
	}

	public JPanel createLowerPanel() {
		JPanel panel = new JPanel();
		panel.setBackground(Color.BLACK);
		timelbl = new JLabel();
		timelbl.setFont(new Font("Arial", Font.PLAIN, 20));
		timelbl.setForeground(Color.WHITE);

		panel.setBackground(Color.BLUE);
		// start the clock

		JLabel lbl = new JLabel();
		lbl.setFont(new Font("Arial", Font.PLAIN, 20));
		lbl.setForeground(Color.WHITE);
		panel.add(lbl);
		panel.add(Box.createHorizontalGlue());
		panel.add(timelbl);

		return panel;
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

	public void showDialog() {
		dialog = new JDialog((JFrame) null, "Search", true);
		dialog.setLayout(new BorderLayout());
		dialog.setIconImage(new IconImage().createIconImage());
		JLabel datelbl = new JLabel("From (date):");
		JLabel datelblt = new JLabel("To (date):");
		datechooser = new JDateChooser("yyyy/MM/dd", "####/##/##", '_');
		datechoosert = new JDateChooser("yyyy/MM/dd", "####/##/##", '_');
		JPanel datepanel = new JPanel(new GridLayout(2, 2));
		datepanel.setOpaque(false);
		datepanel.add(datelbl);
		datepanel.add(datechooser);
		datepanel.add(datelblt);
		datepanel.add(datechoosert);
		dialog.getContentPane().add(datepanel, BorderLayout.NORTH);
		JLabel top = new JLabel("");

		find = new JButton("Find");
		find.addActionListener((event) -> {
			Date fdate = datechooser.getDate();
			datefrom = String.format("%1$tY-%1$tm-%1$td", fdate);
			Date tdate = datechoosert.getDate();
			dateto = String.format("%1$tY-%1$tm-%1$td", tdate);
			insertTab();
			dialog.setVisible(false);

			Calendar cal = Calendar.getInstance();
			cal.setTime(fdate);
			cal.add(Calendar.DAY_OF_YEAR, -1);
			Date oneDayBefore = cal.getTime();

			prevday = String.format("%1$tY-%1$tm-%1$td", oneDayBefore);

		});
		JPanel midpanel = new JPanel(new GridLayout(3, 1));
		midpanel.setOpaque(false);
		midpanel.add(top);
		midpanel.add(find);

		error = new JLabel();
		error.setForeground(Color.red);
		midpanel.add(error);
		dialog.getContentPane().add(midpanel, BorderLayout.CENTER);
		// dialog.getContentPane().setBackground(new Color(0.5f, 0.5f, 1f));
		dialog.getContentPane().setBackground(Color.WHITE);
		dialog.getRootPane().setDefaultButton(find);
		dialog.setSize(400, 255);
		Dimension d = dialog.getSize(), screen = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screen.width - d.width) / 2, y = (screen.height - d.height) / 2;
		dialog.setLocation(x, y);
		dialog.setVisible(true);
		dialog.setAlwaysOnTop(true);

	}

}
