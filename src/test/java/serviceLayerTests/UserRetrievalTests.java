package serviceLayerTests;

import static org.junit.Assert.*;

import org.junit.Test;

public class UserRetrievalTests {

	// These tests should ensure that a request to the repository
	// for user information functions appropriately
	
	// Known User Test
		// Should ensure that a user that is known to exist within
		// the database will return correct information
	
	// Known Non-Existant User Test
		// Should ensure that a user that is known not to exist within
		// the database will not return with incorrect information
	
	// No Duplicate Users Test
		// Should ensure that a user that is already logged in will
		// not be able to retrieve information from another user in the
		// database
	
	// Logout Test
		// Should ensure that, when the user logs out, information about
		// the user can not be accessed
	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
