package mainCode;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WebScrapperTest {
	private static List<WebsiteTemplate> templates;
	private static List<Favorites> favoriteList;
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		WebsiteTemplateDAO.getTemplatesFromDatabase();
		templates = WebsiteTemplateDAO.getTemplateList();
		favoriteList = new ArrayList<Favorites>();

	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		
	}

	@BeforeEach
	void setUp() throws Exception {
		for(Favorites favorite:favoriteList) {
			favorite.setScrappedCreate("");
			favorite.setScrappedUpdate("");
		}
	}

	@AfterEach
	void tearDown() throws Exception {
		for(Favorites favorite:favoriteList) {
			System.out.println("URL: "+favorite.getURL());
			System.out.println("ScrappedCreated: "+favorite.getScrappedCreate());
			System.out.println("ScrappedUpdated: "+favorite.getScrappedUpdate());
			System.out.println("TemplateId: "+favorite.getTemplateId());
			System.out.println();
		}
	}

	@Test
	void testStart() {
		System.out.println("=====TESTING start=====");	
		webScrapper scrapper = new webScrapper(30000, 0, 4, "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36", false, favoriteList, templates);
		scrapper.start();
		assertEquals("10/12/2017",favoriteList.get(1).getScrappedCreate());
	}

	@Test
	void testScrapSite() {
		System.out.println("=====TESTING ScrapSite=====");	
		int counter=0;
		int templateAmt=templates.size();
		String result="";
		WebsiteTemplate template=null;
		webScrapper scrapper = new webScrapper(30000, 0, 4, "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36", false, favoriteList, templates);
		Favorites favorite=favoriteList.get(1);
		counter=0;
		while(counter<templateAmt) {
			template=templates.get(counter);
			if(favorite.getTemplateId()==template.getId()) {
				result=scrapper.scrapSite(favorite.getURL(), template.getDatePosted(), template.getCookieAndValue(), template.getStringRemove());
				break;
			}
			counter++;
		}
		
		assertEquals("10/12/2017",result);
	}

	@Test
	void testScrapSiteSetup() {
		System.out.println("=====TESTING ScrapSiteSetup=====");	
		int counter=0;
		int templateAmt=templates.size();
		WebsiteTemplate template=null;
		webScrapper scrapper = new webScrapper(30000, 0, 4, "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36", false, favoriteList, templates);
		for(Favorites favorite:favoriteList) {
			counter=0;
			while(counter<templateAmt) {
				template=templates.get(counter);
				if(favorite.getTemplateId()==template.getId()) {
					scrapper.scrapSiteSetup(favorite, template);
					counter=templateAmt;
				}
				counter++;
			}
		}
		assertEquals("10/12/2017",favoriteList.get(1).getScrappedCreate());
	}

}
