/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tr.receipt.reprint;

import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

/**
 *
 * @author Wellington
 */
public class ListPopupTrigger extends MouseAdapter {

	JPopupMenu popup;
	ResultSet rs, rs1;
	Statement stm, stmt;
	JTabbedPane tabs;
	JFrame frame;

	public ListPopupTrigger(JPopupMenu popup, ResultSet rs, ResultSet rs1, Statement stm, Statement stmt,
			JTabbedPane tabs, JFrame frame) {
		this.popup = popup;
		this.rs = rs;
		this.rs1 = rs1;
		this.stm = stm;
		this.stmt = stmt;
		this.tabs = tabs;
		this.frame = frame;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void mouseClicked(MouseEvent e) {
		JList source = (JList) e.getSource();
		if (SwingUtilities.isRightMouseButton(e) == true) {
			if (!source.isSelectionEmpty() && source.locationToIndex(e.getPoint()) == source.getSelectedIndex()) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		}
		if (e.getClickCount() == 2) {
			int index = source.locationToIndex(e.getPoint());
			EventQueue.invokeLater(() -> {
				int numberoftabs = tabs.getTabCount();
				boolean exist = false;
				for (int a = 0; a < numberoftabs; a++) {
					if (tabs.getTitleAt(a).trim().equals("Reprints")) {
						exist = true;
						tabs.setSelectedIndex(numberoftabs);
						break;
					}
				}
				if (!exist) {
					if (index > 0) {
						new ReprintRe(rs, rs1, stm, stmt, tabs, frame).insertTab();
					}
				}
			});
		}
	}
}
