package tech.enfuego.services.helpers;

import java.util.ArrayList;
import java.util.List;

import org.mongodb.morphia.Datastore;

import tech.enfuego.db.EnfuegoDB;
import tech.enfuego.models.JobDescription;
import tech.enfuego.models.JobInstance;
import tech.enfuego.models.Skills;
import tech.enfuego.models.User;
import tech.enfuego.scoring.ScoreCalculator;

public class JobDataServiceHelper {
	
	
	public static User createJobInstanceFromJobDescription(User user, JobDescription job) {
		JobInstance jobinstance = new JobInstance();
		jobinstance.setId(job.get_id());
		
		if(user.getAppliedJobs() == null) {
			List<JobInstance> availableJobs = new ArrayList<JobInstance>();
			user.setAppliedJobs(availableJobs);
		}
		
		if(!user.getAppliedJobs().contains(jobinstance)) {
			jobinstance = getScore(user, job, jobinstance);
			user.getAppliedJobs().add(jobinstance);
		}
		return user;
		
	}
	
	
	public static User updateJobInstances(User user) {
		Datastore ds = EnfuegoDB.getDataStore();
		if(user.getAppliedJobs() != null ) {
			
			for(JobInstance jobinstance : user.getAppliedJobs()) {
				JobDescription job  = ds.get(JobDescription.class, jobinstance.getId());
				if(job != null) {
					jobinstance = getScore(user, job, jobinstance);
				}
			}
		}
		return user;
	}
	
	public static JobInstance getScore(User user, JobDescription job, JobInstance jobinstance) {
		double score = ScoreCalculator.calculateFinalScore(user, job);
		jobinstance.setScore(score);
		List<Skills> missingSkills = new ArrayList<Skills>(ScoreCalculator.getMssingSkills(user, job));
		jobinstance.setMissingSkills(missingSkills);
		jobinstance.setApplied(false);
		jobinstance.setCompany(job.getCompanyName());
		jobinstance.setJobTitle(job.getJobTitle());
		jobinstance.setLocation(job.getCompanyLocation());
		return jobinstance;
	}

}
