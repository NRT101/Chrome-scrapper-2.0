package mainCode;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ParametersTableDAOTest {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testGetParametersFromDatabase() {
		System.out.println("=====TESTING GetParametersFromDatabase=====");
		ParametersTableDAO.getParametersFromDatabase();
		String test = ParametersTableDAO.returnValue("CHROME_BOOKMARK_LOCATION","WEBSITE_UPDATE");
		String compareCase="";
		System.out.println("Test string: " +test);
		assertEquals(test,compareCase);
	}
	@Test
	void testReturnValue() {
		System.out.println("=====TESTING ReturnValue=====");
		List<ParametersTable> testList = ParametersTableDAO.getParametersFromDatabase();
		System.out.println("List size: "+testList.size());
		assertEquals(21,testList.size());
	}
}
