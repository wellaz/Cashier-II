package tr.salesreports;

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
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import com.toedter.calendar.JDateChooser;

import buttons.BlinkingLabel;
import helpers.CountRecords;
import helpers.IconImage;
import helpers.TableColumnResizer;
import helpers.TableRenderer;
import helpers.TableRowResizer;

@SuppressWarnings("serial")
public class SalesReport extends JPanel {
	private JDialog dialog;
	private JLabel error;
	private JDateChooser datechooser;
	Statement stm, stmt;
	ResultSet rs, rs1;
	JFrame comp;
	JTabbedPane tabs;
	double sum = 0;
	private JTable table;
	private JDateChooser datechoosert;
	private JButton cashieridbutton;

	public SalesReport(JTabbedPane tabs, Statement stm, ResultSet rs, ResultSet rs1, Statement stmt, JFrame comp) {
		this.rs = rs;
		this.stm = stm;
		this.stmt = stmt;
		this.rs1 = rs1;
		this.comp = comp;
		this.tabs = tabs;
		this.setLayout(new BorderLayout());
	}

	public void showDialog() {
		dialog = new JDialog((JFrame) null, "Sales Report Generator", true);
		dialog.setLayout(new BorderLayout());
		dialog.setIconImage(new IconImage().createIconImage());
		JLabel datelbl = new JLabel("From (Date):");
		JLabel datelblto = new JLabel("To (Date):");
		datechooser = new JDateChooser("yyyy/MM/dd", "####/##/##", '_');
		datechooser.setDate(new Date());
		datechoosert = new JDateChooser("yyyy/MM/dd", "####/##/##", '_');
		datechoosert.setDate(new Date());
		JPanel datepanel = new JPanel(new GridLayout(2, 2, 0, 10));
		datepanel.setOpaque(false);
		datepanel.add(datelbl);
		datepanel.add(datechooser);
		datepanel.add(datelblto);
		datepanel.add(datechoosert);
		// dialog.getContentPane().add(datepanel, BorderLayout.NORTH);
		JLabel top = new BlinkingLabel("Select Period");
		top.setFont(new Font("", Font.PLAIN, 20));
		// top.setForeground(Color.WHITE);

		JPanel midpanel = new JPanel(new GridLayout(3, 1));
		midpanel.setOpaque(false);
		midpanel.add(top);
		midpanel.add(datepanel);
		midpanel.add(new JLabel());

		error = new JLabel();
		error.setForeground(Color.red);
		// midpanel.add(error);
		cashieridbutton = new JButton("OK");
		cashieridbutton.addActionListener((e) -> {
			Date fdate = datechooser.getDate();
			Date todate = datechoosert.getDate();
			String dateString = String.format("%1$tY-%1$tm-%1$td", fdate);
			String dateStringto = String.format("%1$tY-%1$tm-%1$td", todate);
			this.add(createTablepanel(dateString, dateStringto), BorderLayout.CENTER);
			insertTab();
			dialog.dispose();

		});
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

		dialog.setSize(400, 300);
		Dimension d = dialog.getSize(), screen = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screen.width - d.width) / 2, y = (screen.height - d.height) / 2;
		dialog.setLocation(x, y);
		dialog.setVisible(true);
		dialog.setAlwaysOnTop(true);
	}

	public JPanel createTablepanel(String dateString, String dateStringto) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(Color.WHITE);
		String rowsQuery = "SELECT product_name,SUM(quantity),SUM(amount) FROM cashpay WHERE date >= '" + dateString
				+ "' AND date <='" + dateStringto + "'  GROUP BY product_name";
		String extractQuery = "SELECT product_name,SUM(quantity),SUM(amount) FROM cashpay WHERE date >= '" + dateString
				+ "' AND date <='" + dateStringto + "'  GROUP BY product_name";
		try {
			rs1 = stmt.executeQuery(rowsQuery);
			rs1.last();
			int rows = rs1.getRow(), i = 0;
			if (rows > 0) {
				rs = stm.executeQuery(extractQuery);
				Object[][] data = new Object[rows][Header.header.length];
				while (rs.next()) {
					data[i][0] = rs.getString(1);
					data[i][1] = rs.getInt(2);
					double amount = rs.getDouble(3);
					data[i][2] = amount;
					sum += amount;
					i++;
				}
				DefaultTableModel model = new DefaultTableModel(data, Header.header);
				table = new JTable(model) {
					@Override
					public Component prepareRenderer(TableCellRenderer renderer, int Index_row, int Index_col) {
						Component comp = super.prepareRenderer(renderer, Index_row, Index_col);
						// even index, selected or not selected
						comp.setFont(new java.awt.Font("", Font.PLAIN, 18));
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
				TableRenderer.setJTableColumnsWidth(table, 480, 60, 20, 20);
				table.setRowHeight(30);
				table.setAutoCreateRowSorter(true);

				new TableColumnResizer(table);
				new TableRowResizer(table);
				table.setShowGrid(true);
				JScrollPane scroll = new JScrollPane();
				scroll.setViewportView(table);
				panel.add(scroll, BorderLayout.CENTER);
				JPanel lowerpanel = new JPanel(new FlowLayout());
				lowerpanel.add(new JLabel());
				JButton generate = new JButton("<html><p>Download<br>PDF File</p></html>");
				generate.setBackground(new Color(10, 70, 90));
				generate.setForeground(Color.WHITE);
				lowerpanel.add(generate);
				double totals = getTotal();
				generate.addActionListener((ActionEvent event) -> {
					SalesPDF gnpdf = new SalesPDF(table, totals);
					SalesPDF.Worker wk = gnpdf.new Worker();
					wk.execute();
				});

				panel.add(lowerpanel, BorderLayout.SOUTH);

				JLabel l = new JLabel("Period " + dateString + " - " + dateStringto + " Sales $" + totals + ".  "
						+ CountRecords.returnRecords(table.getRowCount()), SwingConstants.CENTER);
				l.setFont(new Font("", Font.BOLD, 30));
				panel.add(l, BorderLayout.NORTH);
			} else {
				panel.setLayout(new GridBagLayout());
				JLabel l = new JLabel("<html><h1>NO DATA</h1>");
				l.setForeground(Color.WHITE);
				panel.add(l);
			}
		} catch (SQLException ee) {
			ee.printStackTrace(System.err);
		}
		return panel;
	}

	public double getTotal() {
		return sum;
	}

	public void insertTab() {
		EventQueue.invokeLater(() -> {
			int numberoftabs = tabs.getTabCount();
			boolean exist = false;
			for (int a = 0; a < numberoftabs; a++) {
				if (tabs.getTitleAt(a).trim().equals("Sales Report")) {
					exist = true;
					tabs.setSelectedIndex(a);
					break;
				}
			}
			if (!exist) {

				// this.add(createTablepanel(), BorderLayout.CENTER);
				tabs.addTab("Sales Report   ", null, this, "Sales Report");
				tabs.setSelectedIndex(numberoftabs);
			}
		});
	}
}
