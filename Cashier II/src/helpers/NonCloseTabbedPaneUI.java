/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package helpers;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.plaf.basic.BasicTabbedPaneUI;

/**
 *
 * @author Wellington
 *
 */

public class NonCloseTabbedPaneUI extends BasicTabbedPaneUI {

	@Override
	protected void installListeners() {
		super.installListeners();
	}

	@Override
	protected void paintTab(Graphics g, int tabPlacement, Rectangle[] rects, int tabIndex, Rectangle iconRect,
			Rectangle textRect) {
		super.paintTab(g, tabPlacement, rects, tabIndex, iconRect, textRect);

		g.setFont(new Font("Courier", Font.BOLD, 10));
		g.setColor(new Color(0.5f, 0.5f, 1f));
	}

}
