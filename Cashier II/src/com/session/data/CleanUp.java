/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.session.data;

/**
 *
 * @author Wellington
 */

public class CleanUp {
	public CleanUp() {
		cleanup();
	}

	public void cleanup() {
		int size = LogData.details.size();
		if (size > 0) {
			LogData.details.clear();
		}
	}
}
