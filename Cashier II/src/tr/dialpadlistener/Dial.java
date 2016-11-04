package tr.dialpadlistener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

/**
 *
 * @author Wellington
 */
public class Dial implements ActionListener {
	JTextField rendered;
	JButton button;

	public Dial(JTextField rendered, JButton button) {
		this.rendered = rendered;
		this.button = button;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		String text = button.getName();
		button.setText(text);
		SimpleAttributeSet set2 = new SimpleAttributeSet(); 
		StyleConstants.setBold(set2, true);
	}

}
