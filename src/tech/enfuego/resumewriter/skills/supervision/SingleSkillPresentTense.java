package tech.enfuego.resumewriter.skills.supervision;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public enum SingleSkillPresentTense {
	
	SENTENCE_1("Supervising %s activity for more than %d months  with excellent results."),
	SENTENCE_2("Accountable for %s activity in a supervision capacity for %d months, delivering strong outcomes.");
	
	
	public String getSentence() {
		return sentence;
	}

	public void setSentence(String sentence) {
		this.sentence = sentence;
	}

	private String sentence;
	
	SingleSkillPresentTense(String sentence){
		this.sentence = sentence;
	}
	
	private static final List<SingleSkillPresentTense> VALUES =
		    Collections.unmodifiableList(Arrays.asList(values()));
	
	private static final int SIZE = VALUES.size();
	  private static final Random RANDOM = new Random();

	  public static SingleSkillPresentTense randomSentence()  {
	    return VALUES.get(RANDOM.nextInt(SIZE));
	  }

}
