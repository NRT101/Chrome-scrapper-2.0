package mainCode;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WebsiteTemplateDAOTest {

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
	void testGetWebsiteTemplate() {
		System.out.println("=====TESTING GetWebsiteTemplate=====");
		List<WebsiteTemplate> testList = WebsiteTemplateDAO.getTemplatesFromDatabase();
		System.out.println("List size: "+testList.size());
		assertEquals(11,testList.size());
	}

}
