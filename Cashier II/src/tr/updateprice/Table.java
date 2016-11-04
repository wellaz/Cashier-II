package tr.updateprice;

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
import helpers.SetDateCreated;
import helpers.TableColumnResizer;
import helpers.TablePopupEditor;
import helpers.TableRenderer;
import helpers.TableRowResizer;

@SuppressWarnings("serial")
public class Table extends JPanel {
	ResultSet rs, rs1;
	Statement stm, stmt;
	DoubleForm df;
	private JTable table;
	JTabbedPane tabs;

	public Table(ResultSet rs, ResultSet rs1, Statement stm, Statement stmt, JTabbedPane tabs) {
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
		String rowsQuery = "SELECT product_name,unit_price,date FROM commonproducts ";
		String extractQuery = "SELECT product_name,unit_price,date FROM commonproducts ";
		try {
			rs1 = stmt.executeQuery(rowsQuery);
			rs1.last();
			int rows = rs1.getRow(), i = 0;
			if (rows > 0) {
				rs = stm.executeQuery(extractQuery);
				Object[][] data = new Object[rows][Header.header.length];
				while (rs.next()) {
					data[i][0] = rs.getString(1);
					double amount = rs.getDouble(2);
					data[i][1] = amount;
					data[i][2] = rs.getString(3);
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
							// catch null pointer exception if mouse is over an
							// empty line
						}

						return tip;
					}

					@Override
					public boolean isCellEditable(int rowIndex, int colIndex) {
						switch (colIndex) {
						case 0:
						case 2:
							return false;
						default:
							return true;
						}
					}
				};
				TableRenderer.setJTableColumnsWidth(table, 480, 60, 20, 20);
				TablePopupEditor popupEditor = new TablePopupEditor();
				table.getColumnModel().getColumn(1).setCellEditor(popupEditor);
				table.setRowHeight(30);
				table.setAutoCreateRowSorter(true);

				new TableColumnResizer(table);
				new TableRowResizer(table);
				table.setShowGrid(true);
				JScrollPane scroll = new JScrollPane();
				scroll.setViewportView(table);
				panel.add(scroll, BorderLayout.CENTER);
				JPanel lowerpanel = new TranslucentJPanel(Color.BLUE);
				lowerpanel.setLayout(new BorderLayout());
				lowerpanel.add(new JLabel());
				JButton generate = new JButton("<html><p>Download<br>PDF File</p></html>");
				generate.setBackground(Color.BLUE);
				generate.setForeground(Color.WHITE);

				generate.addActionListener((ActionEvent event) -> {
					PriceListPDF gnpdf = new PriceListPDF(table);
					PriceListPDF.Worker wk = gnpdf.new Worker();
					wk.execute();
				});

				panel.add(lowerpanel, BorderLayout.SOUTH);

				JLabel l = new JLabel(table.getRowCount() + " records found.".toUpperCase(), SwingConstants.CENTER);
				l.setForeground(Color.WHITE);
				l.setFont(new Font("", Font.BOLD, 30));
				panel.add(l, BorderLayout.NORTH);
				JButton updatebutton = new JButton("<html><h1>BATCH UPDATE</h1></html>");
				updatebutton.setBackground(new Color(10, 70, 90));
				updatebutton.setForeground(Color.WHITE);
				lowerpanel.add(updatebutton, BorderLayout.WEST);
				lowerpanel.add(generate, BorderLayout.EAST);
				updatebutton.addActionListener((event) -> {
					String date = new SetDateCreated().getDate();
					String time = new SetDateCreated().getTime();
					int value = table.getRowCount();
					Update u = new Update(rs, rs1, stm, stmt);
					for (int x = 0; x < value; x++) {
						String product_name = table.getValueAt(x, 0).toString();
						String cost = table.getValueAt(x, 1).toString();
						double price = Double.parseDouble(cost);
						u.update(product_name, df.form(price), date, time);
					}
					JOptionPane.showMessageDialog(null, "Prices Update completed!", "Success",
							JOptionPane.INFORMATION_MESSAGE);
					new RemoveTab(tabs).removeTab("Update Prices");
					// new RemoveTab(tabs).removeTab("New Transaction");
				});
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
				if (tabs.getTitleAt(a).trim().equals("Update Prices")) {
					exist = true;
					tabs.setSelectedIndex(numberoftabs);
					break;
				}
			}
			if (!exist) {
				tabs.addTab("Update Prices   ", null, this, "Update Prices");
				tabs.setSelectedIndex(numberoftabs);
			}
		});
	}

}
