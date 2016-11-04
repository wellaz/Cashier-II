package tr.audio;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JCheckBoxMenuItem;

/**
 *
 * @author Wellington
 */
public class ButtonListener extends MouseAdapter {

	Click click;
	JCheckBoxMenuItem tuner;

	public ButtonListener(JCheckBoxMenuItem tuner) {
		this.tuner = tuner;
		click = new Click();
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		if (tuner.getState()) {
			click.buttonPlay();
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		if (tuner.getState()) {
			// click.buttonStop();
		}
	}

}
