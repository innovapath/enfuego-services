package tech.enfuego.resumewriter.skills.directlyperformed;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum MultipleSkillsWithMonthPastTense {
	
	SENTENCE_1("Delivered superior results in %s, %s and %s activities for more than %d months as an individual contributor.");	
	
	public String getSentence() {
		return sentence;
	}

	public void setSentence(String sentence) {
		this.sentence = sentence;
	}

	private String sentence;
	
	MultipleSkillsWithMonthPastTense(String sentence){
		this.sentence = sentence;
	}
	
	private static final List<MultipleSkillsWithMonthPastTense> VALUES =
		    Collections.unmodifiableList(Arrays.asList(values()));
	
	private static final int SIZE = VALUES.size();
	  private static final Random RANDOM = new Random();

	  public static MultipleSkillsWithMonthPastTense randomSentence()  {
	    return VALUES.get(RANDOM.nextInt(SIZE));
	  }

}
