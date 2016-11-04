/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tr.receipt.reprint;

import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;

import helpers.RemoveTab;

/**
 *
 * @author Wellington
 */
@SuppressWarnings("serial")
public class ListPopup extends JPopupMenu {

	public JMenuItem open, deletep, remove, print, properties;
	public JMenuItem edittest, compilemarks;
	public JMenu sharewith, sendto;
	ResultSet rs, rs1;
	Statement stm, stmt;
	JTabbedPane tabs;
	JFrame frame;

	public ListPopup(ResultSet rs, ResultSet rs1, Statement stm, Statement stmt, JTabbedPane tabs, JFrame frame) {
		this.rs = rs;
		this.rs1 = rs1;
		this.stm = stm;
		this.stmt = stmt;
		this.tabs = tabs;
		this.frame = frame;
		init();
	}

	public final void init() {
		open = new JMenuItem("Open");
		open.addActionListener((event) -> {

		});
		deletep = new JMenuItem("Delete Permanantly");
		deletep.setEnabled(false);
		remove = new JMenuItem("Remove from List");
		remove.setEnabled(false);
		print = new JMenuItem("Print...");
		properties = new JMenuItem("Poperties");
		properties.setEnabled(false);
		edittest = new JMenuItem("Edit");
		edittest.setEnabled(false);
		compilemarks = new JMenuItem("Compile Marks");
		compilemarks.setEnabled(false);

		sharewith = new JMenu("Share with...");
		sendto = new JMenu("Send to...");
		this.add(open);
		this.addSeparator();
		this.add(edittest);
		this.add(compilemarks);
		this.addSeparator();
		this.add(sharewith);
		this.add(sendto);
		this.addSeparator();
		this.add(deletep);
		this.add(remove);
		this.addSeparator();
		this.add(print);
		this.add(properties);

		print.addActionListener((e) -> {
			new RemoveTab(tabs).removeTab("Reprints");
			ReprintRe re = new ReprintRe(rs, rs1, stm, stmt, tabs, frame);
			re.insertTab();
		});
	}
}
