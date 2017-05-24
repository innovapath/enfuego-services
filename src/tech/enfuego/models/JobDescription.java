package tech.enfuego.models;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import tech.enfuego.Constants;
import tech.enfuego.Constants.PROFESSION_TYPES;

@Entity(Constants.JOBS_COLLECTION_NAME)
@XmlRootElement
@JsonRootName(value = "jobdescription")
public class JobDescription implements Serializable 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Id 
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId _id;
	
	@XmlElement(name="minYearsOfExp")
	@JsonProperty("minYearsOfExp")
	private int minYearsOfExp;
	
	@XmlElement(name="maxYearsOfExp")
	@JsonProperty("maxYearsOfExp")
	private int maxYearsOfExp;
	
	
	public int getMinYearsOfExp() {
		return minYearsOfExp;
	}

	public void setMinYearsOfExp(int minYearsOfExp) {
		this.minYearsOfExp = minYearsOfExp;
	}

	public int getMaxYearsOfExp() {
		return maxYearsOfExp;
	}

	public void setMaxYearsOfExp(int maxYearsOfExp) {
		this.maxYearsOfExp = maxYearsOfExp;
	}

	@XmlElement(name="jobTitle")
	@JsonProperty("jobTitle")
	private String jobTitle;
	
	@XmlElement(name="companyName")
	@JsonProperty("companyName")
	private String companyName;
	
	@XmlElement(name="companyLocation")
	@JsonProperty("companyLocation")
	private Address companyLocation;
	
	
	@XmlElement(name="jobSummary")
	@JsonProperty("jobSummary")
	private String jobSummary;
	
	@XmlElement(name="skills")
	@JsonProperty("skills")
	private List<Skills> skills;
	
	@XmlElement(name="degrees")
	@JsonProperty("degrees")
	private List<Degree> degrees;
    public List<Degree> getDegrees() {
		return degrees;
	}

	public void setDegrees(List<Degree> degrees) {
		this.degrees = degrees;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Address getCompanyLocation() {
		return companyLocation;
	}

	public void setCompanyLocation(Address companyLocation) {
		this.companyLocation = companyLocation;
	}

	public String getJobSummary() {
		return jobSummary;
	}

	public void setJobSummary(String jobSummary) {
		this.jobSummary = jobSummary;
	}

	public List<Skills> getSkills() {
		return skills;
	}
	
	

	public void setSkills(List<Skills> skills) {
		this.skills = skills;
	}

	public ObjectId get_id() {
		return _id;
	}

	public void set_id(ObjectId _id) {
		this._id = _id;
	}
	
	@XmlElement(name="professionType")
	@JsonProperty("professionType")
	private PROFESSION_TYPES professionType;
	
	public PROFESSION_TYPES getProfessionType() {
		return professionType;
	}

	public void setProfessionType(PROFESSION_TYPES professionType) {
		this.professionType = professionType;
	}
	
	
	@Override
	public boolean equals(Object anotherSkill) {
		return (anotherSkill instanceof JobDescription) && this.get_id().equals(((JobDescription) anotherSkill).get_id());
	}
	
	
	@Override
	public int hashCode() {
	      return this.get_id().hashCode();
	}
	
	
	
	

}
