package com.deco;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

/**
 *
 * @author Wellington
 */

public class ContainerBorders {

	public static Border createBevelBorder() {
		return BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.GRAY, Color.WHITE, Color.DARK_GRAY,
				Color.DARK_GRAY);
	}

}
