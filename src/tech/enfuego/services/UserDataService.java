package tech.enfuego.services;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.tika.exception.TikaException;
import org.bson.types.ObjectId;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.mongodb.morphia.Datastore;
import org.xml.sax.SAXException;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.healthmarketscience.jackcess.util.OleBlob.ContentType;

import gate.util.GateException;
import tech.enfuego.Status;
import tech.enfuego.S3Service.S3ServiceProvider;
import tech.enfuego.Constants.FILE_TYPES;
import tech.enfuego.db.EnfuegoDB;
import tech.enfuego.models.Accomplishments;
import tech.enfuego.models.Certifications;
import tech.enfuego.models.Degree;
import tech.enfuego.models.JobDescription;
import tech.enfuego.models.JobHistory;
import tech.enfuego.models.JobInstance;
import tech.enfuego.models.SearchMarkets;
import tech.enfuego.models.Skills;
import tech.enfuego.models.User;
import tech.enfuego.services.helpers.JobDataServiceHelper;
import tech.enfuego.services.helpers.ParserServiceHelper;
import tech.enfuego.services.helpers.UserDataServiceHelper;
import tech.enfuego.utils.FileUtils;

@Path("/users")
public class UserDataService {

	
	
	
	@PUT
	@Path("/profilepic")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(value={MediaType.MULTIPART_FORM_DATA})
	public Response uploadProfileImage(@FormDataParam("pic") InputStream inputStream,
			@FormDataParam("user") FormDataBodyPart userpart) {	
		User user = null;
		URL url;

		Datastore ds = EnfuegoDB.getDataStore();

		try 
		{

			userpart.setMediaType(MediaType.APPLICATION_JSON_TYPE);
			user = (User) userpart.getValueAs(User.class);
			//System.out.println("STEP 1 : The user name is :"+user.getEmail());
			List<User> list = ds.createQuery(User.class).field("email").equal(user.getEmail()).asList();

			if(list.size() == 0)
			{
				return Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND).entity(tech.enfuego.Status.getNoUserMessage()).build();
			}
			
			user = list.get(0);

			ObjectMetadata objMetadata = new ObjectMetadata();
			objMetadata.setContentType("image/base64");
			//objMetadata.setContentLength(inputStream.available());
			url = S3ServiceProvider.uploadFiletoS3(inputStream, objMetadata, user.getEmail());
			user.setProfileImageURL(url.toString());
			ds.save(user);
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response.serverError().entity(e).build();
		}
		return Response.ok().entity(url).build();		
		
	}
	
	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUsers() {
		Datastore ds = EnfuegoDB.getDataStore();
		List<User> allusers = ds.find(User.class).project("email", true).asList();
		List<String> emails = new ArrayList<String>();
		
		if(allusers.isEmpty()) {
			return Response.noContent().build();
		}
		
		for(User user : allusers) {
			if(user.getEmail() != null) {
				emails.add(user.getEmail());
			}
		}
		
		return Response.ok().entity(emails).build();
	}
	
	@POST
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createNewUser(User user) {
		Datastore ds = EnfuegoDB.getDataStore();
		user = UserDataServiceHelper.createEmptyUser(user);
		ds.save(user);
		return Response.ok().entity(user).build();
	}


	@PUT
	@Path("/{email}/profile")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateExistingUser(@PathParam("email") String email, User user)
	{

		Datastore ds = EnfuegoDB.getDataStore();
		List<User> list = ds.createQuery(User.class).field("email").equal(email).asList();
		if(list.size() > 0)
		{
			User current_user = list.get(0);
			if(user.getGeneralInfo() != null)
			{
				current_user.setGeneralInfo(user.getGeneralInfo());

			}
			if(user.getProfileImageURL() != null)
			{
				current_user.setProfileImageURL(user.getProfileImageURL());
			}
			if(user.getMembership() != null)
			{
				current_user.setMembership(user.getMembership());
			}


			current_user.setCareerScale(user.getCareerScale());
			current_user.setIncomeReqMax(user.getIncomeReqMax());
			current_user.setIncomeReqMin(user.getIncomeReqMin());
			current_user.setSetupComplete(user.isSetupComplete());

			if(user.getSkillsCloudSrc() != null)
			{
				current_user.setSkillsCloudSrc(user.getSkillsCloudSrc());
			}

			ds.save(current_user);

			return Response.ok().entity(current_user).build();

		}
		else 
			return Response.noContent().entity("NO SUCH USER").build();

	}


	@DELETE
	@Path("/{email}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteUser(@PathParam("email") String email) {
		Datastore ds = EnfuegoDB.getDataStore();

		List<User> list = ds.createQuery(User.class).field("email").equal(email).asList();
		if(list.size() == 0)
		{
			return Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND).entity(Status.getNoUserMessage()).build();
		}
		ds.delete(ds.createQuery(User.class).field("email").equal(email));
		return Response.ok().entity(Status.getDeletedMessage()).build();
	}


	@GET
	@Path("/{email}/profile")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUser(@PathParam("email") String email){

		Datastore ds = EnfuegoDB.getDataStore();
		List<User> list = ds.createQuery(User.class).field("email").equal(email).asList();
		
		

		if(list.size() > 0)
		{
			User currentUser = list.get(0);
			
			List<JobDescription> jobs = ds.createQuery(JobDescription.class).field("professionType").equal(currentUser.getTargetProfession()).asList();
			for(JobDescription job : jobs) {
				currentUser = JobDataServiceHelper.createJobInstanceFromJobDescription(currentUser, job);
			}
			return Response.ok().entity(currentUser).build();
		}
		else
			return Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND).entity(Status.getNoUserMessage()).build();


	}


	@PUT
	@Path("/{email}/jobs/{jobId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateJobHistory(@PathParam("email") String email, @PathParam("jobId") String jobId, JobHistory job) {

		Datastore ds = EnfuegoDB.getDataStore();
		List<User> list = ds.createQuery(User.class).field("email").equal(email).asList();
		
		List<User> foundList = ds.createQuery(User.class).field("jobHistory.id").equal(job.getId()).project("jobHistory.$", true).asList();
		if(foundList.size() == 0) {
			return Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND).entity(Status.getNoSuchJobMessage()).build();
		}


		if(list.size() > 0)
		{
			User user = list.get(0);
			if(user.getJobHistory() == null)
			{
				return Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND).entity(Status.getNoSuchJobMessage()).build();
			}
			for (JobHistory jh : user.getJobHistory())
			{
				if(jh.getId().toHexString().equals(jobId))
				{
					user.getJobHistory().remove(jh);
					break;
				}
			}
			job.setId(new ObjectId(jobId));
			user.getJobHistory().add(job);
			JobDataServiceHelper.updateJobInstances(user);
			ds.save(user);
			List<User> tempList = ds.createQuery(User.class).field("jobHistory.id").equal(job.getId()).project("jobHistory.$", true).asList();
			return Response.ok().entity(tempList.get(0).getJobHistory().get(0)).build();
		}
		else
			return Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND).entity(Status.getNoUserMessage()).build();

	}

	@POST
	@Path("/{email}/jobs")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addJobToJobHistory(@PathParam("email") String email, JobHistory job) {

		Datastore ds = EnfuegoDB.getDataStore();
		List<User> list = ds.createQuery(User.class).field("email").equal(email).asList();


		if(list.size() > 0)
		{
			User user = list.get(0);
			if(user.getJobHistory() == null)
			{
				List<JobHistory> jobHistory = new ArrayList<JobHistory>();
				user.setJobHistory(jobHistory);
			}
			job.setId(ObjectId.get());
			user.getJobHistory().add(job);
			JobDataServiceHelper.updateJobInstances(user);
			ds.save(user);
			List<User> tempList = ds.createQuery(User.class).field("jobHistory.id").equal(job.getId()).project("jobHistory.$", true).asList();
			return Response.ok().entity(tempList.get(0).getJobHistory().get(0)).build();
		}
		else
			return Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND).entity(Status.getNoUserMessage()).build();

	}

	@GET
	@Path("/{email}/jobs/{jobId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getJobHistory(@PathParam("email") String email, @PathParam("jobId") String jobId) {

		Datastore ds = EnfuegoDB.getDataStore();
		List<User> list = ds.createQuery(User.class).field("email").equal(email).asList();


		if(list.size() > 0)
		{
			User user = list.get(0);
			if(user.getJobHistory() == null)
			{
				return Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND).entity(Status.getNoSuchJobMessage()).build();
			}
			List<User> tempList = ds.createQuery(User.class).field("jobHistory.id").equal(new ObjectId(jobId)).project("jobHistory.$", true).asList();
			if(tempList.size() == 0)
			{
				return Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND).entity(Status.getNoSuchJobMessage()).build();
			}
			return Response.ok().entity(tempList.get(0).getJobHistory().get(0)).build();
		}
		else
			return Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND).entity(Status.getNoUserMessage()).build();

	}

	@DELETE
	@Path("/{email}/jobs/{jobId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteJobHistory(@PathParam("email") String email, @PathParam("jobId") String jobId) {
		Datastore ds = EnfuegoDB.getDataStore();

		List<User> tempList = ds.createQuery(User.class).field("jobHistory.id").equal(new ObjectId(jobId)).project("jobHistory.$", true).asList();
		if(tempList.size() == 0)
		{
			return Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND).entity(Status.getNoSuchJobMessage()).build();
		}

		List<User> list = ds.createQuery(User.class).field("email").equal(email).asList();
		if(list.size() > 0)
		{
			User user = list.get(0);
			if(user.getJobHistory() == null)
			{
				return Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND).entity(Status.getNoSuchJobMessage()).build();
			}
			for (JobHistory jh : user.getJobHistory())
			{
				if(jh.getId().toHexString().equals(jobId))
				{
					user.getJobHistory().remove(jh);
					break;
				}
			}
			ds.save(user);
			return Response.ok().entity(Status.getDeletedMessage()).build();
		}
		else
			return Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND).entity(Status.getNoUserMessage()).build();

	}




	@PUT
	@Path("/{email}/searchMarkets/{locationId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateSearchMarket(@PathParam("email") String email, @PathParam("locationId") String locationId, SearchMarkets sm) {

		Datastore ds = EnfuegoDB.getDataStore();
		List<User> list = ds.createQuery(User.class).field("email").equal(email).asList();
		
		List<User> foundSearchMarket = ds.createQuery(User.class).field("searchMarkets._locationId").equal(sm.get_locationId()).project("searchMarkets.$", true).asList();
		
		if(foundSearchMarket.size() == 0) {
			return Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND).entity(Status.getNoSuchSearchMarket()).build();
		}

		if(list.size() > 0)
		{
			User user = list.get(0);
			if(user.getSearchMarkets() == null)
			{
				return Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND).entity(Status.getNoSuchSearchMarket()).build();
			}
			for (SearchMarkets jh : user.getSearchMarkets())
			{
				if(jh.get_locationId().toHexString().equals(locationId))
				{
					user.getSearchMarkets().remove(jh);
					break;
				}
			}
			sm.set_locationId(new ObjectId(locationId));
			user.getSearchMarkets().add(sm);
			ds.save(user);
			List<User> tempList = ds.createQuery(User.class).field("searchMarkets._locationId").equal(sm.get_locationId()).project("searchMarkets.$", true).asList();
			return Response.ok().entity(tempList.get(0).getSearchMarkets().get(0)).build();
		}
		else
			return Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND).entity(Status.getNoUserMessage()).build();

	}

	@POST
	@Path("/{email}/searchMarkets")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addSearchMarket(@PathParam("email") String email, SearchMarkets sm) {

		Datastore ds = EnfuegoDB.getDataStore();
		List<User> list = ds.createQuery(User.class).field("email").equal(email).asList();


		if(list.size() > 0)
		{
			User user = list.get(0);
			if(user.getSearchMarkets() == null)
			{
				List<SearchMarkets> searchMarkets = new ArrayList<SearchMarkets>();
				user.setSearchMarkets(searchMarkets);
			}
			sm.set_locationId(ObjectId.get());
			user.getSearchMarkets().add(sm);
			ds.save(user);
			List<User> tempList = ds.createQuery(User.class).field("searchMarkets._locationId").equal(sm.get_locationId()).project("searchMarkets.$", true).asList();
			return Response.ok().entity(tempList.get(0).getSearchMarkets().get(0)).build();
		}
		else
			return Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND).entity(Status.getNoUserMessage()).build();

	}

	@GET
	@Path("/{email}/searchMarkets/{locationId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getSearchMarket(@PathParam("email") String email, @PathParam("locationId") String locationId) {

		Datastore ds = EnfuegoDB.getDataStore();
		List<User> list = ds.createQuery(User.class).field("email").equal(email).asList();
		
		List<User> foundSearchMarket = ds.createQuery(User.class).field("searchMarkets._locationId").equal(new ObjectId(locationId)).project("searchMarkets.$", true).asList();
		
		if(foundSearchMarket.size() == 0) {
			return Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND).entity(Status.getNoSuchSearchMarket()).build();
		}


		if(list.size() > 0)
		{
			User user = list.get(0);
			if(user.getSearchMarkets() == null)
			{
				return Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND).entity(Status.getNoSuchSearchMarket()).build();
			}
			List<User> tempList = ds.createQuery(User.class).field("searchMarkets._locationId").equal(new ObjectId(locationId)).project("searchMarkets.$", true).asList();
			if(tempList.size() == 0)
			{
				return Response.noContent().entity(Status.getNoSuchJobMessage()).build();
			}
			return Response.ok().entity(tempList.get(0).getSearchMarkets().get(0)).build();
		}
		else
			return Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND).entity(Status.getNoUserMessage()).build();

	}

	@DELETE
	@Path("/{email}/searchMarkets/{locationId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteSearchMarket(@PathParam("email") String email, @PathParam("locationId") String locationId) {
		Datastore ds = EnfuegoDB.getDataStore();

		List<User> tempList = ds.createQuery(User.class).field("searchMarkets._locationId").equal(new ObjectId(locationId)).project("searchMarkets.$", true).asList();
		if(tempList.size() == 0)
		{
			return Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND).entity(Status.getNoSuchSearchMarket()).build();
		}

		List<User> list = ds.createQuery(User.class).field("email").equal(email).asList();
		if(list.size() > 0)
		{
			User user = list.get(0);
			if(user.getSearchMarkets() == null)
			{
				return Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND).entity(Status.getNoSuchSearchMarket()).build();
			}
			for (SearchMarkets jh : user.getSearchMarkets())
			{
				if(jh.get_locationId().toHexString().equals(locationId))
				{
					user.getSearchMarkets().remove(jh);
					break;
				}
			}
			ds.save(user);
			return Response.ok().entity(Status.getDeletedMessage()).build();
		}
		else
			return Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND).entity(Status.getNoUserMessage()).build();

	}



	@PUT
	@Path("/{email}/skills/{skillId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateSkills(@PathParam("email") String email, @PathParam("skillId") String skillId, Skills s) {

		Datastore ds = EnfuegoDB.getDataStore();
		List<User> list = ds.createQuery(User.class).field("email").equal(email).asList();
		
		List<User> foundskillsList = ds.createQuery(User.class).field("skills.id").equal(s.getId()).project("skills.$", true).asList();
		if(foundskillsList.size() == 0)
		{
			return Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND).entity(Status.getNoSkillsMessage()).build();
		}


		if(list.size() > 0)
		{
			User user = list.get(0);
			if(user.getSkills() == null)
			{
				return Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND).entity(Status.getNoSkillsMessage()).build();
			}
			for (Skills jh : user.getSkills())
			{
				if(jh.getId().toHexString().equals(skillId))
				{
					user.getSkills().remove(jh);
					break;
				}
			}
			s.setId(new ObjectId(skillId));
			user.getSkills().add(s);
			JobDataServiceHelper.updateJobInstances(user);
			ds.save(user);
			List<User> tempList = ds.createQuery(User.class).field("skills.id").equal(s.getId()).project("skills.$", true).asList();
			return Response.ok().entity(tempList.get(0).getSkills().get(0)).build();
		}
		else
			return Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND).entity(Status.getNoUserMessage()).build();

	}

	@POST
	@Path("/{email}/skills")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addSkills(@PathParam("email") String email, Skills s) {

		Datastore ds = EnfuegoDB.getDataStore();
		List<User> list = ds.createQuery(User.class).field("email").equal(email).asList();


		if(list.size() > 0)
		{
			User user = list.get(0);
			if(user.getSkills() == null)
			{
				List<Skills> skills = new ArrayList<Skills>();
				user.setSkills(skills);
			}
			s.setId(ObjectId.get());
			user.getSkills().add(s);
			JobDataServiceHelper.updateJobInstances(user);
			ds.save(user);
			List<User> tempList = ds.createQuery(User.class).field("skills.id").equal(s.getId()).project("skills.$", true).asList();
			return Response.ok().entity(tempList.get(0).getSkills().get(0)).build();
		}
		else
			return Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND).entity(Status.getNoUserMessage()).build();

	}

	@GET
	@Path("/{email}/skills/{skillId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getSkill(@PathParam("email") String email, @PathParam("skillId") String skillId) {

		Datastore ds = EnfuegoDB.getDataStore();
		List<User> list = ds.createQuery(User.class).field("email").equal(email).asList();
		if(list.size() > 0)
		{
			User user = list.get(0);
			if(user.getSkills() == null)
			{
				return Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND).entity(Status.getNoSkillsMessage()).build();
			}
			List<User> tempList = ds.createQuery(User.class).field("skills.id").equal(new ObjectId(skillId)).project("skills.$", true).asList();
			if(tempList.size() == 0)
			{
				return Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND).entity(Status.getNoSkillsMessage()).build();
			}
			return Response.ok().entity(tempList.get(0).getSkills().get(0)).build();
		}
		else
			return Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND).entity(Status.getNoUserMessage()).build();

	}

	@DELETE
	@Path("/{email}/skills/{skillId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteSkills(@PathParam("email") String email, @PathParam("skillId") String skillId) {
		Datastore ds = EnfuegoDB.getDataStore();

		List<User> tempList = ds.createQuery(User.class).field("skills.id").equal(new ObjectId(skillId)).project("skills.$", true).asList();
		if(tempList.size() == 0)
		{
			return Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND).entity(Status.getNoSkillsMessage()).build();
		}

		List<User> list = ds.createQuery(User.class).field("email").equal(email).asList();
		if(list.size() > 0)
		{
			User user = list.get(0);
			if(user.getSkills() == null)
			{
				return Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND).entity(Status.getNoSkillsMessage()).build();
			}
			for (Skills jh : user.getSkills())
			{
				if(jh.getId().toHexString().equals(skillId))
				{
					user.getSkills().remove(jh);
					break;
				}
			}
			ds.save(user);
			return Response.ok().entity(Status.getDeletedMessage()).build();
		}
		else
			return Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND).entity(Status.getNoUserMessage()).build();

	}



	@PUT
	@Path("/{email}/degrees/{degreeId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateDegree(@PathParam("email") String email, @PathParam("degreeId") String degreeId, Degree d) {

		Datastore ds = EnfuegoDB.getDataStore();
		List<User> list = ds.createQuery(User.class).field("email").equal(email).asList();
		
		List<User> foundDegreeList = ds.createQuery(User.class).field("degrees.id").equal(d.getId()).project("degrees.$", true).asList();
		
		if(foundDegreeList.size() == 0) {
			return Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND).entity(Status.getNoDegreeMessage()).build();
		}


		if(list.size() > 0)
		{
			User user = list.get(0);
			if(user.getDegrees() == null)
			{
				return Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND).entity(Status.getNoDegreeMessage()).build();
			}
			for (Degree jh : user.getDegrees())
			{
				if(jh.getId().toHexString().equals(degreeId))
				{
					user.getDegrees().remove(jh);
					break;
				}
			}
			d.setId(new ObjectId(degreeId));
			user.getDegrees().add(d);
			JobDataServiceHelper.updateJobInstances(user);
			ds.save(user);
			List<User> tempList = ds.createQuery(User.class).field("degrees.id").equal(d.getId()).project("degrees.$", true).asList();
			return Response.ok().entity(tempList.get(0).getDegrees().get(0)).build();
		}
		else
			return Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND).entity(Status.getNoUserMessage()).build();

	}

	@POST
	@Path("/{email}/degrees")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addDegree(@PathParam("email") String email, Degree d) {

		Datastore ds = EnfuegoDB.getDataStore();
		List<User> list = ds.createQuery(User.class).field("email").equal(email).asList();


		if(list.size() > 0)
		{
			User user = list.get(0);
			if(user.getDegrees() == null)
			{
				List<Degree> degrees = new ArrayList<Degree>();
				user.setDegrees(degrees);
			}
			d.setId(ObjectId.get());
			user.getDegrees().add(d);
			JobDataServiceHelper.updateJobInstances(user);
			ds.save(user);
			List<User> tempList = ds.createQuery(User.class).field("degrees.id").equal(d.getId()).project("degrees.$", true).asList();
			return Response.ok().entity(tempList.get(0).getDegrees().get(0)).build();
		}
		else
			return Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND).entity(Status.getNoUserMessage()).build();

	}

	@GET
	@Path("/{email}/degrees/{degreeId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getDegree(@PathParam("email") String email, @PathParam("degreeId") String degreeId) {

		Datastore ds = EnfuegoDB.getDataStore();
		List<User> list = ds.createQuery(User.class).field("email").equal(email).asList();


		if(list.size() > 0)
		{
			User user = list.get(0);
			if(user.getDegrees() == null)
			{
				return Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND).entity(Status.getNoDegreeMessage()).build();
			}
			List<User> tempList = ds.createQuery(User.class).field("degrees.id").equal(new ObjectId(degreeId)).project("degrees.$", true).asList();
			if(tempList.size() == 0)
			{
				return Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND).entity(Status.getNoDegreeMessage()).build();
			}
			return Response.ok().entity(tempList.get(0).getDegrees().get(0)).build();
		}
		else
			return Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND).entity(Status.getNoUserMessage()).build();

	}

	@DELETE
	@Path("/{email}/degrees/{degreeId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteDegree(@PathParam("email") String email, @PathParam("degreeId") String degreeId) {
		Datastore ds = EnfuegoDB.getDataStore();

		List<User> tempList = ds.createQuery(User.class).field("degrees.id").equal(new ObjectId(degreeId)).project("degrees.$", true).asList();
		if(tempList.size() == 0)
		{
			return Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND).entity(Status.getNoDegreeMessage()).build();
		}

		List<User> list = ds.createQuery(User.class).field("email").equal(email).asList();
		if(list.size() > 0)
		{
			User user = list.get(0);
			if(user.getDegrees() == null)
			{
				return Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND).entity(Status.getNoDegreeMessage()).build();
			}
			for (Degree jh : user.getDegrees())
			{
				if(jh.getId().toHexString().equals(degreeId))
				{
					user.getDegrees().remove(jh);
					break;
				}
			}
			ds.save(user);
			return Response.ok().entity(Status.getDeletedMessage()).build();
		}
		else
			return Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND).entity(Status.getNoUserMessage()).build();

	}


	@PUT
	@Path("/{email}/accomplishments/{accId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateAcc(@PathParam("email") String email, @PathParam("accId") String accId, Accomplishments a) {

		Datastore ds = EnfuegoDB.getDataStore();
		List<User> list = ds.createQuery(User.class).field("email").equal(email).asList();


		if(list.size() > 0)
		{
			User user = list.get(0);
			if(user.getAccs() == null)
			{
				return Response.noContent().entity(Status.getNoSuchJobMessage()).build();
			}
			for (Accomplishments jh : user.getAccs())
			{
				if(jh.getId().toHexString().equals(accId))
				{
					user.getAccs().remove(jh);
					break;
				}
			}
			a.setId(new ObjectId(accId));
			user.getAccs().add(a);
			ds.save(user);
			List<User> tempList = ds.createQuery(User.class).field("accs.id").equal(a.getId()).project("accs.$", true).asList();
			return Response.ok().entity(tempList.get(0).getAccs().get(0)).build();
		}
		else
			return Response.status(javax.ws.rs.core.Response.Status.NOT_FOUND).entity(Status.getNoUserMessage()).build();

	}

	@POST
	@Path("/{email}/accomplishments")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addAcc(@PathParam("email") String email, Accomplishments a) {

		Datastore ds = EnfuegoDB.getDataStore();
		List<User> list = ds.createQuery(User.class).field("email").equal(email).asList();


		if(list.size() > 0)
		{
			User user = list.get(0);
			if(user.getAccs() == null)
			{
				List<Accomplishments> accs = new ArrayList<Accomplishments>();
				user.setAccs(accs);
			}
			a.setId(ObjectId.get());
			user.getAccs().add(a);
			ds.save(user);
			List<User> tempList = ds.createQuery(User.class).field("accs.id").equal(a.getId()).project("accs.$", true).asList();
			return Response.ok().entity(tempList.get(0).getAccs().get(0)).build();
		}
		else
			return Response.noContent().entity(Status.getNoUserMessage()).build();

	}

	@GET
	@Path("/{email}/accomplishments/{accId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getAcc(@PathParam("email") String email, @PathParam("accId") String accId) {

		Datastore ds = EnfuegoDB.getDataStore();
		List<User> list = ds.createQuery(User.class).field("email").equal(email).asList();


		if(list.size() > 0)
		{
			User user = list.get(0);
			if(user.getAccs() == null)
			{
				return Response.noContent().entity(Status.getNoSuchJobMessage()).build();
			}
			List<User> tempList = ds.createQuery(User.class).field("accs.id").equal(new ObjectId(accId)).project("accs.$", true).asList();
			if(tempList.size() == 0)
			{
				return Response.noContent().entity(Status.getNoSuchJobMessage()).build();
			}
			return Response.ok().entity(tempList.get(0).getAccs().get(0)).build();
		}
		else
			return Response.noContent().entity(Status.getNoUserMessage()).build();

	}

	@DELETE
	@Path("/{email}/accomplishments/{accId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteAcc(@PathParam("email") String email, @PathParam("accId") String accId) {
		Datastore ds = EnfuegoDB.getDataStore();

		List<User> tempList = ds.createQuery(User.class).field("accs.id").equal(new ObjectId(accId)).project("accs.$", true).asList();
		if(tempList.size() == 0)
		{
			return Response.noContent().entity(Status.getNoSuchJobMessage()).build();
		}

		List<User> list = ds.createQuery(User.class).field("email").equal(email).asList();
		if(list.size() > 0)
		{
			User user = list.get(0);
			if(user.getAccs() == null)
			{
				return Response.noContent().entity(Status.getNoSuchJobMessage()).build();
			}
			for (Accomplishments jh : user.getAccs())
			{
				if(jh.getId().toHexString().equals(accId))
				{
					user.getAccs().remove(jh);
					break;
				}
			}
			ds.save(user);
			return Response.ok().entity(Status.getDeletedMessage()).build();
		}
		else
			return Response.noContent().entity(Status.getNoUserMessage()).build();

	}


	@PUT
	@Path("/{email}/certifications/{certificationId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateCert(@PathParam("email") String email, @PathParam("certificationId") String certificationId,  Certifications c) {

		Datastore ds = EnfuegoDB.getDataStore();
		List<User> list = ds.createQuery(User.class).field("email").equal(email).asList();


		if(list.size() > 0)
		{
			User user = list.get(0);
			if(user.getCertifications() == null)
			{
				return Response.noContent().entity(Status.getNoSuchJobMessage()).build();
			}
			for (Certifications jh : user.getCertifications())
			{
				if(jh.getId().toHexString().equals(certificationId))
				{
					user.getCertifications().remove(jh);
					break;
				}
			}
			c.setId(new ObjectId(certificationId));
			user.getCertifications().add(c);
			ds.save(user);
			List<User> tempList = ds.createQuery(User.class).field("certifications.id").equal(c.getId()).project("certifications.$", true).asList();
			return Response.ok().entity(tempList.get(0).getCertifications().get(0)).build();
		}
		else
			return Response.noContent().entity(Status.getNoUserMessage()).build();

	}

	@POST
	@Path("/{email}/certifications")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addCert(@PathParam("email") String email, Certifications c) {

		Datastore ds = EnfuegoDB.getDataStore();
		List<User> list = ds.createQuery(User.class).field("email").equal(email).asList();


		if(list.size() > 0)
		{
			User user = list.get(0);
			if(user.getCertifications() == null)
			{
				List<Certifications> certs = new ArrayList<Certifications>();
				user.setCertifications(certs);
			}
			c.setId(ObjectId.get());
			user.getCertifications().add(c);
			ds.save(user);
			List<User> tempList = ds.createQuery(User.class).field("certifications.id").equal(c.getId()).project("certifications.$", true).asList();
			return Response.ok().entity(tempList.get(0).getCertifications().get(0)).build();
		}
		else
			return Response.noContent().entity(Status.getNoUserMessage()).build();

	}

	@GET
	@Path("/{email}/certifications/{certificationId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getCert(@PathParam("email") String email, @PathParam("certificationId") String certificationId) {

		Datastore ds = EnfuegoDB.getDataStore();
		List<User> list = ds.createQuery(User.class).field("email").equal(email).asList();


		if(list.size() > 0)
		{
			User user = list.get(0);
			if(user.getCertifications() == null)
			{
				return Response.noContent().entity(Status.getNoSuchJobMessage()).build();
			}
			List<User> tempList = ds.createQuery(User.class).field("certifications.id").equal(new ObjectId(certificationId)).project("certifications.$", true).asList();
			if(tempList.size() == 0)
			{
				return Response.noContent().entity(Status.getNoSuchJobMessage()).build();
			}
			return Response.ok().entity(tempList.get(0).getCertifications().get(0)).build();
		}
		else
			return Response.noContent().entity(Status.getNoUserMessage()).build();

	}

	@DELETE
	@Path("/{email}/certifications/{certificationId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteCertification(@PathParam("email") String email, @PathParam("certificationId") String certificationId) {
		Datastore ds = EnfuegoDB.getDataStore();

		List<User> tempList = ds.createQuery(User.class).field("certifications.id").equal(new ObjectId(certificationId)).project("certifications.$", true).asList();
		if(tempList.size() == 0)
		{
			return Response.noContent().entity(Status.getNoSuchJobMessage()).build();
		}

		List<User> list = ds.createQuery(User.class).field("email").equal(email).asList();
		if(list.size() > 0)
		{
			User user = list.get(0);
			if(user.getCertifications() == null)
			{
				return Response.noContent().entity(Status.getNoSuchJobMessage()).build();
			}
			for (Certifications jh : user.getCertifications())
			{
				if(jh.getId().toHexString().equals(certificationId))
				{
					user.getCertifications().remove(jh);
					break;
				}
			}
			ds.save(user);
			return Response.ok().entity(Status.getDeletedMessage()).build();
		}
		else
			return Response.noContent().entity(Status.getNoUserMessage()).build();

	}
	
	
	

}
