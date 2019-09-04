package com.revature.repository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.revature.exception.KeyNotFoundException;

public class RetrievalLayer {

	private int user_key;
	private String user_name;
	private String user_funds;
	private HashMap<Integer, List<String>> userData = new HashMap<Integer, List<String>>();

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
		this.userData.put(0, Arrays.asList("John Doe", "255"));
		this.userData.put(1, Arrays.asList("Jane Doe", "100"));
	}

	public void targetUser(int index) throws KeyNotFoundException {
		if(!this.userData.containsKey(index)) {
			throw new KeyNotFoundException();
		}
		
		this.setName(this.userData.get(index).get(0));
		this.setFunds(this.userData.get(index).get(1));
		
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
}
