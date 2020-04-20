package mainCode;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UpdatesTodayDAOTest {
	public static List<UpdatesToday> backup;
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		backup= UpdatesTodayDAO.getUpdatesFromDatabase();
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
		System.out.println();
	}

	@Test
	void testGetUpdatesFromDatabase() {
		System.out.println("=====TESTING GetUpdatesFromDatabase=====");
		List<UpdatesToday> testCase= UpdatesTodayDAO.getUpdates();
		System.out.println("Size of test case: "+testCase.size());
		System.out.println("Size of backup: "+backup.size());
		assertEquals(testCase.size(),backup.size());
	}

	@Test
	void testUpdateToday() {
		
		List<UpdatesToday> test = new ArrayList<UpdatesToday>();
		test.addAll( UpdatesTodayDAO.getUpdatesFromDatabase());
		UpdatesToday testCase=test.get(0);
		String original=testCase.getList();
		testCase.setList("This is a test");
		test.clear();
		test.add(testCase);
		UpdatesTodayDAO.updateToday(test);
		test = UpdatesTodayDAO.getUpdatesFromDatabase();
		String result=test.get(0).getList();
		System.out.println("=====TESTING UpdateToday=====");
		System.out.println("The original data set: "+original);
		System.out.println("The result set: "+result);
		assertNotEquals(result,original);
		UpdatesTodayDAO.updateToday(backup);
		
	}

}
