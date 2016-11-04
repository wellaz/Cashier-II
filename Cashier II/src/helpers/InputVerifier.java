package helpers;

import javax.swing.JComponent;

/**
 *
 * @author Wellington
 */
public abstract class InputVerifier {
	public abstract boolean verify(JComponent input);

	public boolean shouldYieldFocus(JComponent input) {
		return verify(input);
	}
}