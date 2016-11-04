package tr.audio;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JCheckBoxMenuItem;

/**
 *
 * @author Wellington
 */
public class HoverListener implements MouseListener {

	Click click;
	JCheckBoxMenuItem tuner;

	public HoverListener(JCheckBoxMenuItem tuner) {
		this.tuner = tuner;
		click = new Click();
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		if (tuner.getState()) {
			click.tabPlay();
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		if (tuner.getState()) {
			click.hoverPlay();
		}
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		if (tuner.getState()) {
			click.hoverStop();
		}
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		if (tuner.getState()) {
			click.tabStop();
		}
	}

}
