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
public class SearchMarkets implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SearchMarkets() {
		// TODO Auto-generated constructor stub
	}
	
	private double longitude;
	private double latitude;
	
	@JsonSerialize(using = ToStringSerializer.class)
	@XmlElement(name="_locationId")
	@JsonProperty("_locationId")
	private ObjectId _locationId;
	
	@XmlElement(name="city")
	@JsonProperty("city")
	private String city;
	
	@XmlElement(name="state")
	@JsonProperty("state")
	private String state;
	
	public ObjectId get_locationId() {
		return _locationId;
	}
	public void set_locationId(ObjectId _locationId) {
		this._locationId = _locationId;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
}
