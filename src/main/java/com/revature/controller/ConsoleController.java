package com.revature.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.revature.exception.AccountOverdrawnException;
import com.revature.exception.DuplicateUserException;
import com.revature.exception.FundsTooHighException;
import com.revature.exception.InvalidPasswordException;
import com.revature.exception.KeyNotFoundException;
import com.revature.exception.NoNegativeInputException;
import com.revature.service.RetrievalLayer;

public class ConsoleController {

	private RetrievalLayer service_handler = RetrievalLayer.getRetrievalLayer();

	private String menu_header = 
			  "*******************************************************\n"
			+ "*******************************************************\n"
			+ "****          WELCOME TO THE BANKING APP!          ****\n"
			+ "****                                               ****\n"
			+ "*******************************************************\n"
			+ "****                                               ****\n"
			+ "**** Please enter a number to execute that option  ****\n"
			+ "*******************************************************\n"
			+ "*******************************************************";
	
	private String[][] logged_in_menu = { 
			{ "1. View Account Balance", "0" }, 
			{ "2. Deposit Funds", "1" },
			{ "3. Withdraw Funds", "2" }, 
			{ "4. View Transaction History", "3" },
			{ "5. Save to Database", "4" }, 
			{ "6. Log out", "5" } 
			};

	private String[][] logged_out_menu = { 
			{ "1. Create Account", "6" }, 
			{ "2. Log in to account", "7" },
			{ "3. Save and exit", "8" }
			};
	
	private int space_upper_bound = 2;

	private Scanner user_input_control = new Scanner(System.in);
	private boolean loop_execution = true;
	
	private String display_name;
	public ConsoleController() {
		this.runMainMenu();
	}

	private void runMainMenu() {
		System.out.println(this.menu_header);

		do {
			int menu_case = service_handler.getLoggedInStatus() ? 
					menuSelection(this.logged_in_menu) : menuSelection(this.logged_out_menu);
		
			selectMenuFunction(menu_case);
			
		}
		
		
		while(this.loop_execution);
	}
	
	private void selectMenuFunction(int menu_case) {
		switch (menu_case) {
		case 0:
			System.out.println("View account balance!");
			doViewAccountBalance();
			break;
		case 1:
			System.out.println("Deposit funds!");
			doDepositFunds();
			break;
		case 2:
			System.out.println("Withdraw funds!");
			doWithdrawFunds();
			break;
		case 3:
			System.out.println("View transaction history!");
			doShowTransactionHistory();
			break;
		case 4:
			System.out.println("Save to database!");
			doSaveToDatabase();
			break;
		case 5:
			System.out.println("Log out!");
			doLogOut();
			break;
		case 6:
			System.out.println("Create account!");
			doCreateAccount();
			break;
		case 7:
			System.out.println("Log in!");
			doLogIn();
			break;
		case 8:
			System.out.println("Save and exit!");
			doSaveToDatabase();
			this.loop_execution = false;
			break;
		}
		
	}

	private void doSaveToDatabase() {
		
		this.service_handler.saveToDatabase();
		
	}

	private void doLogOut() {
		System.out.print("Are you sure you would like to exit this menu? Type 'EXIT' to logout. "
				+ "Enter anything else to stay logged in: ");
		
		String user_input = this.user_input_control.nextLine();
		
		if(!user_input.equals("EXIT")) {
			return;
		}
		
		this.service_handler.logOut();
		this.setDisplayName("");
		this.setRetrievalKey("");
	}

	private void doWithdrawFunds() {
		this.doViewAccountBalance();
		System.out.print("How much money would you like to withdraw? Type 'EXIT' to exit: ");
		String withdrawal_amount = this.user_input_control.nextLine();
		
		if (withdrawal_amount.equals("EXIT")) {
			return;
		}
		
		try{
			this.service_handler.withdrawFunds(withdrawal_amount);
		}
		catch (NumberFormatException e) {
			System.out.println("Error! You must enter a number!");
			this.doWithdrawFunds();
			return;
		}
		catch (AccountOverdrawnException e) {
			System.out.println("You can not withdraw more money than is in your account!");
			this.doWithdrawFunds();
			return;
		}
		catch (NoNegativeInputException e) {
			System.out.println("Can not make withdrawals with negative numbers!");
			this.doWithdrawFunds();
			return;
		}
		catch (FundsTooHighException e) {
			System.out.println("Your withdrawal has caused your account to overflow! Please do not enter this amount!");
			this.doWithdrawFunds();
			return;
		}
	}

	private void doViewAccountBalance() {
		System.out.println("Your current funds are: $" + this.service_handler.getFunds());
		return;
	}

	private void doLogIn() {
		System.out.print("Please enter the name of the account you wish to log in to (Enter 'EXIT' to exit): ");
		String user_to_find = this.user_input_control.nextLine();
		
		if(user_to_find.equals("EXIT")) {
			return;
		}
		
		System.out.print("Please enter the password associated with this account: ");
		String password_to_test = this.user_input_control.nextLine();
		
		try {
		service_handler.secureTargetUser(user_to_find, password_to_test);
		}
		catch (KeyNotFoundException e) {
			System.out.println("This user is not in the database! Perhaps you meant to register this account?");
			this.doLogIn();
			return;
		}
		catch (InvalidPasswordException e) {
			System.out.println("The password you entered is incorrect!");
			this.doLogIn();
			return;
		}
		
		this.setDisplayName(service_handler.getName());
		this.setRetrievalKey(service_handler.getUserName());
		return;
	}

	private void doDepositFunds() {
		
		this.doViewAccountBalance();
		System.out.print("Note: can not have more than $1000000 in your account.\n"
						+ "How much money would you like to deposit?\n"
						+ "Type 'EXIT' to exit: ");
		
		String deposit_amount = this.user_input_control.nextLine();
		
		if(deposit_amount.equals("EXIT")) {
			return;
		}
		
		try {
			this.service_handler.addFunds(deposit_amount);
		}
		catch (FundsTooHighException e) {
			System.out.println("Your deposit causes your account to exceed $1000000! Please deposit less");
			this.doDepositFunds();
			return;
		}
		catch (NoNegativeInputException e) {
			System.out.println("Can not add funds with a negative number! Perhaps you meant to withdraw?");
			this.doDepositFunds();
			return;
		}
		catch (AccountOverdrawnException e) {
			System.out.println("Your deposit attempt has caused your account to underflow! Do not enter this amount!");
			this.doDepositFunds();
			return;
		}
		catch (NumberFormatException e) {
			System.out.println("You must enter a valid number!");
			this.doDepositFunds();
			return;
		}
	}

	private void doCreateAccount() {
		System.out.print("Please enter the username of the account you would like to create. "
				+ "A username may consist of:\n"
				+ "Upper/Lower case letters\n"
				+ "Numbers 0-9\n"
				+ "Underscores\n"
				+ "Type 'EXIT' to exit: ");
		
		String new_name = this.user_input_control.nextLine();
		
		if(new_name.equals("EXIT")) {
			return;
		}
		
		String validated_name = new_name.replaceAll("[^0-9a-zA-Z_]", "");
		if (new_name.length() != validated_name.length()) {
			System.out.println("Invalid name! Please try again");
			this.doCreateAccount();
			return;
		}
		
		System.out.print("Please enter the real name to be associated with this account. Type 'EXIT'"
				+ "to exit.\nNote: Illegal characters will be stripped from your name: ");
		String actual_name = this.user_input_control.nextLine();
		
		if(actual_name.equals("EXIT")) {
			return;
		}
		
		String validated_actual_name = actual_name.replaceAll("[^0-9a-zA-Z_ ]", "");
		
		System.out.print("Please enter your password. Type 'EXIT' to exit.\nNote: Illegal characters"
				+ "will be stripped from your password: ");
		
		String first_password_entry;
		first_password_entry = this.user_input_control.nextLine();
		
		if(first_password_entry.equals("EXIT")) {
			return;
		}
		
		System.out.print("Please enter your password again for confirmation: ");
		String password_confirm = this.user_input_control.nextLine();
		
		if(!(password_confirm.equals(first_password_entry))){
			System.out.print("Your passwords do not match! Please try again");
			this.doCreateAccount();
			return;
		}
		
		String validated_password = password_confirm.replaceAll("[^0-9a-zA-Z_]", "");
		
		if(validated_password.length() != password_confirm.length()) {
			System.out.println("Your password has invalid characters in it! Please try again");
			this.doCreateAccount();
			return;
		}
		
		System.out.println("User name: " + validated_name);
		System.out.println("Display name: " + validated_actual_name);
		
		System.out.println("Are these settings okay? Enter 'EXIT' to cancel registration attempt, and "
				+ "any other entry to confirm: ");
		
		String final_confirm = this.user_input_control.nextLine();
		
		if(final_confirm.equals("EXIT")) {
			return;
		}
		
		try {
			this.service_handler.addUser(validated_name, validated_actual_name, validated_password);
		}
		catch (DuplicateUserException e) {
			System.out.println("There is already a user with that username!");
			return;
		}
		
		this.service_handler.targetUser(validated_name);
		this.setDisplayName(service_handler.getName());
		this.setRetrievalKey(service_handler.getUserName());
		return;
		
	}
	
	private String getDisplayName() {
		return this.display_name;
	}
	
	private void doShowTransactionHistory() {
		List<List<String>> transaction_history_to_handle = new ArrayList<List<String>>();
		transaction_history_to_handle = this.service_handler.getTargetTransactionHistory();
		System.out.println("Display Transaction History.\nNote: Recent transactions will only "
						 + "display if you have saved to the database!");
		
		
		if(transaction_history_to_handle.size() == 0) {
			System.out.println("There is no transaction history to display!");
			return;
		}
		System.out.println("Transaction history for: " + this.getDisplayName());
		System.out.println("Old Amount\tAction Performed\tNew Amount\tTime Performed");
		System.out.println("==========\t================\t==========\t==============");
		
		for(List<String> data_entry : transaction_history_to_handle) {
			String display_string;
			display_string = (data_entry.get(0) + "\t"
							+ data_entry.get(1) + "\t"
							+ data_entry.get(2) + "\t"
							+ data_entry.get(3));
			System.out.println(display_string);
		}
	}
	
	private void setDisplayName(String display_name) {
		this.display_name = display_name;
	}
	
	private void setRetrievalKey(String retrieval_key) {
	}

	private int menuSelection(String[][] menu_to_show) {

		String user_selection;
		int converted_user_selection;

		for (String[] menu_choice : menu_to_show) {
			System.out.print("**** ");
			System.out.println(menu_choice[0]);
		}
		
		System.out.print("**** ");
		System.out.print("Please enter your choice: ");
		
		user_selection = this.user_input_control.nextLine();
		//System.out.print("\033[H\033[2J");
		
		try {
			converted_user_selection = Integer.valueOf(user_selection);
		}
		catch (NumberFormatException e){
			System.out.print("**** ");
			System.out.println("Invalid menu option! Please enter a number");
			converted_user_selection = this.menuSelection(menu_to_show);
			return converted_user_selection;
		}
		if ((converted_user_selection <= 0) || (converted_user_selection > menu_to_show.length)){
			System.out.print("**** ");
			System.out.println("Invalid menu option! Please choose a valid option");
			converted_user_selection = this.menuSelection(menu_to_show);
			return converted_user_selection;
		}
		
		converted_user_selection = Integer.valueOf(menu_to_show[converted_user_selection - 1][1]);
		
		if (converted_user_selection < 0) {
			System.out.println("Invalid menu option! Please choose a valid option");
			converted_user_selection = this.menuSelection(menu_to_show);
			return converted_user_selection;
		}
		
		for(int create_space = 0; create_space < this.space_upper_bound; create_space++) {
			System.out.println("***********************************");
		}
		
		return converted_user_selection;
	}
}
