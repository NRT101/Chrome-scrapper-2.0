package chrome.scrapper.main;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;

public class WebsiteTemplateDAO {
	private static List<WebsiteTemplate> TemplateList = new ArrayList<WebsiteTemplate>();

	public static List<WebsiteTemplate> getTemplateList() {
		List<WebsiteTemplate> list = new ArrayList<WebsiteTemplate>();
		list.addAll(TemplateList);
		return list;
	}
	// returns the template based off of template
	public static WebsiteTemplate getTemplate(int Id) {
		WebsiteTemplate template =null;
		for(WebsiteTemplate x: TemplateList) {
			if(x.getId()==Id) {
				template=x;
				break;
			}
		}
		return template;
	}
	// This method gets WebsiteTemplate data from database.
		public static List<WebsiteTemplate> getTemplatesFromDatabase() {
			Session session = DataSource.getSessionFactory().openSession();
			@SuppressWarnings("unchecked")
			List<WebsiteTemplate> ListOfTemplates = session.createQuery("FROM WebsiteTemplate").list();
			// Closing The Session Object
			session.close();
			// Makes sure TemplateList matches
			if (!TemplateList.equals(ListOfTemplates)) {
				TemplateList.clear();
				TemplateList.addAll(ListOfTemplates);
			}
			return ListOfTemplates;
		}
}
