package mainCode;

import static org.junit.jupiter.api.Assertions.*;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DateParserTest {
	private static List<Favorites> favoriteList;
	@BeforeAll
	static void setUpBeforeClass() throws Exception {WebsiteTemplateDAO.getTemplatesFromDatabase();
	favoriteList = new ArrayList<Favorites>();


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
	void testIsDateValid() {
		System.out.println("=====TESTING IsDateValid=====");
		DateTimeFormatter dF1 = DateTimeFormatter.ofPattern("MMM dd,yyyy - HH:mm a", Locale.ENGLISH);// checks for Month day, year - Hour:Minute AM\PM
		boolean test =DateParser.isDateValid("Apr 19,2020 - 00:11 AM",dF1);
		System.out.println("Excepted result: true");
		System.out.println("Actual result: "+test);
		assertEquals(test,true);
	}

	@Test
	void testDateParse() {
		System.out.println("=====TESTING DateParse=====");
		Date test = DateParser.DateParse("3/9/2018");
		String result=test.toString();
		System.out.println("Expected result: Fri Mar 09 00:00:00 CST 2018");
		System.out.println("Actual result: "+result);
		assertEquals("Fri Mar 09 00:00:00 CST 2018",result);
	}

	@Test
	void testParseFavorites() {
		System.out.println("=====TESTING ParseFavorites=====");
		DateParser.parseFavorites(favoriteList);
		for(Favorites fav:favoriteList) {
			System.out.println("URL: "+fav.getURL());
			System.out.println("Date Updated: "+fav.getTimeOfLastUpdate());
			System.out.println("Date Created: "+fav.getTimeOfEntry());
			System.out.println("Date Update String: "+fav.getUpdateString());
			System.out.println();
		}
		assertEquals("Sat Apr 18 13:00:00 CDT 2020",favoriteList.get(2).getUpdateString());
	}

}
