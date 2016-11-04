/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author Wellington
 */
public class Increment {

	ResultSet rs;
	Statement stm;

	public Increment(ResultSet rs, Statement stm) {
		this.rs = rs;
		this.stm = stm;

	}

	public int incerentcCategoryId() {
		int rows = 0;
		String query = "SELECT * FROM commonproducts";
		try {
			rs = stm.executeQuery(query);
			rs.last();
			rows = rs.getRow();
		} catch (Exception ex) {
		}
		int aid = rows + 1;
		return aid;
	}

	public int incrementReceiptno() {
		int rows = 0;
		String query = "SELECT * FROM receipt_amount";
		try {
			rs = stm.executeQuery(query);
			rs.last();
			rows = rs.getRow();
		} catch (Exception ex) {
		}
		int aid = rows + 1;
		return aid;
	}
}
