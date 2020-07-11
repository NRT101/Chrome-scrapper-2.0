package chrome.scrapper.main;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class FavoritesDAO {
	private static List<Favorites> ListOfFavorites = new ArrayList<Favorites>();

	public static List<Favorites> getListOfFavorites() {
		List<Favorites> returnList = new ArrayList<Favorites>();
		returnList.addAll(ListOfFavorites);
		return returnList;
	}


	// This inserts batches of Favorites into the database. Will do it in batches of
	// 30 as defined in the hibernate.cfg.xml
	public static void insertFavorites(List<Favorites> FavoritesList) {
		Session session = DataSource.getSessionFactory().openSession();

		// Creating Transaction Object
		Transaction transObj = session.beginTransaction();
		int counter = 0;
		for (Favorites favorite : FavoritesList) {
			session.save(favorite);
			if (counter % 30 == 0) { // 30, same as the JDBC batch size
				// flush a batch of inserts and release memory:
				session.flush();
				session.clear();
			}
			counter++;
		}
		// Transaction Is Committed To Database
		transObj.commit();

		// Closing The Session Object
		session.close();

		getFavoritesFromDatabase();
	}

// This method removes records from the database
	public static void removeFavorites(List<Favorites> FavoritesList) {
		Session session = DataSource.getSessionFactory().openSession();

		// Creating Transaction Object
		Transaction transObj = session.beginTransaction();
		int counter = 0;
		for (Favorites favorite : FavoritesList) {
			session.delete(favorite);
			if (counter % 30 == 0) { // 30, same as the JDBC batch size
				// flush a batch of inserts and release memory:
				session.flush();
				session.clear();
			}
			counter++;
		}
		// Transaction Is Committed To Database
		transObj.commit();

		// Closing The Session Object
		session.close();

		getFavoritesFromDatabase();
	}

// This method gets favorites data from database. Is also used to set the internal parameter ListOfFavorites
	public static List<Favorites> getFavoritesFromDatabase() {
		Session session = DataSource.getSessionFactory().openSession();
		@SuppressWarnings("unchecked")
		List<Favorites> FavoritesList = session.createQuery("FROM Favorites").list();
		// Closing The Session Object
		session.close();
		// Makes sure FavoritesList matches
		if (!ListOfFavorites.equals(FavoritesList)) {
			ListOfFavorites.clear();
			ListOfFavorites.addAll(FavoritesList);
		}
		return FavoritesList;
	}

	// This method updates Favorites via taking in a list of Favorites and updating them to the database
	// It is good practice to call this method once during an update check to avoid performance issues
	public static void updateFavorites(List<Favorites> FavoritesList) {
		Session session = DataSource.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		int counter = 0;
		for (Favorites favorite : FavoritesList) {
			session.update(favorite);
			if (counter % 30 == 0) { // 30, same as the JDBC batch size
				// flush a batch of inserts and release memory:
				session.flush();
				session.clear();
			}
			counter++;
		}
		transaction.commit();
		// Closing The Session Object
		session.close();
		// updating internal record of Favorites list from database
		getFavoritesFromDatabase();
	}
	// This method compares the two Lists and returns what is in List1 but not in List2 via URL comparison
	public static List<Favorites> favoriteListCompare(List<Favorites> List1, List<Favorites> List2){
		List<Favorites> returnList= new ArrayList<Favorites>();
		boolean found=false;
		int counter=0;
		int sizeOfListTwo= List2.size();
		for(Favorites fav1 :List1) {
			found=false; // reset of found check
			counter=0; // reset of loop
			while(counter<sizeOfListTwo) {
				// end search for List1 node in List2
				if(fav1.getURL().equals(List2.get(counter).getURL())){
					found=true;
					counter=sizeOfListTwo; 
				}
				counter++;
			}
			if(found==false) {
				returnList.add(fav1);
			}
		}
		return returnList;
	}
	
	// This method searches the Favorites List and returns what was updated today
	public static List<Favorites> whatUpdatedToday(List<Favorites> list){
		List<Favorites> returnList=new ArrayList<Favorites>();
		LocalDateTime dateToday=LocalDate.now().atStartOfDay().minusSeconds(1); // gets end of yesterday
		// Stream filter on date
		list.stream().filter(favNode->favNode.getTimeOfLastUpdate().isAfter(dateToday))
		.sorted((f1,f2)->f2.getTimeOfLastUpdate().compareTo(f1.getTimeOfLastUpdate()))
		.map(favNode->returnList.add(favNode)).collect(Collectors.toList());
		return returnList;
	}
}
