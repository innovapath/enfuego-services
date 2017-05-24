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
public class Skills implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public Skills()
	{
		
	}
	
	@JsonSerialize(using = ToStringSerializer.class)
	@XmlElement(name="id")
	@JsonProperty("id")
	private ObjectId id;
	
	@XmlElement(name="name")
	@JsonProperty("name")
	private String name;
	
	
	@XmlElement(name="yearsApplied")
	@JsonProperty("yearsApplied")
	private String yearsApplied;
	
	@XmlElement(name="skillsApplied")
	@JsonProperty("skillsApplied")
	private String skillsApplied;
	
	@XmlElement(name="lastApplied")
	@JsonProperty("lastApplied")
	private String lastApplied;
	
	@XmlElement(name="whereApplied")
	@JsonProperty("whereApplied")
	private String whereApplied;
	
	
	public String getWhereApplied() {
		return whereApplied;
	}
	public void setWhereApplied(String whereApplied) {
		this.whereApplied = whereApplied;
	}
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
	public String getYearsApplied() {
		return yearsApplied;
	}
	public void setYearsApplied(String yearsApplied) {
		this.yearsApplied = yearsApplied;
	}
	public String getSkillsApplied() {
		return skillsApplied;
	}
	public void setSkillsApplied(String skillsApplied) {
		this.skillsApplied = skillsApplied;
	}
	public String getLastApplied() {
		return lastApplied;
	}
	public void setLastApplied(String lastApplied) {
		this.lastApplied = lastApplied;
	}
	
	@XmlElement(name="score")
	@JsonProperty("score")
	private double Score;

	
	public double getScore() {
		return Score;
	}
	public void setScore(double score) {
		Score = score;
	}
	
	@Override
	public boolean equals(Object anotherSkill) {
		return anotherSkill instanceof Skills && 
				((Skills)anotherSkill).getName() != null &&
				this.getName() != null &&
				this.getName().equals(((Skills) anotherSkill).getName());
	}
	
	
	@Override
	public int hashCode() {
	      return this.getName().hashCode();
	}
	
	
	
	

}
