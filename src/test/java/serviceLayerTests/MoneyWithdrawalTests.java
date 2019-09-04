package serviceLayerTests;

import static org.junit.Assert.*;

import org.junit.Test;

public class MoneyWithdrawalTests {

	// These tests should ensure that the functions that make requests
	// to the repository act appropriately when the user attempts
	// to withdraw money from an account
	
	// Validated Login Test
		// Should ensure that a user who is logged in can make a
		// request to withdraw money
	
	// Invalid Login Test
		// Should ensure that a user who is not logged in can not make a
		// request to withdraw money
	
	// Valid Withdrawal Test
		// Should ensure that an amount entered that is an int will be
		// able to make a request to withdraw money
	
	// Invalid Withdrawal Test
		// Should ensure that any entry that is not a positive float will
		// not be able to make a request to withdraw money

	// No Negative Number Test
		// Should ensure that a request to withdraw a negative amount of
		// money will be rejected
	
	// Zero Withdrawal Test
		// Should ensure that an attempted withdrawal of zero is handled
		// appropriately
	
	// Withdrawal Upper Limit Test
		// Should ensure that an amount of money that is above the Withdrawal
		// limit is rejected
	
	// Withdrawal Lower Limit Test
		// Should ensure that an attempt to circumvent the upper limit
		// through underflow is rejected
	
	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
