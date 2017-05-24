package tech.enfuego.resumewriter.skills.supported;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum SingleSkillPastTense {
	
	SENTENCE_1("Was accountable for %s support activity in a junior role for %d months, delivering strong outcomes."),
	SENTENCE_2("Assisted the execution of %s activity for more than %d months to support excellent results.");
	
	
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
