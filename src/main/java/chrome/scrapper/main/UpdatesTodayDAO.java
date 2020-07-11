package chrome.scrapper.main;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class UpdatesTodayDAO {
	private static List<UpdatesToday> ListOfUpdates = new ArrayList<UpdatesToday>();
	
	public static List<UpdatesToday> getUpdates() {
		List<UpdatesToday> returnList = new ArrayList<UpdatesToday>();
		returnList.addAll(ListOfUpdates);
		return returnList;
	}
	
	// This method gets favorites data from database. Is also used to set the internal parameter ListOfFavorites
		public static List<UpdatesToday> getUpdatesFromDatabase() {
			Session session = DataSource.getSessionFactory().openSession();
			@SuppressWarnings("unchecked")
			List<UpdatesToday> UpdateList = session.createQuery("FROM UpdatesToday").list();
			// Closing The Session Object
			session.close();
			// Makes sure ListOfUpdates matches
			if (!ListOfUpdates.equals(UpdateList)) {
				ListOfUpdates.clear();
				ListOfUpdates.addAll(UpdateList);
			}
			return UpdateList;
		}

		// Perform update SQL
		public static void updateToday(List<UpdatesToday> UpdateList) {
			Session session = DataSource.getSessionFactory().openSession();
			Transaction transaction = session.beginTransaction();
			int counter = 0;
			for (UpdatesToday update : UpdateList) {
				session.update(update);
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
			// updating internal record
			getUpdatesFromDatabase();
		}
}
