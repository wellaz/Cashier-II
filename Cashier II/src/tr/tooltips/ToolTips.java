package tr.tooltips;

/**
 *
 * @author Wellington
 */
public class ToolTips {
	public String recon = null;
	public String daily_sales_report = null;
	public String expenses_report = null;
	public String pre_sales = null;
	public String in_stock = null;
	public String pro_trans = null;
	public String reprint = null;
	public String changes = null;
	public String sessions = null;
	public String stock_pr = null;
	public String bulk_stock_ent = null;
	public String post_ex = null;
	public String banking = null;
	public String update_price = null;

	public ToolTips() {
		recon = "Generate Reconciliation Statement.";
		daily_sales_report = "Extract Today's Sales Report.";
		expenses_report = "Extract an Expense Report.";
		pre_sales = "Generate Sales Report for any given date.";
		in_stock = "See what's left in stock.";
		pro_trans = "Process any sales, transactions, serve clients and print receipts.";
		reprint = "Reprint any receipt, given a receipt number.";
		changes = "See if any client left their change with us.";
		sessions = "Basic query handling facility, for all General Ledger Account(4131).";
		stock_pr = "Create new product category or type, and put its price tag.";
		bulk_stock_ent = "Enter all new stock, product type and quantity.";
		post_ex = "Post an expense.";
		banking = "For Banking, deposit to OR whthdraw from the system.";
		update_price = "Update any product price here.";
	}

}
