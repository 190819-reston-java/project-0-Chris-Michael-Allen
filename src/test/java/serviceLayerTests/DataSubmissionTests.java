package serviceLayerTests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.revature.exception.AccountOverdrawnException;
import com.revature.exception.DuplicateUserException;
import com.revature.exception.FundsTooHighException;
import com.revature.exception.InitializedFundsBelowZeroException;
import com.revature.exception.NoUserTargetedException;
import com.revature.service.RetrievalLayer;

public class DataSubmissionTests {

	// Should successfully test the functionality of various updates
	// To the database

	static RetrievalLayer test_retrieval_instance;
	
	@Before
	public void setUp() {
		test_retrieval_instance = new RetrievalLayer();
	}
	
	@After
	public void tearDown() {
		test_retrieval_instance = null;
	}

	// Valid User Test
	// Should ensure that the code is looking at a valid user in
	// the database

	// Create and Retrieve User Test
	// Should instantiate a new user in the database, then
	// retrieve that same user

	@Test
	public void createAndRetrieveUserTest() {
		test_retrieval_instance.addUser("customUser", "Jimmy Schmittz");
		test_retrieval_instance.targetUser("customUser");
		assertEquals(test_retrieval_instance.getName(), "Jimmy Schmittz");
	}

	// No Duplicate Users Test
	// Should result in an error message if the user attempts to
	// Create a duplicate user
	@Test (expected = DuplicateUserException.class)
	public void noDuplicateUsersTest() {
		test_retrieval_instance.addUser("testAccountName", "This account already exists");
	}
	
	// Valid Input Test
	// Should ensure that the data input to the repository
	// is in a valid format
	@Test(expected = NumberFormatException.class)
	public void validInputTest() {
		test_retrieval_instance.addUser("Good Username", "Good Name", "Bad Funds");
	}

	// Valid Funds Minimum Zero Test
	// Should ensure that funds entered below zero return
	// An error to the user
	@Test(expected = InitializedFundsBelowZeroException.class)
	public void validFundsMinimumZeroTest() {
		test_retrieval_instance.addUser("Shouldn't be okay if there's", "Less than Enough Money", "-300");
	}

	// Valid Funds Below Maximum Test
	// Should ensure that funds can not be instantiated
	// Above an arbitrary maximum
	@Test(expected = FundsTooHighException.class)
	public void fundsBelowMaximumTest() {
		test_retrieval_instance.addUser("This User Has", "Way too much money", "10000000000");
	}

	// Increase Funds Test
	// Should ensure that it is possible for the user
	// To add funds to their account
	@Test
	public void depositMoneyTest() {
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
		test_retrieval_instance.targetUser("testAccountName");
		test_retrieval_instance.addFunds(200.12345678);
		assertEquals(test_retrieval_instance.getFunds(), "455.12");
	}
	
	// Withdraw Funds Test
	// Should ensure the accuracy of the withdrawal function
	
	@Test
	public void withdrawalFunctionalityTest() {
		test_retrieval_instance.targetUser("testAccountName");
		test_retrieval_instance.withdrawFunds(255);
		assertEquals(test_retrieval_instance.getFunds(), "0.00");
	}
	
	// Valid Withdrawal Request Test
	// Should ensure that a user can only withdraw if they are logged in
	@Test (expected = NoUserTargetedException.class)
	public void validWithdrawalConditionsTest() {
		test_retrieval_instance.withdrawFunds(255);
	}
	
	// No Overdraw Test
	// Should ensure that a user can not withdraw more money than is in their account
	@Test (expected = AccountOverdrawnException.class)
	public void noOverdrawAllowedTest() {
		test_retrieval_instance.targetUser("testAccountName");
		test_retrieval_instance.withdrawFunds(1000);
	}
}