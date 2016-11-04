package supervisor;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import helpers.IconImage;

@SuppressWarnings("serial")
public class Banner extends JPanel {

	public Banner() {
		this.setLayout(new BorderLayout());
		JLabel lbl = new JLabel(new ImageIcon(new IconImage().createBannerImage()), JLabel.CENTER);
		this.add(lbl, BorderLayout.CENTER);
	}
}
