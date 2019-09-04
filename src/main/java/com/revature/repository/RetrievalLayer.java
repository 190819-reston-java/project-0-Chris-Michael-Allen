package com.revature.repository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.revature.exception.KeyNotFoundException;
import com.revature.exception.TargetUserNotFoundException;
import com.revature.exception.FundsTooHighException;
import com.revature.exception.InitializedFundsBelowZeroException;

public class RetrievalLayer {

	private int user_key;
	private String user_name;
	private String user_funds;
	private HashMap<Integer, List<String>> user_data = new HashMap<Integer, List<String>>();
	private boolean targeting_user = false;

	public RetrievalLayer() {
		this.retrieveFromDatabase();
	}

	public void setUserKey(int user_key) {
		this.user_key = user_key;
	}

	public int getUserKey() {
		return (user_key);
	}

	private void retrieveFromDatabase() {
		this.addUser("John Doe", "255.00");
		this.addUser("Jane Doe", "100.00");
	}

	public void targetUser(int index) throws KeyNotFoundException {
		if(!this.user_data.containsKey(index)) {
			throw new KeyNotFoundException();
		}
		
		this.targeting_user = true;
		this.setName(this.user_data.get(index).get(0));
		this.setFunds(this.user_data.get(index).get(1));
		
		this.setUserKey(index);
	}

	public String getName() {
		return this.user_name;
	}

	public void setName(String user_name) {
		this.user_name = user_name;
	}

	public String getFunds() {
		return this.user_funds;
	}
	
	public void setFunds(String user_funds) {
		this.user_funds = user_funds;
	}

	public void addUser(String Name, String Funds) 
	throws NumberFormatException, InitializedFundsBelowZeroException,
	FundsTooHighException {

		// Program will crash if value is not castable
		// As a float
		float validate_money = Float.valueOf(Funds);
		if(validate_money < 0) {
			throw new InitializedFundsBelowZeroException();
		}
		if(validate_money > 1000000) {
			throw new FundsTooHighException();
		}
		
		
		// Set the new user's key within the table to be
		// The size of the map
		
		this.user_data.put(this.user_data.size(), Arrays.asList(Name, Funds));
	}
	
	public void addUser(String name) {
		this.addUser(name, "0.00");
	}

	public void addFunds(float increment_amount) 
	throws TargetUserNotFoundException{
		
		if(this.targeting_user == false) {
			throw new TargetUserNotFoundException();
		}
		
		float new_value = increment_amount + Float.valueOf(this.getFunds());
		
		this.setFunds(String.format("%.2f", new_value));
		
		List<String> update_data = Arrays.asList(this.getName(),this.getFunds());
		this.user_data.replace(this.user_key, update_data);
		
	}

	public void addFunds(double double_increment) {
		this.addFunds((float) double_increment);
		
	}
}
