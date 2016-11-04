/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.TitledBorder;

/**
 *
 * @author Wellington
 */
public class TitledBorderFont {

	public TitledBorderFont() {
		super();
	}

	public TitledBorder setTitledBorder(String string) {
		UIManager.getDefaults().put("TitledBorder.titleColor", Color.GRAY);
		Border lowerEtched = BorderFactory.createSoftBevelBorder(SoftBevelBorder.RAISED);
		TitledBorder title = BorderFactory.createTitledBorder(lowerEtched, string);
		title.setTitleJustification(TitledBorder.BOTTOM);
		Font titleFont = UIManager.getFont("TitledBorder.font");
		title.setTitleFont(titleFont.deriveFont(Font.ITALIC + Font.BOLD));
		return title;
	}

}
