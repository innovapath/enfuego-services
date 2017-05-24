package tech.enfuego.models;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Id;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.mongodb.BasicDBObject;

@Embedded
public class Membership implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Membership() {
		
	}
	
	@JsonSerialize(using = ToStringSerializer.class)
	private ObjectId id;
	
	
	@XmlElement(name="validThrough")
	@JsonProperty("validThrough")
	private String validThrough;
	
	public ObjectId getId() {
		return id;
	}
	public void setId(ObjectId id) {
		this.id = id;
	}

	@XmlElement(name="autoRenewal")
	@JsonProperty("autoRenewal")
	private boolean autoRenewal;
	
	public String getValidThrough() {
		return validThrough;
	}
	public void setValidThrough(String validThrough) {
		this.validThrough = validThrough;
	}
	public boolean isAutoRenewal() {
		return autoRenewal;
	}
	public void setAutoRenewal(boolean autoRenewal) {
		this.autoRenewal = autoRenewal;
	}
	
	

}
