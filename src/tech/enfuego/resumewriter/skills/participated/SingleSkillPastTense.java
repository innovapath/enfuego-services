package tech.enfuego.resumewriter.skills.participated;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum SingleSkillPastTense {
	
	SENTENCE_1("Team contributor to %s activity for more than %d months having delivered excellent results."),
	SENTENCE_2("Previously accountable for %s activity in a collaborative capacity for %d months with strong outcomes.");
	
	
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
