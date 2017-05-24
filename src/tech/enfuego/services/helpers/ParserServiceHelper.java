package tech.enfuego.services.helpers;

import static gate.Utils.stringFor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.datavec.api.util.ClassPathResource;
import org.deeplearning4j.berkeley.Pair;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.paragraphvectors.ParagraphVectors;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Corpus;
import gate.Document;
import gate.Factory;
import gate.FeatureMap;
import gate.util.GateException;
import gate.util.Out;
import tech.enfuego.AnnieSystem;
import tech.enfuego.Constants;
import tech.enfuego.Constants.FILE_TYPES;
import tech.enfuego.Constants.PROFESSION_TYPES;
import tech.enfuego.deeplearning.Document2Vector;
import tech.enfuego.models.Accomplishments;
import tech.enfuego.models.Certifications;
import tech.enfuego.models.Degree;
import tech.enfuego.models.GeneralInfo;
import tech.enfuego.models.JobDescription;
import tech.enfuego.models.JobHistory;
import tech.enfuego.models.Skills;
import tech.enfuego.models.User;
import tech.enfuego.utils.FileUtils;
import tech.enfuego.utils.RegexUtils;

public class ParserServiceHelper {

	public static User getDetailsResume(InputStream inputStream, FILE_TYPES filetype) throws GateException, IOException
	{
		System.out.println("Initialising ANNIE");
		AnnieSystem obj = new AnnieSystem();
		obj.initializeANNIE(filetype);
		Corpus corpus = Factory.newCorpus("Demo Corpus");



		Document resume = gate.Factory.newDocument(FileUtils.getStringFromInputStream(inputStream));
		corpus.add(resume);

		// tell the pipeline about the corpus and run it
		obj.setCorpus(corpus);
		obj.execute();

		Iterator iter = corpus.iterator();
		JSONObject parsedJSON = new JSONObject();
		User user = new User();
		Out.prln("Started parsing...");

		if (iter.hasNext()) { // should technically be while but I am just
			// dealing with one document
			JSONObject profileJSON = new JSONObject();
			GeneralInfo generalInfo = new GeneralInfo();
			Document doc = (Document) iter.next();
			AnnotationSet defaultAnnotSet = doc.getAnnotations();

			AnnotationSet curAnnSet;
			Iterator it;
			Annotation currAnnot;

			// Name
			curAnnSet = defaultAnnotSet.get("NameFinder");
			if (curAnnSet.iterator().hasNext()) { // only one name will be
				// found.
				currAnnot = (Annotation) curAnnSet.iterator().next();
				String gender = (String) currAnnot.getFeatures().get("gender");
				if (gender != null && gender.length() > 0) {
					profileJSON.put("gender", gender);
				}

				// Needed name Features
				JSONObject nameJson = new JSONObject();
				String[] nameFeatures = new String[] { "firstName",
						"middleName", "surname" };
				for (String feature : nameFeatures) {
					String s = (String) currAnnot.getFeatures().get(feature);
					if (s != null && s.length() > 0) {
						if(feature.equals("firstName"))
						{
							generalInfo.setFirstName(s);
						}
						else if(feature.equals("surname") || feature.equals("middleName"))
						{
							generalInfo.setLastName(s);
						}
						nameJson.put(feature, s);
					}
				}

				profileJSON.put("name", nameJson);
			} // name

			// title
			curAnnSet = defaultAnnotSet.get("TitleFound");
			System.out.println("TITLE"+curAnnSet);
			if (curAnnSet.iterator().hasNext()) { // only one title will be
				// found.
				currAnnot = (Annotation) curAnnSet.iterator().next();
				String title = (String) currAnnot.getFeatures().get("TITLE");
				if (title != null && title.length() > 0) {
					profileJSON.put("title", title);
				}
			}// title

			// email,address,phone,url
			String[] annSections = new String[] { "EmailFinder",
					"AddressFinder", "PhoneFinder", "URLFinder" };
			String[] annKeys = new String[] { "email", "address", "phone",
			"url" };
			for (short i = 0; i < annSections.length; i++) {
				String annSection = annSections[i];
				curAnnSet = defaultAnnotSet.get(annSection);
				it = curAnnSet.iterator();
				JSONArray sectionArray = new JSONArray();
				while (it.hasNext()) { // extract all values for each
					// address,email,phone etc..
					currAnnot = (Annotation) it.next();
					String s = stringFor(doc, currAnnot);
					if (s != null && s.length() > 0) {

						if(annSection.equals("PhoneFinder"))
						{
							generalInfo.setMobilePhone(s);
						}
						else if(annSection.equals("AddressFinder"))
						{
							//TO DO ADDRESS FILLER
						}
						else if(annSection.equals("EmailFinder"))
						{
							generalInfo.setGenEmail(s);
						}
						sectionArray.add(s);
					}
				}
				if (sectionArray.size() > 0) {
					profileJSON.put(annKeys[i], sectionArray);
				}
			}
			user.setGeneralInfo(generalInfo);
			if (!profileJSON.isEmpty()) {
				parsedJSON.put("basics", profileJSON);
			}

			// awards,credibility,education_and_training,extracurricular,misc,skills,summary
			String[] otherSections = new String[] { "summary", "skills",
					"awards", "credibility", "extracurricular", "misc" };
			for (String otherSection : otherSections) {
				curAnnSet = defaultAnnotSet.get(otherSection);
				it = curAnnSet.iterator();
				JSONArray subSections = new JSONArray();
				while (it.hasNext()) {

					JSONObject subSection = new JSONObject();
					currAnnot = (Annotation) it.next();
					//System.out.println(currAnnot);
					String key = (String) currAnnot.getFeatures().get(
							"sectionHeading");
					//System.out.println("Getting the subsection : "+key);
					String value = stringFor(doc, currAnnot);
					//System.out.println("Value  : "+value);

					if (!StringUtils.isBlank(key)
							&& !StringUtils.isBlank(value)) {
						subSection.put(key, value);
					}
					if (!subSection.isEmpty()) {
						subSections.add(subSection);
					}
				}
				if (!subSections.isEmpty()) {
					parsedJSON.put(otherSection, subSections);
				}
			}

			curAnnSet = defaultAnnotSet.get("Accomplishments");
			it = curAnnSet.iterator();
			JSONArray accomplishments = new JSONArray();
			List<Accomplishments> accList = new ArrayList<Accomplishments>();
			while(it.hasNext())
			{
				JSONObject accomplishment = new JSONObject();
				Accomplishments singleAccomplishment = new Accomplishments();
				currAnnot = (Annotation)it.next();
				String v = (String) currAnnot.getFeatures().get("Sentence");
				if(!StringUtils.isBlank(v))
				{
					accomplishment.put("sentence", v);
					singleAccomplishment.setAccomplishment(v);
				}
				accList.add(singleAccomplishment);
				if(!accomplishment.isEmpty())
				{
					accomplishments.add(accomplishment);
				}

			}
			if(!accomplishments.isEmpty())
			{
				parsedJSON.put("accomplishments", accomplishments);
			}
			user.setAccs(accList);


			curAnnSet = defaultAnnotSet.get("DegreeFinder");
			//System.out.println(curAnnSet);
			it = curAnnSet.iterator();
			JSONArray degrees = new JSONArray();
			List<Degree> degreeList = new ArrayList<Degree>();

			while(it.hasNext())
			{
				Degree singleDegree = new Degree();
				JSONObject degree = new JSONObject();
				currAnnot = (Annotation)it.next();
				String[] annotations = new String[]{"date_start",
						"date_end", "degree", "university_us"};
				String key = (String) currAnnot.getFeatures().get(
						"sectionHeading");
				System.out.println("KEY IS"+key);
				for(String annotation : annotations){
					String v = (String) currAnnot.getFeatures().get(
							annotation);
					System.out.println("Current V"+v);
					if (!StringUtils.isBlank(v)) {
						// details.put(annotation, v);
						if(annotation.equals("date_start"))
						{
							singleDegree.setYear(v);
						}
						else if(annotation.equals("degree"))
						{
							singleDegree.setDegreeType(v);
						}
						else if(annotation.equals("university_us"))
						{
							singleDegree.setSchool(v);
						}
						degree.put(annotation, v);
					}

				}

				//				String value = stringFor(doc, currAnnot);
				//				System.out.println(value);
				//				if (!StringUtils.isBlank(key) && !StringUtils.isBlank(value)) {
				//					degree.put(key, value);
				//				}
				degreeList.add(singleDegree);
				if (!degree.isEmpty()) {
					degrees.add(degree);
				}
			}
			user.setDegrees(degreeList);
			if (!degrees.isEmpty()) {
				parsedJSON.put("education", degrees);
			}




			// work_experience
			curAnnSet = defaultAnnotSet.get("work_experience");
			List<JobHistory> jobhistory = new ArrayList<JobHistory>();
			List<Skills> skillsList = new ArrayList<Skills>();

			it = curAnnSet.iterator();
			JSONArray workExperiences = new JSONArray();
			while (it.hasNext()) {
				JobHistory singleJob = new JobHistory();
				JSONObject workExperience = new JSONObject();
				currAnnot = (Annotation) it.next();
				String key = (String) currAnnot.getFeatures().get(
						"sectionHeading");
				if ( key != null && key.equals("work_experience_marker")) {
					// JSONObject details = new JSONObject();
					String[] annotations = new String[] { "date_start",
							"date_end", "jobtitle", "organization", "SkillSet" };
					for (String annotation : annotations) {

						if(!annotation.equals("SkillSet"))
						{
							String v = (String) currAnnot.getFeatures().get(
									annotation);
							if(annotation.equals("date_start"))
							{
								singleJob.setStartYear(v);
							}
							else if(annotation.equals("date_end"))
							{
								singleJob.setEndYear(v);
							}
							else if(annotation.equals("jobtitle"))
							{
								singleJob.setTitle(v);
							}
							else if(annotation.equals("organization"))
							{
								singleJob.setCompany(v);
							}

							if (!StringUtils.isBlank(v)) {
								// details.put(annotation, v);
								workExperience.put(annotation, v);
							}
						}

						if(annotation.equals("SkillSet"))
						{
							JSONArray skills = new JSONArray();
							HashSet<String> skillz = (HashSet<String>) currAnnot.getFeatures().get(annotation);
							for(String skill :skillz)
							{
								Skills singleskill = new Skills();
								singleskill.setWhereApplied(singleJob.getCompany());
								singleskill.setName(skill);
								skillsList.add(singleskill);
								skills.add(skill);
							}
							workExperience.put(annotation, skills);

						}
					}
					// if (!details.isEmpty()) {
					// workExperience.put("work_details", details);
					// }
					key = "text";

				}
				String value = stringFor(doc, currAnnot);
				if (!StringUtils.isBlank(key) && !StringUtils.isBlank(value)) {
					workExperience.put(key, value);
				}
				if (!workExperience.isEmpty() && workExperience.containsKey("jobtitle")) {
					jobhistory.add(singleJob);
					workExperiences.add(workExperience);
				}

			}
			user.setJobHistory(jobhistory);
			user.setSkills(skillsList);
			if (!workExperiences.isEmpty()) {
				parsedJSON.put("work_experience", workExperiences);
			}

		}// if
		Out.prln("Completed parsing...");
		return user;

	}


	public static User fillUserDetails(File file, FILE_TYPES filetype, User user, String content) throws GateException, IOException
	{
		System.out.println("STEP 4 :Initialising ANNIE");
		AnnieSystem obj = new AnnieSystem();
		obj.initializeANNIE(filetype);
		Corpus corpus = Factory.newCorpus("Demo Corpus");

		URL u = file.toURI().toURL();
		FeatureMap params = Factory.newFeatureMap();
		params.put("sourceUrl", u);
		params.put("preserveOriginalContent", new Boolean(true));
		params.put("collectRepositioningInfo", new Boolean(true));
		//Out.prln("Creating doc for " + u);
		Document resume = (Document) Factory.createResource(
				"gate.corpora.DocumentImpl", params);
		corpus.add(resume);

		//Document resume = gate.Factory.newDocument(FileUtils.getStringFromInputStream(inputStream));
		//corpus.add(resume);

		// tell the pipeline about the corpus and run it
		obj.setCorpus(corpus);
		obj.execute();

		System.out.println("STEP 5 :ANNIE Executed");
		Iterator iter = corpus.iterator();
		JSONObject parsedJSON = new JSONObject();
		//User user = new User();
		Out.prln("Started parsing...");

		if (iter.hasNext()) { // should technically be while but I am just
			// dealing with one document
			JSONObject profileJSON = new JSONObject();
			GeneralInfo generalInfo = new GeneralInfo();

			Document doc = (Document) iter.next();
			AnnotationSet defaultAnnotSet = doc.getAnnotations();

			AnnotationSet curAnnSet;
			Iterator it;
			Annotation currAnnot;

			// Name
			curAnnSet = defaultAnnotSet.get("NameFinder");
			if (curAnnSet.iterator().hasNext()) { // only one name will be
				// found.
				currAnnot = (Annotation) curAnnSet.iterator().next();
				String gender = (String) currAnnot.getFeatures().get("gender");
				if (gender != null && gender.length() > 0) {
					profileJSON.put("gender", gender);
				}

				// Needed name Features
				JSONObject nameJson = new JSONObject();
				String[] nameFeatures = new String[] { "firstName",
						"middleName", "surname" };
				for (String feature : nameFeatures) {
					String s = (String) currAnnot.getFeatures().get(feature);
					if (s != null && s.length() > 0) {
						if(feature.equals("firstName"))
						{
							generalInfo.setFirstName(s);
						}
						else if(feature.equals("surname") || feature.equals("middleName"))
						{
							generalInfo.setLastName(s);
						}
						nameJson.put(feature, s);
					}
				}

				profileJSON.put("name", nameJson);
			} // name

			// title
			curAnnSet = defaultAnnotSet.get("TitleFound");
			System.out.println("TITLE"+curAnnSet);
			if (curAnnSet.iterator().hasNext()) { // only one title will be
				// found.
				currAnnot = (Annotation) curAnnSet.iterator().next();
				String title = (String) currAnnot.getFeatures().get("TITLE");
				if (title != null && title.length() > 0) {
					profileJSON.put("title", title);
				}
			}// title

			// email,address,phone,url
			String[] annSections = new String[] { "EmailFinder",
					"AddressFinder", "PhoneFinder", "URLFinder" };
			String[] annKeys = new String[] { "email", "address", "phone",
			"url" };
			for (short i = 0; i < annSections.length; i++) {
				String annSection = annSections[i];
				curAnnSet = defaultAnnotSet.get(annSection);
				it = curAnnSet.iterator();
				JSONArray sectionArray = new JSONArray();
				while (it.hasNext()) { // extract all values for each
					// address,email,phone etc..
					currAnnot = (Annotation) it.next();
					String s = stringFor(doc, currAnnot);
					if (s != null && s.length() > 0) {

						if(annSection.equals("PhoneFinder"))
						{
							generalInfo.setMobilePhone(s);
						}
						else if(annSection.equals("AddressFinder"))
						{
							//TO DO ADDRESS FILLER
						}
						else if(annSection.equals("EmailFinder"))
						{
							generalInfo.setGenEmail(s);
						}
						sectionArray.add(s);
					}
				}
				if (sectionArray.size() > 0) {
					profileJSON.put(annKeys[i], sectionArray);
				}
			}
			user.setGeneralInfo(generalInfo);
			if (!profileJSON.isEmpty()) {
				parsedJSON.put("basics", profileJSON);
			}

			// awards,credibility,education_and_training,extracurricular,misc,skills,summary
			String[] otherSections = new String[] { "summary", "skills",
					"awards", "credibility", "extracurricular", "misc" };
			for (String otherSection : otherSections) {
				curAnnSet = defaultAnnotSet.get(otherSection);
				it = curAnnSet.iterator();
				JSONArray subSections = new JSONArray();
				while (it.hasNext()) {

					JSONObject subSection = new JSONObject();
					currAnnot = (Annotation) it.next();
					//System.out.println(currAnnot);
					String key = (String) currAnnot.getFeatures().get(
							"sectionHeading");
					//System.out.println("Getting the subsection : "+key);
					String value = stringFor(doc, currAnnot);
					//System.out.println("Value  : "+value);

					if (!StringUtils.isBlank(key)
							&& !StringUtils.isBlank(value)) {
						subSection.put(key, value);
					}
					if (!subSection.isEmpty()) {
						subSections.add(subSection);
					}
				}
				if (!subSections.isEmpty()) {
					parsedJSON.put(otherSection, subSections);
				}
			}

			curAnnSet = defaultAnnotSet.get("Accomplishments");
			it = curAnnSet.iterator();
			JSONArray accomplishments = new JSONArray();
			List<Accomplishments> accList = new ArrayList<Accomplishments>();
			while(it.hasNext())
			{
				JSONObject accomplishment = new JSONObject();
				Accomplishments singleAccomplishment = new Accomplishments();
				singleAccomplishment.setId(ObjectId.get());
				currAnnot = (Annotation)it.next();
				String v = (String) currAnnot.getFeatures().get("Sentence");
				if(!StringUtils.isBlank(v))
				{
					accomplishment.put("sentence", v);
					singleAccomplishment.setAccomplishment(v);
				}
				accList.add(singleAccomplishment);
				if(!accomplishment.isEmpty())
				{
					accomplishments.add(accomplishment);
				}

			}
			if(!accomplishments.isEmpty())
			{
				parsedJSON.put("accomplishments", accomplishments);
			}
			user.setAccs(accList);


			curAnnSet = defaultAnnotSet.get("DegreeFinder");
			//System.out.println(curAnnSet);
			it = curAnnSet.iterator();
			JSONArray degrees = new JSONArray();
			List<Degree> degreeList = new ArrayList<Degree>();

			while(it.hasNext())
			{
				Degree singleDegree = new Degree();
				singleDegree.setId(ObjectId.get());
				JSONObject degree = new JSONObject();
				currAnnot = (Annotation)it.next();
				String[] annotations = new String[]{"date_start",
						"date_end", "degree", "university_us"};
				String key = (String) currAnnot.getFeatures().get(
						"sectionHeading");
				System.out.println("KEY IS"+key);
				for(String annotation : annotations){
					String v = (String) currAnnot.getFeatures().get(
							annotation);
					System.out.println("Current V"+v);
					if (!StringUtils.isBlank(v)) {
						// details.put(annotation, v);
						if(annotation.equals("date_start"))
						{
							singleDegree.setYear(v);
						}
						else if(annotation.equals("degree"))
						{
							singleDegree.setDegreeType(v);
						}
						else if(annotation.equals("university_us"))
						{
							singleDegree.setSchool(v);
						}
						degree.put(annotation, v);
					}

				}

				//				String value = stringFor(doc, currAnnot);
				//				System.out.println(value);
				//				if (!StringUtils.isBlank(key) && !StringUtils.isBlank(value)) {
				//					degree.put(key, value);
				//				}
				if(singleDegree.getDegreeType() != null)
				{
					degreeList.add(singleDegree);
				}
				if (!degree.isEmpty()) {
					degrees.add(degree);
				}
			}
			user.setDegrees(degreeList);
			if (!degrees.isEmpty()) {
				parsedJSON.put("education", degrees);
			}




			// work_experience
			curAnnSet = defaultAnnotSet.get("work_experience");
			List<JobHistory> jobhistory = new ArrayList<JobHistory>();
			List<Skills> skillsList = new ArrayList<Skills>();
			Document2Vector doc2vector = Document2Vector.getInstance(user.getTargetProfession());
			ClassPathResource resource = new ClassPathResource(Constants.paragraphVectorMap.get(doc2vector.getPtype()));
			ParagraphVectors paragraphVectors = WordVectorSerializer.readParagraphVectors(resource.getFile().getAbsolutePath());
			List<Pair<String, Double>> scores = doc2vector.predict(paragraphVectors, content);
			for(Pair<String, Double> pair : scores) {
				if(pair.getSecond() > 0) {
					Skills new_skill = new Skills();
					new_skill.setId(ObjectId.get());
					new_skill.setName(pair.getFirst());
					new_skill.setScore(Math.round(pair.getSecond()*100));
					skillsList.add(new_skill);
				}
			}
			user.setSkills(skillsList);



			it = curAnnSet.iterator();
			JSONArray workExperiences = new JSONArray();
			while (it.hasNext()) {
				JobHistory singleJob = new JobHistory();
				singleJob.setId(ObjectId.get());
				JSONObject workExperience = new JSONObject();
				currAnnot = (Annotation) it.next();
				String key = (String) currAnnot.getFeatures().get(
						"sectionHeading");
				if ( key != null && key.equals("work_experience_marker")) {
					// JSONObject details = new JSONObject();
					String[] annotations = new String[] { "date_start",
							"date_end", "jobtitle", "organization", "SkillSet" };
					for (String annotation : annotations) {

						if(!annotation.equals("SkillSet"))
						{
							String v = (String) currAnnot.getFeatures().get(
									annotation);
							if(annotation.equals("date_start"))
							{
								singleJob.setStartYear(v);
							}
							else if(annotation.equals("date_end"))
							{
								singleJob.setEndYear(v);
							}
							else if(annotation.equals("jobtitle"))
							{
								singleJob.setTitle(v);
							}
							else if(annotation.equals("organization"))
							{
								singleJob.setCompany(v);
							}

							if (!StringUtils.isBlank(v)) {
								// details.put(annotation, v);
								workExperience.put(annotation, v);
							}
						}

						if(annotation.equals("SkillSet"))
						{
							//							JSONArray skills = new JSONArray();
							//							HashSet<String> skillz = (HashSet<String>) currAnnot.getFeatures().get(annotation);
							//							for(String skill :skillz)
							//							{
							//								Skills singleskill = new Skills();
							//								singleskill.setId(ObjectId.get());
							//								singleskill.setWhereApplied(singleJob.getCompany());
							//								singleskill.setName(skill);
							//								skillsList.add(singleskill);
							//								skills.add(skill);
							//							}
							//workExperience.put(annotation, skills);

						}
					}
					// if (!details.isEmpty()) {
					// workExperience.put("work_details", details);
					// }
					key = "text";

				}
				String value = stringFor(doc, currAnnot);
				if (!StringUtils.isBlank(key) && !StringUtils.isBlank(value)) {
					workExperience.put(key, value);
				}
				if (!workExperience.isEmpty() && workExperience.containsKey("jobtitle")) {
					jobhistory.add(singleJob);
					workExperiences.add(workExperience);
				}

			}
			user.setJobHistory(jobhistory);
			user.setSkills(skillsList);
			if (!workExperiences.isEmpty()) {
				parsedJSON.put("work_experience", workExperiences);
			}

		}// if

		List<Certifications> certList = new ArrayList<Certifications>();
		user.setCertifications(certList);
		System.out.println("STEP 6 : Returning user filled object");
		Out.prln("Completed parsing...");
		Factory.deleteResource(corpus);
		Factory.deleteResource(resume);
		return user;

	}

	public static JSONObject parseResume(InputStream inputStream, FILE_TYPES filetype) throws GateException, IOException
	{
		System.out.println("Initialising ANNIE");
		AnnieSystem obj = new AnnieSystem();
		obj.initializeANNIE(filetype);
		Corpus corpus = Factory.newCorpus("Demo Corpus");

		Document resume = gate.Factory.newDocument(FileUtils.getStringFromInputStream(inputStream));
		corpus.add(resume);

		// tell the pipeline about the corpus and run it
		obj.setCorpus(corpus);
		obj.execute();

		Iterator iter = corpus.iterator();
		JSONObject parsedJSON = new JSONObject();
		Out.prln("Started parsing...");

		if (iter.hasNext()) { // should technically be while but I am just
			// dealing with one document
			JSONObject profileJSON = new JSONObject();
			Document doc = (Document) iter.next();
			AnnotationSet defaultAnnotSet = doc.getAnnotations();

			AnnotationSet curAnnSet;
			Iterator it;
			Annotation currAnnot;

			// Name
			curAnnSet = defaultAnnotSet.get("NameFinder");
			if (curAnnSet.iterator().hasNext()) { // only one name will be
				// found.
				currAnnot = (Annotation) curAnnSet.iterator().next();
				String gender = (String) currAnnot.getFeatures().get("gender");
				if (gender != null && gender.length() > 0) {
					profileJSON.put("gender", gender);
				}

				// Needed name Features
				JSONObject nameJson = new JSONObject();
				String[] nameFeatures = new String[] { "firstName",
						"middleName", "surname" };
				for (String feature : nameFeatures) {
					String s = (String) currAnnot.getFeatures().get(feature);
					if (s != null && s.length() > 0) {
						nameJson.put(feature, s);
					}
				}
				profileJSON.put("name", nameJson);
			} // name

			// title
			curAnnSet = defaultAnnotSet.get("TitleFound");
			System.out.println("TITLE"+curAnnSet);
			if (curAnnSet.iterator().hasNext()) { // only one title will be
				// found.
				currAnnot = (Annotation) curAnnSet.iterator().next();
				String title = (String) currAnnot.getFeatures().get("TITLE");
				if (title != null && title.length() > 0) {
					profileJSON.put("title", title);
				}
			}// title

			// email,address,phone,url
			String[] annSections = new String[] { "EmailFinder",
					"AddressFinder", "PhoneFinder", "URLFinder" };
			String[] annKeys = new String[] { "email", "address", "phone",
			"url" };
			for (short i = 0; i < annSections.length; i++) {
				String annSection = annSections[i];
				curAnnSet = defaultAnnotSet.get(annSection);
				it = curAnnSet.iterator();
				JSONArray sectionArray = new JSONArray();
				while (it.hasNext()) { // extract all values for each
					// address,email,phone etc..
					currAnnot = (Annotation) it.next();
					String s = stringFor(doc, currAnnot);
					if (s != null && s.length() > 0) {
						sectionArray.add(s);
					}
				}
				if (sectionArray.size() > 0) {
					profileJSON.put(annKeys[i], sectionArray);
				}
			}
			if (!profileJSON.isEmpty()) {
				parsedJSON.put("basics", profileJSON);
			}

			// awards,credibility,education_and_training,extracurricular,misc,skills,summary
			String[] otherSections = new String[] { "summary", "skills",
					"awards", "credibility", "extracurricular", "misc" };
			for (String otherSection : otherSections) {
				curAnnSet = defaultAnnotSet.get(otherSection);
				it = curAnnSet.iterator();
				JSONArray subSections = new JSONArray();
				while (it.hasNext()) {

					JSONObject subSection = new JSONObject();
					currAnnot = (Annotation) it.next();
					//System.out.println(currAnnot);
					String key = (String) currAnnot.getFeatures().get(
							"sectionHeading");
					//System.out.println("Getting the subsection : "+key);
					String value = stringFor(doc, currAnnot);
					//System.out.println("Value  : "+value);

					if (!StringUtils.isBlank(key)
							&& !StringUtils.isBlank(value)) {
						subSection.put(key, value);
					}
					if (!subSection.isEmpty()) {
						subSections.add(subSection);
					}
				}
				if (!subSections.isEmpty()) {
					parsedJSON.put(otherSection, subSections);
				}
			}

			curAnnSet = defaultAnnotSet.get("Accomplishments");
			it = curAnnSet.iterator();
			JSONArray accomplishments = new JSONArray();
			while(it.hasNext())
			{
				JSONObject accomplishment = new JSONObject();
				currAnnot = (Annotation)it.next();
				String v = (String) currAnnot.getFeatures().get("Sentence");
				if(!StringUtils.isBlank(v))
				{
					accomplishment.put("sentence", v);
				}
				if(!accomplishment.isEmpty())
				{
					accomplishments.add(accomplishment);
				}

			}
			if(!accomplishments.isEmpty())
			{
				parsedJSON.put("accomplishments", accomplishments);
			}


			curAnnSet = defaultAnnotSet.get("DegreeFinder");
			//System.out.println(curAnnSet);
			it = curAnnSet.iterator();
			JSONArray degrees = new JSONArray();
			while(it.hasNext())
			{
				JSONObject degree = new JSONObject();
				currAnnot = (Annotation)it.next();
				String[] annotations = new String[]{"date_start",
						"date_end", "degree", "university_us"};
				String key = (String) currAnnot.getFeatures().get(
						"sectionHeading");
				System.out.println("KEY IS"+key);
				for(String annotation : annotations){
					String v = (String) currAnnot.getFeatures().get(
							annotation);
					System.out.println("Current V"+v);
					if (!StringUtils.isBlank(v)) {
						// details.put(annotation, v);
						degree.put(annotation, v);
					}
				}

				//				String value = stringFor(doc, currAnnot);
				//				System.out.println(value);
				//				if (!StringUtils.isBlank(key) && !StringUtils.isBlank(value)) {
				//					degree.put(key, value);
				//				}
				if (!degree.isEmpty()) {
					degrees.add(degree);
				}
			}
			if (!degrees.isEmpty()) {
				parsedJSON.put("education", degrees);
			}




			// work_experience
			curAnnSet = defaultAnnotSet.get("work_experience");
			it = curAnnSet.iterator();
			JSONArray workExperiences = new JSONArray();
			while (it.hasNext()) {
				JSONObject workExperience = new JSONObject();
				currAnnot = (Annotation) it.next();
				String key = (String) currAnnot.getFeatures().get(
						"sectionHeading");
				if ( key != null && key.equals("work_experience_marker")) {
					// JSONObject details = new JSONObject();
					String[] annotations = new String[] { "date_start",
							"date_end", "jobtitle", "organization", "SkillSet" };
					for (String annotation : annotations) {
						if(annotation.equals("SkillSet"))
						{
							JSONArray skills = new JSONArray();
							HashSet<String> skillz = (HashSet<String>) currAnnot.getFeatures().get(annotation);
							for(String skill :skillz)
							{
								skills.add(skill);
							}
							workExperience.put(annotation, skills);

						}
						else
						{
							String v = (String) currAnnot.getFeatures().get(
									annotation);
							if (!StringUtils.isBlank(v)) {
								// details.put(annotation, v);
								workExperience.put(annotation, v);
							}
						}
					}
					// if (!details.isEmpty()) {
					// workExperience.put("work_details", details);
					// }
					key = "text";

				}
				String value = stringFor(doc, currAnnot);
				if (!StringUtils.isBlank(key) && !StringUtils.isBlank(value)) {
					workExperience.put(key, value);
				}
				if (!workExperience.isEmpty() && workExperience.containsKey("jobtitle")) {
					workExperiences.add(workExperience);
				}

			}
			if (!workExperiences.isEmpty()) {
				parsedJSON.put("work_experience", workExperiences);
			}

		}// if
		Out.prln("Completed parsing...");
		return parsedJSON;

	}

	public static JobDescription fillJobDetails(File file, FILE_TYPES filetype, JobDescription job, String content) throws GateException, IOException
	{
		System.out.println("STEP 4 :Initialising ANNIE");	
		AnnieSystem obj = new AnnieSystem();
		obj.initializeANNIE(filetype);
		Corpus corpus = Factory.newCorpus("Demo Corpus");

		URL u = file.toURI().toURL();
		FeatureMap params = Factory.newFeatureMap();
		params.put("sourceUrl", u);
		params.put("preserveOriginalContent", new Boolean(true));
		params.put("collectRepositioningInfo", new Boolean(true));
		//Out.prln("Creating doc for " + u);
		Document jobdescription = (Document) Factory.createResource(
				"gate.corpora.DocumentImpl", params);
		corpus.add(jobdescription);

		//Document resume = gate.Factory.newDocument(FileUtils.getStringFromInputStream(inputStream));
		//corpus.add(resume);

		// tell the pipeline about the corpus and run it
		obj.setCorpus(corpus);
		obj.execute();

		System.out.println("STEP 5 :ANNIE Executed");
		Iterator iter = corpus.iterator();
		JSONObject parsedJSON = new JSONObject();
		//User user = new User();
		Out.prln("Started parsing...");

		if (iter.hasNext()) { // should technically be while but I am just
			// dealing with one document
			JSONObject profileJSON = new JSONObject();
			GeneralInfo generalInfo = new GeneralInfo();

			Document doc = (Document) iter.next();
			AnnotationSet defaultAnnotSet = doc.getAnnotations();

			AnnotationSet curAnnSet;
			Iterator it;
			Annotation currAnnot;

			// JOB TITLE
			curAnnSet = defaultAnnotSet.get("TitleFinder");
			if (curAnnSet.iterator().hasNext()) { //only 1 JOB TITLE will be found
				currAnnot = (Annotation) curAnnSet.iterator().next();
				job.setJobTitle(stringFor(doc, currAnnot));
			} 

			// job Description
			curAnnSet = defaultAnnotSet.get("section_body");
			//System.out.println("section Body"+curAnnSet);
			if (curAnnSet.iterator().hasNext()) { // only one job description will be
				// found.
				currAnnot = (Annotation) curAnnSet.iterator().next();
				if(!stringFor(doc, currAnnot).isEmpty()) {
					job.setJobSummary(stringFor(doc, currAnnot));
				}
			}

			// if the summary is still null, then get the summary from first 10 lines of the job description.
			if(job.getJobSummary() == null) {
				curAnnSet = defaultAnnotSet.get("ProfileSection");
				if (curAnnSet.iterator().hasNext()) { //only 1 JOB TITLE will be found
					currAnnot = (Annotation) curAnnSet.iterator().next();
					job.setJobSummary((stringFor(doc, currAnnot)));
				} 
			}


			// company Name
			curAnnSet = defaultAnnotSet.get("Organization");
			//System.out.println("section Body"+curAnnSet);
			if (curAnnSet.iterator().hasNext()) { // only one job description will be
				// found.
				currAnnot = (Annotation) curAnnSet.iterator().next();
				if(!stringFor(doc, currAnnot).isEmpty()) {
					job.setCompanyName((stringFor(doc, currAnnot)));
				}
			}

			// Get the Skills.The Profession Type is very important here to load the correct paragraph vectors.
			List<Skills> job_required_skills = new ArrayList<Skills>();
			Document2Vector doc2vector = Document2Vector.getInstance(job.getProfessionType());
			ClassPathResource resource = new ClassPathResource(Constants.paragraphVectorMap.get(doc2vector.getPtype()));
			ParagraphVectors paragraphVectors = WordVectorSerializer.readParagraphVectors(resource.getFile().getAbsolutePath());
			List<Pair<String, Double>> scores = doc2vector.predict(paragraphVectors, content);
			for(Pair<String, Double> pair : scores) {
				if(pair.getSecond() > 0) {
					Skills new_skill = new Skills();
					new_skill.setName(pair.getFirst());
					new_skill.setScore(Math.round(pair.getSecond()*100));
					job_required_skills.add(new_skill);
				}
			}
			job.setSkills(job_required_skills);

			//find the Min and Max years of experience. 
			curAnnSet = defaultAnnotSet.get("Date");
			it = curAnnSet.iterator();
			while(it.hasNext())
			{
				currAnnot = (Annotation)it.next();
				String v = (String) currAnnot.getFeatures().get("rule");
				if(!StringUtils.isBlank(v) && StringUtils.equals("NumberOfYears", v))
				{	
					int[] minMaxYears = RegexUtils.getMinMaxYears(stringFor(doc, currAnnot));
					job.setMinYearsOfExp(minMaxYears[0]);
					job.setMaxYearsOfExp(minMaxYears[1]);
					break;
				}
			}

			//GET THE DEGREE
			curAnnSet = defaultAnnotSet.get("DegreeFinder");
			//System.out.println(curAnnSet);
			it = curAnnSet.iterator();
			JSONArray degrees = new JSONArray();
			List<Degree> degreeList = new ArrayList<Degree>();

			while(it.hasNext())
			{
				Degree singleDegree = new Degree();
				singleDegree.setId(ObjectId.get());
				JSONObject degree = new JSONObject();
				currAnnot = (Annotation)it.next();
				String[] annotations = new String[]{"date_start",
						"date_end", "degree", "university_us"};
				String key = (String) currAnnot.getFeatures().get(
						"sectionHeading");
				System.out.println("KEY IS"+key);
				for(String annotation : annotations){
					String v = (String) currAnnot.getFeatures().get(
							annotation);
					System.out.println("Current V"+v);
					if (!StringUtils.isBlank(v)) {
						// details.put(annotation, v);
						if(annotation.equals("date_start"))
						{
							singleDegree.setYear(v);
						}
						else if(annotation.equals("degree"))
						{
							singleDegree.setDegreeType(v);
						}
						else if(annotation.equals("university_us"))
						{
							singleDegree.setSchool(v);
						}
						degree.put(annotation, v);
					}

				}
				degreeList.add(singleDegree);
				if (!degree.isEmpty()) {
					degrees.add(degree);
				}
			}
			job.setDegrees(degreeList);
			System.out.println("STEP 6 : Returning user filled object");
			Out.prln("Completed parsing...");
			Factory.deleteResource(corpus);
			Factory.deleteResource(jobdescription);


		}
		return job;

	}
}
