package chrome.scrapper.main;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="UpdatesToday")
public class UpdatesToday {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "InternalId", updatable = false, nullable = false)
	private Integer InternalId;
	@Column(name="List")
	private String List;
	@Column(name="UserId")
	private Integer UserId;
	@Column(name="TimeOfLastUpdate")
	private Date TimeOfLastUpdate;
	
	public Date getTimeOfLastUpdate() {
		return TimeOfLastUpdate;
	}
	public void setTimeOfLastUpdate(Date timeOfLastUpdate) {
		TimeOfLastUpdate = timeOfLastUpdate;
	}
	public Integer getInternalId() {
		return InternalId;
	}
	public void setInternalId(Integer internalId) {
		InternalId = internalId;
	}
	public String getList() {
		return List;
	}
	public void setList(String list) {
		List = list;
	}
	public Integer getUserId() {
		return UserId;
	}
	public void setUserId(Integer userId) {
		UserId = userId;
	}
	
	
}
