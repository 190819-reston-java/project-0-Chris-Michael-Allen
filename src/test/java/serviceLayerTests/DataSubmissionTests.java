package serviceLayerTests;

import static org.junit.Assert.*;

import org.junit.Test;

import com.revature.exception.FundsTooHighException;
import com.revature.exception.InitializedFundsBelowZeroException;
import com.revature.service.RetrievalLayer;

public class DataSubmissionTests {

	// Should successfully test the functionality of various updates
	// To the database
	
	// Valid User Test
		// Should ensure that the code is looking at a valid user in
		// the database
	
	// Create and Retrieve User Test
		// Should instantiate a new user in the database, then
		// retrieve that same user
	

	@Test
	public void createAndRetrieveUserTest() {
		RetrievalLayer test_retrieval_instance = new RetrievalLayer();
		test_retrieval_instance.addUser("customUser", "Jimmy Schmittz");
		test_retrieval_instance.targetUser("customUser");
		assertEquals(test_retrieval_instance.getName(), "Jimmy Schmittz");
	}
	
	// Valid Input Test
		// Should ensure that the data input to the repository
		// is in a valid format
	@Test (expected = NumberFormatException.class)
	public void validInputTest() {
		RetrievalLayer test_retrieval_instance = new RetrievalLayer();
		test_retrieval_instance.addUser("Good Username", "Good Name", "Bad Funds");
	}

	// Valid Funds Minimum Zero Test
		// Should ensure that funds entered below zero return
		// An error to the user
	@Test (expected = InitializedFundsBelowZeroException.class)
		public void validFundsMinimumZeroTest() {
		RetrievalLayer test_retrieval_instance = new RetrievalLayer();
		test_retrieval_instance.addUser("Shouldn't be okay if there's", "Less than Enough Money", "-300");
	}
	
	// Valid Funds Below Maximum Test
		// Should ensure that funds can not be instantiated
		// Above an arbitrary maximum
	@Test (expected = FundsTooHighException.class)
	public void fundsBelowMaximumTest() {
		RetrievalLayer test_retrieval_instance = new RetrievalLayer();
		test_retrieval_instance.addUser("This User Has", "Way too much money", "10000000000");
	}
	
	// Increase Funds Test
		// Should ensure that it is possible for the user
		// To add funds to their account
	@Test
	public void depositMoneyTest() {
		RetrievalLayer test_retrieval_instance = new RetrievalLayer();
		test_retrieval_instance.targetUser("testAccount2");
		test_retrieval_instance.addFunds(200);
		assertEquals(test_retrieval_instance.getFunds(), "300.00");
	}
	
	// Valid Funds Format Test
		// Should ensure that an amount submitted rounds to
		// Two decimal places, regardless of what the user
		// Inserts
	@Test
	public void validFundsFormatTest() {
		RetrievalLayer test_retrieval_instance = new RetrievalLayer();
		test_retrieval_instance.targetUser("testAccountName");
		test_retrieval_instance.addFunds(200.12345678);
		assertEquals(test_retrieval_instance.getFunds(), "455.12");
	}
}
