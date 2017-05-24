package tech.enfuego.parsers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FilenameUtils;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.ToXMLContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public abstract class ANNIEParser implements IANNIEParser {
	
	@Override
	public File converToHTML(File file) throws IOException, SAXException, TikaException
	{
		String fileExtension = FilenameUtils.getExtension(file.getAbsolutePath());
		String finalFileFormat = "";

		if(fileExtension.equalsIgnoreCase("html") | fileExtension.equalsIgnoreCase("pdf")| 
				fileExtension.equalsIgnoreCase("docx") | fileExtension.equalsIgnoreCase("doc"))
		{
			finalFileFormat = ".html";
		}
		else if(fileExtension.equalsIgnoreCase("txt") | fileExtension.equalsIgnoreCase("rtf"))
		{
			finalFileFormat = ".txt";
		}
		else
		{
			return null;
		}
		String FINAL_FILE_NAME = FilenameUtils.removeExtension(file.getAbsolutePath())
				+ finalFileFormat;
		ContentHandler handler = new ToXMLContentHandler();
		// ContentHandler handler = new BodyContentHandler();
		// ContentHandler handler = new BodyContentHandler(
		// new ToXMLContentHandler());
		InputStream stream = new FileInputStream(file.getAbsolutePath());
		AutoDetectParser parser = new AutoDetectParser();
		Metadata metadata = new Metadata();
		try {
			parser.parse(stream, handler, metadata);
			FileWriter htmlFileWriter = new FileWriter(FINAL_FILE_NAME);
			htmlFileWriter.write(handler.toString());
			htmlFileWriter.flush();
			htmlFileWriter.close();
			return new File(FINAL_FILE_NAME);
		} finally {
			stream.close();
		}
	}

	

}
