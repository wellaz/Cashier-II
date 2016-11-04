package com.billing;

import java.sql.ResultSet;
import java.sql.Statement;

import helpers.SetDateCreated;
import helpers.ThisMonth;

/**
 *
 * @author Wellington
 */

public class DailyBillingThread {
	Statement stm;
	ResultSet rs;

	public DailyBillingThread(Statement stm, ResultSet rs) {
		this.rs = rs;
		this.stm = stm;
	}

	public void analyzeData() {
		GetDailyBillingFee getdailybilingfee = new GetDailyBillingFee(rs, stm);
		getdailybilingfee.getData();
		GetDailyBillLastDate lastchargedate = new GetDailyBillLastDate(rs, stm);
		double fee = getdailybilingfee.getFee();
		String date1 = new SetDateCreated().getDateStrock().trim();
		// String date2 = lastchargedate.getDate().trim();
		if (lastchargedate.allDates().size() > 0) {
			if (!lastchargedate.isValidDate(date1)) {
				postData(fee);
				System.out.println("Did not exists and daily bill was posted");
			} else {
				System.out.println("Already charged");
			}
		} else {
			System.out.println("ArrayList was empty and daily bill processed");
			postData(fee);
		}

		/*
		 * if (!date2.equals("null")) { Date thatDate =
		 * lastchargedate.returnDate(); LocalDate date =
		 * LocalDate.of(thatDate.getYear(), thatDate.getMonth(),
		 * thatDate.getDate()); LocalDate now = LocalDate.now(); if
		 * (now.isAfter(date)) {
		 * 
		 * if (date1.equals(date2)) { System.out.println("Already charged"); }
		 * else { postData(fee); } } else if (now.isBefore(date)) {
		 * System.out.println("Kindly correct your date");
		 * JOptionPane.showMessageDialog(null, "Incorrect System Date",
		 * "Kindly correch the system date!", JOptionPane.ERROR_MESSAGE);
		 * 
		 * System.exit(0); }
		 * 
		 * } else { postData(fee); }
		 */
	}

	public void postData(double fee) {
		PostDailyBill pdb = new PostDailyBill(stm);
		String date = new SetDateCreated().getDate();
		String time = new SetDateCreated().getTime();
		String month = ThisMonth.thisMonth();
		pdb.postData(date, time, fee, month);
	}

	/*
	 * public static void main(String[] args) { AccessDbase adbase = new
	 * AccessDbase(); adbase.connectionDb(); DailyBillingThread th = new
	 * DailyBillingThread(adbase.stm, adbase.rs); th.analyzeData(); }
	 */
}
