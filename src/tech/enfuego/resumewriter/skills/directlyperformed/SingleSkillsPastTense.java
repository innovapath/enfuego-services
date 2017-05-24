package tech.enfuego.resumewriter.skills.directlyperformed;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum SingleSkillsPastTense {
	
	SENTENCE_1("Directly contributed to %s activity for more than %d months with excellent results."),
	SENTENCE_2("Held accountability for %s activity in an individual contributor capacity for %d months; delivered strong outcomes.");
	
	
	public String getSentence() {
		return sentence;
	}

	public void setSentence(String sentence) {
		this.sentence = sentence;
	}

	private String sentence;
	
	SingleSkillsPastTense(String sentence){
		this.sentence = sentence;
	}
	
	private static final List<SingleSkillsPastTense> VALUES =
		    Collections.unmodifiableList(Arrays.asList(values()));
	
	private static final int SIZE = VALUES.size();
	  private static final Random RANDOM = new Random();

	  public static SingleSkillsPastTense randomSentence()  {
	    return VALUES.get(RANDOM.nextInt(SIZE));
	  }

}
