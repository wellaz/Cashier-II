package tr.dailysales;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import com.deco.TranslucentJPanel;

import helpers.TableColumnResizer;
import helpers.TableRenderer;
import helpers.TableRowResizer;

/**
 *
 * @author Wellington
 */
@SuppressWarnings("serial")
public class DailySales extends JPanel implements ActionListener {
	ResultSet rs, rs1;
	Statement stm, stmt;
	double sum = 0;
	private JTable table;
	JTabbedPane tabs;

	public DailySales(ResultSet rs, ResultSet rs1, Statement stm, Statement stmt, JTabbedPane tabs) {
		this.stm = stm;
		this.stmt = stmt;
		this.rs = rs;
		this.rs1 = rs1;
		this.tabs = tabs;
		this.setLayout(new BorderLayout());
		this.add(createTablepanel(), BorderLayout.CENTER);
	}

	public JPanel createTablepanel() {
		JPanel panel = new TranslucentJPanel(Color.BLUE);
		panel.setLayout(new BorderLayout());
		String rowsQuery = "SELECT product_name,SUM(quantity),SUM(amount) FROM cashpay WHERE date = CURDATE() GROUP BY product_name";
		String extractQuery = "SELECT product_name,SUM(quantity),SUM(amount) FROM cashpay WHERE date = CURDATE() GROUP BY product_name";
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
					DailySalesPDF gnpdf = new DailySalesPDF(table, totals);
					DailySalesPDF.Worker wk = gnpdf.new Worker();
					wk.execute();
				});

				panel.add(lowerpanel, BorderLayout.SOUTH);

				JLabel l = new JLabel("Today's Sales $" + totals + ".  (" + table.getRowCount() + ") Records Found.",
						SwingConstants.CENTER);
				l.setForeground(Color.WHITE);
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

	public void insertTab() {
		EventQueue.invokeLater(() -> {
			int numberoftabs = tabs.getTabCount();
			boolean exist = false;
			for (int a = 0; a < numberoftabs; a++) {
				if (tabs.getTitleAt(a).trim().equals("Today's Sales")) {
					exist = true;
					tabs.setSelectedIndex(numberoftabs);
					break;
				}
			}
			if (!exist) {
				tabs.addTab("Today's Sales   ", null, this, "Today's Sales Report");
				tabs.setSelectedIndex(numberoftabs);
			}
		});
	}

	public double getTotal() {
		return sum;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

	}

}
