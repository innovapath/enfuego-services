package tech.enfuego;


import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

import tech.enfuego.services.JobsDataService;
import tech.enfuego.services.ParserService;
import tech.enfuego.services.UserDataService;


/**
 * 
 * @author abhishek
 * <p>
 * This project is a Dynamic Web project with REST services based on JAX-RS framework.
 * We also use Jersey framework which was built on top of the JAX-RS framework.
 * 
 * <p>
 * In this class you simply define the service classes in your application. 
 * All the REST web services are defined in the below classes
 *
 */
@ApplicationPath("/")
public class AppConfig  extends ResourceConfig{

	public AppConfig()
	{
		super(ParserService.class, UserDataService.class, JobsDataService.class, MultiPartFeature.class, CORSResponseFilter.class);
	}
}
