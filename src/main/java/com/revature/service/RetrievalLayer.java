package com.revature.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.revature.exception.KeyNotFoundException;
import com.revature.exception.NoUserTargetedException;
import com.revature.exception.FundsTooHighException;
import com.revature.exception.InitializedFundsBelowZeroException;

public class RetrievalLayer {

	private String user_name = null;
	private String actual_name = null;
	private String user_funds = null;
	private HashMap<String, List<String>> user_data = new HashMap<String, List<String>>();
	private boolean targeting_user = false;

	public RetrievalLayer() {
		this.retrieveFromDatabase();
	}

	private void retrieveFromDatabase() {

		// Eventually, this will instantiate a database connection object
		// For now, data is hard coded
		this.addUser("testAccountName", "John Doe", "255.00");
		this.addUser("testAccount2", "Jane Doe", "100.00");
	}

	public void targetUser(String userKey) throws KeyNotFoundException {
		if (!this.user_data.containsKey(userKey)) {
			throw new KeyNotFoundException();
		}

		this.targeting_user = true;
		this.setName(this.user_data.get(userKey).get(1));
		this.setFunds(this.user_data.get(userKey).get(2));
		this.setUserName(this.user_data.get(userKey).get(0));
	}

	public String getName() throws NoUserTargetedException {
		if (!targeting_user) {
			throw new NoUserTargetedException();
		}
		return this.actual_name;
	}

	public String getUserName() throws NoUserTargetedException {
		if (!targeting_user) {
			throw new NoUserTargetedException();
		}
		return this.user_name;
	}

	private void setUserName(String new_user_name) throws NoUserTargetedException {
		if (!targeting_user) {
			throw new NoUserTargetedException();
		}
		this.user_name = new_user_name;
	}

	private void setName(String new_name) throws NoUserTargetedException {
		if (!targeting_user) {
			throw new NoUserTargetedException();
		}
		this.actual_name = new_name;
	}

	public String getFunds() throws NoUserTargetedException {
		if (!targeting_user) {
			throw new NoUserTargetedException();
		}
		return this.user_funds;
	}

	private void setFunds(String user_funds) throws NoUserTargetedException {
		if (!targeting_user) {
			throw new NoUserTargetedException();
		}
		this.user_funds = user_funds;
	}

	public void addUser(String user_name, String actual_name, String funds)
			throws NumberFormatException, InitializedFundsBelowZeroException, FundsTooHighException {

		// Program will crash if value is not castable
		// As a float
		float validate_money = Float.valueOf(funds);
		if (validate_money < 0) {
			throw new InitializedFundsBelowZeroException();
		}
		if (validate_money > 1000000) {
			throw new FundsTooHighException();
		}

		// Set the new user's key within the table to be
		// The size of the map

		this.user_data.put(user_name, Arrays.asList(user_name, actual_name, funds));
	}

	public void addUser(String user_name, String actual_name) {
		this.addUser(user_name, actual_name, "0.00");
	}

	public void addFunds(float increment_amount) throws NoUserTargetedException {

		if (!this.targeting_user) {
			throw new NoUserTargetedException();
		}

		float new_value = increment_amount + Float.valueOf(this.getFunds());

		this.setFunds(String.format("%.2f", new_value));

		List<String> update_data = Arrays.asList(this.getName(), this.getFunds());
		this.user_data.replace(this.user_name, update_data);

	}

	public void addFunds(double double_increment) {
		this.addFunds((float) double_increment);

	}

	public void addFunds(int int_increment) {
		this.addFunds((float) int_increment);
	}

	public void logOut() {
		this.user_funds = null;
		this.user_name = null;
		this.actual_name = null;
		this.targeting_user = false;
	}

	public boolean getLoggedInStatus() {
		return this.targeting_user;
	}
}
