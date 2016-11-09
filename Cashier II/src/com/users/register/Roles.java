package com.users.register;

import java.util.ArrayList;

/**
 *
 * @author Wellington
 */

public class Roles {

	public static ArrayList<String> getRoles() {
		String[] roles = new String[] { "admin", "cashier" };
		ArrayList<String> rolesArray = new ArrayList<>();
		for (int i = 0; i < roles.length; i++) {
			rolesArray.add(roles[i]);
		}
		return rolesArray;
	}
}
