package tech.enfuego.resumewriter.skills.supervision;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum MultiSkillsWithoutMonthPastTense {
	
SENTENCE_3("Experienced in supervising %s, %s and %s activities with accountability for results.");	
	
	public String getSentence() {
		return sentence;
	}

	public void setSentence(String sentence) {
		this.sentence = sentence;
	}

	private String sentence;
	
	MultiSkillsWithoutMonthPastTense(String sentence){
		this.sentence = sentence;
	}
	
	private static final List<MultiSkillsWithoutMonthPastTense> VALUES =
		    Collections.unmodifiableList(Arrays.asList(values()));
	
	private static final int SIZE = VALUES.size();
	  private static final Random RANDOM = new Random();

	  public static MultiSkillsWithoutMonthPastTense randomSentence()  {
	    return VALUES.get(RANDOM.nextInt(SIZE));
	  }

}
