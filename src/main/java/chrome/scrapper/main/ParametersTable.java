package chrome.scrapper.main;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ParameterTable")
public class ParametersTable  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "Attribute")
	private String Attribute;
	@Id
	@Column(name = "Parameter")
	private String Parameter;
	@Column(name = "Value")
	private String Value;
	@Column(name = "Description")
	private String Description;
	public String getAttribute() {
		return Attribute;
	}
	public void setAttribute(String attribute) {
		Attribute = attribute;
	}
	public String getParameter() {
		return Parameter;
	}
	public void setParameter(String parameter) {
		Parameter = parameter;
	}
	public String getValue() {
		return Value;
	}
	public void setValue(String value) {
		Value = value;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	
	
}
