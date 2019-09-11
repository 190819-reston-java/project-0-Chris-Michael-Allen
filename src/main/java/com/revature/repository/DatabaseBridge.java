package com.revature.repository;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.revature.service.RetrievalLayer;

public class DatabaseBridge {

	private List<List<String>> transaction_history_data = new ArrayList<List<String>>();

	public void sendToUserDatabase(HashMap<String, List<String>> data_to_send) {
		Statement statement = null;
		ResultSet results = null;
		Connection conn = null;

		for (List<String> user_data : data_to_send.values()) {
			String query_string;

			// Check if there is already an entry in the database for the username

			query_string = "SELECT * FROM users WHERE users.user_name = 'entry_username';";
			query_string = query_string.replaceAll("entry_username", user_data.get(0));

			try {
				conn = ConnectionUtil.getConnection();

				statement = conn.createStatement();
				results = statement.executeQuery(query_string);

				if (results.next()) {
					query_string = this.updateTableEntry(user_data);
				} else {
					query_string = this.insertTableEntry(user_data);
				}

				statement.executeUpdate(query_string);
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					results.close();
					statement.close();
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		}

		insertToTransactionHistoryTable();
	}

	public List<List<String>> retrieveFromTransactionHistory(String user_key) {
		
		this.insertToTransactionHistoryTable();
		
		List<List<String>> transaction_history_for_a_user = new ArrayList<List<String>>();
		Statement statement = null;
		ResultSet results = null;
		Connection conn = null;
		
		String retrieval_query = "SELECT prior_amount, transfer_amount, new_amount, transaction_time "
				+ "FROM transaction_history "
				+ "WHERE reference_user = 'user_key' "
				+ "ORDER BY transaction_time DESC;";
				retrieval_query.replaceAll("user_key", user_key);
		
				try {
					conn = ConnectionUtil.getConnection();
					statement = conn.createStatement();
					results = statement.executeQuery(retrieval_query);
					
					String prior_amount;
					String transfer_amount;
					String new_amount;
					String transaction_time;
					List<String> data_array;
					
					while(results.next()) {
						prior_amount = results.getString("prior_amount");
						transfer_amount = results.getString("transfer_amount");
						new_amount = results.getString("new_amount");
						transaction_time = results.getString("transaction_time");
						
						data_array = Arrays.asList(prior_amount, transfer_amount, new_amount, transaction_time);
						transaction_history_for_a_user.add(data_array);
					}
				}
				catch (SQLException e) {
					e.printStackTrace();
				}
				finally {
					try {
						results.close();
						statement.close();
						conn.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				
		return transaction_history_for_a_user;
	}
	
	public HashMap<String, List<String>> retrieveFromDatabase() {

		HashMap<String, List<String>> data_to_retrieve = new HashMap<String, List<String>>();
		Statement statement = null;
		ResultSet results = null;
		Connection conn = null;
		String retrieval_query = "SELECT * FROM users";

		try {
			conn = ConnectionUtil.getConnection();

			statement = conn.createStatement();

			results = statement.executeQuery(retrieval_query);
			String this_username;
			String this_name;
			String this_funds;
			String this_password;
			List<String> data_array;

			while (results.next()) {
				this_username = results.getString("user_name");
				this_name = results.getString("name");
				this_funds = results.getString("funds");
				this_password = results.getString("user_password");

				data_array = Arrays.asList(this_username, this_name, this_funds, this_password);
				data_to_retrieve.put(this_username, data_array);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				results.close();
				statement.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return data_to_retrieve;
	}

	private String insertTableEntry(List<String> entry_data) {
		String query_string;

		// For a user that does not exist in the database
		query_string = "INSERT INTO users " + "(user_name, name, funds, user_password) " + "VALUES "
				+ "('entry_username', 'entry_name', 'entry_funds', 'entry_password');";
		query_string = query_string.replaceAll("entry_username", entry_data.get(0));
		query_string = query_string.replaceAll("entry_name", entry_data.get(1));
		query_string = query_string.replaceAll("entry_funds", entry_data.get(2));
		query_string = query_string.replaceAll("entry_password", entry_data.get(3));

		return query_string;
	}

	private String updateTableEntry(List<String> entry_data) {

		String query_string;
		// For a user that already is in the database
		query_string = "UPDATE users " + "SET user_name = 'entry_username', " + "name = 'entry_name', "
				+ "funds = 'entry_funds', " + "user_password = 'entry_password' "
				+ "WHERE user_name = 'entry_username';";
		query_string = query_string.replaceAll("entry_username", entry_data.get(0));
		query_string = query_string.replaceAll("entry_name", entry_data.get(1));
		query_string = query_string.replaceAll("entry_funds", entry_data.get(2));
		query_string = query_string.replaceAll("entry_password", entry_data.get(3));

		return query_string;
	}

	public void addToTransactionHistory(List<String> transaction_history_data_entry) {
		this.transaction_history_data.add(transaction_history_data_entry);
	}

	private void insertToTransactionHistoryTable() {
		String entry_query;
		Statement statement = null;
		Connection conn = null;

		for (List<String> update_item : this.transaction_history_data) {
			entry_query = "INSERT INTO transaction_history (reference_user, prior_amount, transfer_amount, new_amount, transaction_time) "
					+ "VALUES ('entry_user_name', 'entry_prior_amount', 'entry_transfer_amount', 'entry_new_amount', 'entry_timestamp');";

			entry_query = entry_query.replaceAll("entry_user_name", update_item.get(0));
			entry_query = entry_query.replaceAll("entry_prior_amount", update_item.get(1));
			entry_query = entry_query.replaceAll("entry_transfer_amount", update_item.get(2));
			entry_query = entry_query.replaceAll("entry_new_amount", update_item.get(3));
			entry_query = entry_query.replaceAll("entry_timestamp", update_item.get(4));

			try {
				conn = ConnectionUtil.getConnection();

				statement = conn.createStatement();

				statement.executeUpdate(entry_query);
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					statement.close();
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		this.transaction_history_data.clear();
	}
}
