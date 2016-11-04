package com.bean;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import helpers.AccessDbase;

/**
 *
 * @author Wellington
 */
public class CreateTables {
	Statement stm;
	ResultSet rs;

	public CreateTables(Statement stm) {
		this.stm = stm;
	}

	public void createExpensestable() {
		String query = "CREATE TABLE IF NOT EXISTS expenses(amount DOUBLE NOT NULL,date DATE NOT NULL,time TIME NOT NULL,year INT(11),month VARCHAR(30) NOT NULL,details TEXT(100))ENGINE=InnoDB DEFAULT CHARSET=latin1;";
		try {
			stm.execute(query);
			System.out.println("Table created");
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
	}

	public void createMemocheckTable() {
		String query = "CREATE TABLE IF NOT EXISTS memockeck(month VARCHAR(30),year INT(11),attempts INT(11))ENGINE=InnoDB DEFAULT CHARSET=latin1;";
		try {
			stm.execute(query);
			System.out.println("Table created");
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
	}

	public void createCashpayTable() {
		String query = "CREATE TABLE IF NOT EXISTS cashpay(receiptno INT(11) NOT NULL,cashier_id INT(11) NOT NULL,product_name VARCHAR(100) NOT NULL,quantity INT(11) NOT NULL,amount DOUBLE NOT NULL,date DATE NOT NULL,time TIME NOT NULL)ENGINE=InnoDB DEFAULT CHARSET=latin1;";
		try {
			stm.execute(query);
			System.out.println("Table created");
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
	}

	public void createBankDepositTable() {
		String query = "CREATE TABLE IF NOT EXISTS bank_dep(amount DOUBLE NOT NULL,date DATE NOT NULL,time TIME NOT NULL,month VARCHAR(30) NOT NULL)ENGINE=InnoDB DEFAULT CHARSET=latin1;";
		try {
			stm.execute(query);
			System.out.println("Table created");
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
	}

	public void createWithdrawalTable() {
		String query = "CREATE TABLE IF NOT EXISTS bank_with(amount DOUBLE NOT NULL,date DATE NOT NULL,time TIME NOT NULL,month VARCHAR(30) NOT NULL)ENGINE=InnoDB DEFAULT CHARSET=latin1;";
		try {
			stm.execute(query);
			System.out.println("Table created");
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
	}

	public void createBulkStockTable() {
		String query = "CREATE TABLE IF NOT EXISTS bulk_stock(product_name VARCHAR(30) NOT NULL,quantity INT(11) NOT NULL,items_sold INT(11) NOT NULL,remaining INT(11) NOT NULL,date DATE NOT NULL,time TIME NOT NULL,year INT(11) NOT NULL)ENGINE=InnoDB DEFAULT CHARSET=latin1;";
		try {
			stm.execute(query);
			System.out.println("Table created");
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
	}

	public void createSolidBulkTable() {
		String query = "CREATE TABLE IF NOT EXISTS solid_bulk(product_name VARCHAR(30) NOT NULL,quantity INT(11) NOT NULL,date DATE NOT NULL,time TIME NOT NULL,year INT(11) NOT NULL)ENGINE=InnoDB DEFAULT CHARSET=latin1;";
		try {
			stm.execute(query);
			System.out.println("Table created");
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
	}

	public void createLedgerTable() {
		String query = "CREATE TABLE IF NOT EXISTS ledger(date DATE NOT NULL,time TIME NOT NULL,month VARCHAR(30) NOT NULL,year INT(11) NOT NULL,debit DOUBLE NOT NULL,credit DOUBLE NOT NULL,details TEXT(500) NOT NULL)ENGINE=InnoDB DEFAULT CHARSET=latin1;";
		try {
			stm.execute(query);
			System.out.println("Table created");
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
	}

	public void receiptAmountTable() {
		String query = "CREATE TABLE IF NOT EXISTS receipt_amount(receiptno INT(11) NOT NULL,amount DOUBLE NOT NULL,amount_given DOUBLE NOT NULL,amount_changed DOUBLE NOT NULL,change_collected DOUBLE NOT NULL,date DATE NOT NULL,time TIME NOT NULL)ENGINE=InnoDB DEFAULT CHARSET=latin1;";
		try {
			stm.execute(query);
			System.out.println("Table created");
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
	}

	public void createCommonProductsTable() {
		String query = "CREATE TABLE IF NOT EXISTS commonproducts(product_id INT(11) NOT NULL,product_name VARCHAR(100) NOT NULL,unit_price DOUBLE NOT NULL,date DATE NOT NULL,time TIME NOT NULL,year INT(11) NOT NULL)ENGINE=InnoDB DEFAULT CHARSET=latin1;";
		try {
			stm.execute(query);
			System.out.println("Table created");
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
	}

	public void createTransactiontmpTable() {
		String query = "CREATE TABLE IF NOT EXISTS transaction_tmp(product_name VARCHAR(30) NOT NULL,quantity INT(11) NOT NULL,amount DOUBLE NOT NULL)ENGINE=InnoDB DEFAULT CHARSET=latin1;";
		try {
			stm.execute(query);
			System.out.println("Table created");
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
	}

	public void createUsersTable() {
		String query = "CREATE TABLE IF NOT EXISTS users (usernames varchar(50) NOT NULL, password varchar(50) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
		try {
			stm.execute(query);
			System.out.println("Table created");
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
	}

	public void createKeysTable() {
		String query = "CREATE TABLE IF NOT EXISTS memo (month int(11) DEFAULT '0',k_ey varchar(30) NOT NULL,year int(11) DEFAULT '0',		  PRIMARY KEY (`k_ey`)) ENGINE=InnoDB DEFAULT CHARSET=latin1;";
		try {
			stm.execute(query);
			System.out.println("Table created");
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
	}

	public void createCashiersTable() {
		String query = "CREATE TABLE IF NOT EXISTS cashiers(cashierid INT(11) NOT NULL,first_name VARCHAR(30) NOT NULL, last_name VARCHAR(30) NOT NULL)ENGINE=InnoDB DEFAULT CHARSET=latin1;";
		try {
			stm.execute(query);
			System.out.println("Table created");
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
	}

	public void checkAndPerformOperations() {
		createExpensestable();
		createMemocheckTable();
		createCashpayTable();
		createBankDepositTable();
		createWithdrawalTable();
		createBulkStockTable();
		createSolidBulkTable();
		createLedgerTable();
		receiptAmountTable();
		createCommonProductsTable();
		createTransactiontmpTable();
		createUsersTable();
		createCashiersTable();
		createKeysTable();
		processKeyCheck();
		processUsersList();
	}

	public void processKeyCheck() {
		KeysArray ka = new KeysArray();
		ArrayList<String> data = ka.storeKeys();
		for (String k : data) {
			if (!isValidKey(k)) {
				String insertString = "INSERT INTO memo(month,k_ey,year)VALUES('" + 0 + "','" + k + "','" + 0 + "')";
				try {
					stm.execute(insertString);
				} catch (SQLException ee) {
					ee.printStackTrace();
				}
			} else
				System.out.println("Key Already inserted");
		}
	}

	public void processUsersList() {
		UsersArray ka = new UsersArray();
		String data = ka.returnCommonUser();
		if (!isUserValid(data)) {
			String insertString = "INSERT INTO users(usernames,password)VALUES('" + data + "','" + data + "')";
			try {
				stm.execute(insertString);
			} catch (SQLException ee) {
				ee.printStackTrace();
			}
		} else
			System.out.println("User Already inserted");
	}

	public ArrayList<String> getInstalledKeys() {
		String query = "SELECT k_ey FROM memo";
		ArrayList<String> data = new ArrayList<>();
		try {
			rs = stm.executeQuery(query);
			while (rs.next()) {
				String key = rs.getString(1);
				data.add(key);
			}
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
		return data;
	}

	public ArrayList<String> getAllUsers() {
		String query = "SELECT password FROM users";
		ArrayList<String> data = new ArrayList<>();
		try {
			rs = stm.executeQuery(query);
			while (rs.next()) {
				String key = rs.getString(1);
				data.add(key);
			}
		} catch (SQLException ee) {
			ee.printStackTrace();
		}
		return data;
	}

	public boolean isUserValid(String user) {
		List<String> imgTypes = null;
		int arrsize = getAllUsers().size();
		String[] dat = new String[arrsize];
		for (int i = 0; i < arrsize; i++)
			dat[i] = getAllUsers().get(i);

		imgTypes = Arrays.asList(dat);
		return imgTypes.stream().anyMatch(t -> user.equals(t));
	}

	public boolean isValidKey(String key) {
		List<String> imgTypes = null;
		int arrsize = getInstalledKeys().size();
		String[] dat = new String[arrsize];
		for (int i = 0; i < arrsize; i++)
			dat[i] = getInstalledKeys().get(i);

		imgTypes = Arrays.asList(dat);
		return imgTypes.stream().anyMatch(t -> key.equals(t));
	}

	public static void main(String[] args) {
		AccessDbase adbase = new AccessDbase();
		adbase.connectionDb();
		CreateTables c = new CreateTables(adbase.stm);
		c.checkAndPerformOperations();
	}
}
