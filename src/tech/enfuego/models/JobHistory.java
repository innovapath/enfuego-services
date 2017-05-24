package tech.enfuego.models;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Id;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.mongodb.BasicDBObject;

@Embedded
public class JobHistory implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public JobHistory() {	
		// TODO Auto-generated constructor stub
	}
	
	@JsonSerialize(using = ToStringSerializer.class)
	@XmlElement(name="id")
	@JsonProperty("id")
	private ObjectId id;
	
	@XmlElement(name="company")
	@JsonProperty("company")
	private String company;
	
	
	@XmlElement(name="startMonth")
	@JsonProperty("startMonth")
	private String startMonth;
	
	@XmlElement(name="title")
	@JsonProperty("title")
	private String title;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	@XmlElement(name="startYear")
	@JsonProperty("startYear")
	private String startYear;
	
	@XmlElement(name="endMonth")
	@JsonProperty("endMonth")
	private String endMonth;
	
	@XmlElement(name="endYear")
	@JsonProperty("endYear")
	private String endYear;
	
	@XmlElement(name="present")
	@JsonProperty("present")
	private boolean present;
	
	
	public ObjectId getId() {
		return id;
	}
	public void setId(ObjectId id) {
		this.id = id;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getStartMonth() {
		return startMonth;
	}
	public void setStartMonth(String startMonth) {
		this.startMonth = startMonth;
	}
	public String getStartYear() {
		return startYear;
	}
	public void setStartYear(String startYear) {
		this.startYear = startYear;
	}
	public String getEndMonth() {
		return endMonth;
	}
	public void setEndMonth(String endMonth) {
		this.endMonth = endMonth;
	}
	public String getEndYear() {
		return endYear;
	}
	public void setEndYear(String endYear) {
		this.endYear = endYear;
	}
	public boolean isPresent() {
		return present;
	}
	public void setPresent(boolean present) {
		this.present = present;
	}

}
