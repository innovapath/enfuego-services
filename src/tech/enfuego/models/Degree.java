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
public class Degree implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Degree() {
		// TODO Auto-generated constructor stub
	}
	
	@JsonSerialize(using = ToStringSerializer.class)
	@XmlElement(name="id")
	@JsonProperty("id")
	private ObjectId id;
	
	public ObjectId getId() {
		return id;
	}
	public void setId(ObjectId id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSchool() {
		return school;
	}
	public void setSchool(String school) {
		this.school = school;
	}
	public String getDegreeType() {
		return degreeType;
	}
	public void setDegreeType(String degreeType) {
		this.degreeType = degreeType;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	@XmlElement(name="name")
	@JsonProperty("name")
	private String name;
	
	@XmlElement(name="school")
	@JsonProperty("school")
	private String school;
	
	@XmlElement(name="degreeType")
	@JsonProperty("degreeType")
	private String degreeType;
	
	@XmlElement(name="year")
	@JsonProperty("year")
	private String year;
	
	
	@Override
	public boolean equals(Object anotherDegree) {
		return anotherDegree instanceof Degree && 
				((Degree)anotherDegree).getDegreeType() != null &&
				this.getDegreeType() != null &&
				this.getDegreeType().equalsIgnoreCase(((Degree) anotherDegree).getDegreeType());
	}
	
	
	@Override
	public int hashCode() {
	      return this.getDegreeType().hashCode();
	}
	

}
