package mainCode;

import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class webScrapper implements Runnable {
	private List<WebsiteTemplate> templates; // stores all the templates from WebsiteTemplate table
	private int timeoutPeriod = 30000; // stores the timeout limit for each scrap
	private int startPoint = 0; // stores the start point of this thread for searching through the Favorites
								// list; lowest value 0
	private int endPoint = FavoritesDAO.getListOfFavorites().size() - 1; // stores the end point of this thread for
																			// searching through the Favorites list;
																			// highest value ListOfFavorites.size()-1
	private String userAgent = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36"; // stores
																																		// the
																																		// userAgent
																																		// value
																																		// to
																																		// use
																																		// for
																																		// scrapping
																																		// websites
	private List<Favorites> favoritesSearch;// stores the list of favorite to search

	public List<WebsiteTemplate> getTemplates() {
		return templates;
	}

	public void setTemplates(List<WebsiteTemplate> templates) {
		this.templates = templates;
	}

	public int getTimeoutPeriod() {
		return timeoutPeriod;
	}

	public void setTimeoutPeriod(int timeoutPeriod) {
		this.timeoutPeriod = timeoutPeriod;
	}

	public int getStartPoint() {
		return startPoint;
	}

	public void setStartPoint(int startPoint) {
		this.startPoint = startPoint;
	}

	public int getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(int endPoint) {
		this.endPoint = endPoint;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public List<Favorites> getFavoritesSearch() {
		return favoritesSearch;
	}

	public void setFavoritesSearch(List<Favorites> favoritesSearch) {
		this.favoritesSearch = favoritesSearch;
	}

	public webScrapper(int timeoutPeriod, int startPoint, int endPoint, String userAgent,
			boolean validateTLSCertificates, List<Favorites> favoritesSearch,List<WebsiteTemplate> templates) {
		super();
		this.timeoutPeriod = timeoutPeriod;
		this.startPoint = startPoint;
		this.endPoint = endPoint;
		this.userAgent = userAgent;
		this.favoritesSearch = favoritesSearch;
		this.templates=templates;
	}

	public void start() {
		int templateAmt = templates.size();
		WebsiteTemplate template = null;
		int counter = 0;
		int templateId;
		// loop for each favorite to scrap and grab data for
		while (startPoint <= endPoint) {
			counter=0;
			Favorites favorite = favoritesSearch.get(startPoint);
			if (favorite.getTemplateId() != -1) {
				// gets the specific template
				while (counter < templateAmt) {
					if(templates.get(counter).getId()==favorite.getTemplateId())
						break;
					else
						counter++;
				}
				favorite = scrapSiteSetup(favorite, templates.get(counter));
				
			}
			// this is a new favorite value and needs to get both update and posted value
			else {
				// checking every template to see if the correct value is there
				while (counter < templateAmt) {
					template = templates.get(counter);
					if (favorite.getURL().contains(template.getWebsite())) {
						favorite = scrapSiteSetup(favorite, template);
						// checks to break the loop as all necessary values are defined
						// need to check if both date posted and date updated failed
						if (template.getDatePosted() != null) {
							// The URL has to have successfully scrapped post and update date
							if (favorite.getScrappedCreate() != null && favorite.getScrappedUpdate() != null && !favorite.getScrappedUpdate().equals("1900-01-02") && !favorite.getScrappedCreate().equals("1900-01-02")) {
								favorite.setTemplateId(template.getId());
								counter=templateAmt;
							}
						}
						// The URL has to have successfully scrapped update date
						else {
							if (favorite.getScrappedUpdate() != null && !favorite.getScrappedUpdate().equals("1900-01-02")) {
								favorite.setTemplateId(template.getId());
								counter=templateAmt;
							}
						}
					}
					counter++;
				}
			}
			// complete scrap failure check
			templateId=favorite.getTemplateId();
			counter=0;
			while (counter < templateAmt) {
				if(templateId==templates.get(counter).getId()) {
					template=templates.get(counter);
					break;
				}
				counter++;
			}
			// for templates with both DatePosted and DateUpdated
			if (template.getDatePosted()!=null &&favorite.getScrappedCreate().equals("1900-01-02") && favorite.getScrappedUpdate().equals("1900-01-02")) {
				System.out.println("Failed to parse: " +favorite.getName() +" URL: "+favorite.getURL());
				favorite.setSuccessFlag(0);
			}
			// for just DatePosted examples
			else if(favorite.getScrappedUpdate().equals("1900-01-02")) {
				System.out.println("Failed to parse: " +favorite.getName() +" URL: "+favorite.getURL());
				favorite.setSuccessFlag(0);
			}
			// Scrap succeeded
			else {
				favorite.setSuccessFlag(1);
			}
			startPoint++;
		}
	}

	// This actually reaches the scrap site URL with appropriate cookie values, gets
	// the specific data from path, and removes unnecessary strings toRemove
	public String scrapSite(String URL, String path, String cookieValue, String toRemove) {
		Document doc = null;
		String fromSite = null;
		// handles the cookie
	try {
		if (cookieValue == null) {
			
				doc = Jsoup.connect(URL).userAgent(userAgent).timeout(timeoutPeriod).get();
		} else {
			String[] cookieSplitValue = cookieValue.split(";");

				doc = Jsoup.connect(URL).cookie(cookieSplitValue[0], cookieSplitValue[1]).userAgent(userAgent)
						.timeout(timeoutPeriod).get();
		}

		Element data;
		String[] direction = path.split(";");
		// handles the various methods of data retrieval
		switch (direction[1]) {
		case "first":
			data = doc.select(direction[0]).first(); // this gets the first line in
			fromSite = data.text();
			break;
		case "last":
			data = doc.select(direction[0]).last(); // this gets the last line in
			fromSite = data.text();
			break;
		default:
			fromSite = "FROM PROGRAM: NO DATA";
			break;
		}
		if(toRemove!=null) {
			fromSite=fromSite.replaceAll(toRemove, "");
		}
	} catch (Exception e) {
		fromSite="1900-01-02";
	}
		return fromSite;
	}

	// sets up what to grab for each favorites; it will grab for update and create if listed
	public Favorites scrapSiteSetup(Favorites favorite, WebsiteTemplate template) {
		String cookie = template.getCookieAndValue();
		String toRemove = template.getStringRemove();
		String URL = favorite.getURL();
		favorite.setScrappedUpdate(scrapSite(URL, template.getDateUpdated(), cookie, toRemove)); // always perform
																									// update check
		// perform creation check only if not already checked and there is a path to do so
		if (favorite.getTimeOfEntry() == null && template.getDatePosted() != null) {
			favorite.setScrappedCreate(scrapSite(URL, template.getDatePosted(), cookie, toRemove));
		}
		return favorite;
	}

	// this is called when the thread is running
	@Override
	public void run() {
		this.start();
	}
}
