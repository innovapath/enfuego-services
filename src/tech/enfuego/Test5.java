package tech.enfuego;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;



import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;

import tech.enfuego.models.Accomplishments;
import tech.enfuego.models.Degree;
import tech.enfuego.models.JobDescription;
import tech.enfuego.models.JobHistory;
import tech.enfuego.models.SearchMarkets;
import tech.enfuego.models.Skills;
import tech.enfuego.models.User;
import tech.enfuego.resumewriter.FontStyles;
import tech.enfuego.resumewriter.ResumeGenerator;
import tech.enfuego.resumewriter.Sentences;
import tech.enfuego.scoring.ScoreCalculator;


public class Test5 {

	private static 	String FILE = "Z1.pdf";

	public static void main(String [] args) throws JsonParseException, JsonMappingException, IOException, DocumentException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		ObjectMapper mapper = new ObjectMapper();
		User user = mapper.readValue(new File("/home/abhishek/git/enfuego-services/resources/bob.json"), User.class);
		JobDescription job = mapper.readValue(new File("/home/abhishek/git/enfuego-services/resources/job.json"), JobDescription.class);

		ResumeGenerator.generateResume(user, job, FILE);
	}

}
