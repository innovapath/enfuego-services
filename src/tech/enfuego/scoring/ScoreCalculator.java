package tech.enfuego.scoring;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.joda.time.Years;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

import com.google.common.collect.Sets;

import me.xdrop.fuzzywuzzy.FuzzySearch;
import tech.enfuego.models.Degree;
import tech.enfuego.models.JobDescription;
import tech.enfuego.models.JobHistory;
import tech.enfuego.models.Skills;
import tech.enfuego.models.User;
import tech.enfuego.Constants;





/**
 * 
 * @author abhishek
 * 
 * In this class we define methods to get a score between a users profile and a job description
 *
 */
public class ScoreCalculator {
	
	/**
	 * Method to calculate the final score between a user and a job
	 * There are 3 different factors with different weights.
	 * 2/5 - For the SkillSet 
	 * 2/5 - For the workExperience in number of years
	 * 1/5 - For the degree requirements as mentioned by the job description
	 * @param user
	 * @param job
	 * @return
	 */
	public static double calculateFinalScore(User user, JobDescription job) {

		double finalScore = 0.0d;
		finalScore = ((float)2/5)*calculateScore(user, job) + 
				((float)2/5)*getScoreForExperience(user, job) + 
				((float)1/5)*Math.max(getApproximateScoreForDegree(user, job), getScoreForDegrees(user, job)); 

		return Math.round(finalScore);
	}



	/***
	 * In this method, we calculate the overlap score in between the user skills and job description skills
	 * For example : 
	 * <br>
	 * User Skills U = (A, B, C)
	 * <br>
	 * Job Skills J = (A, B, C, D)
	 * <br>
	 * Overlap Score = len(intersection(set(U), set(J))) / len(set(J))
	 * <br>
	 * In this case it will be 3/4 = 75%
	 * 
	 * @param user
	 * @param job
	 * @return
	 */
	public static double calculateScore(User user, JobDescription job) {

		double overlapScore = 0.0d;
		if(user.getSkills() == null) {
			return 0.0;
		}
		
		if (job.getSkills() == null) {
			return 100.0;
		}
		Set<Skills> userSkills = new HashSet<Skills>(user.getSkills());
		Set<Skills> jobSkills = new HashSet<Skills>(job.getSkills());

		Set<Skills> commonSkills = 	Sets.intersection(userSkills, jobSkills);


		if (commonSkills.size() > 0) {
			overlapScore = ((float)commonSkills.size()/jobSkills.size()) * 100;
		}


		return overlapScore;
	}
	
	/**
	 * Return the common skills in between the job description skillset and the user skillset
	 * @param user
	 * @param job
	 * @return
	 */
	public static Set<Skills> getCommonSkills(User user, JobDescription job){
		if(user.getSkills() == null && job.getSkills() == null) {
			return new HashSet<Skills>();
		}
		if(user.getSkills() == null) {
			return new HashSet<Skills>(job.getSkills());
		}
		if(job.getSkills() == null) {
			return new HashSet<Skills>();
		}
		Set<Skills> userSkills = new HashSet<Skills>(user.getSkills());
		Set<Skills> jobSkills = new HashSet<Skills>(job.getSkills());		
		return Sets.intersection(jobSkills, userSkills);
	}
	
	/**
	 * Return the set of skills that are missing i.e. required by the job description but missing in the user profile
	 * @param user
	 * @param job
	 * @return
	 */
	public static  Set<Skills> getMssingSkills(User user, JobDescription job){
		if(user.getSkills() == null && job.getSkills() == null) {
			return new HashSet<Skills>();
		}
		if(user.getSkills() == null) {
			return new HashSet<Skills>(job.getSkills());
		}
		if(job.getSkills() == null) {
			return new HashSet<Skills>();
		}
		Set<Skills> userSkills = new HashSet<Skills>(user.getSkills());
		Set<Skills> jobSkills = new HashSet<Skills>(job.getSkills());		
		return Sets.difference(jobSkills, userSkills);
	}

	/**
	 * Return the approximate String matching score in between the user's degree and job description required degree
	 * @param user
	 * @param job
	 * @return
	 */
	public static double getApproximateScoreForDegree(User user, JobDescription job) {

		double score = 0.0d;
		int counter = 0;
		System.out.println("IS JOB NULL :"+job);
		if (job.getDegrees() == null || user.getDegrees() == null) {
			return 0;
		}
		for (Degree udegree : user.getDegrees()) {
			for(Degree jdegree : job.getDegrees()) {
				
				if (udegree.getDegreeType() != null && jdegree.getDegreeType() != null && FuzzySearch.partialRatio(udegree.getDegreeType(), jdegree.getDegreeType()) >= 75) {
					score += 100;
					counter += 1;
				}
			}
		}

		if (score == 0 || counter == 0) {
			return 0;
		}

		return ((float)score/job.getSkills().size());
	}


	/**
	 * Get a score for degree requirements. 
	 * <p>
	 * Again this is an overlap score
	 * @param user
	 * @param job
	 * @return
	 */
	public static double getScoreForDegrees(User user, JobDescription job) {

		double overlapScore = 0.0d;
		if (job.getDegrees() == null || user.getDegrees() == null) {
			return 0;
		}
		Set<Degree> userDegrees = new HashSet<Degree>(user.getDegrees());
		Set<Degree> jobDegrees = new HashSet<Degree>(job.getDegrees());

		Set<Degree> commonSkills = Sets.intersection(userDegrees, jobDegrees);

		if (commonSkills.size() > 0) {
			overlapScore = ((float)commonSkills.size()/jobDegrees.size()) * 100;
		}

		return overlapScore;
	}
	
	/**
	 * Get a score for the number of years of work experience
	 * <p>
	 * Example if the job description asks for 8 years and the user has 3 years of experience 
	 * then 3/8 is the score of work experience
	 * 
	 * @param user
	 * @param job
	 * @return
	 */
	public static double getScoreForExperience(User user, JobDescription job) {
		double expScore = 0.0d;
		int userExperience = getTotalUserExp(user);

		int maxJobExp = job.getMaxYearsOfExp();

		if (maxJobExp == 0) {
			return 100.0;
		}
		else {
			expScore = ((float)userExperience/maxJobExp) * 100;
		}

		return expScore;

	}
	
	/**
	 * Get the total experience in years after summing the experience for each job history in the user profile
	 * @param user
	 * @return
	 */
	public static int getTotalUserExp(User user) {

		int totalExp = 0;
		if(user.getJobHistory() == null) {
			return 0;
		}
		for(JobHistory exp : user.getJobHistory()) {

			totalExp += getExperienceForJob(exp);

		}
		return totalExp;
	}

	/**
	 * Get the work experience in number of years for passed job.
	 * <p>
	 * In case of any exception 0 will be returned
	 * @param job
	 * @return
	 */
	public static int getExperienceForJob(JobHistory job) {
		int diffInYears = 0;
		try
		{
			Calendar calendar = Calendar.getInstance();
			calendar.clear();
			calendar.set(Calendar.MONTH, Constants.monthMap.getOrDefault(job.getStartMonth(), 0));
			calendar.set(Calendar.YEAR, Integer.parseInt(job.getStartYear()));
			Date startdate = calendar.getTime();
			calendar.clear();
			calendar.set(Calendar.MONTH, Constants.monthMap.getOrDefault(job.getEndMonth(), 0));
			calendar.set(Calendar.YEAR, Integer.parseInt(job.getEndYear()));
			Date enddate = calendar.getTime();
			diffInYears = Math.round((enddate.getTime() - startdate.getTime()) / 1000 / 60 / 60 / 24 / 365);
			return diffInYears;
		}

		catch( Exception e) {

			return 0;
		}
	}


}
