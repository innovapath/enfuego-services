package tech.enfuego;

import java.util.HashMap;
import java.util.Map;

import tech.enfuego.resumewriter.skills.directlyperformed.SingleSkillPresentTense;
import tech.enfuego.resumewriter.skills.participated.SingleSkillPastTense;
/**
 * 
 * @author abhishek
 * 
 * Define constants and configuration related values here
 *
 */
public class Constants {
	
	/**
	 * 
	 * @author abhishek
	 * <p>
	 * 3 different File types are supported as of now by the Enfuego Parser Service.
	 * <p>
	 * <ul>
	 * <li> LINKEDIN - LinkedIn profile PDF of the user
	 * <li> RESUME - A resume file of the user. Can be *.pdf, *.doc, *.docx, *.txt
	 * <li> JOB DESCRIPTION - A job description document. Can be *.pdf, *.doc, *.docx, *.txt
	 * </ul>
	 */
	public static enum FILE_TYPES {
		LINKEDIN, RESUME, JOB_DESCRIPTION
	}
	
	/**
	 * 
	 * @author abhishek
	 * 
	 * <p>
	 * This is defined to load the corresponding Doc2Vector trained model for  the corresponding profession.
	 * We have defined 2 Profession types as of now. But as and when we train a deep learning neural network on 
	 * job descriptions belonging to more professions, we will expand 
	 *
	 */
	public static enum PROFESSION_TYPES {
		MANAGEMENT, SOFTWARE
	}
	
	public final static String CALIBRI_FONT_FILE = "calibri.ttf";
	
	
	//The *.gapp files for each of the FileType that we will load for information extraction.
	public final static String RESUME_GAPP_FILE_NAME = "ResumeGATE.gapp";
	public final static String LINKEDIN_GAPP_FILE_NAME = "LinkedInGATE.gapp";
	public final static String JOB_DESCRIPTION_GAPP_FILE_NAME = "JobDescriptionParser.gapp";
	
	
	// Deep learning training. Define the folder name of the job descriptions.
	public final static String MANAGERS_TRAINING_SET = "Managers";
	public final static String MANAGERS_PARAGRAPH_VECTORS = "ManagersPara2Vec.zip";
	
	// Deep learning training. Define the folder name of the job descriptions.
	public final static String SOFTWARE_ENGINEER_SET = "Software_Development";
	public final static String SOFTWARE_PARAGRAPH_VECTORS = "SoftwareEngineerPara2Vec.zip";
	
	
	public final static Map<PROFESSION_TYPES, String> professionMap = new HashMap<PROFESSION_TYPES, String>();
	public final static Map<PROFESSION_TYPES, String> paragraphVectorMap = new HashMap<PROFESSION_TYPES, String>();
	
	public final static Map<FILE_TYPES, String> fileMap = new HashMap<FILE_TYPES, String>();
	
	public final static Map<String, Integer> monthMap = new HashMap<String, Integer>();
	
	// This is used in dynamic resume generation for generating english sentences.
	public final static String IN_A_TEAM = "participated_in_a_team";
	public final static String SUPPORTED = "supported_people";
	public final static String SUPERVISED = "supervised";
	public final static String DIRECTLY_PERFORMED = "directly_performed";
	
	// Which class to load based on actions. They are defined below. Again this is used for dynamic resume generation
	public final static Map<String, Class<?>> actionMap = new HashMap<String, Class<?>>();
	
	
	
	
	
	// Load the hashMaps
	static
	{
		// Map FileType to *.gapp file
		fileMap.put(FILE_TYPES.RESUME, RESUME_GAPP_FILE_NAME);
		fileMap.put(FILE_TYPES.LINKEDIN, LINKEDIN_GAPP_FILE_NAME);
		fileMap.put(FILE_TYPES.JOB_DESCRIPTION, JOB_DESCRIPTION_GAPP_FILE_NAME);
		
		// Map professionType to training set and paragraph vectors *.zip file
		professionMap.put(PROFESSION_TYPES.MANAGEMENT, MANAGERS_TRAINING_SET);
		paragraphVectorMap.put(PROFESSION_TYPES.MANAGEMENT, MANAGERS_PARAGRAPH_VECTORS);
		
		// Map professionType to training set and paragraph vectors *.zip file
		professionMap.put(PROFESSION_TYPES.SOFTWARE, SOFTWARE_ENGINEER_SET);
		paragraphVectorMap.put(PROFESSION_TYPES.SOFTWARE, SOFTWARE_PARAGRAPH_VECTORS);
		
		monthMap.put("Jan", 0);
		monthMap.put("Feb", 1);
		monthMap.put("Mar", 2);
		monthMap.put("Apr", 3);
		monthMap.put("May", 4);
		monthMap.put("Jun", 5);
		monthMap.put("Jul", 6);
		monthMap.put("Aug", 7);
		monthMap.put("Sep", 8);
		monthMap.put("Oct", 9);
		monthMap.put("Nov", 10);
		monthMap.put("Dec", 11);
		
		monthMap.put("jan", 0);
		monthMap.put("feb", 1);
		monthMap.put("mar", 2);
		monthMap.put("apr", 3);
		monthMap.put("may", 4);
		monthMap.put("jun", 5);
		monthMap.put("jul", 6);
		monthMap.put("aug", 7);
		monthMap.put("sep", 8);
		monthMap.put("oct", 9);
		monthMap.put("nov", 10);
		monthMap.put("dec", 11);
		
		monthMap.put("January", 0);
		monthMap.put("February", 1);
		monthMap.put("March", 2);
		monthMap.put("April", 3);
		monthMap.put("May", 4);
		monthMap.put("June", 5);
		monthMap.put("July", 6);
		monthMap.put("August", 7);
		monthMap.put("September", 8);
		monthMap.put("October", 9);
		monthMap.put("November", 10);
		monthMap.put("December", 11);
		
		
		actionMap.put(Constants.IN_A_TEAM+"SingleSkillPresentTense", tech.enfuego.resumewriter.skills.participated.SingleSkillPresentTense.class);
		actionMap.put(Constants.IN_A_TEAM+"SingleSkillPastTense", tech.enfuego.resumewriter.skills.participated.SingleSkillPastTense.class);
		actionMap.put(Constants.IN_A_TEAM+"MultiSkillsWithMonthPastTense", tech.enfuego.resumewriter.skills.participated.MultiSkillsPastTense.class);
		actionMap.put(Constants.IN_A_TEAM+"MultiSkillsWithoutMonthPastTense", tech.enfuego.resumewriter.skills.participated.MultiSkillsPastTense.class);
		actionMap.put(Constants.IN_A_TEAM+"MultiSkillsWithMonthPresentTense", tech.enfuego.resumewriter.skills.participated.MultiSkillsWithMonthPresentTense.class);
		actionMap.put(Constants.IN_A_TEAM+"MultiSkillsWithoutMonthPresentTense", tech.enfuego.resumewriter.skills.participated.MultiSkillsWithoutMonthPresentTense.class);

		actionMap.put(Constants.SUPERVISED+"SingleSkillPresentTense", tech.enfuego.resumewriter.skills.supervision.SingleSkillPresentTense.class);
		actionMap.put(Constants.SUPERVISED+"SingleSkillPastTense", tech.enfuego.resumewriter.skills.supervision.SingleSkillPastTense.class);
		actionMap.put(Constants.SUPERVISED+"MultiSkillsWithMonthPastTense", tech.enfuego.resumewriter.skills.supervision.MultiSkillsWithMonthPastTense.class);
		actionMap.put(Constants.SUPERVISED+"MultiSkillsWithoutMonthPastTense", tech.enfuego.resumewriter.skills.supervision.MultiSkillsWithoutMonthPastTense.class);
		actionMap.put(Constants.SUPERVISED+"MultiSkillsWithMonthPresentTense", tech.enfuego.resumewriter.skills.supervision.MultiSkillsWithMonthPresentTense.class);
		actionMap.put(Constants.SUPERVISED+"MultiSkillsWithoutMonthPresentTense", tech.enfuego.resumewriter.skills.supervision.MultiSkillsWithoutMonthPresentTense.class);
		
		actionMap.put(Constants.DIRECTLY_PERFORMED+"SingleSkillPresentTense", tech.enfuego.resumewriter.skills.directlyperformed.SingleSkillPresentTense.class);
		actionMap.put(Constants.DIRECTLY_PERFORMED+"SingleSkillPastTense", tech.enfuego.resumewriter.skills.directlyperformed.SingleSkillsPastTense.class);
		actionMap.put(Constants.DIRECTLY_PERFORMED+"MultiSkillsWithMonthPastTense", tech.enfuego.resumewriter.skills.directlyperformed.MultipleSkillsWithMonthPastTense.class);
		actionMap.put(Constants.DIRECTLY_PERFORMED+"MultiSkillsWithoutMonthPastTense", tech.enfuego.resumewriter.skills.directlyperformed.MultipleSkillsWithoutMonthPastTense.class);
		actionMap.put(Constants.DIRECTLY_PERFORMED+"MultiSkillsWithMonthPresentTense", tech.enfuego.resumewriter.skills.directlyperformed.MultiSkillsWithMonthPresentTense.class);
		actionMap.put(Constants.DIRECTLY_PERFORMED+"MultiSkillsWithoutMonthPresentTense", tech.enfuego.resumewriter.skills.directlyperformed.MultiSkillsWithoutMonthPresentTense.class);
		
		actionMap.put(Constants.SUPPORTED+"SingleSkillPresentTense", tech.enfuego.resumewriter.skills.supported.SingleSkillPresentTense.class);
		actionMap.put(Constants.SUPPORTED+"SingleSkillPastTense", tech.enfuego.resumewriter.skills.supported.SingleSkillPastTense.class);
		actionMap.put(Constants.SUPPORTED+"MultiSkillsWithMonthPastTense", tech.enfuego.resumewriter.skills.supported.MultiSkillsWithMonthPastTense.class);
		actionMap.put(Constants.SUPPORTED+"MultiSkillsWithoutMonthPastTense", tech.enfuego.resumewriter.skills.supported.MultiSkillsWithoutMonthPastTense.class);
		actionMap.put(Constants.SUPPORTED+"MultiSkillsWithMonthPresentTense", tech.enfuego.resumewriter.skills.supported.MultiSkillsWithMonthPresentTense.class);
		actionMap.put(Constants.SUPPORTED+"MultiSkillsWithoutMonthPresentTense", tech.enfuego.resumewriter.skills.supported.MultiSkillsWithoutMonthPresentTense.class);
		
		
		
		
		
		
	}
	
	public final static String DBNAME = "Enfuego";
	public final static String RESUME_COLLECTION_NAME = "Resume";
	public final static String JOBS_COLLECTION_NAME = "Jobs";
	
	public final static String REGEX_FOR_NUMBER = "\\d+";
	
}
