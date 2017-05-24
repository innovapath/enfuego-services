package tech.enfuego.utils;

import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;


import tech.enfuego.Constants;

public class RegexUtils {
	
	private static Pattern numberPattern = null;
	
	private static Pattern getNumberPattern() {
		if(numberPattern == null) {
			numberPattern = Pattern.compile(Constants.REGEX_FOR_NUMBER);
		}
		return numberPattern;
	}
	
	public static int[] getMinMaxYears(String input) {
		int [] minMax = new int[2];
		List<Integer> inputList = new ArrayList<Integer>();
		Matcher m = getNumberPattern().matcher(input);
		while(m.find()) {
			inputList.add(Integer.parseInt(m.group()));
		}
		Collections.sort(inputList);
		minMax[0] = inputList.get(0);
		minMax[1] = inputList.get(inputList.size()-1);
		return minMax;
		
	}

}
