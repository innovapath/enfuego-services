package tech.enfuego.resumewriter.skills.directlyperformed;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum MultipleSkillsWithoutMonthPastTense {
	
SENTENCE_1("Experienced in fulfilling %s, %s and %s responsibilities directly attributed to my position.");	
	
	public String getSentence() {
		return sentence;
	}

	public void setSentence(String sentence) {
		this.sentence = sentence;
	}

	private String sentence;
	
	MultipleSkillsWithoutMonthPastTense(String sentence){
		this.sentence = sentence;
	}
	
	private static final List<MultipleSkillsWithoutMonthPastTense> VALUES =
		    Collections.unmodifiableList(Arrays.asList(values()));
	
	private static final int SIZE = VALUES.size();
	  private static final Random RANDOM = new Random();

	  public static MultipleSkillsWithoutMonthPastTense randomSentence()  {
	    return VALUES.get(RANDOM.nextInt(SIZE));
	  }

}
