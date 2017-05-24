package tech.enfuego.models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

@Embedded
public class JobInstance implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@JsonSerialize(using = ToStringSerializer.class)
	@XmlElement(name="id")
	@JsonProperty("id")
	private ObjectId id;
	
	@XmlElement(name="score")
	@JsonProperty("score")
	private double score;
	
	@XmlElement(name="applied")
	@JsonProperty("applied")
	private boolean applied;
	
	@XmlElement(name="appliedOn")
	@JsonProperty("appliedOn")
	private String appliedOn;
	
	@XmlElement(name="jobTitle")
	@JsonProperty("jobTitle")
	private String jobTitle;
	
	@XmlElement(name="company")
	@JsonProperty("company")
	private String company;
	
	@XmlElement(name="location")
	@JsonProperty("location")
	private Address location;
	
	@XmlElement(name="missingSkills")
	@JsonProperty("missingSkills")
	private List<Skills> missingSkills;
	
	public ObjectId getId() {
		return id;
	}
	public void setId(ObjectId id) {
		this.id = id;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public boolean isApplied() {
		return applied;
	}
	public void setApplied(boolean applied) {
		this.applied = applied;
	}
	public String getAppliedOn() {
		return appliedOn;
	}
	public void setAppliedOn(String appliedOn) {
		this.appliedOn = appliedOn;
	}
	public String getJobTitle() {
		return jobTitle;
	}
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public Address getLocation() {
		return location;
	}
	public void setLocation(Address location) {
		this.location = location;
	}
	public List<Skills> getMissingSkills() {
		return missingSkills;
	}
	public void setMissingSkills(List<Skills> missingSkills) {
		this.missingSkills = missingSkills;
	}
	
	
	@Override
	public boolean equals(Object jobInstance) {
		return (jobInstance instanceof JobInstance) && this.getId().equals(((JobInstance) jobInstance).getId());
	}
	
	
	@Override
	public int hashCode() {
	      return this.getId().hashCode();
	}
	
	

}
