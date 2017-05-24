package tech.enfuego.models;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.IndexOptions;
import org.mongodb.morphia.annotations.Indexed;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import tech.enfuego.Constants;
import tech.enfuego.Constants.PROFESSION_TYPES;


@Entity(Constants.RESUME_COLLECTION_NAME)
@XmlRootElement
@JsonRootName(value = "user")
public class User implements Serializable{
    private static final long serialVersionUID = 1L;
    
    
    @Id 
    @JsonSerialize(using = ToStringSerializer.class)
    private ObjectId _id;
    
    public ObjectId get_id() {
		return _id;
	}

	public void set_id(ObjectId _id) {
		this._id = _id;
	}

	public GeneralInfo getGeneralInfo() {
		return generalInfo;
	}

	public void setGeneralInfo(GeneralInfo generalInfo) {
		this.generalInfo = generalInfo;
	}

	public String getProfileImageURL() {
		return profileImageURL;
	}

	public void setProfileImageURL(String profileImageURL) {
		this.profileImageURL = profileImageURL;
	}

	public float getCareerScale() {
		return careerScale;
	}

	public void setCareerScale(float careerScale) {
		this.careerScale = careerScale;
	}

	public float getIncomeReqMin() {
		return incomeReqMin;
	}

	public void setIncomeReqMin(float incomeReqMin) {
		this.incomeReqMin = incomeReqMin;
	}

	public float getIncomeReqMax() {
		return incomeReqMax;
	}

	public void setIncomeReqMax(float incomeReqMax) {
		this.incomeReqMax = incomeReqMax;
	}

	public List<JobHistory> getJobHistory() {
		return jobHistory;
	}

	public void setJobHistory(List<JobHistory> jobHistory) {
		this.jobHistory = jobHistory;
	}

	public List<SearchMarkets> getSearchMarkets() {
		return searchMarkets;
	}

	public void setSearchMarkets(List<SearchMarkets> searchMarkets) {
		this.searchMarkets = searchMarkets;
	}

	public List<Skills> getSkills() {
		return skills;
	}

	public void setSkills(List<Skills> skills) {
		this.skills = skills;
	}

	public List<Accomplishments> getAccs() {
		return accs;
	}

	public void setAccs(List<Accomplishments> accs) {
		this.accs = accs;
	}

	public String getSkillsCloudSrc() {
		return skillsCloudSrc;
	}

	public void setSkillsCloudSrc(String skillsCloudSrc) {
		this.skillsCloudSrc = skillsCloudSrc;
	}

	public Membership getMembership() {
		return membership;
	}

	public void setMembership(Membership membership) {
		this.membership = membership;
	}

	public boolean isSetupComplete() {
		return setupComplete;
	}

	public void setSetupComplete(boolean setupComplete) {
		this.setupComplete = setupComplete;
	}

	public List<Degree> getDegrees() {
		return degrees;
	}

	public void setDegrees(List<Degree> degrees) {
		this.degrees = degrees;
	}
	
	@XmlElement(name="targetProfession")
	@JsonProperty("targetProfession")
	private PROFESSION_TYPES targetProfession;
	
	public PROFESSION_TYPES getTargetProfession()
	{
		return targetProfession;
	}
	

	public void setTargetProfession(PROFESSION_TYPES targetProfession) 
	{
		this.targetProfession = targetProfession;
	}

	public List<Certifications> getCertifications() {
		return certifications;
	}

	public void setCertifications(List<Certifications> certifications) {
		this.certifications = certifications;
	}




	@Embedded
	@XmlElement(name="generalInfo")
	@JsonProperty("generalInfo")
    private GeneralInfo generalInfo;
    
	
    @XmlElement(name="profileImageURL")
	@JsonProperty("profileImageURL")
    private String profileImageURL;
    
    @XmlElement(name="careerScale")
	@JsonProperty("careerScale")
    private float careerScale;
    
	
    @XmlElement(name="incomeReqMin")
	@JsonProperty("incomeReqMin")
    private float incomeReqMin;
    
    @XmlElement(name="incomeReqMax")
	@JsonProperty("incomeReqMax")
    private float incomeReqMax;
    
    @Embedded
    @XmlElement(name="jobHistory")
  	@JsonProperty("jobHistory")
    private List<JobHistory> jobHistory;
    
    @Embedded
    @XmlElement(name="searchMarkets")
  	@JsonProperty("searchMarkets")
    private List<SearchMarkets> searchMarkets;
    
    @Embedded
    @XmlElement(name="skills")
  	@JsonProperty("skills")
    private List<Skills> skills;
    
    @Embedded
    @XmlElement(name="accs")
  	@JsonProperty("accs")
    private List<Accomplishments> accs;
    
    @XmlElement(name="skillsCloudSrc")
  	@JsonProperty("skillsCloudSrc")
    private String skillsCloudSrc;
    
    @Embedded
    @XmlElement(name="membership")
  	@JsonProperty("membership")
    private Membership membership;
    
    @XmlElement(name="setupComplete")
  	@JsonProperty("setupComplete")
    private boolean setupComplete;
    
    @Embedded
    @XmlElement(name="degrees")
  	@JsonProperty("degrees")
    private List<Degree> degrees;
    
    @Embedded
    @XmlElement(name="certifications")
  	@JsonProperty("certifications")
    private List<Certifications> certifications;
    
    
    
    public User() {
    	
    }
    
    
    
    @Indexed(options = @IndexOptions(unique=true))
	@XmlElement(name="email")
	@JsonProperty("email")
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@XmlElement(name="jobs")
	@JsonProperty("jobs")
	private List<JobInstance> appliedJobs;

	public List<JobInstance> getAppliedJobs() {
		return appliedJobs;
	}
	
	public void setAppliedJobs(List<JobInstance> appliedJobs) {
		this.appliedJobs = appliedJobs;
	}
	

	

}
