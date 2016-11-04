/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTabbedPane;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

/**
 *
 * @author Wellington
 */
@SuppressWarnings("unused")
public class Ribbon extends BasicTabbedPaneUI {

    Rectangle xRect;

    @Override
    protected void installListeners() {
        super.installListeners();
        tabPane.addMouseListener(new MyMouseHandler());
        
    }

    @Override
    protected void paintTab(Graphics g, int tabPlacement,
            Rectangle[] rects, int tabIndex,
            Rectangle iconRect, Rectangle textRect) {
        super.paintTab(g, tabPlacement, rects, tabIndex, iconRect, textRect);

        Font f = g.getFont();
        g.setFont(new Font("Courier", Font.BOLD, 10));
        g.setFont(f);
    }

    public class MyMouseHandler extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            JTabbedPane tabPane = (JTabbedPane) e.getSource();
        }

    }
}
