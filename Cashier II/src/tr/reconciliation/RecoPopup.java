package tr.reconciliation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

@SuppressWarnings("serial")
public class RecoPopup extends JPopupMenu implements ActionListener {

	public JMenuItem sub_summ, ex_summ, prop;
	String from, to;
	ResultSet rs;
	Statement stm;
	JFrame frame;

	public RecoPopup(String from, String to, ResultSet rs, Statement stm, JFrame frame) {

		this.from = from;
		this.to = to;
		this.stm = stm;
		this.rs = rs;
		this.frame = frame;
		init();
	}

	public final void init() {
		sub_summ = new JMenuItem("Tuition Summary");
		sub_summ.addActionListener(this);
		ex_summ = new JMenuItem("Expenses Summary");
		ex_summ.addActionListener(this);
		prop = new JMenuItem("Others...");
		prop.setEnabled(false);

		this.add(sub_summ);
		this.addSeparator();
		this.add(ex_summ);
		this.addSeparator();
		this.add(prop);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == sub_summ) {
			GetTotalSales ts = new GetTotalSales(rs, stm, frame);
			ts.getAllValues(from, to);
		}
		if (e.getSource() == ex_summ) {
			GetTotalExp ex = new GetTotalExp(rs, stm, frame);
			ex.getAllExValues(from, to);
		}
	}
}
