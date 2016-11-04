package buttons;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.Timer;

/**
 *
 * @author Wellington
 */
@SuppressWarnings("serial")
public class BlinkingButton extends JButton {
	// inhstance variable decleration here
	Color c;

	// constructor decleration here
	public BlinkingButton(Color c) {
		this.c = c;
		blinkButton(c);
	}

	// access specifier is hidden to to default so that the mmethod is only
	// locally used by the contructor
	void blinkButton(Color c) {
		Timer blinkTimer = new Timer(500, new ActionListener() {
			private int counter = 0;
			private int maxCount = 4;
			private boolean on = false;

			@Override
			public void actionPerformed(ActionEvent e) {
				if (counter >= maxCount) {
					BlinkingButton.this.setBackground(null);
					((Timer) e.getSource()).stop();
				} else {
					BlinkingButton.this.setBackground(on ? c : null);
					on = !on;
					counter++;
				}
			}
		});
		blinkTimer.start();
	}
}
