package repositoryLayerTests;
import com.revature.exception.KeyNotFoundException;
import com.revature.repository.RetrievalLayer;

import static org.junit.Assert.*;

import org.junit.Test;

public class DataRetrievalTests {

	// These tests should validate that data retrieved from the database
	// Is handled correctly
	
	// Known User Test
		// Ensure that credentials for a known user successfully return
	@Test
	public void retrieveKnownUserTest() {
		RetrievalLayer test_retrieval_instance = new RetrievalLayer();
		test_retrieval_instance.targetUser(0);
		assertEquals(test_retrieval_instance.getUserKey(), 0);
	}
	
	// Known Non-Existant User Test
		// Ensure that credentials for a known non-existant user return
		// with negative results
	@Test (expected = KeyNotFoundException.class)
	public void failRetrieveUnknownUserTest() {
		RetrievalLayer test_retrieval_instance = new RetrievalLayer();
		test_retrieval_instance.targetUser(255);
	}
	
	// Retrieve User Data Test
		// Ensure functionality for retrieval of user credentials
	@Test
	public void retrieveUserDataTest() {
		RetrievalLayer test_retrieval_instance = new RetrievalLayer();
		test_retrieval_instance.targetUser(0);
		assertEquals(test_retrieval_instance.getName(), "John Doe");
	}
	
	// Ensure User Privacy Test
		// Ensure that user will accurately receive their own user information
	@Test
	public void retrievePrivateInformationTest() {
		RetrievalLayer test_retrieval_instance = new RetrievalLayer();
		test_retrieval_instance.targetUser(1);
		assertEquals(test_retrieval_instance.getName(), "Jane Doe");
	}
	
	// Retrieve Funds Test
		// Ensure that functionality for retrieving funds is accurate
	
	@Test
	public void retrieveFundsTest() {
		RetrievalLayer test_retrieval_instance = new RetrievalLayer();
		test_retrieval_instance.targetUser(0);
		assertEquals(test_retrieval_instance.getFunds(), "255.00");
	}
}
