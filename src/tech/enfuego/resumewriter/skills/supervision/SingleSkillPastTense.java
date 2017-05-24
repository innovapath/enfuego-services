package tech.enfuego.resumewriter.skills.supervision;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum SingleSkillPastTense {
	
	SENTENCE_1("Supervised %s activity for more than %d months  with excellent results."),
	SENTENCE_2("Accountable for %s activity in a supervisory capacity for %d months; delivered strong outcomes.");
	
	
	public String getSentence() {
		return sentence;
	}

	public void setSentence(String sentence) {
		this.sentence = sentence;
	}

	private String sentence;
	
	SingleSkillPastTense(String sentence){
		this.sentence = sentence;
	}
	
	private static final List<SingleSkillPastTense> VALUES =
		    Collections.unmodifiableList(Arrays.asList(values()));
	
	private static final int SIZE = VALUES.size();
	  private static final Random RANDOM = new Random();

	  public static SingleSkillPastTense randomSentence()  {
	    return VALUES.get(RANDOM.nextInt(SIZE));
	  }

}
