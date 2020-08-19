package assign1;

import java.util.HashSet;
import java.util.Set;

// CS108 HW1 -- String static methods

public class StringCode {

	/**
	 * Given a string, returns the length of the largest run.
	 * A a run is a series of adajcent chars that are the same.
	 * @param str
	 * @return max run length
	 */
	public static int maxRun(String str) {
		if(str.isEmpty()) {
			return 0;
		}
		int maxRun = 0;
		for(int i = 0; i < str.length(); i++) {
			int run = 1;
			// find the longest adjacent same chars starting from str[i]
			for(int j = i+1; j < str.length(); j++) {
				if(str.charAt(i) != str.charAt(j)) {
					break;
				}
				run ++;
			}
			if(run > maxRun) {
				maxRun = run;
			}
		}
		return maxRun;
	}

	
	/**
	 * Given a string, for each digit in the original string,
	 * replaces the digit with that many occurrences of the character
	 * following. So the string "a3tx2z" yields "attttxzzz".
	 * @param str
	 * @return blown up string
	 */
	public static String blowup(String str) {		
		StringBuilder newString = new StringBuilder(); // String is immutable in JAVA
		String replace = new String();
		for(int i = 0; i < str.length(); i++) {
			// if char is digit, replace it with digit * following chars
			if(Character.isDigit(str.charAt(i))){
				replace = "";
				if(i != str.length() - 1) {
				    for(int j = 0; j < Character.getNumericValue(str.charAt(i)); j++) {
					    replace += Character.toString(str.charAt(i+1));
				    }
				}
				newString.append(replace);
			}else {
				newString.append(str.charAt(i)); // if not, append the original char
			}
		}
		return newString.toString(); 
	}
}
