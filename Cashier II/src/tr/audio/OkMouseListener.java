package tr.audio;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JCheckBoxMenuItem;

public class OkMouseListener extends MouseAdapter {
	Click click;
	JCheckBoxMenuItem tuner;

	public OkMouseListener(JCheckBoxMenuItem tuner) {
		this.tuner = tuner;
		click = new Click();
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		if (tuner.getState()) {
			click.okPlay();
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		if (tuner.getState()) {
			//click.okStop();
		}
	}
}
