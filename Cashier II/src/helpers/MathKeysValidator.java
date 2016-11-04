package helpers;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 *
 * @author Wellington
 */
public class MathKeysValidator extends KeyAdapter {

	@Override
	public void keyTyped(KeyEvent e) {
		char character = e.getKeyChar();
		if (!(Character.isDigit(character) || (character == KeyEvent.VK_BACK_SPACE) || (character == KeyEvent.VK_DELETE)
				|| (character == KeyEvent.VK_ENTER) || (character == KeyEvent.VK_PERIOD)
				|| (character == KeyEvent.VK_ADD) || (character == KeyEvent.VK_ASTERISK)
				|| (character == KeyEvent.VK_BRACELEFT) || (character == KeyEvent.VK_BRACERIGHT)
				|| (character == KeyEvent.VK_PLUS) || (character == KeyEvent.VK_MINUS)
				|| (character == KeyEvent.VK_MULTIPLY) || (character == KeyEvent.VK_DIVIDE)||(character == KeyEvent.VK_SHIFT))) {
			java.awt.Toolkit.getDefaultToolkit().beep();
			e.consume();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

}
