package tr.change.search;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import com.deco.TranslucentJPanel;

import helpers.DoubleForm;
import helpers.RemoveTab;
import helpers.TableColumnResizer;
import helpers.TableRenderer;
import helpers.TableRowResizer;

@SuppressWarnings("serial")
public class AllChangeList extends JPanel {
	ResultSet rs, rs1;
	Statement stm, stmt;
	DoubleForm df;
	private JTable table;
	JTabbedPane tabs;

	public AllChangeList(ResultSet rs, ResultSet rs1, Statement stm, Statement stmt, JTabbedPane tabs) {
		this.stm = stm;
		this.stmt = stmt;
		this.rs = rs;
		this.rs1 = rs1;
		df = new DoubleForm();
		this.tabs = tabs;
		this.setLayout(new BorderLayout());
		this.add(createTablepanel(), BorderLayout.CENTER);
	}

	public JPanel createTablepanel() {
		JPanel panel = new TranslucentJPanel(Color.BLUE);
		panel.setLayout(new BorderLayout());
		String rowsQuery = "SELECT receiptno,amount_changed,change_collected,date FROM receipt_amount WHERE change_collected =  '"
				+ 0 + "'";
		String extractQuery = "SELECT receiptno,amount_changed,change_collected,date FROM receipt_amount WHERE change_collected =  '"
				+ 0 + "'";
		try {
			rs1 = stmt.executeQuery(rowsQuery);
			rs1.last();
			int rows = rs1.getRow(), i = 0;
			Object[][] data = new Object[rows][Header.header.length];
			if (rows > 0) {
				rs = stm.executeQuery(extractQuery);
				while (rs.next()) {
					data[i][0] = rs.getInt(1);
					double amount_changed = rs.getDouble(2);
					double mount_collected = rs.getDouble(3);

					data[i][1] = amount_changed;
					data[i][2] = mount_collected;
					double outstanding = df.form(amount_changed - mount_collected);
					data[i][3] = outstanding;
					data[i][4] = rs.getString(4);
					i++;
				}

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
							e1.printStackTrace(System.err);
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
				TableRenderer.setJTableColumnsWidth(table, 480, 20, 20, 20, 20, 20);
				table.setRowHeight(30);
				table.setAutoCreateRowSorter(true);

				new TableColumnResizer(table);
				new TableRowResizer(table);
				table.setShowGrid(true);
				JScrollPane scroll = new JScrollPane();
				scroll.setViewportView(table);
				panel.add(scroll, BorderLayout.CENTER);
				JPanel lowerpanel = new JPanel(new BorderLayout());
				JButton generate = new JButton("<html><p>Download<br>PDF File</p></html>");
				generate.setForeground(Color.WHITE);
				generate.setBackground(Color.BLUE);
				generate.addActionListener((ActionEvent event) -> {
					AllChangePDF gnpdf = new AllChangePDF(table);
					AllChangePDF.Worker wk = gnpdf.new Worker();
					wk.execute();
				});

				panel.add(lowerpanel, BorderLayout.SOUTH);
				int rowcount = table.getRowCount();
				String labelString = (rowcount == 1) ? rowcount + " record found".toUpperCase()
						: rowcount + " records found".toUpperCase();

				JLabel l = new JLabel(labelString, SwingConstants.CENTER);
				l.setForeground(Color.WHITE);
				l.setFont(new Font("", Font.BOLD, 30));
				panel.add(l, BorderLayout.NORTH);
				JButton update = new JButton("<html><h1>CLEAR CHANGE</h1></html>");
				update.setBackground(new Color(10, 70, 90));
				update.setForeground(Color.WHITE);
				update.addActionListener((event) -> {
					// for (int x = 0; x < rowcount; x++) {
					String receipt = table.getValueAt(0, 0).toString();
					String initial_change = table.getValueAt(0, 1).toString();
					// String change_collected = table.getValueAt(0,
					// 2).toString();
					String outstanding = table.getValueAt(0, 3).toString();

					// }

					if (!receipt.equals("")) {
						String clearString = "UPDATE receipt_amount SET change_collected = '"
								+ Double.parseDouble(initial_change) + "' WHERE receiptno = '"
								+ Integer.parseInt(receipt) + "'";
						try {
							int con = JOptionPane.showConfirmDialog(null,
									"Confirm that the customer has been given $" + outstanding + " ?", "Confirm",
									JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
							if (con == JOptionPane.YES_OPTION) {
								stm.executeUpdate(clearString);
								new RemoveTab(tabs).removeTab("Change");
							}
						} catch (SQLException ee) {
							// ee.printStackTrace(System.err);
						}
					}
				});

				lowerpanel.add(update, BorderLayout.WEST);
				lowerpanel.add(generate, BorderLayout.EAST);

			} else {
				JOptionPane.showMessageDialog(null, "NO DATA", "NO DATA", JOptionPane.INFORMATION_MESSAGE);
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
				if (tabs.getTitleAt(a).trim().equals("Outstanding Change List")) {
					exist = true;
					tabs.setSelectedIndex(numberoftabs);
					break;
				}
			}
			if (!exist) {
				tabs.addTab("Outstanding Change List   ", null, this, "Outstanding Change List");
				tabs.setSelectedIndex(numberoftabs);
			}
		});
	}

}
