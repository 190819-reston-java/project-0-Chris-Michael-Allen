package com.revature.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.revature.exception.KeyNotFoundException;
import com.revature.exception.NoNegativeInputException;
import com.revature.exception.NoUserTargetedException;
import com.revature.exception.AccountOverdrawnException;
import com.revature.exception.DuplicateUserException;
import com.revature.exception.FundsTooHighException;
import com.revature.exception.InitializedFundsBelowZeroException;
import com.revature.exception.InvalidPasswordException;

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
		this.addUser("testAccountName", "John Doe", "255.00", "testPassword");
		this.addUser("testAccount2", "Jane Doe", "100.00", "testPassword2");
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
		this.user_funds = String.format("%.2f", Float.valueOf(user_funds));
	}

	public void addUser(String user_name, String actual_name, String funds, String password)
			throws NumberFormatException, InitializedFundsBelowZeroException, FundsTooHighException,
			DuplicateUserException {

		// Program will crash if value is not castable
		// As a float
		float validate_money = Float.valueOf(funds);
		if (validate_money < 0) {
			throw new InitializedFundsBelowZeroException();
		}
		if (validate_money > 1000000) {
			throw new FundsTooHighException();
		}

		
		if(this.user_data.containsKey(user_name)) {
			throw new DuplicateUserException();
		}
		
		password = hashPassword(password);
		
		this.user_data.put(user_name, Arrays.asList(user_name, actual_name, funds, password));
	}

	public void addUser(String user_name, String actual_name, String password) {
		this.addUser(user_name, actual_name, "0.00", password);
	}

	public void addFunds(float increment_amount) throws NoUserTargetedException,
				NoNegativeInputException, FundsTooHighException, AccountOverdrawnException {

		if (!this.targeting_user) {
			throw new NoUserTargetedException();
		}

		if(increment_amount < 0) {
			throw new NoNegativeInputException();
		}
		
		float new_value = increment_amount + Float.valueOf(this.getFunds());
		
		if(new_value < 0) {
			throw new AccountOverdrawnException();
		}
		
		if(new_value > 1000000) {
			throw new FundsTooHighException();
		}

		this.setFunds(new_value);

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

	public void withdrawFunds(float withdraw_amount) 
	throws NoUserTargetedException, AccountOverdrawnException,
		   NoNegativeInputException, FundsTooHighException {
		
		if(!this.targeting_user) {
			throw new NoUserTargetedException();
		}
		
		if(withdraw_amount < 0) {
			throw new NoNegativeInputException();
		}
		
		float new_amount = Float.valueOf(this.getFunds()) - withdraw_amount;
		if (new_amount < 0) {
			throw new AccountOverdrawnException();
		}
		
		if (new_amount > 1000000) {
			throw new FundsTooHighException();
		}
		
		this.setFunds(new_amount);
		List<String> update_array = Arrays.asList(this.getName(), this.getFunds());
		this.user_data.replace(this.user_name, update_array);
	}
	
	private void setFunds(float f) {
		this.setFunds(String.valueOf(f));
		
	}

	public void withdrawFunds(int i) {
		this.withdrawFunds((float) i);
		
	}
	
	public void withdrawFunds(String s) {
		this.withdrawFunds(Float.valueOf(s));
	}
	
	private String hashPassword(String unsalted_password) {
		String salt = "nlSKJrhao3u4YO%*&Q";
		String pepper = "gy0689bw7ftavb-fga8435";
		
		String seasoned_password = salt + unsalted_password + pepper;
		
		String safe_password = hashingFunction(seasoned_password);
		
		return safe_password;
	}
	
	private String hashingFunction(String seasoned_password) {
		int hash = 3089;
		
		
		for(char password_char : seasoned_password.toCharArray()) {
			hash = hash * 31 + Character.getNumericValue(password_char);
		}
		
		return String.valueOf(hash);
	}
	
	public void secureTargetUser(String user_name, String password_attempt) 
	throws InvalidPasswordException, KeyNotFoundException {
		
		if(!(this.user_data.containsKey(user_name))) {
			throw new KeyNotFoundException();
		}
		if(!(hashPassword(password_attempt).equals(this.user_data.get(user_name).get(3)))){
			throw new InvalidPasswordException();
		}
		
		this.targetUser(user_name);
	}

	
	
	public void addFunds(String string) {
		this.addFunds(Float.valueOf(string));
		
	}
}
