package com.revature.controller;

public class ConsoleController {

	public ConsoleController() {
		this.runMenu();
	}
	
	private void runMenu() {
		System.out.println("*******************************************************\n"
						 + "*******************************************************\n"
						 + "****          WELCOME TO THE BANKING APP!          ****\n"
						 + "****                                               ****\n"
						 + "*******************************************************\n"
						 + "****                                               ****\n"
						 + "**** Please enter a number to execute that option  ****\n"
						 + "*******************************************************\n");
		
		int menuOption = 0;
		for(String functionToPrint : userFunctionArray) {
			menuOption++;
			System.out.println("**** " + menuOption + ". " + functionToPrint);
		}
		
		
	}
}
