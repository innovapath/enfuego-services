package tech.enfuego.deeplearning;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.datavec.api.util.ClassPathResource;
import org.deeplearning4j.berkeley.Pair;
import org.deeplearning4j.models.embeddings.inmemory.InMemoryLookupTable;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.paragraphvectors.ParagraphVectors;
import org.deeplearning4j.models.word2vec.VocabWord;
import org.deeplearning4j.text.documentiterator.FileLabelAwareIterator;
import org.deeplearning4j.text.documentiterator.LabelAwareIterator;
import org.deeplearning4j.text.documentiterator.LabelledDocument;
import org.deeplearning4j.text.stopwords.StopWords;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.nd4j.linalg.api.ndarray.INDArray;

import tech.enfuego.Constants;
import tech.enfuego.Constants.PROFESSION_TYPES;
import tech.enfuego.utils.LabelSeeker;
import tech.enfuego.utils.MeansBuilder;

/***
 * 
 * @author abhishek
 * 
 * <p>
 * Document2Vector means learning word representations of a labelled set of documents
 * <p>
 * We use this approach to learn the word representations of various "Skills" in each professionTypes
 * <p>
 * For example for the professionType "SOFTWARE" : the skill Java may have different word representations.
 * We train our document2vector model on existing job descriptions which talk about these skills and then we persist the trained model.
 * 
 * <p>
 * Now, when we see an unlabeled document i.e. either a job description or a resume, we load the persisted model and get a score for each skill.
 * 
 * <p>
 * One important thing to note is; that this document2Vector models are for each professionType.
 * As of now, we support 2 professionTypes :
 * <ul>
 * <li> MANAGEMENT </li>
 * <li> SOFTWARE </li>
 * </ul>
 * <p>
 * We have the training set ready for more 5 professionTypes. Please check the resources folder in this project.
 * We will just need train a document2vector model for these additional 5 profession Types
 *
 */
public class Document2Vector {

	private PROFESSION_TYPES ptype;
	private LabelAwareIterator iterator;
	private TokenizerFactory tokenizerFactory;
	
	// This will be specific to each professionType. Need to think of a better design to do this.
	private static Document2Vector forManagers;
	private static Document2Vector forSoftwareEngineers;

	/**
	 * Based on the professionType return the instance of the Document2Vector model.
	 * Currently, we support 2 professionTypes : 
	 * 
	 * <ul>
	 * <li> MANAGEMENT </li>
	 * <li> SOFTWARE </li>
	 * </ul>
	 * <p>
	 * 
	 * But as we add 5 more in near future(and more as we expand our business), the depth of <code>if..else</code> loop in the method 
	 * will increase
	 * @param inputPtype
	 * @return
	 * @throws FileNotFoundException
	 */
	public static Document2Vector getInstance(PROFESSION_TYPES inputPtype) throws FileNotFoundException {
		Document2Vector result = null;
		//define for managment
		if (inputPtype.equals(PROFESSION_TYPES.MANAGEMENT))
		{
			if (null != forManagers) {
				return forManagers;
			}
			forManagers = new Document2Vector(inputPtype);
			result = forManagers;
		}
		else if(inputPtype.equals(PROFESSION_TYPES.SOFTWARE)) {
			//define for Software Engineer Engineer
			if(null != forSoftwareEngineers) {
				return forSoftwareEngineers;
			}
			forSoftwareEngineers = new Document2Vector(inputPtype);
			result = forSoftwareEngineers;
		}
		return result;

	}
	
	/**
	 *  GETTER
	 * @return
	 */
	public PROFESSION_TYPES getPtype() {
		return ptype;
	}

	/**
	 * SETTER
	 * @param ptype
	 */
	public void setPtype(PROFESSION_TYPES ptype) {
		this.ptype = ptype;
	}

	//Public Constructor : Provide the type of profession you would like to train for
	// Example : MANAGER, SOFTWARE ENGINEER, etc
	private Document2Vector(PROFESSION_TYPES profession_type) throws FileNotFoundException {
		ClassPathResource resource = new ClassPathResource(Constants.professionMap.get(profession_type));
		this.ptype = profession_type;
		this.iterator = new FileLabelAwareIterator.Builder()
				.addSourceFolder(resource.getFile())
				.build();
		this.tokenizerFactory = new DefaultTokenizerFactory();
		this.tokenizerFactory.setTokenPreProcessor(new CommonPreprocessor());
	}

	/**
	 * Method to train the document3Vector model
	 * @return
	 * @throws Exception
	 */
	public ParagraphVectors train()  throws Exception {
		// ParagraphVectors training configuration
		ParagraphVectors paragraphVectors = new ParagraphVectors.Builder()
				.stopWords(StopWords.getStopWords())
				.learningRate(0.025)
				.minLearningRate(0.001)
				.batchSize(1000)
				.epochs(30)
				.iterate(iterator)
				.trainElementsRepresentation(true)
				.trainSequencesRepresentation(true)
				.trainWordVectors(true)
				.tokenizerFactory(tokenizerFactory)
				.build();

		// Start model training
		paragraphVectors.fit();
		WordVectorSerializer.writeParagraphVectors(paragraphVectors, Constants.paragraphVectorMap.get(this.ptype));
		return paragraphVectors;
	}

	/**
	 * Based on the professionType, we use this method to predict scores of the skills on an unseen document.
	 * <p>
	 * For example given a resume for a Software Engineer profession, what is the score for Java, Python, etc.
	 * @param paragraphVectors
	 * @param content
	 * @return List of Skill name and score for the respective skill. This list is sorted as per the score.
	 * @throws IOException
	 */
	public List<Pair<String, Double>> predict(ParagraphVectors paragraphVectors, String content) throws IOException {
		MeansBuilder meansBuilder = new MeansBuilder(
				(InMemoryLookupTable<VocabWord>)paragraphVectors.getLookupTable(),
				tokenizerFactory);
		LabelSeeker seeker = new LabelSeeker(iterator.getLabelsSource().getLabels(),
				(InMemoryLookupTable<VocabWord>) paragraphVectors.getLookupTable());

		LabelledDocument document = new LabelledDocument();
		document.setContent(content);
		INDArray documentAsCentroid = meansBuilder.documentAsVector(document);
		
		List<Pair<String, Double>> scores = seeker.getScores(documentAsCentroid);
		//scores.sort(new Pair.SecondComparator<String, Double>());
		Collections.sort(scores, new Pair.SecondComparator<String, Double>().reversed());
		return scores;
	}
}
