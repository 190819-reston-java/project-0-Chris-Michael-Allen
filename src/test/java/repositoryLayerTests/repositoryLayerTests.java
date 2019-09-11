package repositoryLayerTests;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.revature.repository.DatabaseBridge;
import com.revature.service.RetrievalLayer;

public class repositoryLayerTests {

	static RetrievalLayer test_retrieval_instance;
	static DatabaseBridge test_repository_instance;
	
	// Repository Layer Tests
	// Should ensure the accuracy of the functionality of the conversion of data
	// From the Service Layer and Database Layer via proper SQL statement syntax
	
	@Before
	public void setUp(){
		test_retrieval_instance = RetrievalLayer.getRetrievalLayer();
		test_repository_instance = new DatabaseBridge();
	}

	@After
	public void tearDown() {
		test_retrieval_instance.logOut();
		test_repository_instance = null;
	}

	// Valid Query test
	// Ensures that the string built by the query building function returns a valid query
	@Test
	public void validQueryTest() {
		HashMap<String, List<String>> test_information = new HashMap<String, List<String>>();
		List<String> test_user_data = Arrays.asList("testInformation", "actual Name", "324.12", "-8123491");
		List<String> test_user_data_2 = Arrays.asList("testInformation2", "actual Name2", "2324.12", "-8123491");
		
		test_information.put("testInformation", test_user_data);
		test_information.put("testInformation2", test_user_data_2);
		
		test_repository_instance.sendToUserDatabase(test_information);
	}

}
