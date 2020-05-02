package mainCode;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/*
 * Java class representation of the Favorites table
 */
@Entity
@Table(name="Favorites2")
public class Favorites{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "InternalId", updatable = false, nullable = false)
	private Integer InternalId;
	@Column(name="URL")
	private String URL;
	@Column(name="TemplateId")
	private Integer TemplateId;
	@Column(name="Name")
	private String Name;
	@Column(name="Directory")
	private String Directory;
	@Column(name="TimeOfLastUpdate")
	private Date TimeOfLastUpdate=null;
	@Column(name="TimeOfEntry")
	private Date TimeOfEntry=null;
	@Column(name="UpdateString")
	private String UpdateString=null;
	@Column(name="SuccessFlag")
	private Integer SuccessFlag;
	
	private String scrappedUpdate=null;
	private String scrappedCreate=null;
	/*
	 * Getters and setters
	 */
	public String getURL() {
		return URL;
	}

	public void setURL(String uRL) {
		URL = uRL;
	}

	public Integer getInternalId() {
		return InternalId;
	}

	public void setInternalId(Integer IntegerernalId) {
		InternalId = IntegerernalId;
	}

	public Integer getTemplateId() {
		return TemplateId;
	}

	public void setTemplateId(Integer templateId) {
		TemplateId = templateId;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getDirectory() {
		return Directory;
	}

	public void setDirectory(String directory) {
		Directory = directory;
	}

	public Date getTimeOfLastUpdate() {
		return TimeOfLastUpdate;
	}

	public void setTimeOfLastUpdate(Date date) {
		TimeOfLastUpdate = date;
	}

	public Date getTimeOfEntry() {
		return TimeOfEntry;
	}

	public void setTimeOfEntry(Date timeOfEntry) {
		TimeOfEntry = timeOfEntry;
	}

	public String getUpdateString() {
		return UpdateString;
	}

	public void setUpdateString(String updateString) {
		UpdateString = updateString;
	}

	public Integer getSuccessFlag() {
		return SuccessFlag;
	}

	public void setSuccessFlag(Integer successFlag) {
		SuccessFlag = successFlag;
	}

	public String getScrappedUpdate() {
		return scrappedUpdate;
	}

	public void setScrappedUpdate(String scrappedUpdate) {
		this.scrappedUpdate = scrappedUpdate;
	}

	public String getScrappedCreate() {
		return scrappedCreate;
	}

	public void setScrappedCreate(String scrappedCreate) {
		this.scrappedCreate = scrappedCreate;
	}
}
