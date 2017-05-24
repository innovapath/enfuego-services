package tech.enfuego.resumewriter.skills.supported;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum MultiSkillsWithMonthPastTense {

	
SENTENCE_4("Delivered superior results in %s, %s and %s support activities for more than %d months.");
	
	public String getSentence() {
		return sentence;
	}

	public void setSentence(String sentence) {
		this.sentence = sentence;
	}

	private String sentence;
	
	MultiSkillsWithMonthPastTense(String sentence){
		this.sentence = sentence;
	}
	
	private static final List<MultiSkillsWithMonthPastTense> VALUES =
		    Collections.unmodifiableList(Arrays.asList(values()));
	
	private static final int SIZE = VALUES.size();
	  private static final Random RANDOM = new Random();

	  public static MultiSkillsWithMonthPastTense randomSentence()  {
	    return VALUES.get(RANDOM.nextInt(SIZE));
	  }

}
