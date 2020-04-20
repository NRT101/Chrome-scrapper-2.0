package mainCode;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonParserTest {

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
	void testReadInFile() {
		System.out.println("=====TESTING ReadInFile=====");
		JsonParser.readInFile("src/test/resources/jsonExample","","roots->bookmark_bar->children","name","url","");
		int size =JsonParser.getReturnList().size();
		for(Favorites f: JsonParser.getReturnList()) {
			System.out.println("Name: "+f.getName());
			System.out.println("URL: "+f.getURL());
			System.out.println("Directory: "+f.getDirectory());
		}
		System.out.println("Expected size: 15; Actual size: "+size);
		assertEquals(15,size);
	}

	
	@Test
	void testRetrieveData() {
		System.out.println("=====TESTING RetrieveData=====");
		String testData = " ";
		Object object=null;
		JSONArray arrayObj=null;
		JSONParser jsonParser=new JSONParser();
		try {
			object=jsonParser.parse(testData);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		arrayObj=(JSONArray) object;
		JsonParser.setReturnList(new ArrayList<Favorites>());
		JsonParser.retrieveData(arrayObj, "","children","name","url","");
		List<Favorites> test= JsonParser.getReturnList();
		int size = test.size();
		System.out.println("Expected size: 65; Actual size: "+size);
		assertEquals(65,size);
	}

	@Test
	void testfindJsonArray() {
		System.out.println("=====TESTING findJsonArray=====");
		String testData = " [ ";
		Object object=null;
		JSONArray arrayObj=null;
		JSONParser jsonParser=new JSONParser();
		try {
			object=jsonParser.parse(testData);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		arrayObj=(JSONArray) object;
		JsonParser.findJsonArray(arrayObj,"Possible Fanfiction","children","name");
		JSONArray test= JsonParser.getFoundArray();
		int size = test.size();
		System.out.println("Expected size: 18; Actual size: "+size);
		assertEquals(18,size);
	}

}
