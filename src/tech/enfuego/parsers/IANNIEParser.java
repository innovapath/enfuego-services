package tech.enfuego.parsers;

import java.io.File;
import java.io.IOException;

import org.apache.tika.exception.TikaException;
import org.json.simple.JSONObject;
import org.xml.sax.SAXException;

import gate.util.GateException;

public interface IANNIEParser {
	
	public File converToHTML(File file) throws IOException, SAXException, TikaException;
	public JSONObject loadGateAndAnnie(File file) throws GateException, IOException;
}
