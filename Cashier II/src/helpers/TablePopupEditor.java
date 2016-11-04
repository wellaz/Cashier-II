/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author Wellington
 */

@SuppressWarnings("serial")
public class TablePopupEditor extends DefaultCellEditor implements TableCellEditor {
	private final PopupDialog popup;
	private String currentText = "";
	private final JButton editorBtn;

	public TablePopupEditor() {
		super(new JTextField());
		setClickCountToStart(1);
		editorBtn = new JButton();
		editorBtn.setBackground(Color.white);
		editorBtn.setBorderPainted(false);
		editorBtn.setContentAreaFilled(false);
		editorBtn.setFocusable(false);
		popup = new PopupDialog();
	}

	@Override
	public Object getCellEditorValue() {
		return currentText;
	}

	// @SuppressWarnings("deprecation")
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		SwingUtilities.invokeLater(() -> {
			popup.setText(currentText);
			Point p = editorBtn.getLocationOnScreen();
			popup.setLocation(p.x, p.y + editorBtn.getSize().height);
			// popup.show();
			popup.setVisible(true);
			fireEditingStopped();
		});

		currentText = value.toString();
		editorBtn.setText(currentText);
		return editorBtn;
	}

	class PopupDialog extends JDialog implements ActionListener {
		private final JTextArea textArea;

		public PopupDialog() {
			super((JFrame) null, "Text", true);
			setIconImage(new IconImage().createIconImage());
			textArea = new JTextArea(5, 20);
			textArea.setLineWrap(true);
			textArea.setWrapStyleWord(true);
			textArea.setBackground(Color.BLACK);
			textArea.setForeground(Color.WHITE);
			textArea.setFont(new Font("", Font.BOLD, 15));
			KeyStroke keyStroke = KeyStroke.getKeyStroke("ENTER");
			textArea.getInputMap().put(keyStroke, "none");
			JScrollPane scrollPane = new JScrollPane(textArea);
			getContentPane().add(scrollPane);

			JButton cancel = new JButton("Cancel");
			cancel.addActionListener(this);
			JButton ok = new JButton("Ok");
			ok.setPreferredSize(cancel.getPreferredSize());
			ok.addActionListener(this);

			JPanel buttons = new JPanel();
			buttons.add(ok);
			buttons.add(cancel);
			getContentPane().add(buttons, BorderLayout.SOUTH);
			pack();
			setAlwaysOnTop(true);
			getRootPane().setDefaultButton(ok);
		}

		public void setText(String text) {
			textArea.setText(text);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if ("Ok".equals(e.getActionCommand())) {
				currentText = textArea.getText();
			}
			textArea.requestFocusInWindow();
			setVisible(false);
		}
	}
}
