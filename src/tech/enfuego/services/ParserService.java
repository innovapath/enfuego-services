package tech.enfuego.services;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.tika.exception.TikaException;
import org.bson.types.ObjectId;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.mongodb.morphia.Datastore;
import org.xml.sax.SAXException;

import gate.util.GateException;
import tech.enfuego.Constants.FILE_TYPES;
import tech.enfuego.Constants.PROFESSION_TYPES;
import tech.enfuego.db.EnfuegoDB;
import tech.enfuego.models.JobDescription;
import tech.enfuego.models.User;
import tech.enfuego.services.helpers.JobDataServiceHelper;
import tech.enfuego.services.helpers.ParserServiceHelper;
import tech.enfuego.utils.FileUtils;



@Path("/parse")
public class ParserService {



	@POST
	@Path("/resume")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(value={MediaType.MULTIPART_FORM_DATA})
	public Response parseFile(@FormDataParam("file") InputStream inputStream,
			@FormDataParam("user") FormDataBodyPart userpart)
	{
		User user = null;
		Datastore ds = EnfuegoDB.getDataStore();
		try 
		{

			userpart.setMediaType(MediaType.APPLICATION_JSON_TYPE);
			user = (User) userpart.getValueAs(User.class);	
			System.out.println("STEP 1 : The user name is :"+user.getEmail());
			List<User> list = ds.createQuery(User.class).field("email").equal(user.getEmail()).asList();

			InputStream [] copies = FileUtils.clone(inputStream, 2);
			File file = FileUtils.convertoHTML(copies[0]);
			String contentForSkills = FileUtils.getStringUsingTika(copies[1]);

			if(list.size() > 0)
			{
				return Response.status(Status.CONFLICT).entity(tech.enfuego.Status.getExistingUserMessage()).build();
			}

			user = ParserServiceHelper.fillUserDetails(file, FILE_TYPES.RESUME, user, contentForSkills);
			//newUser.setUsername(user.getUsername());
			System.out.println("STEP 7 : Filled user details :"+user.getEmail());

			List<JobDescription> jobs = ds.createQuery(JobDescription.class).field("professionType").equal(user.getTargetProfession()).asList();
			if (jobs != null) {
				for(JobDescription job : jobs) {
					if (job != null)
					{
						user = JobDataServiceHelper.createJobInstanceFromJobDescription(user, job);
					}
				}
			}

			ds.save(user);
			System.out.println("STEP 8 : Saved user in DB :"+user.getEmail());
		} 
		catch (GateException | IOException | SAXException | TikaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.serverError().entity("UNEXPECTED SYSTEM ERROR").build();
		}
		return Response.ok().entity(user).build();
	}


	@POST
	@Path("/resume/test")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(value={MediaType.MULTIPART_FORM_DATA})
	public Response parseFileTest(@FormDataParam("file") InputStream inputStream,
			@FormDataParam("user") FormDataBodyPart userpart)
	{
		User user = null;
		Datastore ds = EnfuegoDB.getDataStore();
		try 
		{

			userpart.setMediaType(MediaType.APPLICATION_JSON_TYPE);
			user = (User) userpart.getValueAs(User.class);	
			System.out.println("STEP 1 : The user name is :"+user.getEmail());
			List<User> list = ds.createQuery(User.class).field("email").equal(user.getEmail()).asList();

			InputStream [] copies = FileUtils.clone(inputStream, 2);
			File file = FileUtils.convertoHTML(copies[0]);
			String contentForSkills = FileUtils.getStringUsingTika(copies[1]);

			user = ParserServiceHelper.fillUserDetails(file, FILE_TYPES.RESUME, user, contentForSkills);

			if(list.size() > 0)
			{
				user.set_id(list.get(0).get_id());
			}
			//newUser.setUsername(user.getUsername());
			System.out.println("STEP 7 : Filled user details :"+user.getEmail());

			List<JobDescription> jobs = ds.createQuery(JobDescription.class).field("professionType").equal(user.getTargetProfession()).asList();
			for(JobDescription job : jobs) {
				user = JobDataServiceHelper.createJobInstanceFromJobDescription(user, job);
			}

			ds.save(user);
			System.out.println("STEP 8 : Saved user in DB :"+user.getEmail());
		} 
		catch (GateException | IOException | SAXException | TikaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.serverError().entity("UNEXPECTED SYSTEM ERROR").build();
		}
		return Response.ok().entity(user).build();
	}


	@POST
	@Path("/linkedIn")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response parseLinkedInFile(@FormDataParam("file") InputStream inputStream, 
			@FormDataParam("user") FormDataBodyPart userpart
			) throws SAXException, TikaException
	{

		User user = null;
		Datastore ds = EnfuegoDB.getDataStore();
		try 
		{
			userpart.setMediaType(MediaType.APPLICATION_JSON_TYPE);
			user = userpart.getValueAs(User.class);
			List<User> list = ds.createQuery(User.class).field("email").equal(user.getEmail()).asList();

			InputStream [] copies = FileUtils.clone(inputStream, 2);
			File file = FileUtils.convertoHTML(copies[0]);
			String contentForSkills = FileUtils.getStringUsingTika(copies[1]);

			if(list.size() > 0)
			{
				return Response.status(Status.CONFLICT).entity(tech.enfuego.Status.getExistingUserMessage()).build();
			}
			user = ParserServiceHelper.fillUserDetails(file, FILE_TYPES.LINKEDIN, user, contentForSkills);

			List<JobDescription> jobs = ds.createQuery(JobDescription.class).field("professionType").equal(user.getTargetProfession()).asList();
			for(JobDescription job : jobs) {
				user = JobDataServiceHelper.createJobInstanceFromJobDescription(user, job);
			}

			ds.save(user);
		} 
		catch (GateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.serverError().entity("UNEXPECTED SYSTEM ERROR").build();
		}
		return Response.ok().entity(user).build();
	}

	@POST
	@Path("/linkedIn/test")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response parseLinkedInFileTest(@FormDataParam("file") InputStream inputStream,
			@FormDataParam("user") FormDataBodyPart userpart
			) throws SAXException, TikaException
	{

		User user = null;
		Datastore ds = EnfuegoDB.getDataStore();
		try 
		{
			userpart.setMediaType(MediaType.APPLICATION_JSON_TYPE);
			user = userpart.getValueAs(User.class);
			List<User> list = ds.createQuery(User.class).field("email").equal(user.getEmail()).asList();

			InputStream [] copies = FileUtils.clone(inputStream, 2);
			File file = FileUtils.convertoHTML(copies[0]);
			String contentForSkills = FileUtils.getStringUsingTika(copies[1]);

			if(list.size() > 0)
			{
				user.set_id(list.get(0).get_id());
			}
			user = ParserServiceHelper.fillUserDetails(file,  FILE_TYPES.LINKEDIN, user, contentForSkills);

			List<JobDescription> jobs = ds.createQuery(JobDescription.class).field("professionType").equal(user.getTargetProfession()).asList();
			for(JobDescription job : jobs) {
				user = JobDataServiceHelper.createJobInstanceFromJobDescription(user, job);
			}

			ds.save(user);
		} 
		catch (GateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.serverError().entity("UNEXPECTED SYSTEM ERROR").build();
		}
		return Response.ok().entity(user).build();
	}

	@POST
	@Path("/jobs/")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response parseJobProfile(@FormDataParam("file") InputStream inputStream, 
			@FormDataParam("job") FormDataBodyPart jobpart)
	{
		JobDescription job = null;
		try
		{
			jobpart.setMediaType(MediaType.APPLICATION_JSON_TYPE);
			job = (JobDescription) jobpart.getValueAs(JobDescription.class);	
			Datastore ds = EnfuegoDB.getDataStore();
			InputStream [] copies = FileUtils.clone(inputStream, 2);

			File file = FileUtils.convertoHTML(copies[0]);
			String contentForSkills = FileUtils.getStringUsingTika(copies[1]);

			job = ParserServiceHelper.fillJobDetails(file, FILE_TYPES.JOB_DESCRIPTION, job, contentForSkills);
			job.set_id(ObjectId.get());
			ds.save(job);
		}
		catch(IOException | TikaException | GateException | SAXException e) {
			e.printStackTrace();
			return Response.serverError().entity("UNEXPECTED SYSTEM ERROR").build();
		}
		return Response.ok().entity(job).build();
	}



}
