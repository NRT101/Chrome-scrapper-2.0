package mainCode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/*
 * Things to do:
 * Create a URL change functionality
 * email
 */

public class Main {
	private static List<Favorites> favoriteList;

	/*
	 * Function that used to send email via SMTP
	 */
	public static void sendEmail() {
		// generates email setup and checks if email needs to be sent
		List<Favorites> updatedToday = FavoritesDAO.whatUpdatedToday(favoriteList);
		UpdatesTodayDAO.getUpdatesFromDatabase();
		StringBuilder email = new StringBuilder();
		UpdatesToday fromDB = UpdatesTodayDAO.getUpdates().get(0);
		String dbList=fromDB.getList();
		StringBuilder updatesTodayValue = new StringBuilder();
		boolean newUpdate= false;
		email.append("<table><thead><tr><th>Name</th><th>Last Updated</th></tr></thead><tbody>");

		for (Favorites fav : updatedToday) {
			if(!dbList.contains(";"+fav.getInternalId().toString() +";")){
					newUpdate=true;
			}
			updatesTodayValue.append(";"+fav.getInternalId() + ";");
			email.append("<tr><td><a target=\"_blank\" href=\"" + fav.getURL() + "\">" + fav.getName() + "</a></td><td>"
					+ fav.getUpdateString() + "</td></tr>");
		}
		email.append("</tbody></table>");
		String updatesToday = updatesTodayValue.toString();
		Date dateToday = new Date();
		// Checks that the update list is different than what is recorded and this
		// update was after the last one
		if (newUpdate && dateToday.after(fromDB.getTimeOfLastUpdate())) {
			// update the database
			fromDB.setList(updatesToday);
			fromDB.setTimeOfLastUpdate(dateToday);
			List<UpdatesToday> updateList= new ArrayList<UpdatesToday>();
			updateList.add(fromDB);
			UpdatesTodayDAO.updateToday(updateList);
			
			// complete email setup and send the email
			String htmlBody = "<strong>This is an HTML Message</strong>";

			final String username = ParametersTableDAO.returnValue("EMAIL_CLIENT_USERNAME", "WEBSITE_UPDATE");
			final String password = ParametersTableDAO.returnValue("EMAIL_CLIENT_PASSWORD", "WEBSITE_UPDATE");
			Properties prop = new Properties();
			prop.put("mail.smtp.auth", ParametersTableDAO.returnValue("AUTHENTICATE", "WEBSITE_UPDATE"));
			prop.put("mail.smtp.host", ParametersTableDAO.returnValue("EMAIL_HOST", "WEBSITE_UPDATE"));
			prop.put("mail.smtp.port", ParametersTableDAO.returnValue("EMAIL_CLIENT_PORT", "WEBSITE_UPDATE"));
			prop.put("mail.smtp.starttls.enable", ParametersTableDAO.returnValue("STARTTLSENABLE", "WEBSITE_UPDATE"));
			try {
				// Connects to server
				Session session = Session.getDefaultInstance(prop, new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				});

				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(username));
				message.setRecipients(Message.RecipientType.TO,
						InternetAddress.parse(ParametersTableDAO.returnValue("EMAIL_TARGET", "WEBSITE_UPDATE")));
				message.setSubject(ParametersTableDAO.returnValue("EMAIL_SUBJECT", "WEBSITE_UPDATE"));
				MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
				mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
				mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
				mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
				mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
				mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
				CommandMap.setDefaultCommandMap(mc);
				message.setText(htmlBody);
				message.setContent(email.toString(), "text/html");
				Transport.send(message);
				System.out.println("sent email");
			} catch (MessagingException e) {
				System.out.println("Email failed to send");
				e.printStackTrace();
			} catch (Exception e) {
				System.out.println("All other email failures");
				e.printStackTrace();
			}
		}
	}

	/*
	 * Handles the multi-threading
	 */
	public static void threadScrap() {
		int threadCount = Integer.parseInt(ParametersTableDAO.returnValue("THREAD_COUNTER", "WEBSITE_UPDATE"));
		int scrapTimeout=Integer.parseInt(ParametersTableDAO.returnValue("TIMEOUT_PERIOD", "WEBSITE_UPDATE"));
		String userAgent = ParametersTableDAO.returnValue("USER_AGENT", "WEBSITE_UPDATE");
		ExecutorService threadPool = Executors.newFixedThreadPool(threadCount);
		int size = favoriteList.size();
		int startPoint = 0;
		int partition = size / threadCount;
		int endPoint = partition;
		int endLoop = size - partition - 1; // stop point
		List<WebsiteTemplate> templates = WebsiteTemplateDAO.getTemplateList();
		while (endPoint < endLoop) {
			Runnable threadScrap = new webScrapper(scrapTimeout, startPoint, endPoint, userAgent, false, favoriteList,
					templates);
			threadPool.execute(threadScrap);
			startPoint = startPoint + partition;
			endPoint = endPoint + partition;
		}
		Runnable threadScrap = new webScrapper(scrapTimeout, startPoint, size - 1, userAgent, false, favoriteList, templates);
		threadPool.execute(threadScrap);
		threadPool.shutdown();
		// goes through and checks every thread. Waits until all have done executing
		while (!threadPool.isTerminated()) {
			// waiting
		}
	}

	public static void main(String[] args) {
		// initial set up
		java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.SEVERE); // controls the logging level
		ParametersTableDAO.getParametersFromDatabase();
		String runFavorite = ParametersTableDAO.returnValue("FAVORITES", "MAIN");
		int loopWait = 600000; // default 10 minutes(60*1000*10)
		Date date;
		while (runFavorite.equals("true")) {
			date = new Date();
			System.out.println("Start time: " + date.toString());
			// loads parameters
			List<Favorites> databaseList = FavoritesDAO.getFavoritesFromDatabase();
			WebsiteTemplateDAO.getTemplatesFromDatabase();

			// steps to parse the json file
			String filePath = ParametersTableDAO.returnValue("CHROME_BOOKMARK_LOCATION", "WEBSITE_UPDATE");
			String urlChange = ParametersTableDAO.returnValue("URL_CHANGE", "WEBSITE_UPDATE");
			String startPointJson = ParametersTableDAO.returnValue("JSON_START_POINT", "WEBSITE_UPDATE");
			String searchForList = ParametersTableDAO.returnValue("SEARCH_FOLDERS_AND_METHOD", "WEBSITE_UPDATE");
			JsonParser.readInFile(filePath, searchForList, startPointJson, "name", "url",urlChange);

			// checking what needs to be inserted, deleted, and updated
			favoriteList = JsonParser.getReturnList(); // gets the current list of favorites from the JSON file
			// removing favorites from database that are no longer listed in json file
			List<Favorites> checkFavorites = FavoritesDAO.favoriteListCompare(databaseList, favoriteList);
			if (checkFavorites.size() > 0)
				FavoritesDAO.removeFavorites(checkFavorites);
			// adding favorites from json file that were not in the database
			checkFavorites = FavoritesDAO.favoriteListCompare(favoriteList, databaseList);
			if (checkFavorites.size() > 0)
				FavoritesDAO.insertFavorites(checkFavorites);
			favoriteList = FavoritesDAO.getFavoritesFromDatabase(); // refresh the internal parameter
			// performing update checks
			threadScrap();
			DateParser.parseFavorites(favoriteList);
			FavoritesDAO.updateFavorites(favoriteList);
			sendEmail();
			// end point
			date = new Date();
			System.out.println("End time: " + date.toString());
			loopWait = Integer.parseInt(ParametersTableDAO.returnValue("LOOP_WAIT", "WEBSITE_UPDATE")); // gets how long
																										// to wait in
																										// milliseconds
			try {
				Thread.sleep(loopWait);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(0);
			}
			ParametersTableDAO.getParametersFromDatabase();
		}
		System.exit(0);
	}

}
