package tech.enfuego.services.helpers;

import java.util.List;

import org.bson.types.ObjectId;

import java.util.ArrayList;

import tech.enfuego.models.Degree;
import tech.enfuego.models.JobDescription;
import tech.enfuego.models.Skills;

public class JobDataserviceHelper {
	
	
	public static JobDescription createEmptyDescription(JobDescription job) {
		
		List<Skills> skills = new ArrayList<Skills>();
		List<Degree> degrees = new ArrayList<Degree>();
		job.setSkills(skills);
		job.setDegrees(degrees);
		job.set_id(ObjectId.get());
		return job;
		
		
	}

}
