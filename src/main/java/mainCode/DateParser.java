package mainCode;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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

	@SuppressWarnings({ "deprecation", "null" })
	public static Date DateParse(String dateString) {
		Date datetime = new Date(); // will be used for date storage. Will be converted to String and put into
		// result
		try {

		// Time data conversion
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
		LocalDate date = null;
		ZoneId defaultZoneId = ZoneId.systemDefault();

		// This portion is handling hard coded date formats
		String currentYear= Integer.toString(LocalDate.now().getYear());
		DateTimeFormatter dF1 = DateTimeFormatter.ofPattern("MMM dd,yyyy - HH:mm a", Locale.ENGLISH);// checks for Month
																										// day, year -
																										// Hour:Minute
																										// AM\PM
		DateTimeFormatter dF2 = DateTimeFormatter.ofPattern("M-d", Locale.ENGLISH);// checks for Month-day
		DateTimeFormatter dF3 = DateTimeFormatter.ofPattern("yyyy-M-d", Locale.ENGLISH);// checks for year-month-day

		if (isDateValid(dateString, dF1)) {
			date = LocalDate.parse(dateString, dF1);
			dateString = date.toString();
			datetime = Date.from(date.atStartOfDay(defaultZoneId).toInstant());
		} else if (isDateValid(dateString, dF2)) {
			date = LocalDate.parse(dateString, dF2);
			dateString = date.toString();
			datetime = Date.from(date.atStartOfDay(defaultZoneId).toInstant());
		}

		// adding Year to start of date
		else if(isDateValid(currentYear+"-"+dateString.replace("/", "-"),dF3)){
			date= LocalDate.parse(currentYear+"-"+dateString.replace("/", "-"), dF3);
			dateString=date.toString();
			datetime=Date.from(date.atStartOfDay(defaultZoneId).toInstant());
		}
		 else if (isDateValid(dateString, dF3)) {
			date = LocalDate.parse(dateString, dF3);
			dateString = date.toString();
			datetime = (Date) Date.from(date.atStartOfDay(defaultZoneId).toInstant());
		}
		// converting the results to proper result format
		else if (dateString.contains("Today") == true) {
			datetime.setMinutes(0);
			datetime.setSeconds(0);
			datetime.setHours(0);
		}
		// just updated so get the current result-time
		else if (dateString.contains("just now")) {
			dateString = datetime.toString();
		} else if (dateString.contains("Yesterday") == true) {
			datetime.setMinutes(0);
			datetime.setSeconds(0);
			datetime.setHours(0);
			datetime.setDate(datetime.getDate() - 1);

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
			int hour = Integer.parseInt(dateString);
			datetime.setHours(datetime.getHours() - hour);
			datetime.setMinutes(0);
			datetime.setSeconds(0);
			dateString = datetime.toString();
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
			int min = Integer.parseInt(dateString);
			datetime.setMinutes(datetime.getMinutes() - min);
			datetime.setSeconds(0);
			dateString = datetime.toString();
		} else if (dateString.equals(null) == false) {
			try {
				long value = Date.parse(dateString);
				datetime.setTime(value);

			} catch (IllegalArgumentException e) {
				System.out.println("Unable to parse: " + dateString + ".");
			}
		} 
		else {
			long value = Date.parse(dateString);
			datetime= new Date(value);
			// System.out.println(datetime.toString() + " ------ "+ value);
		}
		dateString = ft.format(datetime);
		}catch(Exception e) {
			System.out.print("Failed parsing date: "+dateString);
		}
		return datetime;
	}

	public static List<Favorites> parseFavorites(List<Favorites> list) {
		Date date;
		for (Favorites favorite : list) {
			date = DateParse(favorite.getScrappedUpdate());
			favorite.setUpdateString(date.toString());
			favorite.setTimeOfLastUpdate(date);
			if (favorite.getScrappedCreate() != null && favorite.getTimeOfEntry() == null) {
				date = DateParse(favorite.getScrappedCreate());
				favorite.setTimeOfEntry(date);
			}
		}
		return list;
	}
}
