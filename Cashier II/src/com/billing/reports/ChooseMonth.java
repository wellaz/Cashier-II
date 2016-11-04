package com.billing.reports;

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
import helpers.DoubleForm;
import helpers.IconImage;
import helpers.TableColumnResizer;
import helpers.TableRenderer;
import helpers.TableRowResizer;

/**
 *
 * @author Wellington
 */

@SuppressWarnings("serial")
public class ChooseMonth extends JPanel {
	private JDateChooser datechooser;
	private JDateChooser datechoosert;
	private JDialog dialog;
	private JLabel error;
	private JButton cashieridbutton;

	ResultSet rs, rs1;
	Statement stm, stmt;
	double sum = 0;
	private JTable table;
	JTabbedPane tabs;
	int rows = 0;
	DoubleForm df;

	public ChooseMonth(ResultSet rs, ResultSet rs1, Statement stm, Statement stmt, JTabbedPane tabs) {
		this.stm = stm;
		this.stmt = stmt;
		this.rs = rs;
		this.rs1 = rs1;
		this.tabs = tabs;
		this.setLayout(new BorderLayout());
		df = new DoubleForm();

	}

	public void showDialog() {
		dialog = new JDialog((JFrame) null, "Bill Report Generator", true);
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
			this.add(validateDates(dateString, dateStringto), BorderLayout.CENTER);
			insertTab();
			dialog.dispose();
		});
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

	public JPanel validateDates(String dateString, String dateStringto) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(Color.WHITE);

		String text = "SELECT * FROM daily_bill WHERE date >= '" + dateString + "' AND date <='" + dateStringto
				+ "' ORDER BY date,time ASC";
		String query = "SELECT * FROM daily_bill WHERE date >= '" + dateString + "' AND date <='" + dateStringto
				+ "' ORDER BY date,time ASC";
		try {
			rs = stm.executeQuery(text);
			rs.last();
			rows = rs.getRow();
			int i = 0;
			if (rows > 0) {
				Object[][] data = new Object[rows][Header.header.length];
				rs1 = stmt.executeQuery(query);
				while (rs1.next()) {
					String date = rs1.getString(1);
					String time = rs1.getString(2);
					double amount = rs1.getDouble(3);
					String month = rs1.getString(4);

					data[i][0] = date;
					data[i][1] = time;
					data[i][2] = amount;
					data[i][3] = month;
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
				TableRenderer.setJTableColumnsWidth(table, 480, 25, 25, 25, 25);
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
					BillPDF gnpdf = new BillPDF(table, totals, getRows());
					BillPDF.Worker wk = gnpdf.new Worker();
					wk.execute();
				});

				panel.add(lowerpanel, BorderLayout.SOUTH);

				JLabel l = new JLabel("Total bill $" + totals + ".  " + CountRecords.returnRecords(getRows()),
						SwingConstants.CENTER);
				// l.setForeground(Color.WHITE);
				l.setFont(new Font("", Font.BOLD, 30));
				panel.add(l, BorderLayout.NORTH);

			} else {
				panel.setLayout(new GridBagLayout());
				JLabel l = new JLabel("<html><h1>NO DATA</h1>");
				l.setForeground(Color.WHITE);
				panel.add(l);
			}

		} catch (SQLException ee) {
			ee.printStackTrace();
		}
		return panel;
	}

	public double getTotal() {
		return df.form(sum);
	}

	public int getRows() {
		return rows;
	}

	public void insertTab() {
		EventQueue.invokeLater(() -> {
			int numberoftabs = tabs.getTabCount();
			boolean exist = false;
			for (int a = 0; a < numberoftabs; a++) {
				if (tabs.getTitleAt(a).trim().equals("Billing report")) {
					exist = true;
					tabs.setSelectedIndex(numberoftabs);
					break;
				}
			}
			if (!exist) {
				tabs.addTab("Billing report   ", null, this, "Billing report");
				tabs.setSelectedIndex(numberoftabs);
			}
		});
	}
}
