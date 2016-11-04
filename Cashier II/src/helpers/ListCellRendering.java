/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 *
 * @author Wellington
 */
@SuppressWarnings({ "serial", "rawtypes" })
public class ListCellRendering extends JLabel implements ListCellRenderer {

    public ListCellRendering() {
        setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        setText(value.toString());
        if (index % 2 == 0) {
            setBackground(new Color(235, 235, 235));
        } else {
            setBackground(new Color(204, 204, 204));
        }
        if (list.isSelectedIndex(index)) {
            setBackground(new Color(0.5f, 0.5f, 1f));
        }
        return this;
    }
}
