/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package helpers;

import javax.swing.JSplitPane;

/**
*
* @author Wellington
*/

public class SplitPane{
	public JSplitPane split;

	public SplitPane() {
		split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		split.setDividerSize(3);
		split.setOneTouchExpandable(true);
		split.setDividerLocation(200);
	}

}
