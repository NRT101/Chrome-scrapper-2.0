package mainCode;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FavoritesDAOTest {
		public static List<Favorites> backup;
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		backup= FavoritesDAO.getFavoritesFromDatabase();
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.SEVERE); // controls the logging level
		
	}

	@AfterEach
	void tearDown() throws Exception {
		
		System.out.println();
	}

	@Test
	void testGetListOfFavorites() {
		System.out.println("=====TESTING GetListOfFavorites=====");
		List<Favorites> testCase= FavoritesDAO.getListOfFavorites();
		System.out.println("Size of test case: "+testCase.size());
		System.out.println("Size of backup: "+backup.size());
		assertEquals(testCase.size(),backup.size());
	}

	@Test
	void testInsertFavorites() {
		System.out.println("=====TESTING InsertFavorites=====");
		List<Favorites> controlCase= FavoritesDAO.getFavoritesFromDatabase();
		int controlSize=controlCase.size();
		System.out.println("Size of control case: "+controlSize);
		List<Favorites> testBin= new ArrayList<Favorites>();
		Favorites testCase1 = new Favorites();
		testCase1.setName("test 1");
		Favorites testCase2 = new Favorites();
		testCase2.setName("test 2");
		Favorites testCase3 = new Favorites();
		testCase3.setName("test 3");
		testBin.add(testCase3);
		testBin.add(testCase2);
		testBin.add(testCase1);
		FavoritesDAO.insertFavorites(testBin);
		List<Favorites> testResult=FavoritesDAO.getFavoritesFromDatabase();
		System.out.println("Size of test case after addition: "+testResult.size());
		assertNotEquals(testResult.size(),controlCase.size());
		FavoritesDAO.removeFavorites(testBin); // cleanup
	}

	@Test
	void testRemoveFavorites() {
		System.out.println("=====TESTING RemoveFavorites=====");
		List<Favorites> testBin= new ArrayList<Favorites>();
		Favorites testCase1 = new Favorites();
		testCase1.setName("test 1");
		Favorites testCase2 = new Favorites();
		testCase2.setName("test 2");
		Favorites testCase3 = new Favorites();
		testCase3.setName("test 3");
		testBin.add(testCase3);
		testBin.add(testCase2);
		testBin.add(testCase1);
		FavoritesDAO.insertFavorites(testBin);
		List<Favorites> testInsert=FavoritesDAO.getFavoritesFromDatabase();
		System.out.println("Size of test case after addition: "+testInsert.size());
		FavoritesDAO.removeFavorites(testBin);
		List<Favorites> testResult =FavoritesDAO.getFavoritesFromDatabase();
		System.out.println("Size of result case after delete: "+testResult.size());
		assertNotEquals(testInsert.size(),testResult.size());
	}

	@Test
	void testGetFavoritesFromDatabase() {
		System.out.println("=====TESTING GetFavoritesFromDatabase=====");
		List<Favorites> testResult =FavoritesDAO.getFavoritesFromDatabase();
		System.out.println("Size of testResult: "+testResult.size());
		System.out.println("Size of backup: "+backup.size());
		assertEquals(backup.size(),testResult.size());
	}

	@Test
	void testUpdateFavorites() {
		System.out.println("=====TESTING UpdateFavorites=====");
		// creates and add test case
		Favorites testCase1 = new Favorites();
		testCase1.setName("test 1");
		List<Favorites> testBin= new ArrayList<Favorites>();
		testBin.add(testCase1);
		FavoritesDAO.insertFavorites(testBin);
		
		// modifying test case
		testCase1.setName("Ice Cream");
		testBin.clear();
		testBin.add(testCase1);
		FavoritesDAO.updateFavorites(testBin);
		
		//checking test
		List<Favorites> testInsert=FavoritesDAO.getFavoritesFromDatabase();
		Favorites testResult=testInsert.get(testInsert.size()-1);
		System.out.println("Test Case is Name: Ice Cream");
		String name= testResult.getName();
		System.out.println("Result is: "+ name);
		
		// cleanup
		FavoritesDAO.removeFavorites(testBin); 
		
		
		assertEquals("Ice Cream",name);
		
	}
	@Test
	void testFavoriteListCompare() {
		System.out.println("=====TESTING FavoriteListCompare=====");
		List<Favorites> list1= new ArrayList<Favorites>();
		Favorites fav = new Favorites();
		fav.setURL("test1");
		list1.add(fav);
		fav = new Favorites();
		fav.setURL("test2");
		list1.add(fav);
		fav = new Favorites();
		fav.setURL("test3");
		list1.add(fav);
		fav = new Favorites();
		fav.setURL("test4");
		list1.add(fav);
		List<Favorites> list2= new ArrayList<Favorites>();
		fav = new Favorites();
		fav.setURL("test5");
		list2.add(fav);
		fav = new Favorites();
		fav.setURL("test1");
		list2.add(fav);
		fav = new Favorites();
		fav.setURL("test3");
		list2.add(fav);
		fav = new Favorites();
		fav.setURL("test4");
		list2.add(fav);
		List<Favorites>result = FavoritesDAO.favoriteListCompare(list1, list2);
		int resultSize=result.size();
		System.out.println("Test Case expected size: 1");
		System.out.println("Test Case actual size: "+resultSize);
		System.out.println("Result list:");
		for(Favorites f: result) {
			System.out.println(f.getURL());
		}
		assertEquals(1,resultSize);
	}
	
	@SuppressWarnings("deprecation")
	@Test
	void testWhatUpdatedToday() {
		System.out.println("=====TESTING WhatUpdatedToday=====");
		List<Favorites> list= new ArrayList<Favorites>();
		Favorites fav = new Favorites();
		fav.setTimeOfLastUpdate(new Date());
		list.add(fav);
		fav = new Favorites();
		fav.setTimeOfLastUpdate(new Date());
		list.add(fav);
		Date yesterday= new Date();
		yesterday.setDate(yesterday.getDay()-1);
		fav = new Favorites();
		fav.setTimeOfLastUpdate(new Date());
		list.add(fav);
		fav = new Favorites();
		fav.setTimeOfLastUpdate(yesterday);
		list.add(fav);
		List<Favorites> result= FavoritesDAO.whatUpdatedToday(list);
		int resultSize=result.size();
		System.out.println("Test Case expected size: 3");
		System.out.println("Test Case actual size: "+resultSize);
		System.out.println("Result list:");
		for(Favorites f: result) {
			System.out.println(f.getTimeOfLastUpdate());
		}
		assertEquals(3,resultSize);
	}
	
}
