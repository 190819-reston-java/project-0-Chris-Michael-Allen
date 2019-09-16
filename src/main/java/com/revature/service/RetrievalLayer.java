package com.revature.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;

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
	
	static final Logger logger = Logger.getLogger(RetrievalLayer.class);

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
		logger.info("Retrieved initial information from the database");
	
	}

	public static RetrievalLayer getRetrievalLayer() {
		logger.info("Returning retrieval layer singleton");
		return retrieval_instance;
	}

	public void setUpTestDatabase() {
		
		logger.info("Initialzing test scenario");
		this.user_data = new HashMap<String, List<String>>();
		this.is_testing = true;
		logger.info("Boolean for testing set to true - some functions may now throw exceptions");
		this.addUser("testAccountName", "John Doe", "255.00", "testPassword");
		this.addUser("testAccount2", "Jane Doe", "100.00", "testPassword2");
		this.addUser("testAccount3", "Uncle Pennybags", "312.22", "bigBusiness");
		
	}

	public String getName() throws NoUserTargetedException {
		if (!targeting_user) {
			logger.debug("Function getName ran without a user being targeted");
			throw new NoUserTargetedException();
		}
		
		logger.info("Returning the target user's name: " + this.actual_name);
		return this.actual_name;
	}

	public String getUserName() throws NoUserTargetedException {
		if (!targeting_user) {
			logger.debug("Function getUserName ran without a user being targeted");
			throw new NoUserTargetedException();
		}
		
		logger.info("Returning the target user's username: " + this.user_name);
		return this.user_name;
	}

	public String getFunds() throws NoUserTargetedException {
		if (!targeting_user) {
			logger.debug("Function getFunds ran without a user being targeted");
			throw new NoUserTargetedException();
		}
		
		logger.info("Returning the target user's funds: " + this.user_funds);
		return this.user_funds;
	}

	public List<List<String>> getTargetTransactionHistory() throws NoUserTargetedException {
		
		if(!targeting_user) {
			logger.debug("Function getTargetTransactionHistory ran without a user being targeted");
			throw new NoUserTargetedException();
		}
		
		logger.info("Returning the target user's transaction history");
		return this.target_transaction_history;

	}

	public boolean getLoggedInStatus() {
		logger.info("Returning whether a user is being targeted or not - in effect, if a user is logged in");
		return this.targeting_user;
	}

	public void setTargetTransactionHistory() throws NoUserTargetedException {

		if (!targeting_user) {
			logger.debug("Function setTargetTransactionHistory ran without a user being targeted");
			throw new NoUserTargetedException();
		}

		logger.info("Retrieving the target user's transaction history from the database");
		this.target_transaction_history = database_bridge_instance.retrieveFromTransactionHistory(this.getUserName());
	}

	public HashMap<String, List<String>> getUserData() {
		logger.info("Returning the HashMap that contains all user data");
		return this.user_data;
	}

	private void setUserName(String new_user_name) throws NoUserTargetedException {
		if (!targeting_user) {
			logger.debug("Function setUsername ran without a user being targeted");
			throw new NoUserTargetedException();
		}
		
		logger.info("Changing the target user's username from: " + this.user_name + " to: " + new_user_name);
		this.user_name = new_user_name;
	}

	private void setName(String new_name) throws NoUserTargetedException {
		if (!targeting_user) {
			logger.debug("Function setName ran without a user being targeted");
			throw new NoUserTargetedException();
		}
		
		logger.info("Changing the target's name from: " + this.actual_name + " to: " + new_name);
		this.actual_name = new_name;
	}

	private void setFunds(String user_funds) throws NoUserTargetedException {
		if (!targeting_user) {
			logger.debug("Function setFunds ran without a user being targeted");
			throw new NoUserTargetedException();
		}
		
		logger.info("Changing user's funds from: " + this.user_funds + " to: " + String.format("%.2f", Float.valueOf(user_funds)));
		this.user_funds = String.format("%.2f", Float.valueOf(user_funds));
	}

	private void setFunds(float f) {
		logger.warn("Function setFunds was called with a float argument - float will be converted to string");
		this.setFunds(String.valueOf(f));

	}

	public void addUser(String user_name, String actual_name, String funds, String password)
			throws NumberFormatException, InitializedFundsBelowZeroException, FundsTooHighException,
			DuplicateUserException {

		// Program will crash if value is not castable
		// As a float
		float validate_money = Float.valueOf(funds);
		if (validate_money < 0) {
			logger.debug("Function call for addUser attempted to initialize a user with funds below zero");
			throw new InitializedFundsBelowZeroException();
		}
		if (validate_money > 1000000) {
			logger.debug("Function call for addUser attempted to initialize a user with funds above the arbitrary maximum");
			throw new FundsTooHighException();
		}

		if (this.user_data.containsKey(user_name)) {
			logger.debug("Function call for addUser attempted to initialize a user with a duplicate key (i.e. a repeated username)");
			throw new DuplicateUserException();
		}

		password = hashPassword(password);

		this.user_data.put(user_name, Arrays.asList(user_name, actual_name, funds, password));
		logger.info("Inserted new valid data into the user data HashMap");
	}

	public void addUser(String user_name, String actual_name, String password) {
		this.addUser(user_name, actual_name, "0.00", password);
		logger.warn("Function addUser called without a field for funds - assuming that funds are initialized to 0.00");
	}

	public void addFunds(float increment_amount)
			throws NoUserTargetedException, NoNegativeInputException, FundsTooHighException, AccountOverdrawnException {

		if (!this.targeting_user) {
			logger.debug("Function addFunds called without a user being targeted");
			throw new NoUserTargetedException();
		}

		if (increment_amount < 0) {
			logger.debug("Function addFunds attempted to execute with a negative argument");
			throw new NoNegativeInputException();
		}

		logger.info("Incrementing user's funds from: " + this.getFunds() + " to: " + increment_amount + Float.valueOf(this.getFunds()));
		float new_value = increment_amount + Float.valueOf(this.getFunds());

		if (new_value < 0) {
			logger.debug("Function addFunds was executed with a value that set the user's funds below zero");
			throw new AccountOverdrawnException();
		}

		if (new_value > 1000000) {
			logger.debug("Function addFunds was executed with a value that set the user's funds above the arbitrary maximum");
			throw new FundsTooHighException();
		}

		String date = Instant.now().toString();
		date = date.replaceAll("T", " ");
		date = date.replaceAll(".[0-9]*Z", "");
		
		logger.info("Got the current timestamp and converted it to the proper format for the database: " + date);

		List<String> transaction_history_data_entry = Arrays.asList(this.getUserName(), this.getFunds(),
				String.valueOf(increment_amount), String.valueOf(new_value), date);
		this.database_bridge_instance.addToTransactionHistory(transaction_history_data_entry);
		logger.info("Created a valid SQL insert statement and added to the list of statements to be executed upon database update");

		logger.info("Set user's funds from: " + this.getFunds() + " to: " + new_value);
		this.setFunds(new_value);

		List<String> update_data = Arrays.asList(this.getUserName(), this.getName(), this.getFunds(),
				this.user_data.get(user_name).get(3));
		this.user_data.replace(this.user_name, update_data);		
		logger.info("Updated entry in user data Hashmap to reflect new changes");

	}

	public void addFunds(double double_increment) {
		logger.warn("Attempted to execute function addFunds with a double - converting to a float");
		this.addFunds((float) double_increment);

	}

	public void addFunds(int int_increment) {
		logger.warn("Attempted to execute function addFunds with an int - converting to a float");
		this.addFunds((float) int_increment);
	}

	public void addFunds(String string) {
		logger.warn("Attempted to execute function addFunds with a string - converting to a float");
		this.addFunds(Float.valueOf(string));

	}

	public void withdrawFunds(float withdraw_amount)
			throws NoUserTargetedException, AccountOverdrawnException, NoNegativeInputException, FundsTooHighException {

		if (!this.targeting_user) {
			logger.debug("Function withdrawFunds ran without a user being targeted");
			throw new NoUserTargetedException();
		}

		if (withdraw_amount < 0) {
			logger.debug("Function withdrawFunds ran with a negative number");
			throw new NoNegativeInputException();
		}

		float new_amount = Float.valueOf(this.getFunds()) - withdraw_amount;
		logger.info("Attempting to set funds from: " + this.getFunds() + " to: " + new_amount);
		
		if (new_amount < 0) {
			logger.debug("Function withdrawFunds resulted in an amount that left account balance negative");
			throw new AccountOverdrawnException();
		}

		if (new_amount > 1000000) {
			logger.debug("Function withdrawFunds resulted in an amount that left account balance above arbitrary maximum");
			throw new FundsTooHighException();
		}

		String date = Instant.now().toString();
		date = date.replaceAll("T", " ");
		date = date.replaceAll(".[0-9]*Z", "");
		logger.info("Got the current timestamp and converted it to the proper format for the database: " + date);
	
		
		List<String> transaction_history_data_entry = Arrays.asList(this.getUserName(), this.getFunds(),
				"-" + String.valueOf(withdraw_amount), String.valueOf(new_amount), date);
		this.database_bridge_instance.addToTransactionHistory(transaction_history_data_entry);
		logger.info("Created a valid SQL insert statement and added to the list of statements to be executed upon database update");

		logger.info("Set user's funds from: " + this.getFunds() + " to: " + new_amount);		
		this.setFunds(new_amount);
		
		List<String> update_array = Arrays.asList(this.getUserName(), this.getName(), this.getFunds(),
				this.user_data.get(this.getUserName()).get(3));
		this.user_data.replace(this.user_name, update_array);
		logger.info("Updated entry in user data Hashmap to reflect new changes");
	}

	public void withdrawFunds(int i) {
		logger.warn("Function withdrawFunds attempted to execute with an int - converting to float");
		this.withdrawFunds((float) i);

	}

	public void withdrawFunds(String s) {
		logger.warn("Function withdrawFunds attempted to execute with a string - converting to float");
		this.withdrawFunds(Float.valueOf(s));
	}

	public void logOut() {
		this.user_funds = null;
		this.user_name = null;
		this.actual_name = null;
		this.target_transaction_history = new ArrayList<List<String>>();
		this.targeting_user = false;
		
		logger.info("User has logged out. All fields used for targeting have been set to null, and transaction history is an empty list");
	}

	public void secureTargetUser(String user_name, String password_attempt)
			throws InvalidPasswordException, KeyNotFoundException {

		if (!(this.user_data.containsKey(user_name))) {
			logger.debug("Attempted to target a user with an invalid target");
			throw new KeyNotFoundException();
		}
		if (!(hashPassword(password_attempt).equals(this.user_data.get(user_name).get(3)))) {
			logger.debug("Password entry attempt did not match hash value in database");
			throw new InvalidPasswordException();
		}

		logger.info("User information and password validated. Attempting to target the user");
		this.targetUser(user_name);
	}

	public void targetUser(String userKey) throws KeyNotFoundException {
		if (!this.user_data.containsKey(userKey)) {
			logger.debug("Attempted to target a user in function targetUser without a valid target");
			throw new KeyNotFoundException();
		}

		this.targeting_user = true;
		this.setName(this.user_data.get(userKey).get(1));
		this.setFunds(this.user_data.get(userKey).get(2));
		this.setUserName(this.user_data.get(userKey).get(0));
		logger.info("Now targeting user: " + this.getUserName());
		
		this.setTargetTransactionHistory();
	}

	private String hashPassword(String unsalted_password) {
		String salt = "nlSKJrhao3u4YO%*&Q";
		String pepper = "gy0689bw7ftavb-fga8435";

		String seasoned_password = salt + unsalted_password + pepper;
		logger.info("Added salt and pepper to password");
		
		String safe_password = hashingFunction(seasoned_password);
		logger.info("Password successfully hashed");
		
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
			logger.debug("Attempted to save to database while using test data");
			throw new DatabaseProtectionFromTestDataException();
		}
		logger.info("Passing HashMap of user data to the repository layer to send to database");
		this.database_bridge_instance.sendToUserDatabase(this.getUserData());
		logger.info("All user data has been saved in database");
		
		if(this.targeting_user) {
			logger.info("After saving to the database, if targeting a user, retrieve the transaction history");
			this.setTargetTransactionHistory();
		}
	}

}
