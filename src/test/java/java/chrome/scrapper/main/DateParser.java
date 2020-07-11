package chrome.scrapper.main;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

/*
 * This class is to parse dates in scrappedUpdate and scrappedCreate
 */
public class DateParser {

	/*
	 * A Date time parser that validates based of inputed format
	 */
	public static boolean isDateValid(String dateStr, DateTimeFormatter format) {
		try {
			LocalDate.parse(dateStr, format);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static LocalDateTime DateParse(String dateString) {
		LocalDateTime dateTime = LocalDateTime.now(); // will be used for date storage. Will be converted to String and put into
		// result
		try {
			dateString=dateString.trim();
			// This portion is handling hard coded date formats
			String currentYear= Integer.toString(LocalDate.now().getYear());
			DateTimeFormatter dF1 = DateTimeFormatter.ofPattern("MMM dd,yyyy - HH:mm a", Locale.ENGLISH);// checks for Month
			// day, year -
			// Hour:Minute
			// AM\PM
			DateTimeFormatter dF2 = DateTimeFormatter.ofPattern("M-d", Locale.ENGLISH);// checks for Month-day
			DateTimeFormatter dF3 = DateTimeFormatter.ofPattern("yyyy-M-d", Locale.ENGLISH);// checks for year-month-day
			DateTimeFormatter dF4 = DateTimeFormatter.ofPattern("M/d/yyyy", Locale.ENGLISH);// checks for month/day/year
			DateTimeFormatter dF5 = DateTimeFormatter.ofPattern("LLL d,yyyy", Locale.ENGLISH);// checks for Month day,year
			DateTimeFormatter dF6 = DateTimeFormatter.ofPattern("d LLL yyyy", Locale.ENGLISH);// checks for Month day year

			if (isDateValid(dateString, dF1)) {
				dateTime=LocalDate.parse(dateString, dF1).atStartOfDay();
			} 
			else if (isDateValid(dateString, dF2)) {
				dateTime=LocalDate.parse(dateString, dF2).atStartOfDay();
			}
			// adding Year to start of date
			else if(isDateValid(currentYear+"-"+dateString.replace("/", "-"),dF3)){
				dateTime= LocalDate.parse(currentYear+"-"+dateString.replace("/", "-"), dF3).atStartOfDay();
			}
			else if (isDateValid(dateString, dF3)) {
				dateTime= LocalDate.parse(dateString, dF3).atStartOfDay();
			}
			else if (isDateValid(dateString, dF4)) {
				dateTime= LocalDate.parse(dateString, dF4).atStartOfDay();
			}
			else if (isDateValid(dateString, dF5)) {
				dateTime= LocalDate.parse(dateString, dF5).atStartOfDay();
			}
			else if (isDateValid(dateString, dF6)) {
				dateTime= LocalDate.parse(dateString, dF6).atStartOfDay();
			}
			// converting the results to proper result format
			else if (dateString.contains("Today") == true) {
				dateTime= LocalDate.now().atStartOfDay();
			}
			// just updated so get the current result-time
			else if (dateString.contains("just now")) {
				dateString = dateTime.toString();
			} 
			else if (dateString.contains("Yesterday") == true) {
				dateTime= LocalDate.now().minusDays(1).atStartOfDay();
			}
			// made or updated between 1-23 hours ago
			else if (dateString.contains("hours ago") == true || dateString.contains("hour ago") == true
					|| dateString.contains("h") == true) {
				// removes the hour
				dateString = dateString.replace("hours ago", "");
				dateString = dateString.replace("hour ago", "");
				dateString = dateString.replace("h", "");
				dateString = dateString.replace(" ", "");
				dateString.trim(); // removes white space
				long hour = Long.parseLong(dateString);
				dateTime=dateTime.minusHours(hour).minusMinutes(dateTime.getMinute());
			}
			// this is a minute value so will need to convert for current time |
			// Updated\Created less than an hour ago
			else if (dateString.contains("m") == true || dateString.contains("minutes ago") == true
					|| dateString.contains("min") == true || dateString.contains("min ago") == true
					|| dateString.contains("minute ago") == true || dateString.contains("mins ago") == true) {
				dateString = dateString.replace("minutes ago", ""); // removes the minute
				dateString = dateString.replace("minute ago", ""); // removes the minute
				dateString = dateString.replace("mins ago", ""); // removes the minute
				dateString = dateString.replace("min ago", ""); // removes the minute
				dateString = dateString.replace("min", ""); // removes the minute
				dateString = dateString.replace("m", ""); // removes the minute
				dateString = dateString.replace(" ", "");
				dateString.trim(); // removes white space
				long min = Long.parseLong(dateString);
				dateTime=dateTime.minusMinutes(min);
			} 
			else if (dateString.equals(null) == false) {
				try {
					dateTime = LocalDateTime.parse(dateString);
				} catch (IllegalArgumentException e) {
					System.out.println("Unable to parse: " + dateString + ".");
				}
			} 
			else {
				dateTime = LocalDateTime.parse(dateString);
				// System.out.println(datetime.toString() + " ------ "+ value);
			}
		}catch(Exception e) {
			System.out.print("Failed parsing date: "+dateString);
			System.out.println(" "+e);
		}
		return dateTime;
	}

	public static List<Favorites> parseFavorites(List<Favorites> list) {
		LocalDateTime date;
		// Time data conversion
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		for (Favorites favorite : list) {
			date = DateParse(favorite.getScrappedUpdate());
			favorite.setUpdateString(date.format(format));
			favorite.setTimeOfLastUpdate(date);
			if (favorite.getScrappedCreate() != null && favorite.getTimeOfEntry() == null) {
				date = DateParse(favorite.getScrappedCreate());
				favorite.setTimeOfEntry(date);
			}
		}
		return list;
	}
}
