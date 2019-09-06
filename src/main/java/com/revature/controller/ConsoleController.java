package com.revature.controller;

import java.util.InputMismatchException;
import java.util.Scanner;

import com.revature.service.RetrievalLayer;

public class ConsoleController {

	private RetrievalLayer service_handler = new RetrievalLayer();

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
			break;
		case 1:
			System.out.println("Deposit funds!");
			break;
		case 2:
			System.out.println("Withdraw funds!");
			break;
		case 3:
			System.out.println("View transaction history!");
			break;
		case 4:
			System.out.println("Save to database!");
			break;
		case 5:
			System.out.println("Log out!");
			break;
		case 6:
			System.out.println("Create account!");
			break;
		case 7:
			System.out.println("Log in!");
			break;
		case 8:
			System.out.println("Save and exit!");
			this.loop_execution = false;
			break;
		}
		
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
		
		if (converted_user_selection < 1) {
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
