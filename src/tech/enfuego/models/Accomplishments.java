package tech.enfuego.models;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Id;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

@Embedded
public class Accomplishments implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Accomplishments() {
		// TODO Auto-generated constructor stub
	}
	
	@JsonSerialize(using = ToStringSerializer.class)
	@XmlElement(name="id")
	@JsonProperty("id")
	private ObjectId id;
	
	@XmlElement(name="accomplishment")
	@JsonProperty("accomplishment")
	private String accomplishment;
	
	@XmlElement(name="company")
	@JsonProperty("company")
	private String company;
	
	@XmlElement(name="month")
	@JsonProperty("month")
	private String month;
	
	@XmlElement(name="year")
	@JsonProperty("year")
	private String year;
	
	public ObjectId getId() {
		return id;
	}
	public void setId(ObjectId id) {
		this.id = id;
	}
	public String getAccomplishment() {
		return accomplishment;
	}
	public void setAccomplishment(String accomplishment) {
		this.accomplishment = accomplishment;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}

	
	
	

}
