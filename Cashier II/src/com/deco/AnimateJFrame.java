package com.deco;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.Timer;

/**
 *
 * @author Wellington
 */

public class AnimateJFrame {
	public AnimateJFrame() {

	}

	public void fadeIn(final JFrame frame, int delay) {
		new Timer(delay, new ActionListener() {
			int counter = 0;

			public void actionPerformed(ActionEvent e) {
				counter++;
				if (counter == 10) {
					((Timer) e.getSource()).stop();
				}
				frame.setOpacity(counter * 0.1f);
			}
		}).start();
		frame.setVisible(true);
	}

	public void fadeOut(final JFrame frame, int delay) {
		new Timer(delay, new ActionListener() {
			int counter = 10;

			public void actionPerformed(ActionEvent e) {
				counter--;
				if (counter == 0) {
					((Timer) e.getSource()).stop();
					frame.dispose();
				}
				frame.setOpacity(counter * 0.1f);
			}
		}).start();
	}

}
