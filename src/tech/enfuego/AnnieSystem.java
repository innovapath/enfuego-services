package tech.enfuego;

import java.io.File;
import java.io.IOException;

import gate.Corpus;
import gate.CorpusController;
import gate.Gate;
import gate.util.GateException;
import gate.util.persistence.PersistenceManager;
import tech.enfuego.Constants.FILE_TYPES;

/**
 * 
 * @author abhishek
 * <p>
 * This class defines 3 methods as of now :
 * <ul>
 * <li> Initialize ANNIE(A New Information Extraction Engine). This is a term coined by GATE(General Architecture for Text Engineering). </li>
 * <li> Set the Corpus object. A corpus is a simple collections of documents </li>
 * <li> Execute the ANNIE pipeline on the corpus object.</li>
 * </ul>
 * <p>
 * The above mentioned tasks may sound too much like jargon and hence it is recommended to read 
 * an introduction on GATE(General Architecture for Text Engineering)
 * <p>
 * I have documented each method in much detail.
 *
 */
public class AnnieSystem {
	
	
private CorpusController annieController;
	
	/**
	 * <p>
	 * Every GATE project works in the following way.
	 * <p>
	 * <b>STEP 1</b> : Using the GATE UI define  the pipeline of tasks that you want to perform for information extraction.
	 * <p>
	 * <b> STEP 2</b> : Once, the GATE pipeline is ready, you can persist the entire application using the GATE UI. 
	 * These applications are stored in the file formats of *.gapp files.
	 * <p>
	 * <b> STEP 3 </b> Now, these *.gapp files can be invoked into your java code. You can run the same pipeline of tasks in your program. This is exactly what we have done here.
	 * In this method, we load the *.gapp file based on the File Type which is passed as input to the function.
	 * 
	 * 
	 * @param fileType can be of 3 types as defined in the enum
	 * <code>
	 * Constants.FILE_TYPES
	 * </code>
	 * @throws GateException
	 * @throws IOException
	 */
	public void initializeANNIE(FILE_TYPES fileType) throws GateException, IOException{
		File annieGapp = new File(Gate.getGateHome(), Constants.fileMap.get(fileType));
	    annieController = (CorpusController) PersistenceManager.loadObjectFromFile(annieGapp);
	    System.out.println(annieController.getName());
	    System.out.println(annieController.getFeatures());
	    System.out.println("ANNIE loaded from file successfully");
	}
	
	
	 /** Tell ANNIE's controller about the corpus you want to run on */
	  public void setCorpus(Corpus corpus) {
	    annieController.setCorpus(corpus);
	  } // setCorpus

	  /** Run ANNIE */
	  public void execute() throws GateException {
		System.out.println("Running processing engine...");
	    annieController.execute();
	    System.out.println("...processing engine complete");
	  } // execute()


}
