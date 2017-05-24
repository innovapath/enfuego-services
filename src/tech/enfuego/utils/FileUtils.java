package tech.enfuego.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.io.FilenameUtils;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.ToXMLContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class FileUtils {


	private static Tika tika = null;

	private static Tika getTikaInstance() {

		if(tika == null) {
			tika = new Tika();
			return tika;
		}
		else return tika;

	}

	public static String getStringFromInputStream(InputStream is) {

		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {

			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return sb.toString();

	}

	public static String getStringUsingTika(InputStream is) throws IOException, TikaException 
	{
		return getTikaInstance().parseToString(is);
	}


	public static File convertoHTML(InputStream inputStream) throws IOException, SAXException, TikaException
	{
		System.out.println("STEP 3: CONVERTING TO HTML");
		String TEMP_DIR = FileUtils.class.getClassLoader().getResource("/tempFiles").getPath();
		File tempFile = new File(TEMP_DIR+"/test.html");
		try
		{
			ContentHandler handler = new ToXMLContentHandler();	
			AutoDetectParser parser = new AutoDetectParser();
			Metadata metadata = new Metadata();
			parser.parse(inputStream, handler, metadata);
			FileWriter htmlFileWriter = new FileWriter(tempFile);
			htmlFileWriter.write(handler.toString());
			htmlFileWriter.flush();
			htmlFileWriter.close();
			System.out.println("STEP 3: CONVERTED TO HTML : "+tempFile.getAbsolutePath());
			//return new ByteArrayInputStream(handler.toString().getBytes("UTF-8"));
			return tempFile;
		}
		finally {
			inputStream.close();

		}
		
	}


	public static String readFile(String path, Charset encoding) 
			throws IOException 
	{
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	public static InputStream[] clone(final InputStream inputStream, int copies) {
		try {
			InputStream[] result = new InputStream[copies];
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int readLength = 0;
			while ((readLength = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, readLength);
			}
			outputStream.flush();
			for(int i = 0; i < copies; i ++) {
				result[i] = new ByteArrayInputStream(outputStream.toByteArray());
			}
			return result;
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}


}
