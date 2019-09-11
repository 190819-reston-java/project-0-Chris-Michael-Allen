package com.revature.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.time.Instant;

import com.revature.exception.KeyNotFoundException;
import com.revature.exception.NoNegativeInputException;
import com.revature.exception.NoUserTargetedException;
import com.revature.repository.DatabaseBridge;
import com.revature.exception.AccountOverdrawnException;
import com.revature.exception.DatabaseProtectionFromTestDataException;
import com.revature.exception.DuplicateUserException;
import com.revature.exception.FundsTooHighException;
import com.revature.exception.InitializedFundsBelowZeroException;
import com.revature.exception.InvalidPasswordException;

public class RetrievalLayer {

	private String user_name = null;
	private String actual_name = null;
	private String user_funds = null;
	private HashMap<String, List<String>> user_data = new HashMap<String, List<String>>();
	private List<List<String>> target_transaction_history = new ArrayList<List<String>>();
	private boolean targeting_user = false;
	private static RetrievalLayer retrieval_instance = new RetrievalLayer();
	private DatabaseBridge database_bridge_instance = new DatabaseBridge();
	private boolean is_testing = false;

	private RetrievalLayer() {
		this.user_data = database_bridge_instance.retrieveFromDatabase();
	}

	public static RetrievalLayer getRetrievalLayer() {
		return retrieval_instance;
	}

	public void setUpTestDatabase() {
		this.user_data = new HashMap<String, List<String>>();
		this.is_testing = true;
		this.addUser("testAccountName", "John Doe", "255.00", "testPassword");
		this.addUser("testAccount2", "Jane Doe", "100.00", "testPassword2");
		this.addUser("testAccount3", "Uncle Pennybags", "312.22", "bigBusiness");
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

	public String getFunds() throws NoUserTargetedException {
		if (!targeting_user) {
			throw new NoUserTargetedException();
		}
		return this.user_funds;
	}

	public List<List<String>> getTargetTransactionHistory() throws NoUserTargetedException {
		
		if(!targeting_user) {
			throw new NoUserTargetedException();
		}
		
		return this.target_transaction_history;

	}

	public boolean getLoggedInStatus() {
		return this.targeting_user;
	}

	public void setTargetTransactionHistory() throws NoUserTargetedException {

		if (!targeting_user) {
			throw new NoUserTargetedException();
		}

		this.target_transaction_history = database_bridge_instance.retrieveFromTransactionHistory(this.getUserName());
	}

	public HashMap<String, List<String>> getUserData() {
		return this.user_data;
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

	private void setFunds(String user_funds) throws NoUserTargetedException {
		if (!targeting_user) {
			throw new NoUserTargetedException();
		}
		this.user_funds = String.format("%.2f", Float.valueOf(user_funds));
	}

	private void setFunds(float f) {
		this.setFunds(String.valueOf(f));

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

		if (this.user_data.containsKey(user_name)) {
			throw new DuplicateUserException();
		}

		password = hashPassword(password);

		this.user_data.put(user_name, Arrays.asList(user_name, actual_name, funds, password));
	}

	public void addUser(String user_name, String actual_name, String password) {
		this.addUser(user_name, actual_name, "0.00", password);
	}

	public void addFunds(float increment_amount)
			throws NoUserTargetedException, NoNegativeInputException, FundsTooHighException, AccountOverdrawnException {

		if (!this.targeting_user) {
			throw new NoUserTargetedException();
		}

		if (increment_amount < 0) {
			throw new NoNegativeInputException();
		}

		float new_value = increment_amount + Float.valueOf(this.getFunds());

		if (new_value < 0) {
			throw new AccountOverdrawnException();
		}

		if (new_value > 1000000) {
			throw new FundsTooHighException();
		}

		String date = Instant.now().toString();
		date = date.replaceAll("T", " ");
		date = date.replaceAll(".[0-9]*Z", "");

		List<String> transaction_history_data_entry = Arrays.asList(this.getUserName(), this.getFunds(),
				String.valueOf(increment_amount), String.valueOf(new_value), date);
		this.database_bridge_instance.addToTransactionHistory(transaction_history_data_entry);

		this.setFunds(new_value);

		List<String> update_data = Arrays.asList(this.getUserName(), this.getName(), this.getFunds(),
				this.user_data.get(user_name).get(3));
		this.user_data.replace(this.user_name, update_data);

	}

	public void addFunds(double double_increment) {
		this.addFunds((float) double_increment);

	}

	public void addFunds(int int_increment) {
		this.addFunds((float) int_increment);
	}

	public void addFunds(String string) {
		this.addFunds(Float.valueOf(string));

	}

	public void withdrawFunds(float withdraw_amount)
			throws NoUserTargetedException, AccountOverdrawnException, NoNegativeInputException, FundsTooHighException {

		if (!this.targeting_user) {
			throw new NoUserTargetedException();
		}

		if (withdraw_amount < 0) {
			throw new NoNegativeInputException();
		}

		float new_amount = Float.valueOf(this.getFunds()) - withdraw_amount;
		if (new_amount < 0) {
			throw new AccountOverdrawnException();
		}

		if (new_amount > 1000000) {
			throw new FundsTooHighException();
		}

		String date = Instant.now().toString();
		date = date.replaceAll("T", " ");
		date = date.replaceAll(".[0-9]*Z", "");

		List<String> transaction_history_data_entry = Arrays.asList(this.getUserName(), this.getFunds(),
				"-" + String.valueOf(withdraw_amount), String.valueOf(new_amount), date);
		this.database_bridge_instance.addToTransactionHistory(transaction_history_data_entry);

		this.setFunds(new_amount);
		List<String> update_array = Arrays.asList(this.getUserName(), this.getName(), this.getFunds(),
				this.user_data.get(this.getUserName()).get(3));
		this.user_data.replace(this.user_name, update_array);
	}

	public void withdrawFunds(int i) {
		this.withdrawFunds((float) i);

	}

	public void withdrawFunds(String s) {
		this.withdrawFunds(Float.valueOf(s));
	}

	public void logOut() {
		this.user_funds = null;
		this.user_name = null;
		this.actual_name = null;
		this.target_transaction_history = new ArrayList<List<String>>();
		this.targeting_user = false;
	}

	public void secureTargetUser(String user_name, String password_attempt)
			throws InvalidPasswordException, KeyNotFoundException {

		if (!(this.user_data.containsKey(user_name))) {
			throw new KeyNotFoundException();
		}
		if (!(hashPassword(password_attempt).equals(this.user_data.get(user_name).get(3)))) {
			throw new InvalidPasswordException();
		}

		this.targetUser(user_name);
	}

	public void targetUser(String userKey) throws KeyNotFoundException {
		if (!this.user_data.containsKey(userKey)) {
			throw new KeyNotFoundException();
		}

		this.targeting_user = true;
		this.setName(this.user_data.get(userKey).get(1));
		this.setFunds(this.user_data.get(userKey).get(2));
		this.setUserName(this.user_data.get(userKey).get(0));

		this.setTargetTransactionHistory();
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

		for (char password_char : seasoned_password.toCharArray()) {
			hash = hash * 31 + Character.getNumericValue(password_char);
		}

		return String.valueOf(hash);
	}

	public void saveToDatabase() throws DatabaseProtectionFromTestDataException {
		if (this.is_testing) {
			throw new DatabaseProtectionFromTestDataException();
		}
		this.database_bridge_instance.sendToUserDatabase(this.getUserData());
	}

}
