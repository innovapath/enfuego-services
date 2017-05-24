package tech.enfuego.resumewriter.skills.supported;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum MultiSkillsWithoutMonthPresentTense {
	
	SENTENCE_3("Currently supporting %s, %s and %s responsibilities attributed to senior positions.");	
	
	public String getSentence() {
		return sentence;
	}

	public void setSentence(String sentence) {
		this.sentence = sentence;
	}

	private String sentence;
	
	MultiSkillsWithoutMonthPresentTense(String sentence){
		this.sentence = sentence;
	}
	
	private static final List<MultiSkillsWithoutMonthPresentTense> VALUES =
		    Collections.unmodifiableList(Arrays.asList(values()));
	
	private static final int SIZE = VALUES.size();
	  private static final Random RANDOM = new Random();

	  public static MultiSkillsWithoutMonthPresentTense randomSentence()  {
	    return VALUES.get(RANDOM.nextInt(SIZE));
	  }


}
