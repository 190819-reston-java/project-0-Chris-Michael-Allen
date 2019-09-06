package serviceLayerTests;

import com.revature.exception.KeyNotFoundException;
import com.revature.exception.NoUserTargetedException;
import com.revature.service.RetrievalLayer;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DataRetrievalTests {
	
	static RetrievalLayer test_retrieval_instance;
	
	@Before
	public void setUp() {
		test_retrieval_instance = new RetrievalLayer();
	}
	
	@After
	public void tearDown() {
		test_retrieval_instance = null;
	}

	// These tests should validate that data retrieved from the database
	// Is handled correctly

	// Known User Test
	// Ensure that credentials for a known user successfully return

	@Test
	public void retrieveKnownUserTest() {
		test_retrieval_instance.targetUser("testAccountName");
		assertEquals(test_retrieval_instance.getName(), "John Doe");
	}

	// Known Non-Existant User Test
	// Ensure that credentials for a known non-existant user return
	// with negative results
	@Test(expected = KeyNotFoundException.class)
	public void failRetrieveUnknownUserTest() {
		test_retrieval_instance.targetUser("Does Not Exist");
	}

	// Ensure User Privacy Test
	// Ensure that user will accurately receive their own user information
	@Test
	public void retrievePrivateInformationTest() {
		test_retrieval_instance.targetUser("testAccount2");
		assertEquals(test_retrieval_instance.getName(), "Jane Doe");
	}

	// Retrieve Funds Test
	// Ensure that functionality for retrieving funds is accurate

	@Test
	public void retrieveFundsTest() {
		test_retrieval_instance.targetUser("testAccountName");
		assertEquals(test_retrieval_instance.getFunds(), "255.00");
	}

	// Invalid Login Tests
	// Ensures that an error will be thrown if no user is currently being looked at

	@Test(expected = NoUserTargetedException.class)
	public void invalidFundRetrievalTest() {
		test_retrieval_instance.addFunds(500);
	}

	@Test(expected = NoUserTargetedException.class)
	public void invalidUserNameRetrievalTest() {
		assertNotEquals(test_retrieval_instance.getUserName(), "This should never run");
	}

	@Test(expected = NoUserTargetedException.class)
	public void invalidNameRetrievalTest() {
		assertNotEquals(test_retrieval_instance.getName(), "This should never run");
	}
}