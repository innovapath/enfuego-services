package tech.enfuego.resumewriter.skills.participated;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum MultiSkillsPastTense {

	
SENTENCE_1("Delivered superior results in %s, %s and %s activities as a team contributor."),
SENTENCE_2("Fulfilled %s, %s and %s responsibilities shared by my business unit team.");

	
	public String getSentence() {
		return sentence;
	}

	public void setSentence(String sentence) {
		this.sentence = sentence;
	}

	private String sentence;
	
	MultiSkillsPastTense(String sentence){
		this.sentence = sentence;
	}
	
	private static final List<MultiSkillsPastTense> VALUES =
		    Collections.unmodifiableList(Arrays.asList(values()));
	
	private static final int SIZE = VALUES.size();
	  private static final Random RANDOM = new Random();

	  public static MultiSkillsPastTense randomSentence()  {
	    return VALUES.get(RANDOM.nextInt(SIZE));
	  }

}
