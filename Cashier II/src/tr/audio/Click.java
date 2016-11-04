package tr.audio;

import java.io.File;
import java.io.IOException;

import javax.media.CannotRealizeException;
import javax.media.Manager;
import javax.media.NoPlayerException;
import javax.media.Player;

/**
 *
 * @author Wellington
 */

public class Click {
	Player bplayer, mathplayer, charplayer, dialogplayer, closeappplayer, hoverplayer, tabplayer, okplayer;
	File bfile, mathfile, charfile, dialogfile, closeappfile, hoverfile, tabfile, okfile;

	public Click() {
		bfile = new File(
				System.getProperty("user.home") + File.separatorChar + "resources" + File.separatorChar + "0.wav");
		mathfile = new File(System.getProperty("user.home") + File.separatorChar + "resources" + File.separatorChar
				+ "mathbutton.wav");
		charfile = new File(System.getProperty("user.home") + File.separatorChar + "resources" + File.separatorChar
				+ "charbutton.wav");
		dialogfile = new File(
				System.getProperty("user.home") + File.separatorChar + "resources" + File.separatorChar + "dialog.wav");
		closeappfile = new File(System.getProperty("user.home") + File.separatorChar + "resources" + File.separatorChar
				+ "closeapp.wav");
		hoverfile = new File(
				System.getProperty("user.home") + File.separatorChar + "resources" + File.separatorChar + "hover.wav");
		tabfile = new File(
				System.getProperty("user.home") + File.separatorChar + "resources" + File.separatorChar + "hittab.wav");
		okfile = new File(System.getProperty("user.home") + File.separatorChar + "resources" + File.separatorChar
				+ "okbutton.wav");
	}

	public void buttonPlay() {
		try {
			bplayer = Manager.createRealizedPlayer(bfile.toURI().toURL());
			bplayer.start();
		} catch (IOException | NoPlayerException | CannotRealizeException e) {
			e.printStackTrace();
		}
	}

	public void buttonStop() {
		bplayer.stop();
	}

	public void mathOppPlay() {
		try {
			mathplayer = Manager.createRealizedPlayer(mathfile.toURI().toURL());
			mathplayer.start();
		} catch (IOException | NoPlayerException | CannotRealizeException e) {
			e.printStackTrace();
		}
	}

	public void mathOppStop() {
		mathplayer.stop();
	}

	public void charButtonPlay() {
		try {
			charplayer = Manager.createRealizedPlayer(charfile.toURI().toURL());
			charplayer.start();
		} catch (IOException | NoPlayerException | CannotRealizeException e) {
			e.printStackTrace();
		}
	}

	public void charButtonStop() {
		charplayer.stop();
	}

	public void dialogPlayer() {
		try {
			dialogplayer = Manager.createRealizedPlayer(dialogfile.toURI().toURL());
			dialogplayer.start();
		} catch (IOException | NoPlayerException | CannotRealizeException e) {
			e.printStackTrace();
		}
	}

	public void closeappPlay() {
		try {
			closeappplayer = Manager.createRealizedPlayer(closeappfile.toURI().toURL());
			closeappplayer.start();
		} catch (IOException | NoPlayerException | CannotRealizeException e) {
			e.printStackTrace();
		}

	}

	public void hoverPlay() {
		try {
			hoverplayer = Manager.createRealizedPlayer(hoverfile.toURI().toURL());
			hoverplayer.start();
		} catch (IOException | CannotRealizeException | NoPlayerException e) {
			e.printStackTrace();
		}
	}

	public void hoverStop() {
		hoverplayer.stop();
	}

	public void tabPlay() {
		try {
			tabplayer = Manager.createRealizedPlayer(tabfile.toURI().toURL());
			tabplayer.start();
		} catch (IOException | NoPlayerException | CannotRealizeException e) {
			e.printStackTrace();
		}
	}

	public void tabStop() {
		// tabplayer.stop();
	}

	public void okPlay() {
		try {
			okplayer = Manager.createRealizedPlayer(okfile.toURI().toURL());
			okplayer.start();
		} catch (IOException | NoPlayerException | CannotRealizeException e) {
			e.printStackTrace();
		}
	}

	public void okStop() {
		okplayer.stop();
	}
}
