package tech.enfuego;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import org.apache.tika.Tika;
import org.datavec.api.util.ClassPathResource;
import org.deeplearning4j.berkeley.Pair;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.paragraphvectors.ParagraphVectors;

import tech.enfuego.Constants.PROFESSION_TYPES;
import tech.enfuego.deeplearning.Document2Vector;
import tech.enfuego.utils.FileUtils;

/**
 * 
 * @author abhishek
 * <p>
 * This is just an example of how to train document2Vector model for various profession types.
 * <p>
 * Please read the documentation of the class <code>tech.enfuego.deeplearning.Document2Vector</code> for more understanding
 *
 */
public class DocumentClassifier {

	public static void main(String[] args) throws Exception {
		
		Document2Vector doc1 = Document2Vector.getInstance(PROFESSION_TYPES.MANAGEMENT);
		doc1.train();
		
		Document2Vector doc2 = Document2Vector.getInstance(PROFESSION_TYPES.SOFTWARE);
		doc2.train();
		
//		ClassPathResource resource = new ClassPathResource(Constants.paragraphVectorMap.get(doc2.getPtype()));
//		System.out.println(resource.getFile().getAbsolutePath());
//		
//		ParagraphVectors paragraphVectors = WordVectorSerializer.readParagraphVectors(resource.getFile().getAbsolutePath());
//		
//		Tika tika = new Tika();
//		
//		//String content = tika.parseToString(new File("/home/aosingh/Downloads/1.docx"));
//        InputStream inputStream = new BufferedInputStream(new FileInputStream(new File("/home/aosingh/git/enfuego-services/resources/Jobs/AMAZON Economic Development.docx")));
//        InputStream [] copies = FileUtils.clone(inputStream, 2);
//        String content = FileUtils.getStringUsingTika(copies[0]);
//        String content2 = FileUtils.getStringUsingTika(copies[1]);
//	    //String content = FileUtils.readFile("/home/aosingh/git/enfuego-services/resources/test_data/consulting_resume", StandardCharsets.UTF_8);
//	    
//	    List<Pair<String, Double>> scores = doc1.predict(paragraphVectors, content2);
//	    Collections.sort(scores, new Pair.SecondComparator<String, Double>().reversed());
//	    for (Pair<String, Double> score: scores) {
//       	 	System.out.println("" + score.getFirst() + ": " + score.getSecond());
//        }
	}
}
