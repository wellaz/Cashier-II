package tr.printer;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.swing.JTextArea;

/**
 *
 * @author Wellington
 */
public class Printer implements Printable {

	JTextArea display;

	public Printer(JTextArea f) {
		display = f;
	}

	@Override
	public int print(Graphics g, PageFormat pf, int page) throws PrinterException {

		if (page > 0) {
			return NO_SUCH_PAGE;
		}

		Graphics2D g2d = (Graphics2D) g;
		g2d.translate(pf.getImageableX(), pf.getImageableY());

		display.print(g);

		return PAGE_EXISTS;
	}

	public void send() {
		PrinterJob job = PrinterJob.getPrinterJob();
		job.setPrintable(this);
		//boolean ok = job.printDialog();
		//if (ok) {
		try {
			job.print();
		} catch (PrinterException ex) {
			ex.printStackTrace();
		}
		// }
	}
}
