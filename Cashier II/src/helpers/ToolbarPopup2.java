/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;

/**
 *
 * @author Wellington
 */
@SuppressWarnings("serial")
public class ToolbarPopup2 extends JPopupMenu implements ActionListener {

	JCheckBoxMenuItem showmenu, showtoolbar, showstartpanel, smallicons;
	JMenuItem resettoolbar, customise, preferences;
	JFrame frame;
	JToolBar toolbar;
	JMenuBar mnb;
	JTabbedPane tabs;

	public ToolbarPopup2(JFrame frame, JToolBar toolbar, JMenuBar mnb, JTabbedPane tabs) {
		this.frame = frame;
		this.toolbar = toolbar;
		this.mnb = mnb;
		this.tabs = tabs;
		init();
	}

	public final void init() {
		frame.setJMenuBar(mnb);
		showmenu = new JCheckBoxMenuItem("Hide Menu");
		showtoolbar = new JCheckBoxMenuItem("Hide Tool Bar");
		showstartpanel = new JCheckBoxMenuItem("Hide Start Panel");
		smallicons = new JCheckBoxMenuItem("Small Tool Bar Icons ");
		showmenu.addActionListener(this);
		showtoolbar.addActionListener(this);
		showstartpanel.addActionListener(this);
		smallicons.addActionListener(this);

		preferences = new JMenuItem("Preferences");
		preferences.setEnabled(false);
		customise = new JMenuItem("Customise...");
		customise.setEnabled(false);
		resettoolbar = new JMenuItem("Reset Tool Bar");
		resettoolbar.setEnabled(false);
		preferences.addActionListener(this);
		customise.addActionListener(this);
		resettoolbar.addActionListener(this);
		this.add(showmenu);
		this.add(showtoolbar);
		this.addSeparator();
		this.add(smallicons);
		this.add(showstartpanel);
		this.addSeparator();
		this.add(resettoolbar);
		this.add(customise);
		this.addSeparator();
		this.add(preferences);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == showtoolbar) {
			if (showtoolbar.getState()) {
				removeAllTools();
			} else {
				repaintTools();
			}
		}

		if (e.getSource() == showmenu) {
			if (showmenu.getState()) {
				EventQueue.invokeLater(() -> {
					frame.setJMenuBar(null);
					frame.revalidate();
					frame.repaint();
				});
			} else {
				EventQueue.invokeLater(() -> {
					frame.setJMenuBar(mnb);
					frame.revalidate();
					frame.repaint();
				});
			}
		}
		if (e.getSource() == resettoolbar) {
		}
	}

	private void removeAllTools() {
		EventQueue.invokeLater(() -> {
			toolbar.removeAll();
			JButton b = new JButton();
			b.addActionListener((e) -> {
				repaintTools();
				showtoolbar.setSelected(false);
			});
			b.setIcon(new ImageIcon(new IconImage().createToggleImage()));
			toolbar.add(b);
			toolbar.revalidate();
			toolbar.repaint();
		});
	}

	private void repaintTools() {
		EventQueue.invokeLater(() -> {
			toolbar.removeAll();
			toolbar.add(tabs);
			toolbar.add(Box.createHorizontalGlue());
			toolbar.revalidate();
			toolbar.repaint();
		});
	}
}
