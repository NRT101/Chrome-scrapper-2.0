package chrome.scrapper.main;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="WebsiteTemplate")
public class WebsiteTemplate {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="InternalId")
	private Integer Id;
	@Column(name="Website")
	private String Website;
	@Column(name="DateUpdated")
	private String DateUpdated;
	@Column(name="DatePosted")
	private String DatePosted;
	@Column(name="stringRemove")
	private String stringRemove;
	@Column(name="CookieAndValue")
	private String CookieAndValue;
	public Integer getId() {
		return Id;
	}
	public void setId(Integer id) {
		Id = id;
	}
	public String getWebsite() {
		return Website;
	}
	public void setWebsite(String website) {
		Website = website;
	}
	public String getDateUpdated() {
		return DateUpdated;
	}
	public void setDateUpdated(String dateUpdated) {
		DateUpdated = dateUpdated;
	}
	public String getDatePosted() {
		return DatePosted;
	}
	public void setDatePosted(String datePosted) {
		DatePosted = datePosted;
	}
	public String getStringRemove() {
		return stringRemove;
	}
	public void setStringRemove(String stringRemove) {
		this.stringRemove = stringRemove;
	}
	public String getCookieAndValue() {
		return CookieAndValue;
	}
	public void setCookieAndValue(String cookieAndValue) {
		CookieAndValue = cookieAndValue;
	}
	
	
}
