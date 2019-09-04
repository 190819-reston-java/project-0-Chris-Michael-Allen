package serviceLayerTests;

import static org.junit.Assert.*;

import org.junit.Test;

public class MoneyDepositTests {

	// These tests should ensure that the functions that make requests
	// to the repository act appropriately when the user attempts
	// to deposit money to an account
	
	// Validated Login Test
		// Should ensure that a user who is logged in can make a
		// request to deposit money
	
	// Invalid Login Test
		// Should ensure that a user who is not logged in can not make a
		// request to deposit money
	
	// Valid Deposit Test
		// Should ensure that an amount entered that is an int will be
		// able to make a request to deposit money
	
	// Invalid Deposit Test
		// Should ensure that any entry that is not a positive float will
		// not be able to make a request to deposit money

	// No Negative Number Test
		// Should ensure that a request to deposit a negative amount of
		// money will be rejected
	
	// Zero Deposit Test
		// Should ensure that an attempted deposit of zero is handled
		// appropriately
	
	// Deposit Upper Limit Test
		// Should ensure that an amount of money that is above the deposit
		// limit is rejected
	
	// Deposit Lower Limit Test
		// Should ensure that an attempt to circumvent the upper limit
		// through underflow is rejected
	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
