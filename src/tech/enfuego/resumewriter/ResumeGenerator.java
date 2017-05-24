package tech.enfuego.resumewriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;

import tech.enfuego.Constants;
import tech.enfuego.models.Accomplishments;
import tech.enfuego.models.Degree;
import tech.enfuego.models.JobDescription;
import tech.enfuego.models.JobHistory;
import tech.enfuego.models.SearchMarkets;
import tech.enfuego.models.Skills;
import tech.enfuego.models.User;
import tech.enfuego.scoring.ScoreCalculator;


/**
 * In this class we define methods to generate a dynamic resume 
 * We use the iText pdf library to generate the dynamic Resume
 * @author abhishek
 *
 */
public class ResumeGenerator {
	
	/**
	 * The orchestrator for generation of a dynamic resume. We define the methods in an order to define the generate different sections of the resume in an roder
	 * @param user
	 * @param job
	 * @param FILE
	 * @throws FileNotFoundException
	 * @throws DocumentException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static void generateResume(User user, JobDescription job, String FILE) throws FileNotFoundException, DocumentException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		Set<Skills> commonSkills = ScoreCalculator.getCommonSkills(user, job);
		Document document = new Document();
		PdfWriter.getInstance(document, new FileOutputStream(FILE));
		document.open();
		addLineSeparator(document, 2.0f);
		addEmptyLine(document, 1);
		addName(document, user);
		addPhone(document, user);
		addContact(document, user);
		addLineSeparator(document, 2.0f);
		addEmptyLine(document, 1);
		addSubHeading(document, Sentences.OBJECTIVES);
		addLineSeparator(document, 0.0f);
		addSummary(document, user);
		addNomadSkills(document, commonSkills);
		addRelocation(document, user);
		addEmptyLine(document, 1);
		addSubHeading(document, Sentences.PROFESSIONAL_EXPERIENCE);
		addLineSeparator(document, 0.0f);
		addProfessionalExperiences(document, user, commonSkills);
		addEmptyLine(document, 1);
		addSubHeading(document, Sentences.EDUCATION_CERTIFICATIONS);
		addLineSeparator(document, 0.0f);
		addEducationAndCertification(user, document);
		document.close();
	}

	/**
	 * Add the name to the resume
	 * @param document
	 * @param user
	 */
	public static void addName(Document document, User user) {
		Paragraph name = new Paragraph();
		name.setLeading(12,0);
		name.setAlignment(Element.ALIGN_CENTER);
		//addEmptyLine(name, 1);

		name.add(new Paragraph(user.getGeneralInfo().getFirstName()+" "+user.getGeneralInfo().getLastName(), FontStyles.firstLastName));

		try {
			document.add(name);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Add a simple line separator with a specific linewidth
	 * @param document
	 * @param linewidth
	 */
	public static void addLineSeparator(Document document, float linewidth) {
		LineSeparator separator = new LineSeparator();
		separator.setLineWidth(linewidth);
		Chunk linebreak = new Chunk(separator);
		try {
			document.add(linebreak);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Add the number of empty lines in the resume after any section.
	 * @param document
	 * @param number
	 */
	private static void addEmptyLine(Document document,  int number) {
		Paragraph paragraph = new Paragraph();
		paragraph.setLeading(8,0);
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
		try {
			document.add(paragraph);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Add Phone number in the dynamic resume
	 * @param document
	 * @param user
	 */
	private static void addPhone(Document document, User user) {
		PdfPTable  table = new PdfPTable(1);
		table.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table.getDefaultCell().setBorder(0);
		//table.setWidthPercentage(50);
		Phrase p = new Phrase();
		p.setLeading(8);
		p.add(new Chunk(" "));
		p.add(new Chunk(new VerticalPositionMark()));
		p.add(new Chunk(user.getGeneralInfo().getMobilePhone(), FontStyles.contactAddressInfo));
		table.addCell(p);
		try {
			document.add(table);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Add the contact i.e. mailing address and email
	 * @param document
	 * @param user
	 */
	private static void addContact(Document document, User user) {
		PdfPTable  table = new PdfPTable(1);
		table.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.getDefaultCell().setBorder(0);
		table.setWidthPercentage(100.0f);
		Phrase p = new Phrase();
		p.setLeading(0);
		p.add(new Chunk(user.getGeneralInfo().getAddress().getCity()+","+user.getGeneralInfo().getAddress().getStreet(), FontStyles.contactAddressInfo));
		p.add(new Chunk(new VerticalPositionMark()));
		p.add(new Chunk(user.getEmail(), FontStyles.contactAddressInfo));
		table.addCell(p);
		try {
			document.add(table);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	/**
	 * Add a subheading in the resume
	 * @param document
	 * @param headingString
	 */
	private static void addSubHeading(Document document, String headingString) {
		Paragraph heading = new Paragraph();
		heading.setLeading(8,0);
		heading.setAlignment(Element.ALIGN_LEFT);
		//addEmptyLine(name, 1);

		heading.add(new Paragraph(headingString, FontStyles.sectionHeading));

		try {
			document.add(heading);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Add a summary statement in the resume
	 * @param document
	 * @param user
	 */
	private static void addSummary(Document document, User user) {
		Paragraph heading = new Paragraph();
		heading.setLeading(8,0);
		heading.setAlignment(Element.ALIGN_LEFT);
		//addEmptyLine(name, 1);

		heading.add(new Paragraph(String.format(Sentences.SUMMARY, Sentences.ASPRIATION_3, user.getTargetProfession().toString().toLowerCase() ), FontStyles.normalText));

		try {
			document.add(heading);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Add general skills in the resume which are present in the user profile and job skills
	 * @param document
	 * @param skills
	 */
	private static void addNomadSkills(Document document, Set<Skills> skills) {
		PdfPTable  table = new PdfPTable(3);
		table.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.getDefaultCell().setBorder(0);
		table.setWidthPercentage(100.0f);
		List<Skills> skillList = new ArrayList<Skills>();
		for(Skills s : skills) {
			skillList.add(s);
		}
		int i = 0;
		for(int r = 0; r < skillList.size()/table.getNumberOfColumns(); r++) {
			for(int c = 0; c < table.getNumberOfColumns(); c++) {
				Phrase p = new Phrase();
				p.setLeading(0);
				p.add(new Chunk(skillList.get(i).getName().replace("_", " "), FontStyles.nomadSkills));
				table.addCell(p);
				i++;
			}
		}

		try {
			document.add(table);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	/**
	 * Add Search markets in the resume
	 * @param document
	 * @param user
	 */
	private static void addRelocation(Document document, User user) {
		if (user.getSearchMarkets() != null) {
			String searchMarkets = Sentences.RELOCATION;

			for(SearchMarkets s : user.getSearchMarkets()) {
				searchMarkets += s.getState()+" ";
			}
			Phrase firstLine = new Phrase(searchMarkets, FontStyles.normalText);
			com.itextpdf.text.List list = new com.itextpdf.text.List(com.itextpdf.text.List.UNORDERED);
			ListItem item = new ListItem(firstLine);
			list.add(item);
			try {
				document.add(list);
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Add professional experiences in the resume
	 * @param document
	 * @param user
	 * @param commonSkills
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	private static void addProfessionalExperiences(Document document, User user, Set<Skills> commonSkills) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		PdfPTable  table = new PdfPTable(1);
		table.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.getDefaultCell().setBorder(0);
		table.setWidthPercentage(90.0f);

		if(user.getJobHistory() != null) {
			JobHistory currentJob = getMostRecentExperience(user);
			if(currentJob != null) {
				addCompanyNameDate(table, currentJob, true);
				addJobTitle(table, currentJob);
				HashMap<String, List<Skills>> currentSkillsMap = getSkillsMapForJobHistory(user, currentJob, commonSkills);

				if (currentSkillsMap != null) {
					addSkillsForPresentJob(table, currentSkillsMap);
				}
				
				List<Accomplishments> currentAccomplishments = getAccomplishments(user, currentJob);
				
				if(currentAccomplishments != null) {
					
					addAccomplishments(table, currentAccomplishments);
				}
				
				
				sortWorkHistory(user.getJobHistory());
				for(JobHistory job : user.getJobHistory()) {
					if(!job.isPresent())
					{
						addCompanyNameDate(table, job, false);
						addJobTitle(table, job);
						HashMap<String, List<Skills>> SkillsMap = getSkillsMapForJobHistory(user, job, commonSkills);

						if (SkillsMap != null) {
							addSkillsForPastJob(table, SkillsMap);
						}
						
						List<Accomplishments> accomplishments = getAccomplishments(user, job);
						if(accomplishments != null) {
							addAccomplishments(table, accomplishments);
						}

					}
				}

			}
			try {
				document.add(table);
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		// GET THE REMAINING JOBS.
		// GET THE COUNT OF 
	}
	
	/**
	 * Add accomplishments if any for any jobhistory.
	 * @param table
	 * @param accs
	 */
	private static void addAccomplishments(PdfPTable table, List<Accomplishments> accs) {
		Phrase title = new Phrase();
		title.setLeading(0);
		title.add(new Chunk("Accomplishments:", FontStyles.accTitle));
		table.addCell(title);
		
		for(Accomplishments acc : accs) {
			Phrase p = new Phrase();
			p.setLeading(0);
			p.add(new Chunk(acc.getAccomplishment(), FontStyles.normalText));
			table.addCell(p);
		}
	}
	
	private static List<Accomplishments> getAccomplishments(User user, JobHistory jobHistory) {
		
		List<Accomplishments> accomplishements = null;
		for(Accomplishments acc : user.getAccs()) {
			if(acc.getCompany().equalsIgnoreCase(jobHistory.getCompany()) && inbetween(acc, jobHistory)) {
				if(accomplishements == null) {
					accomplishements = new ArrayList<Accomplishments>();
				}
				accomplishements.add(acc);
			}
		}
		
		return accomplishements;
	}
	
	private static boolean inbetween(Accomplishments acc, JobHistory jobhistory) {
		
		if (acc.getYear() == null && acc.getMonth() == null) {
			return false;
		}
		
		Calendar acc_cal = Calendar.getInstance();
		int year = Integer.parseInt(acc.getYear());
		int month = Constants.monthMap.getOrDefault(acc.getMonth(), 0);
		acc_cal.set(Calendar.MONTH, month);
		acc_cal.set(Calendar.YEAR, year);	
		
		Calendar jobStart = Calendar.getInstance();
		jobStart.set(Calendar.MONTH, Constants.monthMap.getOrDefault(jobhistory.getStartMonth(), 0));
		jobStart.set(Calendar.YEAR, Integer.parseInt(jobhistory.getStartYear()));
		
		if(jobhistory.isPresent()) {

			return acc_cal.after(jobStart) || acc_cal.getTime().equals(jobStart.getTime());
			
		}
		else {
			
			Calendar jobEnd = Calendar.getInstance();
			jobEnd.set(Calendar.MONTH, Constants.monthMap.getOrDefault(jobhistory.getEndMonth(), 0));
			jobEnd.set(Calendar.YEAR, Integer.parseInt(jobhistory.getEndYear()));
			return acc_cal.after(jobStart) && (acc_cal.before(jobEnd) || acc_cal.getTime().equals(jobEnd.getTime()));
		}
		
	}

	private static void addCompanyNameDate(PdfPTable  table, JobHistory job, boolean ispresent) {
		Phrase p = new Phrase();
		p.setLeading(0);
		p.add(new Chunk(job.getCompany(), FontStyles.employerName));
		p.add(new Chunk(new VerticalPositionMark()));
		String result = "";
		if(ispresent) {
			result = job.getStartMonth()+" "+job.getStartYear()+" - Present";
		}
		else 
		{
			if(job.getStartMonth() == null) {
				job.setStartMonth("");
			}
			if(job.getEndMonth() == null) {
				job.setEndMonth("");
			}
			result = job.getStartMonth()+" "+job.getStartYear()+" - "+job.getEndMonth()+" "+job.getEndYear();
		}
		p.add(new Chunk(result, FontStyles.workExpDate));
		table.addCell(p);
	}

	private static void addJobTitle(PdfPTable table, JobHistory job) {
		Phrase title = new Phrase();
		title.setLeading(0);
		title.add(new Chunk(job.getTitle(), FontStyles.jobTitle));
		table.addCell(title);
	}

	private static void addSkillsForPresentJob(PdfPTable table, HashMap<String, List<Skills>> currentSkillsMap) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		com.itextpdf.text.List list = new com.itextpdf.text.List(com.itextpdf.text.List.UNORDERED);
		for(Entry<String, List<Skills>> currentEntry : currentSkillsMap.entrySet()) {
			String howApplied = currentEntry.getKey();
			List<Skills> skillList = currentEntry.getValue();
			if (skillList.size() <= 2) {
				for(Skills s : skillList) {
					Method method = Constants.actionMap.get(howApplied+"SingleSkillPresentTense").getMethod("randomSentence", null);
					Object obj = method.invoke(null, null);
					Method method2 = Constants.actionMap.get(howApplied+"SingleSkillPresentTense").getMethod("getSentence", null);
					Object sentence = method2.invoke(obj, null);
					Phrase sent = new Phrase(String.format(sentence.toString(), s.getName().replace("_", " "), Integer.parseInt(s.getYearsApplied())*12), FontStyles.normalText);
					ListItem item = new ListItem(sent);
					list.add(item);
				}
			}
			else
			{
				int listSize = skillList.size();
				int i = 0;
				while(listSize > 2)
				{
					Skills s1 = skillList.get(i);
					i++;
					Skills s2 = skillList.get(i);
					i++;
					Skills s3 = skillList.get(i);
					i++;
					listSize -= i;
					Method method = Constants.actionMap.get(howApplied+"MultiSkillsWithoutMonthPresentTense").getMethod("randomSentence", null);
					Object obj = method.invoke(null, null);
					Method method2 = Constants.actionMap.get(howApplied+"MultiSkillsWithoutMonthPresentTense").getMethod("getSentence", null);
					Object sentence = method2.invoke(obj, null);
					Phrase sent = new Phrase(String.format(sentence.toString(), s1.getName().replace("_", " "), s2.getName().replace("_", " "), s3.getName().replace("_", " ")), FontStyles.normalText);
					ListItem item = new ListItem(sent);
					list.add(item);
				}
			}

		}
		//sentences.add(list);
		PdfPCell phraseCell = new PdfPCell();
		phraseCell.disableBorderSide(1);
		phraseCell.disableBorderSide(2);
		phraseCell.disableBorderSide(4);
		phraseCell.disableBorderSide(8);
		table.setSpacingBefore(5);
		phraseCell.addElement(list);   
		table.addCell(phraseCell);
	}


	private static void addSkillsForPastJob(PdfPTable table, HashMap<String, List<Skills>> currentSkillsMap) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		com.itextpdf.text.List list = new com.itextpdf.text.List(com.itextpdf.text.List.UNORDERED);
		for(Entry<String, List<Skills>> currentEntry : currentSkillsMap.entrySet()) {

			String howApplied = currentEntry.getKey();
			List<Skills> skillList = currentEntry.getValue();
			if(howApplied != null) {
				if (skillList.size() <= 2) {
					for(Skills s : skillList) {
						//System.out.println(howApplied+"SingleSkillPastTense");
						Method method = Constants.actionMap.get(howApplied+"SingleSkillPastTense").getMethod("randomSentence", null);
						Object obj = method.invoke(null, null);
						Method method2 = Constants.actionMap.get(howApplied+"SingleSkillPastTense").getMethod("getSentence", null);
						Object sentence = method2.invoke(obj, null);
						Phrase sent = new Phrase(String.format(sentence.toString(), s.getName().replace("_", " "), Integer.parseInt(s.getYearsApplied())*12), FontStyles.normalText);
						ListItem item = new ListItem(sent);
						list.add(item);
					}
				}
				else
				{
					int listSize = skillList.size();
					int i = 0;
					while(listSize > 2)
					{
						Skills s1 = skillList.get(i);
						i++;
						Skills s2 = skillList.get(i);
						i++;
						Skills s3 = skillList.get(i);
						i++;
						listSize -= i;
						Method method = Constants.actionMap.get(howApplied+"MultiSkillsWithoutMonthPastTense").getMethod("randomSentence", null);
						Object obj = method.invoke(null, null);
						Method method2 = Constants.actionMap.get(howApplied+"MultiSkillsWithoutMonthPastTense").getMethod("getSentence", null);
						Object sentence = method2.invoke(obj, null);
						Phrase sent = new Phrase(String.format(sentence.toString(), s1.getName().replace("_", " "), s2.getName().replace("_", " "), s3.getName().replace("_", " ")), FontStyles.normalText);
						ListItem item = new ListItem(sent);
						list.add(item);
					}
				}
			}
		}
		//sentences.add(list);
		PdfPCell phraseCell = new PdfPCell();
		phraseCell.disableBorderSide(1);
		phraseCell.disableBorderSide(2);
		phraseCell.disableBorderSide(4);
		phraseCell.disableBorderSide(8);
		table.setSpacingBefore(5);
		phraseCell.addElement(list);   
		table.addCell(phraseCell);
	}

	private static JobHistory getMostRecentExperience(User user) {
		if (user.getJobHistory() != null) {
			for(JobHistory jobhistory : user.getJobHistory()) {
				if(jobhistory.isPresent()) return jobhistory;
			}
		}
		return null;
	}

	private static HashMap<String, List<Skills>> getSkillsMapForJobHistory(User user, JobHistory jobhistory, Set<Skills> commonSkills){
		HashMap<String, List<Skills>> skillMap = new HashMap<String, List<Skills>>();

		if (user.getSkills() != null) {
			for(Skills s : user.getSkills()) {
				if (s.getWhereApplied()!= null 
						&& s.getWhereApplied().equalsIgnoreCase((jobhistory.getCompany()))
						&& commonSkills.contains(s)) {
					if (skillMap.containsKey(s.getSkillsApplied())) {
						skillMap.get(s.getSkillsApplied()).add(s);
					}
					else {
						List<Skills> skillSet = new ArrayList<Skills>();
						skillSet.add(s);
						skillMap.put(s.getSkillsApplied(), skillSet);
					}
				}
			}
		}
		return skillMap;
	}
	
	private static void addEducationAndCertification(User user, Document document) {
		PdfPTable  table = new PdfPTable(1);
		table.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.getDefaultCell().setBorder(0);
		table.setWidthPercentage(100.0f);

		if(user.getDegrees() != null) {
			sortDegrees(user.getDegrees());
			
			for(Degree d : user.getDegrees()) {
				addSchoolNameYear(table, d);
				addDegreeName(table, d);
			}
		}
		
		try {
			document.add(table);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	private static void addSchoolNameYear(PdfPTable table, Degree d) {
		Phrase p = new Phrase();
		p.setLeading(0);
		p.add(new Chunk(d.getSchool(), FontStyles.schoolName));
		p.add(new Chunk(new VerticalPositionMark()));
		p.add(new Chunk(d.getYear(), FontStyles.schoolDate));
		table.addCell(p);
	}
	
	private static void addDegreeName(PdfPTable table, Degree d) {
		Phrase p = new Phrase();
		p.setLeading(0);
		p.add(new Chunk(d.getName(), FontStyles.degreeName));
		table.addCell(p);
	}
	
	private static void sortDegrees(List<Degree> listOfDegrees) {
		
		Collections.sort(listOfDegrees, new Comparator<Degree>() {

			@Override
			public int compare(Degree o1, Degree o2) {
				if (o1.getYear() == null && o2.getYear() == null) {
					return 0;
				}
				int year1 = Integer.parseInt(o1.getYear()); // 1988\
				int year2 = Integer.parseInt(o2.getYear());
				Calendar calendar1 = Calendar.getInstance();
				calendar1.clear();
				calendar1.set(Calendar.YEAR, year1);
				Calendar calendar2 = Calendar.getInstance();
				calendar2.clear();
				calendar2.set(Calendar.YEAR, year2);
				return -calendar1.getTime().compareTo(calendar2.getTime());
			}
			
		});
		
	}
	
	
private static void sortWorkHistory(List<JobHistory> workExperiences) {
		
		Collections.sort(workExperiences, new Comparator<JobHistory>() {

			@Override
			public int compare(JobHistory o1, JobHistory o2) {
				if (o1.getStartYear() == null && o2.getStartYear() == null) {
					return 0;
				}
				int year1 = Integer.parseInt(o1.getStartYear()); 
				int year2 = Integer.parseInt(o2.getStartYear());
				Calendar calendar1 = Calendar.getInstance();
				calendar1.clear();
				calendar1.set(Calendar.YEAR, year1);
				Calendar calendar2 = Calendar.getInstance();
				calendar2.clear();
				calendar2.set(Calendar.YEAR, year2);
				return -calendar1.getTime().compareTo(calendar2.getTime());
			}
			
		});
		
	}

}
