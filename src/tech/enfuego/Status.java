package tech.enfuego;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * 
 * @author abhishek
 * 
 * STATUS Messages for different REST web services.
 *
 */
@XmlRootElement
@JsonRootName(value = "status")
public class Status {
	
	
	public Status()
	{
		
	}
	
	public Status(String message) {
		this.message = message;
	}
	
	@JsonProperty
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	} 
	
	
	public static Status getNoUserMessage()
	{
		return new Status("NO SUCH USER");
	}
	
	public static Status getNoSuchJobMessage()
	{
		return new Status("NO SUCH JOB");
	}
	
	public static Status getNoSuchSearchMarket()
	{
		return new Status("NO SUCH SEARCH MARKET");
	}
	
	public static Status getDeletedMessage()
	{
		return new Status("RESOURCE DELETED");
	}
	
	public static Status getExistingUserMessage()
	{
		return new Status("USER EMAIL ALREADY EXISTS");
	}
	
	public static Status getNoSkillsMessage()
	{
		return new Status("NO SUCH SKILL");
	}
	
	public static Status getNoDegreeMessage()
	{
		return new Status("NO SUCH DEGREE");
	}
	
	public static Status getNoProfessionTypeMessage() 
	{
		return new Status("PLEASE PROVIDE PROFESSION TYPE IN INPUT");
	}
	
	public static Status getNoTargetProfessionTypeMessage() {
		return new Status("PLEASE PROVIDE TARGET PROFESSION IN INPUT");	
	}
	
	public static Status alreadyAppliedMessage() {
		return new Status("JOB ALREADY APPLIED");
	}

}
