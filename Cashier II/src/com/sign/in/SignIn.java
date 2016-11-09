package com.sign.in;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import helpers.SetDateCreated;
import tr.audio.Click;
import tr.temporarydata.delete.DeleteTMPTableData;

/**
 *
 * @author Wellington
 */

public class SignIn {
	Statement stm;
	ResultSet rs;

	public SignIn(Statement stm, ResultSet rs) {
		this.rs = rs;
		this.stm = stm;
	}

	public void signOff() {
		deleteTemporaryData();
		postSignInDetails();
		new Click().dialogPlayer();
	}

	public void deleteTemporaryData() {
		DeleteTMPTableData delete = new DeleteTMPTableData(rs, stm);
		delete.deleteTemporaryData();
	}

	public String getUsername() {
		return Monitor.users.get(Monitor.users.size() - 1);
	}

	public String getFullname() {
		return Monitor.fullname.get(Monitor.fullname.size() - 1);
	}

	public String getDate() {
		return new SetDateCreated().getDate();
	}

	public String getTime() {
		return new SetDateCreated().getTime();
	}

	private void postSignInDetails() {
		String query = "INSERT INTO sign_in(username,full_name,date,time)VALUES('" + getUsername() + "','"
				+ getFullname() + "','" + getDate() + "','" + getTime() + "')";
		try {
			stm.execute(query);
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
	}
}