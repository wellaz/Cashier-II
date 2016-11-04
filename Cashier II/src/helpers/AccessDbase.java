/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package helpers;

import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.troubleshoot.Window;

/**
 *
 * @author Wellington
 */
public class AccessDbase {
	public Statement stmt, stmt1, stm;
	public Connection conn, conn1;
	public ResultSet rs, rs1;
	public PreparedStatement pstmt;

	public AccessDbase() {
	}

	public void connectionDb() {
		try {
			String url = "jdbc:mysql://localhost/tracer";
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(url, "root", "");
			conn1 = DriverManager.getConnection(url, "root", "");
			stmt = conn1.createStatement();
			stm = conn1.createStatement();
			stmt1 = conn1.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException sqle) {
			Window w = new Window();
			EventQueue.invokeLater(() -> {
				w.setVisible(true);
			});
		}
	}
}
