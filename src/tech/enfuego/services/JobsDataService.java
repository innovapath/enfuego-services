package tech.enfuego.services;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;

import tech.enfuego.Status;
import tech.enfuego.db.EnfuegoDB;
import tech.enfuego.models.JobDescription;
import tech.enfuego.models.JobInstance;
import tech.enfuego.models.Skills;
import tech.enfuego.models.User;

import tech.enfuego.scoring.ScoreCalculator;
import tech.enfuego.services.helpers.JobDataserviceHelper;

@Path("/jobs")
public class JobsDataService {
	
	
	@POST
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response createEmptyJob(JobDescription job) {
		if(job.getProfessionType() == null) {
			return Response.status(javax.ws.rs.core.Response.Status.BAD_REQUEST).entity(Status.getNoProfessionTypeMessage()).build();
		}
		job = JobDataserviceHelper.createEmptyDescription(job);
		Datastore ds = EnfuegoDB.getDataStore();
		ds.save(job);
		return Response.status(javax.ws.rs.core.Response.Status.OK).entity(job).build();
	}
	
	
	
	@GET
	@Path("/{jobId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getJob(@PathParam("jobId") String jobId) {
		Datastore ds = EnfuegoDB.getDataStore();
		ObjectId objectId = new ObjectId(jobId);
		JobDescription job  = ds.get(JobDescription.class, objectId);
		if (job == null) {
			return Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND).entity(Status.getNoSuchJobMessage()).build();
		}
		
		return Response.status(javax.ws.rs.core.Response.Status.OK).entity(job).build();
	}
	
	
	@PUT
	@Path("/{jobId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateJob(@PathParam("jobId") String jobId, JobDescription newjob) {
		Datastore ds = EnfuegoDB.getDataStore();
		ObjectId objectId = new ObjectId(jobId);
		JobDescription job = ds.get(JobDescription.class, objectId);
		if(job == null) {
			return Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND).entity(Status.getNoSuchJobMessage()).build();
		}
		newjob.set_id(job.get_id());
		ds.save(newjob);
		return Response.status(javax.ws.rs.core.Response.Status.OK).entity(newjob).build();
		
		
	}
	
	@POST
	@Path("/{jobId}/users/{email}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response applyJob(@PathParam("jobId") String jobId, @PathParam("email") String email) {
		
		Datastore ds = EnfuegoDB.getDataStore();
		List<User> list = ds.createQuery(User.class).field("email").equal(email).asList();
		
		ObjectId objectId = new ObjectId(jobId);
		JobDescription job  = ds.get(JobDescription.class, objectId);
		
		if(list.size() == 0) {
			return Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND).entity(Status.getNoUserMessage()).build();
		}
		
		if (job == null) {
			return Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND).entity(Status.getNoSuchJobMessage()).build();
		}
		
		User user = list.get(0);
		
		
		double score = ScoreCalculator.calculateFinalScore(user, job);
		JobInstance jobinstance = new JobInstance();
		jobinstance.setId(job.get_id());
		jobinstance.setScore(score);
		List<Skills> missingSkills = new ArrayList<Skills>(ScoreCalculator.getMssingSkills(user, job));
		jobinstance.setMissingSkills(missingSkills);
		jobinstance.setApplied(true);
		DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		jobinstance.setAppliedOn(formatter.format(new Date()));
		
		jobinstance.setCompany(job.getCompanyName());
		jobinstance.setJobTitle(job.getJobTitle());
		jobinstance.setLocation(job.getCompanyLocation());
		
		if(user.getAppliedJobs() != null && user.getAppliedJobs().contains(jobinstance)) {
			
			JobInstance appliedJobinstance = user.getAppliedJobs().get(user.getAppliedJobs().indexOf(jobinstance));
			if (appliedJobinstance.isApplied())
			{
				return Response.status(javax.ws.rs.core.Response.Status.CONFLICT).entity(Status.alreadyAppliedMessage()).build();
			}
			user.getAppliedJobs().remove(jobinstance);
		}
		
		if(user.getAppliedJobs() == null) {
			List<JobInstance> appliedJobs = new ArrayList<JobInstance>(); 
			user.setAppliedJobs(appliedJobs);
		}
		
		user.getAppliedJobs().add(jobinstance);
		
		ds.save(user);
		
		return Response.ok().entity(jobinstance).build();
		
	}

}
