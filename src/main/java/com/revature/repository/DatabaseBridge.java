package com.revature.repository;

import java.util.HashMap;
import java.util.List;

import com.revature.service.RetrievalLayer;

public class DatabaseBridge {

	private RetrievalLayer retrieval_layer_singleton = RetrievalLayer.getRetrievalLayer();
	private HashMap<String, List<String>> current_data_instance;
	
	
	public static void main(String[] args) {
		ConnectionUtil.getConnection();
	}
	
	public void sendToUserDatabase(HashMap<String, List<String>> data_to_send) {

		for(List<String> user_data : data_to_send.values()) {
			String query_string;
			
			// Check if there is already an entry in the database for the username
			
			query_string = "SELECT * FROM users WHERE users.username = entry_username;";
			query_string = query_string.replaceAll("entry_username", user_data.get(0));
			
			// Enter the query into the database, and switch on if the data is null to either
			// add or update the data within the table		
			
			//query_string = insertTableEntry(user_data);
			//query_string = updateTableEntry(user_data);
			
		}
		
	}
	
	public HashMap<String, List<String>> retrieveFromDatabase() {
		HashMap<String, List<String>> data_to_retrieve = new HashMap<String, List<String>>();
		ConnectionUtil.getConnection();
		
		// Retrieve all data in the table as a query
		
		String retrieval_query = "SELECT * FROM users";
		
		// Iterate through the array for each user name, and parse the data into the map.
		// Once the map has been completed, return the map
		
		return data_to_retrieve;
	}
	
	private String insertTableEntry(List<String> entry_data) {
		String query_string;
		
		// For a user that does not exist in the database
		query_string = "INSERT INTO users "
						+ "(user_name, name, funds, password) "
					+ "VALUES "
						+ "(entry_username, entry_name, entry_funds, entry_password);";
		query_string = query_string.replaceAll("entry_username", entry_data.get(0));
		query_string = query_string.replaceAll("entry_name", entry_data.get(1));
		query_string = query_string.replaceAll("entry_funds", entry_data.get(2));
		query_string = query_string.replaceAll("entry_password", entry_data.get(3));

		return query_string;
	}
	
	private String updateTableEntry(List<String> entry_data) {
		
		String query_string;
		// For a user that already is in the database
		query_string = "UPDATE users "
				+ 		"SET user_name = entry_username, "
				+		 	"name = entry_name, "
				+			"funds = entry_funds, "
				+			"password = entry_password "
				+		"WHERE user_name = entry_username;";
		query_string = query_string.replaceAll("entry_username", entry_data.get(0));
		query_string = query_string.replaceAll("entry_name", entry_data.get(1));
		query_string = query_string.replaceAll("entry_funds", entry_data.get(2));
		query_string = query_string.replaceAll("entry_password", entry_data.get(3));	
		
		return query_string;
	}
}
