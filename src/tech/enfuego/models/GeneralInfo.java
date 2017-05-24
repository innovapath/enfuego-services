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
public class GeneralInfo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GeneralInfo() {
		// TODO Auto-generated constructor stub
	}
	
	
	@Embedded
	@XmlElement(name="address")
	@JsonProperty("address")
	private Address address;
	
	@XmlElement(name="firstName")
	@JsonProperty("firstName")
	private String firstName; 
	
	@XmlElement(name="lastName")
	@JsonProperty("lastName")
    private String lastName; 
	
	@XmlElement(name="genEmail")
	@JsonProperty("genEmail")
    private String genEmail;
	
	@XmlElement(name="mobilePhone")
	@JsonProperty("mobilePhone")
    private String mobilePhone;
	
	@XmlElement(name="birthDay")
	@JsonProperty("birthDay")
    private String birthDay;
    
    public Address getAddress() {
		return address;
	}
    
	public void setAddress(Address address) {
		this.address = address;
	}
	
    public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getGenEmail() {
		return genEmail;	
	}

	public void setGenEmail(String genEmail) {
		this.genEmail = genEmail;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	public String getBirthDay() {
		return birthDay;
	}
	public void setBirthDay(String birthDay) {
		this.birthDay = birthDay;
	}
	public String getBirthMonth() {
		return birthMonth;
	}
	public void setBirthMonth(String birthMonth) {
		this.birthMonth = birthMonth;
	}
	private String birthMonth;

}
