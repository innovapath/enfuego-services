package tech.enfuego.services.helpers;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import tech.enfuego.models.Accomplishments;
import tech.enfuego.models.Address;
import tech.enfuego.models.Degree;
import tech.enfuego.models.GeneralInfo;
import tech.enfuego.models.JobHistory;
import tech.enfuego.models.Membership;
import tech.enfuego.models.SearchMarkets;
import tech.enfuego.models.Skills;
import tech.enfuego.models.User;

public class UserDataServiceHelper {
	
	
	public static User createEmptyUser(User user) {
		// SET DUMMY for GENERAL INFO
		GeneralInfo generalInfo = new GeneralInfo();
		generalInfo.setGenEmail(user.getEmail());
		Address address = new Address();
		//address.setId(ObjectId.get());
		generalInfo.setAddress(address);
		user.setGeneralInfo(generalInfo);
		
		// SET DUMMY for SKILLS
		List<Skills> skills = new ArrayList<Skills>();
		user.setSkills(skills);
		
		
		//SET DUMMY for SEARCH MARKETS
		List<SearchMarkets> searchMarkets = new ArrayList<SearchMarkets>();
		user.setSearchMarkets(searchMarkets);
		
		// SET DUMMY for degrees
		List<Degree> degrees = new ArrayList<Degree>();
		user.setDegrees(degrees);
		
		// SET DUMMY for jobHistory
		List<JobHistory> jobs = new ArrayList<JobHistory>();		
		user.setJobHistory(jobs);
		
		
		// SET DUMMY for jobHistory
		Membership membership = new Membership();
		membership.setId(ObjectId.get());
		user.setMembership(membership);
		
		//SET Accomplishments
		List<Accomplishments> accs = new ArrayList<Accomplishments>();
		user.setAccs(accs);
	
		return user;
	}

}
