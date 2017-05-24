package tech.enfuego.models;


import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

@Embedded
public class Certifications implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@JsonProperty(value="year")
	private String year;
	
	@JsonProperty(value="institution")
	private String institutions;
	
	@JsonProperty(value="name")
	private String name;
	
	@JsonSerialize(using = ToStringSerializer.class)
	@XmlElement(name="id")
	@JsonProperty("id")
	private ObjectId id;
	
	@JsonProperty(value="month")
	private String month;
	

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

	public String getInstitutions() {
		return institutions;
	}

	public void setInstitutions(String institutions) {
		this.institutions = institutions;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}
	
	
	
	

}
